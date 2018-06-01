package fr.badblock.api.common.permissions;

import fr.badblock.api.common.utils.permissions.Permission;
import fr.badblock.api.common.utils.permissions.Permission.PermissionResult;
import junit.framework.TestCase;

/**
 * @author LeLanN
 */
public class PermissionsTest extends TestCase
{
	public void testCompare() throws Exception
	{
		Permission perm1 = new Permission("test.*");
		Permission perm2 = new Permission("test.t");
		Permission perm3 = new Permission("test2.t");
		Permission perm4 = new Permission("-test.t");
		Permission perm5 = new Permission("-*");

		assertEquals(PermissionResult.YES, perm1.compare(perm2));
		assertEquals(PermissionResult.UNKNOWN, perm1.compare(perm3));
		assertEquals(PermissionResult.UNKNOWN, perm2.compare(perm3));
		assertEquals(PermissionResult.NO, perm1.compare(perm4));
		assertEquals(PermissionResult.NO, perm2.compare(perm4));
		assertEquals(PermissionResult.NO, perm5.compare(perm1));
		assertEquals(PermissionResult.YES, perm5.compare(perm4));
	}
}
