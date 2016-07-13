package kumagai.diary;

import java.util.*;

/**
 * 検索結果から生成可能な日ごとの日記の分量。
 */
public class VolumePerDayFromSearchResultDay
	extends HashMap <Calendar, Integer>
{
	/**
	 * オブジェクトの構築とともに、指定の検索結果について日ごとの日記の分量を求
	 * める。
	 *
	 * @param results 検索結果
	 */
	public VolumePerDayFromSearchResultDay(ArrayList<SearchResultDay> results)
	{
		for (SearchResultDay resultDay : results)
		{
			int count = 0;

			for (SearchResult result : resultDay)
			{
				for (String line : result.lines)
				{
					count += line.length();
				}
			}

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(resultDay.date);
			put(calendar, count);
		}
	}
}
