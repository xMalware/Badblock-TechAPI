package fr.badblock.api.common.minecraft.party;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Party player
 * 
 * @author xMalware
 *
 */
public class PartyPlayer {

	/**
	 * Follow
	 * 
	 * @param Set
	 *            the follow bool
	 * @return Returns the follow bool
	 */
	private boolean follow = true;

	/**
	 * Username
	 * 
	 * @param Set
	 *            the username
	 * @return Returns the username
	 */
	private String name;

	/**
	 * Role
	 * 
	 * @param Set
	 *            the role
	 * @return Returns the role
	 */
	private PartyPlayerRole role;

	/**
	 * State
	 * 
	 * @param Set
	 *            the state
	 * @return Returns the state
	 */
	private PartyPlayerState state;

	/**
	 * Constructor
	 * 
	 * @param Username
	 * @param Role
	 * @param State
	 */
	public PartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state) {
		// use constructor with lombok (with follow as default)
		this(true, name, role, state);
	}

	/**
	 * Returns the object as a database object
	 * 
	 * @return A database object
	 */
	public DBObject toObject() {
		// Create a basic database object
		BasicDBObject dbObject = new BasicDBObject();

		// Put the name
		dbObject.put("name", getName());
		// Put the role
		dbObject.put("role", role != null ? role.name() : null);
		// Put the state
		dbObject.put("state", state != null ? state.name() : null);
		// Put the follow state
		dbObject.put("follow", follow);

		// Returns the database object
		return dbObject;
	}

}
