package fr.badblock.api.common.minecraft.friends;

/**
 * 
 * Listing the possibilities of the legibility to be added as a friend
 * 
 * @author xMalware
 *
 */
public enum FriendListable {

	// No, a player can't add the other player
	NO,
	// Yes, a player can add the other player
	YES;

	/**
	 * Get a possibility from a string
	 * 
	 * @param string
	 * @return
	 */
	public static FriendListable getByString(String string) {
		// For each existing possibility
		for (FriendListable friendListable : values()) {
			// We check if the existing possibility is the one requested
			if (string.equalsIgnoreCase(friendListable.name())) {
				// So we return the exact possibility
				return friendListable;
			}
		}

		// No possibility to return, we return nothing
		return null;
	}

}
