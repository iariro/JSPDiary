package kumagai.diary.dayone;

import java.util.*;

/**
 * JSONトップ要素
 */
public class DayOneJson
{
	public Metadata metadata;
	public ArrayList<Entry> entries;
}

/**
 * metadataオブジェクト
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
 * entries配列の要素
 */
class Entry
{
	public ArrayList<String> tags;
	public String text;
	public String creationDate;
	public Location location;
}

/**
 * locationオブジェクト
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
		return String.format("%s %s %s", administrativeArea, localityName, placeName);
	}
}
