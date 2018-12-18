package fr.badblock.api.common.minecraft.party;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.common.utils.data.Callback;

public class PartySyncManager
{

	private MongoService mongoService;
	
	public PartySyncManager(MongoService mongoService)
	{
		this.mongoService = mongoService;
	}

	/**
	 * Update party data
	 * 
	 * @param party
	 */
	public void update(Party party) {
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("parties");
				// New query
				BasicDBObject query = new BasicDBObject();

				// Set the id
				query.put("uuid", party.getUuid());

				// Set the updater as a setter
				BasicDBObject updater = new BasicDBObject("$set", party.toObject());

				// Update in the collection
				collection.update(query, updater);
			}
		});
	}
	
	/**
	 * Remove party data
	 * 
	 * @param party
	 */
	public void delete(Party party) {
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("parties");
				// New query
				BasicDBObject query = new BasicDBObject();

				// Set the id
				query.put("uuid", party.getUuid());

				// Update in the collection
				collection.remove(query);
			}
		});
	}

	/**
	 * Get the party
	 * 
	 * @param player
	 * @param callback
	 */
	public void getParty(String player, Callback<Party> callback) {
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Use asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("parties");
				// Create empty query
				DBObject query = new BasicDBObject("players." + player.toLowerCase(),
						new BasicDBObject("$exists", true));
				// Cursor
				DBCursor cursor = collection.find(query);

				// If the cursor isn't null
				if (cursor != null && cursor.hasNext()) {
					// Get the DBObject
					BasicDBObject dbObject = (BasicDBObject) cursor.next();
					BasicDBObject players = (BasicDBObject) dbObject.get("players");
					
					BasicDBObject obj = (BasicDBObject) players.get(player.toLowerCase());
					String s = obj.getString("state");
					
					if (s != null && !s.equals("ACCEPTED"))
					{
						callback.done(null, null);
						return;
					}
					
					// Done callback
					callback.done(new Party(dbObject), null);
				} else {
					// Empty callback
					callback.done(null, null);
				}
			}
		});
	}

	/**
	 * In group
	 * 
	 * @param player
	 * @param callback
	 */
	public void inGroup(String player, Callback<Boolean> callback) {
		// Set to lower case username
		player = player.toLowerCase();
		// Get party
		getParty(player, new Callback<Party>() {

			/**
			 * When we receive the party data
			 */
			@Override
			public void done(Party result, Throwable error) {
				// Callback
				callback.done(result != null, null);
			}

		});
	}

	/**
	 * Insert the party
	 * 
	 * @param party
	 */
	public void insert(Party party) {
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get database
				DB db = mongoService.getDb();
				// Get collection
				DBCollection collection = db.getCollection("parties");
				// Insert the collection
				collection.insert(party.toObject());
			}
		});
	}
	
}