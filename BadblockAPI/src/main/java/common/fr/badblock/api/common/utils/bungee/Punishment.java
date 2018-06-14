package fr.badblock.api.common.utils.bungee;

import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Punishment {
	
	@SuppressWarnings("serial")
	private static Type t = new TypeToken<String[]>() {}.getType();

	private String		uuid;
	private String		punishedUuid;
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
		punishedUuid = jsonObject.get("punishedUuid").getAsString();
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

	public Punishment(DBObject dbObject)
	{
		uuid = dbObject.get("uuid").toString();
		punishedUuid = dbObject.get("punishedUuid").toString();
		punishedIp = dbObject.get("punishedIp").toString();
		type = PunishType.getFromString(dbObject.get("type").toString());
		timestamp = Long.parseLong(dbObject.get("timestamp").toString());
		expire = Long.parseLong(dbObject.get("expire").toString());
		date = dbObject.get("date").toString();
		reason = dbObject.get("reason").toString();
		isReasonKey = Boolean.parseBoolean(dbObject.get("isReasonKey").toString());
		proof = GsonUtils.getGson().fromJson(dbObject.get("proof").toString(), t);
		punisher = dbObject.get("punisher").toString();
		punisherIp = dbObject.get("punisherIp").toString();
	}

	public DBObject toObject()
	{
		DBObject dbObject = new BasicDBObject();

		dbObject.put("uuid", getUuid());
		dbObject.put("punished", getPunishedUuid());
		dbObject.put("punishedIp", getPunishedIp());
		dbObject.put("type", getType().name());
		dbObject.put("timestamp", getTimestamp());
		dbObject.put("expire", getExpire());
		dbObject.put("date", getDate());
		dbObject.put("reason", getReason());
		dbObject.put("isReasonKey", isReasonKey());
		dbObject.put("proof", getProof());
		dbObject.put("punisher", getPunisher());
		dbObject.put("punisherIp", getPunisherIp());

		return dbObject;
	}

}