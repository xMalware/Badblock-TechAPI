package fr.badblock.api.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.api.common.utils.permissions.Permission.PermissionResult;
import lombok.ToString;

/**
 * Permissible
 * @author LeLanN
 */
@ToString
public class Permissible
{

	@SuppressWarnings("serial")
	Type inheritanceList = new TypeToken<List<String>>() {}.getType();
	@SuppressWarnings("serial")
	Type permissionList = new TypeToken<List<PermissionSet>>() {}.getType();

	private String				name;
	private boolean				displayable;
	private int					power;
	private List<String>		inheritances;
	private List<PermissionSet>	permissions;

	public Permissible(String name, List<String> inheritances, List<PermissionSet> permissions, boolean displayable, int power)
	{
		this.name = name;
		this.inheritances = inheritances;
		this.permissions = permissions;
		this.displayable = displayable;
		this.power = power;
	}

	public Permissible()
	{
		this.name = "default";
		this.inheritances = new ArrayList<>();
		this.permissions = new ArrayList<>();
		this.displayable = true;
		this.power = 0;
	}

	public Permissible(JsonObject jsonObject)
	{
		name = jsonObject.get("name").getAsString();
		inheritances = GsonUtils.getPrettyGson().fromJson(jsonObject.get("inheritances"), inheritanceList);
		permissions = GsonUtils.getPrettyGson().fromJson(jsonObject.get("permissions"), permissionList);
		displayable = jsonObject.get("displayable").getAsBoolean();
		power = jsonObject.get("power").getAsInt();
	}
	
	public List<PermissionSet> getPermissions()
	{
		return this.permissions;
	}

	public DBObject getDBObject()
	{
		BasicDBObject query = new BasicDBObject();
		query.put("name", name);
		query.put("inheritances", inheritances);
		query.put("permissions", permissions);
		query.put("displayable", displayable);
		query.put("power", power);
		return query;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isDisplayable()
	{
		return displayable;
	}
	
	public int getPower()
	{
		return power;
	}

	public String getRawData(String data)
	{
		return PermissionsManager.I18N_PREFIX_KEY + getName() + I18n.SEPARATOR + data;
	}
	
	public String getRawPrefix(String type)
	{
		return getRawData("prefix_" + type);
	}
	
	public String getRawSuffix(String type)
	{
		return getRawData("suffix_" + type);
	}
	
	public String[] getTranslatedData(Locale locale, String data)
	{
		return I18n.getInstance().get(locale, getRawData(data));
	}
	
	public String getPrefix(Locale locale, String type)
	{
		return getTranslatedData(locale, "prefix_" + type)[0];
	}
	
	public String getSuffix(Locale locale, String type)
	{
		return getTranslatedData(locale, "suffix_" + type)[0];
	}

	/**
	 * Return the inheritances of the permissible
	 * @return A collection of permissible
	 */
	public Collection<Permissible> getInheritances()
	{
		List<Permissible> inheritances = new ArrayList<>();

		for (String inheritance : this.inheritances)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(inheritance);

			if (permissible != null)
			{
				inheritances.add(permissible);
			}
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
		return testPermission(new Permission(permission)) == PermissionResult.YES;
	}

	/**
	 * Test the permission on the permissible.
	 * @param perm The permission
	 * @return Return {@link PermissionResult#NO} if the result is explicitly false. Return {@link PermissionResult#YES} if the result is explicitly true. Otherwise, return {@link PermissionResult#UNKNOW}
	 */
	public PermissionResult testPermission(Permission perm)
	{
		PermissionResult finalResult = PermissionResult.UNKNOWN, currentResult = null;

		for (PermissionSet set : permissions)
		{
			if (!set.isCompatible())
			{
				continue;
			}

			currentResult = set.hasPermission(perm);
			
			if (currentResult.equals(PermissionResult.NO))
			{
				return PermissionResult.NO;
			}
			if (currentResult.equals(PermissionResult.YES))
			{
				finalResult = PermissionResult.YES;
			}
		}

		if (finalResult.equals(PermissionResult.YES))
		{
			return finalResult;
		}

		for (Permissible permissible : getInheritances())
		{
			
			currentResult = permissible.testPermission(perm);

			if (currentResult == PermissionResult.NO)
			{
				return PermissionResult.NO;
			}
			if (currentResult == PermissionResult.YES)
			{
				finalResult = PermissionResult.YES;
			}
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

		for (PermissionSet set : permissions)
		{
			if (!set.isCompatible())
				continue;

			currentValue = set.getPower(label);

			if(currentValue > max)
			{
				max = currentValue;
				result = set;
			}
		}

		for (Permissible permissible : getInheritances())
		{
			currentSet = permissible.getSetWithMaximalPower(label);

			if (currentSet == null)
			{
				continue;
			}

			currentValue = currentSet.getPower(label);

			if (currentValue > max)
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
