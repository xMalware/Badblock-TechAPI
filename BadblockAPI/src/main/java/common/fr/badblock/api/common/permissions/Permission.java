package fr.badblock.api.common.permissions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
/**
 * Represents the id of a permission
 * @author LeLanN
 */
public class Permission
{
	private transient PermissionResult result;
	private transient boolean isAll = false;
	private String[] permission;

	/**
	 * Created a permission from a string. For example:
	 * <ul>
	 * <li> permission.example </li>
	 * <li> permission.example.* </li>
	 * <li> -permission.example </li>
	 * <li> -permission.example.* </li>
	 * </ul>
	 * @param permission The permission string
	 */
	public Permission(String permission)
	{
		result = PermissionResult.YES;

		if(permission == null)
			permission = "";

		char first = permission.charAt( 0 );
		char last  = permission.charAt( permission.length() - 1 );

		if(first == '-')
		{
			result = PermissionResult.NO;
			permission = permission.substring(1);
		}

		if(last == '*')
		{
			isAll = true;
			permission = permission.substring(0, permission.length() - ( permission.length() == 1 ? 1 : 2 ));
		}

		if(permission.isEmpty())
			this.permission = new String[] {};
		else this.permission = permission.toLowerCase().split("\\.");
	}

	private PermissionResult fResult(Permission permission)
	{
		if(permission.getResult() == getResult())
		{
			return PermissionResult.YES;
		}
		
		return PermissionResult.NO;
	}
	
	/**
	 * Compare two permissions
	 * @param permission The second permission
	 * @return A PermissionResult
	 */
	public PermissionResult compare(Permission permission)
	{
		String[] perm1 = getPermission(), perm2 = permission.getPermission();

		int len = 0;
		
		if(perm1.length == perm2.length)
		{
			len = perm1.length;
		}
		else if(isAll && perm2.length >= perm1.length)
		{
			len = perm1.length;
		}

		if(len == 0)
		{
			return isAll ? fResult(permission) : PermissionResult.UNKNOW;
		}
		
		for(int i = 0; i < perm1.length; i++)
		{
			if(!perm1[i].equals(perm2[i]))
			{
				return PermissionResult.UNKNOW;
			}
		}

		return fResult(permission);
	}

	@Override
	public String toString()
	{
		return (result == PermissionResult.NO ? "-" : "")					// -
				+ permission												// permissioon
				+ (isAll ? (permission.length == 0 ? "*" : ".*") : "");		// *
	}

	public enum PermissionResult
	{
		YES,
		NO,
		UNKNOW;
	}
}