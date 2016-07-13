package kumagai.diary;

import java.util.*;

/**
 * 年とカウント情報のコレクション。
 * @author kumagai
 */
public class YearCountCollection
	extends ArrayList<YearCount>
{
	/**
	 * 年とカウント情報のコレクションを構築。
	 * @param results 検索結果
	 */
	public YearCountCollection(ArrayList<SearchResultDay> results)
	{
		for (SearchResultDay result : results)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(result.date);
			int year = calendar.get(Calendar.YEAR);

			boolean find = false;

			for (YearCount yearCount : this)
			{
				if (yearCount.getYear() == year)
				{
					// 年は一致する。

					yearCount.incrementCount();
					find = true;
					break;
				}
			}

			if (! find)
			{
				// 初出。

				add(new YearCount(year, 1));
			}
		}
	}
}
