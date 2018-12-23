package fr.badblock.api.common.minecraft;

/**
 * 
 * PM Privacy
 * 
 * @author xMalware
 *
 */
public enum PMPrivacy {

	/**
	 * Accept PM from anyone
	 */
	WITH_EVERYONE,

	/**
	 * Don't accept PM
	 */
	WITH_NOBODY,

	/**
	 * Accept PM only from his friends
	 */
	WITH_ONLY_HIS_FRIENDS;

	/**
	 * Get a possibility from a string
	 * 
	 * @param string
	 * @return
	 */
	public static PMPrivacy getByString(String string) {
		// For each existing possibility
		for (PMPrivacy pmPrivacy : values()) {
			// We check if the existing possibility is the one requested
			if (string.equalsIgnoreCase(pmPrivacy.name())) {
				// So we return the exact possibility
				return pmPrivacy;
			}
		}

		// No possibility to return, we return nothing
		return null;
	}

}