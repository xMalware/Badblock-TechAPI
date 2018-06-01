package fr.badblock.api.common.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import fr.badblock.api.common.permissions.Permission.PermissionResult;
import fr.badblock.api.common.utils.GsonUtils;

public class Permissible
{
	Type inheritanceList = new TypeToken<List<String>>() {}.getType();
	Type permissionList = new TypeToken<List<PermissionSet>>() {}.getType();

	private List<String> inheritances;
	private List<PermissionSet> permissions;

	public Permissible(List<String> inheritances, List<PermissionSet> permissions)
	{
		this.inheritances = inheritances;
		this.permissions = permissions;
	}
	
	public Permissible()
	{
		this.inheritances = new ArrayList<>();
		this.permissions = new ArrayList<>();
	}
	
	public Permissible(JsonObject jsonObject)
	{
		inheritances = GsonUtils.getGson().fromJson(jsonObject.get("inheritances"), inheritanceList);
		permissions = GsonUtils.getGson().fromJson(jsonObject.get("permissions"), permissionList);
	}
	
	/**
	 * Return the inheritances of the permissible
	 * @return A collection of permissible
	 */
	public Collection<Permissible> getInheritances()
	{
		List<Permissible> inheritances = new ArrayList<>();
		
		for(String inheritance : this.inheritances)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(inheritance);
			
			if(permissible != null)
				inheritances.add(permissible);
		}
		
		return inheritances;
	}
	
	/**
	 * Check if the permissible has the permission <i>permission</io>
	 * @param permission The permission label
	 * @return Return: <b>true</b> if the permission has the permission, <b>false</b> otherwise
	 */
	public boolean hasPermission(String permission)
	{
		return testPermission( new Permission(permission) ) == PermissionResult.YES;
	}
	
	/**
	 * Check if the permissible has the permission <i>permission</io>
	 * @param permission The permission label
	 * @return Return: <b>true</b> if the permission has the permission, <b>false</b> otherwise
	 */
	public boolean hasPermission(Permission permission)
	{
		return testPermission(permission) == PermissionResult.YES;
	}
	
	/**
	 * Test the permission on the permissible.
	 * @param perm The permission
	 * @return Return {@link PermissionResult#NO} if the result is explicitly false. Return {@link PermissionResult#YES} if the result is explicitly true. Otherwise, return {@link PermissionResult#UNKNOW}
	 */
	public PermissionResult testPermission(Permission perm)
	{
		PermissionResult finalResult = PermissionResult.UNKNOW, currentResult = null;
		
		for(PermissionSet set : permissions)
		{
			if(!set.isCompatible())
				continue;

			currentResult = set.hasPermission(perm);

			if(currentResult == PermissionResult.NO) return PermissionResult.NO;
			if(currentResult == PermissionResult.YES) finalResult = PermissionResult.YES;
		}
		
		if(finalResult == PermissionResult.YES)
			return finalResult;
		
		for(Permissible permissible : getInheritances())
		{
			currentResult = permissible.testPermission(perm);
			
			if(currentResult == PermissionResult.NO) return PermissionResult.NO;
			if(currentResult == PermissionResult.YES) finalResult = PermissionResult.YES;
		}
		
		return finalResult;
	}
	
	/**
	 * Return the power for <i>label</i> for this permissible.
	 * @param label The power label
	 * @return Return an unsigned integer representing the power.
	 */
	public int getPower(String label)
	{
		PermissionSet set = getSetWithMaximalPower(label);
		return set == null ? 0 : set.getPower(label);
	}
	
	/**
	 * Return the permission set having the biggest power value for <i>label</i>
	 * @param label The power label
	 * @return Return the permission set. Can be null.
	 */
	public PermissionSet getSetWithMaximalPower(String label)
	{
		int max = -1, currentValue = 0;
		PermissionSet result = null, currentSet = null;
		
		for(PermissionSet set : permissions)
		{
			if(!set.isCompatible())
				continue;
			
			currentValue = set.getPower(label);
			
			if(currentValue > max)
			{
				max = currentValue;
				result = set;
			}
		}
		
		for(Permissible permissible : getInheritances())
		{
			currentSet = permissible.getSetWithMaximalPower(label);
			
			if(currentSet == null)
				continue;
			
			currentValue = currentSet.getPower(label);
			
			if(currentValue > max)
			{
				max = currentValue;
				result = currentSet;
			}
		}
		
		return result;
	}
	
	/**
	 * Return the value <i>valueLabel</i> of the permissible. The value is taken from the permission set having the biggest power value for <i>powerLabel</i>
	 * @param powerLabel The power label
	 * @param valueLabel The value label
	 * @return Return the label. Can be null.
	 */
	public JsonElement getSimpleValue(String powerLabel, String valueLabel)
	{
		PermissionSet set = getSetWithMaximalPower(powerLabel);
		return set == null ? null : set.getValue(valueLabel);
	}
}