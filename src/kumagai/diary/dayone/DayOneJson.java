package kumagai.diary.dayone;

import java.util.*;

/**
 * JSON�g�b�v�v�f
 */
public class DayOneJson
{
	public Metadata metadata;
	public ArrayList<Entry> entries;
}

/**
 * metadata�I�u�W�F�N�g
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
 * entries�z��̗v�f
 */
class Entry
{
	public ArrayList<String> tags;
	public String text;
	public String creationDate;
	public Location location;
}

/**
 * location�I�u�W�F�N�g
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
