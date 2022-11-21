package kumagai.diary.dayone;

import java.util.ArrayList;

/**
 * JSONデシリアライズ型
 */
public class DayOneJson
{
	public Metadata metadata;
	public ArrayList<Entry> entries;
}

/**
 * metadataデシリアライズ型
 */
class Metadata
{
	public String version;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return version;
	}
}

/**
 * entriesデシリアライズ型
 */
class Entry
{
	public ArrayList<String> tags;
	public String text;
	public String creationDate;
	public Location location;
}

/**
 * locationデシリアライズ型
 */
class Location
{
	public String country;
	public String administrativeArea;
	public String localityName;
	public String placeName;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    if (administrativeArea == null &&
	        localityName == null &&
	        placeName == null)
	    {
	        return "";
	    }

	    return String.format("%s %s %s", administrativeArea, localityName, placeName);
	}
}
