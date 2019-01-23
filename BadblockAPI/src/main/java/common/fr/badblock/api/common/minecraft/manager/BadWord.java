package fr.badblock.api.common.minecraft.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BadWord {

	private String byPlayerName;
	private String playerName;
	private String message;
	private long timestamp;
	private String date;
	private boolean processed;
	private boolean punished;

	public DBObject toDatabaseObject() {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("byPlayerName", getByPlayerName());
		dbObject.put("playerName", getPlayerName());
		dbObject.put("message", getMessage());
		dbObject.put("timestamp", getTimestamp());
		dbObject.put("date", getDate());
		dbObject.put("processed", isProcessed());
		dbObject.put("punished", isPunished());
		return dbObject;
	}

}
