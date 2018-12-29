package fr.badblock.api.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.permissions.Permission.PermissionResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PermissionUser
{

	@SuppressWarnings("serial")
	private static transient Type groupType = new TypeToken<Map<String, Map<String, Long>>>() {}.getType();

	private Map<String, Map<String, Long>>	groups;
	private Map<String, Map<String, Long>> permissions;
	

	public PermissionUser(JsonObject jsonObject)
	{
		groups = GsonUtils.getPrettyGson().fromJson(jsonObject.get("groups"), groupType);
		if (groups == null)
		{
			groups = new HashMap<>();
		}
		
		permissions = GsonUtils.getPrettyGson().fromJson(jsonObject.get("permissions"), groupType);
		if (permissions == null)
		{
			permissions = new HashMap<>();
		}
	}
	
	public PermissionUser()
	{
		groups = new HashMap<>();
	}

	public List<String> getValidRanks(String place)
	{
		if (!groups.containsKey(place))
		{
			return Arrays.asList("default");
		}
		List<String> s = groups.get(place).entrySet().stream().filter(entry -> entry.getValue() > System.currentTimeMillis() || entry.getValue() <= 0).map(d -> d.getKey()).collect(Collectors.toList());
		if (s.isEmpty())
		{
			return Arrays.asList("default");
		}
		return s;
	}

	public Permissible getHighestRank(String place, boolean onlyDisplayables)
	{
		List<String> g = getValidRanks(place);
		if (g == null || g.isEmpty())
		{
			return PermissionsManager.getManager().getGroup("default");
		}

		Permissible result = null;
		for (String group : g)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(group);
			
			if (permissible == null)
			{
				continue;
			}
			
			if (onlyDisplayables && !permissible.isDisplayable())
			{
				continue;
			}
			
			if (result == null || permissible.getPower() > result.getPower())
			{
				result = permissible;
			}
		}

		if (result == null)
		{
			return PermissionsManager.getManager().getGroup("default");
		}
		
		return result;
	}
	
	public List<PermissionSet> getPermissions(String place)
	{
		if (groups == null)
		{
			return new ArrayList<>();
		}
		
		if (!groups.containsKey(place))
		{
			HashMap<String, Long> map = new HashMap<>();
			map.put("default", -1L);
			groups.put(place, map);
			return getPermissions(place);
		}
		
		List<String> g = getValidRanks(place);
		
		if (g == null)
		{
			return new ArrayList<>();
		}
		
		List<PermissionSet> permissions = new ArrayList<>();
		
		for (String group : g)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(group);
			if (permissible == null)
			{
				continue;
			}

			if (permissible.getPermissions() == null)
			{
				continue;
			}
			
			permissions.addAll(permissible.getPermissions());
		}
		
		if (this.permissions.containsKey(place))
		{
			Map<String, Long> perms = this.permissions.get(place);
			List<Permission> permObjects = new ArrayList<>();
			for (Entry<String, Long> entry : perms.entrySet())
			{
				if (entry.getValue() > 0 && entry.getValue() < System.currentTimeMillis())
				{
					continue;
				}
				
				permObjects.add(new Permission(entry.getKey()));
			}
			
			PermissionSet set = new PermissionSet(Arrays.asList(place), permObjects, new HashMap<>(), new HashMap<>());
			permissions.add(set);
		}
		
		return permissions;
	}

	public boolean hasPermission(String place, String permission)
	{
		if (groups == null)
		{
			return false;
		}
		
		if (!groups.containsKey(place))
		{
			HashMap<String, Long> map = new HashMap<>();
			map.put("default", -1L);
			groups.put(place, map);
			return hasPermission(place, permission);
		}
		
		List<String> g = getValidRanks(place);
		
		if (g == null)
		{
			return false;
		}
		
		PermissionResult permissionResult = null;
		Permission permissionObject = new Permission(permission);
		
		for (String group : g)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(group);
			if (permissible == null)
			{
				continue;
			}

			permissionResult = permissible.testPermission(permissionObject);
			if (permissionResult.equals(PermissionResult.YES))
			{
				return true;
			}
		}
		
		if (permissions.containsKey(place))
		{
			Map<String, Long> perms = permissions.get(place);
			for (Entry<String, Long> entry : perms.entrySet())
			{
				if (entry.getValue() > 0 && entry.getValue() < System.currentTimeMillis())
				{
					continue;
				}
				
				permissionResult = new Permission(entry.getKey()).compare(permissionObject);
				if (permissionResult.equals(PermissionResult.YES))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public DBObject getDBObject()
	{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("groups", groups);
		dbObject.put("permissions", permissions);
		return dbObject;
	}

}
