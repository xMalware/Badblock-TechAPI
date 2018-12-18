package fr.badblock.api.common.minecraft.party;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Party object
 * 
 * @author xMalware
 *
 */
public class Party {

	private static Map<MongoService, PartySyncManager> partyManagers = new HashMap<>();
	
	/**
	 * Map type
	 */
	private static final Type collectionType = new TypeToken<Map<String, PartyPlayer>>() {
	}.getType();

	/**
	 * Players in the party
	 * 
	 * @param Set
	 *            the party
	 * @return Returns the party
	 */
	public Map<String, PartyPlayer> players;

	/**
	 * Unique ID
	 * 
	 * @param Set
	 *            the unique ID
	 * @return Returns the unique ID
	 */
	public String uuid;

	/**
	 * Constructor
	 * 
	 * @param Database
	 *            object
	 */
	public Party(DBObject dbObject) {
		// Set the unique id
		uuid = dbObject.get("uuid").toString();
		// Set the player map
		players = GsonUtils.getGson().fromJson(dbObject.get("players").toString(), collectionType);
	}

	/**
	 * Constructor
	 * 
	 * @param Leader
	 *            of the party
	 */
	public Party(MongoService mongoService, String leader) {
		// Set UUID
		uuid = UUID.randomUUID().toString();
		// Create a new map
		players = new HashMap<>();
		// Add the leader as admin
		add(mongoService, leader, PartyPlayerRole.ADMIN);
	}

	/**
	 * Constructor
	 * 
	 * @param Leader
	 *            of the party
	 * @param Invited
	 *            player
	 */
	public Party(MongoService mongoService, String leader, String invited) {
		// Do the first thing
		this(mongoService, leader);
		// Invite as a default player
		invite(mongoService, invited, PartyPlayerRole.DEFAULT);
	}

	/**
	 * Accept a player
	 * 
	 * @param Username
	 */
	public void accept(MongoService mongoService, String name) {
		// Add the player
		add(mongoService, name, getPartyPlayer(name).getRole());
	}

	/**
	 * Add a player
	 * 
	 * @param Username
	 * @param Player
	 *            party role
	 */
	public void add(MongoService mongoService, String name, PartyPlayerRole role) {
		// Add the player
		add(mongoService, name, role, PartyPlayerState.ACCEPTED);
	}

	/**
	 * Add a player in the party
	 * 
	 * @param Name
	 *            of the player
	 * @param Role
	 *            of the player
	 * @param Party
	 *            state of the player
	 */
	public void add(MongoService mongoService, String name, PartyPlayerRole role, PartyPlayerState state) {
		// Set the name to lower case
		name = name.toLowerCase();
		// If the player is in the list
		if (players.containsKey(name)) {
			// Then remove the player
			players.remove(name);
		}
		// Create a party player
		PartyPlayer partyPlayer = toPartyPlayer(name, role, state);
		// Add the party player in the map
		players.put(name, partyPlayer);
		// Save the party
		save(mongoService);
	}

	/**
	 * Get the party player
	 * 
	 * @param The
	 *            username
	 * @return Returns the PartyPlayer object
	 */
	public PartyPlayer getPartyPlayer(String name) {
		// Get the PartyPlayer object
		return getPlayers().get(name.toLowerCase());
	}

	/**
	 * Invite a player
	 * 
	 * @param Username
	 * @param Player
	 *            party role
	 */
	public void invite(MongoService mongoService, String name, PartyPlayerRole role) {
		// Add the player
		add(mongoService, name, role, PartyPlayerState.WAITING);
	}

	/**
	 * Remove the party
	 */
	public void remove(MongoService mongoService) {
		// If the unique id is null
		if (uuid == null) {
			// So we stop there
			return;
		}

		PartySyncManager partyManager = null;
		if (!partyManagers.containsKey(mongoService))
		{
			partyManager = new PartySyncManager(mongoService);
			partyManagers.put(mongoService, partyManager);
		}
		else
		{
			partyManager = partyManagers.get(mongoService);
		}
		
		// Remove the party
		partyManager.delete(this);
	}

	/**
	 * Removethe player
	 * 
	 * @param Username
	 */
	public void remove(MongoService mongoService, String name) {
		// Set the username to lower case
		name = name.toLowerCase();
		// Remove the username from the map
		players.remove(name);
		// Save the party
		save(mongoService);
	}

	/**
	 * Save the party
	 */
	public void save(MongoService mongoService) {
		// If the unique id is null
		if (uuid == null) {
			// So we stop there
			return;
		}

		PartySyncManager partyManager = null;
		if (!partyManagers.containsKey(mongoService))
		{
			partyManager = new PartySyncManager(mongoService);
			partyManagers.put(mongoService, partyManager);
		}
		else
		{
			partyManager = partyManagers.get(mongoService);
		}
		
		// Update the party
		partyManager.update(this);
	}

	/**
	 * Set user role
	 * 
	 * @param name
	 * @param partyPlayerRole
	 */
	public void setRole(MongoService mongoService, PartyPlayer partyPlayer, PartyPlayerRole partyPlayerRole) {
		// Null player
		assert partyPlayer == null;
		// Unknown player
		assert !players.containsKey(partyPlayer.getName().toLowerCase());
		// Unknown role
		assert partyPlayerRole == null;

		// Set player role
		partyPlayer.setRole(partyPlayerRole);

		// Put in map
		players.put(partyPlayer.getName().toLowerCase(), partyPlayer);

		// Save the party
		save(mongoService);
	}

	/**
	 * To object
	 * 
	 * @return A DBObject object
	 */
	public DBObject toObject() {
		/**
		 * Create a new object
		 */
		BasicDBObject object = new BasicDBObject();

		// If the unique id isn't null
		if (uuid != null) {
			// Put the unique id
			object.put("uuid", uuid);
		}

		// Create a map
		Map<String, DBObject> map = new HashMap<>();

		// Put all players in the map
		players.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue().toObject()));

		// Put the player map
		object.put("players", map);

		// Returns the object
		return object;
	}

	/**
	 * To Party Player
	 * 
	 * @param Name
	 *            of the player
	 * @param Role
	 *            of the player
	 * @param Party
	 *            state of the player
	 * @return Returns the PartyPlayer object
	 */
	private PartyPlayer toPartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state) {
		// Create a new PartyPlayer object
		return new PartyPlayer(name, role, state);
	}

}