package fr.badblock.api.common.utils.bungee;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Punishment {

	private String		uuid;
	private String		punished;
	private String		punishedIp;
	private PunishType	type;
	private long		timestamp;
	private long		expire;
	private String		date;
	private String		reason;
	private boolean		isReasonKey;
	private String[]	proof;
	private String		punisher;
	private String		punisherIp;

	public Punishment(JsonObject jsonObject)
	{
		uuid = jsonObject.get("uuid").getAsString();
		punished = jsonObject.get("punished").getAsString();
		punishedIp = jsonObject.get("punishedIp").getAsString();
		type = PunishType.getFromString(jsonObject.get("type").getAsString());
		timestamp = jsonObject.get("timestamp").getAsLong();
		expire = jsonObject.get("expire").getAsLong();
		date = jsonObject.get("date").getAsString();
		reason = jsonObject.get("reason").getAsString();
		isReasonKey = jsonObject.get("isReasonKey").getAsBoolean();
		if (!jsonObject.get("proof").isJsonNull())
		{
			proof = new String[jsonObject.get("proof").getAsJsonArray().size()];
			for (int i = 0; i < proof.length; i++)
			{
				proof[i] = jsonObject.get("proof").getAsJsonArray().get(i).getAsString();
			}
		}
		punisher = jsonObject.get("punisher").getAsString();
		punisherIp = jsonObject.get("punisherIp").getAsString();
	}

	public DBObject toObject()
	{
		DBObject dbObject = new BasicDBObject();

		dbObject.put("uuid", getUuid());
		dbObject.put("punished", getPunished());
		dbObject.put("punishedIp", getPunishedIp());
		dbObject.put("type", getType().name());
		dbObject.put("timestamp", getTimestamp());
		dbObject.put("expire", getExpire());
		dbObject.put("date", getDate());
		dbObject.put("reason", getReason());
		dbObject.put("isReasonKey", isReasonKey());
		dbObject.put("proof", getProof());
		dbObject.put("punisher", getPunished());
		dbObject.put("punisherIp", getPunisherIp());

		return dbObject;
	}

}