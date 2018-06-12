package fr.badblock.api.common.utils.bungee;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.api.common.utils.time.Time;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
public class Punished
{

	private boolean ban, mute;

	@Getter @Setter
	private long	banEnd, muteEnd;

	@Getter @Setter
	private String	banReason, muteReason;

	@Getter @Setter
	private String	banner, muter;

	@Getter @Setter
	private UUID	banId;
	
	@Getter @Setter
	private UUID	muteId;

	public Punished()
	{
		ban 	   = false;
		mute 	   = false;
		banEnd     = -1;
		muteEnd    = -1;
		banReason  = null;
		muteReason = null;
		banner     = null;
		muter	   = null;
	}

	public Punished(JsonObject jsonObject)
	{
		// Shitty workaround
		if (jsonObject.has("ban") && !jsonObject.get("ban").isJsonNull())
		{
			ban = jsonObject.get("ban").getAsBoolean();
		}
		if (jsonObject.has("mute") && !jsonObject.get("mute").isJsonNull())
		{
			mute = jsonObject.get("mute").getAsBoolean();
		}
		if (jsonObject.has("banEnd")&& !jsonObject.get("banEnd").isJsonNull())
		{
			banEnd = jsonObject.get("banEnd").getAsLong();
		}
		if (jsonObject.has("muteEnd") && !jsonObject.get("muteEnd").isJsonNull())
		{
			muteEnd = jsonObject.get("muteEnd").getAsLong();
		}
		if (jsonObject.has("banReason") && !jsonObject.get("banReason").isJsonNull())
		{
			banReason = jsonObject.get("banReason").getAsString();
		}
		if (jsonObject.has("muteReason") && !jsonObject.get("muteReason").isJsonNull())
		{
			muteReason = jsonObject.get("muteReason").getAsString();
		}
		if (jsonObject.has("banner") && !jsonObject.get("banner").isJsonNull())
		{
			banner = jsonObject.get("banner").getAsString();
		}
		if (jsonObject.has("muter") && !jsonObject.get("muter").isJsonNull())
		{
			muter = jsonObject.get("muter").getAsString();
		}
		if (jsonObject.has("banId")&& !jsonObject.get("banId").isJsonNull())
		{
			banId = UUID.fromString(jsonObject.get("banId").getAsString());
		}
		if (jsonObject.has("muteId")&& !jsonObject.get("muteId").isJsonNull())
		{
			muteId = UUID.fromString(jsonObject.get("muteId").getAsString());
		}
	}

	public DBObject getDBObject()
	{
		BasicDBObject query = new BasicDBObject();
		query.put("ban", ban);
		query.put("mute", mute);
		query.put("banEnd", banEnd);
		query.put("muteEnd", muteEnd);
		query.put("banReason", banReason);
		query.put("muteReason", muteReason);
		query.put("banner", banner);
		query.put("muter", muter);
		query.put("banId", banId);
		query.put("muteId", muteId);
		return query;
	}

	public void checkEnd()
	{
		if (ban && banEnd != -1 && banEnd < System.currentTimeMillis())
		{
			ban 	  = false;
			banEnd 	  = -1;
			banReason = null;
			banner 	  = null;
		}

		if (mute && muteEnd != -1 && muteEnd < System.currentTimeMillis())
		{
			mute 	   = false;
			muteEnd    = -1;
			muteReason = null;
			muter 	   = null;
		}
	}

	public boolean isBan()
	{
		return ban && banEnd > TimeUtils.time();
	}
	
	public boolean isMute()
	{
		return mute && muteEnd > TimeUtils.time();
	}
	
	public String buildBanTime(Locale locale)
	{
		if(banEnd != -1){
			return Time.MILLIS_SECOND.toFrench(banEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}

	public String buildMuteTime(Locale locale)
	{
		if(muteEnd != -1){
			return Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}

	public String[] buildMuteReason(Locale locale)
	{
		String time = "";
		if (muteEnd != -1)
		{
			time = I18n.getInstance().get(locale, "punishments.for")[0] + Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		}
		return I18n.getInstance().get(locale, "punishments.youvebeenmute", muteReason, time);
	}

}
