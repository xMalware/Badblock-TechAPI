package fr.badblock.api.common.utils.bungee;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.api.common.utils.time.Time;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class Punished
{

	private Punishment	ban;

	private Punishment	mute;

	private Punishment	warn;

	public Punished()
	{
	}

	public Punished(JsonObject jsonObject)
	{
		if (jsonObject.has("ban") && !jsonObject.get("ban").isJsonNull())
		{
			ban = new Punishment(jsonObject.get("ban").getAsJsonObject());
		}
		
		if (jsonObject.has("mute") && !jsonObject.get("mute").isJsonNull())
		{
			mute = new Punishment(jsonObject.get("mute").getAsJsonObject());
		}
		
		if (jsonObject.has("warn") && !jsonObject.get("warn").isJsonNull())
		{
			warn = new Punishment(jsonObject.get("warn").getAsJsonObject());
		}
	}

	public DBObject getDBObject()
	{
		BasicDBObject query = new BasicDBObject();
		query.put("ban", ban != null ? ban.toObject() : null);
		query.put("mute", mute != null ? mute.toObject() : null);
		query.put("warn", warn != null ? warn.toObject() : null);
		return query;
	}
	
	public boolean isBan()
	{
		return ban != null && ban.getType().equals(PunishType.BAN) &&
				(ban.getExpire() == -1 || ban.getExpire() > TimeUtils.time());
	}
	
	public boolean isMute()
	{
		return mute != null && mute.getType().equals(PunishType.MUTE) && mute.getExpire() > TimeUtils.time();
	}
	
	public String buildBanTime(Locale locale)
	{
		if(ban != null && ban.getExpire() != -1){
			return Time.MILLIS_SECOND.toFrench(ban.getExpire() - TimeUtils.time(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}
	
	public String buildMuteTime(Locale locale)
	{
		if(mute != null && mute.getExpire() != -1){
			return Time.MILLIS_SECOND.toFrench(mute.getExpire() - TimeUtils.time(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}

}