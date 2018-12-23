package fr.badblock.api.common.utils.net;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * IPHub object
 * 
 * @author xMalware
 *
 */
public class IPHubObject {

	/**
	 * ASN
	 * 
	 * @param Set
	 *            the new ASN
	 * @return Returns the ASN
	 */
	private int asn;

	/**
	 * Block
	 * 
	 * @param Set
	 *            the new block id
	 * @return Returns the new block id
	 */
	private int block;

	/**
	 * Country code
	 * 
	 * @param Set
	 *            the new country code
	 * @return Returns the country code
	 */
	private String countryCode;

	/**
	 * Country name
	 * 
	 * @param Set
	 *            the new country name
	 * @return Returns the country name
	 */
	private String countryName;

	/**
	 * IP
	 * 
	 * @param Set
	 *            the new IP
	 * @return Returns the current IP
	 */
	private String ip;

	/**
	 * ISP
	 * 
	 * @param Set
	 *            the new ISP
	 * @return Returns the ISP
	 */
	private String isp;

	public IPHubObject(JsonObject jsonObject) {
		// Set asn
		asn = jsonObject.get("asn").getAsInt();
		// Set block
		block = jsonObject.get("asn").getAsInt();
		// Set country code
		countryCode = jsonObject.get("countryCode").getAsString();
		// Set country name
		countryName = jsonObject.get("countryName").getAsString();
		// Set ip
		ip = jsonObject.get("ip").getAsString();
		// Set ISP
		isp = jsonObject.get("isp").getAsString();
	}

	/**
	 * To database object
	 * 
	 * @return
	 */
	public DBObject toDbObject() {
		// Create a database object
		DBObject dbObject = new BasicDBObject();
		// Put ASN
		dbObject.put("asn", asn);
		// Put block
		dbObject.put("block", block);
		// Put country code
		dbObject.put("countryCode", countryCode);
		// Put country name
		dbObject.put("countryName", countryName);
		// Put IP
		dbObject.put("ip", ip);
		// Put ISP
		dbObject.put("isp", isp);
		// Returns the database object
		return dbObject;
	}

}