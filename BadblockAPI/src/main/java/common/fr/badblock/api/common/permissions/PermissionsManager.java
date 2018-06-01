package fr.badblock.api.common.permissions;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import fr.badblock.api.common.utils.GsonUtils;
import lombok.Getter;

/**
 * Permission manager
 * @author LeLanN
 */
public class PermissionsManager
{
	private static final Type permissionList = new TypeToken<Map<String, Permissible>>() {}.getType();
	@Getter
	private static PermissionsManager manager;
	
	public static void createPermissionManager(JsonElement obj, String place)
	{
		Map<String, Permissible> groups = GsonUtils.getGson().fromJson(obj, permissionList);
	
		if(groups == null)
		{
			throw new IllegalArgumentException("Fail to read groups");
		}
		
		createPermissionManager(groups, place);
	}
	
	/**
	 * Create the permission manager
	 * @param groups Groups
	 * @param place Place of the server
	 */
	public static void createPermissionManager(Map<String, Permissible> groups, String place)
	{
		if(manager != null)
		{
			throw new IllegalStateException("Permission manager already created!");
		}
		
		manager = new PermissionsManager(groups, place);
	}
	
	@Getter
	private String currentPlace;
	private Map<String, Permissible> groups;
	
	private PermissionsManager(Map<String, Permissible> groups, String place)
	{
		this.currentPlace = place;
		reloadGroups(groups);
	}
	
	public void reloadGroups(Map<String, Permissible> groups)
	{
		this.groups = groups;
	}
	
	public Permissible getGroup(String name)
	{
		return groups.get(name);
	}
	
	public Permissible loadPermissible(JsonObject value)
	{
		return GsonUtils.getGson().fromJson(value, Permissible.class);
	}
}