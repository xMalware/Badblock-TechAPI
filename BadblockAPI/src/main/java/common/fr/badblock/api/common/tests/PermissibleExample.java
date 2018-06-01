package fr.badblock.api.common.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.Permission;
import fr.badblock.api.common.utils.permissions.PermissionSet;
import fr.badblock.api.common.utils.permissions.PermissionsManager;

public class PermissibleExample
{

	public static void main(String[] args)
	{
		String place = "bungee";
		Map<String, Permissible> permissions = new HashMap<>();
		
		List<String> inheritances = Arrays.asList("staff");
		List<PermissionSet> permissionSet = new ArrayList<>();
		List<String> places = Arrays.asList("bungee");
		List<Permission> permissionss2 = new ArrayList<>();
		permissionss2.add(new Permission("commands.end"));
		permissionss2.add(new Permission("commands.alert"));
		Map<String, Integer> powers = new HashMap<>();
		powers.put("slotFriendList", 32);
		powers.put("slotPartyList", 16);
		Map<String, JsonElement> values = new HashMap<>();
		
		permissionSet.add(new PermissionSet(places, permissionss2, powers, values));
		
		List<String> places2 = Arrays.asList("hub", "minigames");
		List<Permission> permissionss3 = new ArrayList<>();
		permissionss3.add(new Permission("hub.broadcastjoin"));
		permissionss3.add(new Permission("api.adminmode"));
		Map<String, Integer> powers3 = new HashMap<>();
		powers.put("mapVotesMultiplier", 8);
		Map<String, JsonElement> values3 = new HashMap<>();
		
		permissionSet.add(new PermissionSet(places2, permissionss3, powers3, values3));
		
		Permissible permissible = new Permissible("admin", inheritances, permissionSet, true, 75);
		
		permissions.put("admin", permissible);
		PermissionsManager manager = new PermissionsManager(permissions, place);
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(manager));
	}
	
}
