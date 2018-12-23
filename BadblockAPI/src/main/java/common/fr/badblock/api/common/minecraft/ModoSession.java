package fr.badblock.api.common.minecraft;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ModoSession {

	private String playerName;
	private String playerUuid;
	private long timestamp;
	private long startTime;
	private long endTime;
	private long totalTime;
	private long punishments;
	private long punishmentTime;

	public void incrementPunishment() {
		setPunishments(getPunishments() + 1);
	}

	public DBObject toDatabaseObject() {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("playerName", getPlayerName());
		dbObject.put("playerUuid", getPlayerUuid());
		dbObject.put("timestamp", getTimestamp());
		dbObject.put("startTime", getStartTime());
		dbObject.put("endTime", getEndTime());
		dbObject.put("totalTime", getTotalTime());
		dbObject.put("punishments", getPunishments());
		dbObject.put("punishmentTime", getPunishmentTime());
		return dbObject;
	}

}