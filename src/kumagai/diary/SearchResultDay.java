package kumagai.diary;

import java.text.*;
import java.util.*;

/**
 * １日分の検索結果本文。
 * @author kumagai
 */
public class SearchResultDay
	extends ArrayList<SearchResult>
{
	static public final SimpleDateFormat dateFormat1;
	static public final SimpleDateFormat dateFormat2;
	static public final SimpleDateFormat dateFormatDayOne;

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		dateFormat1 = new SimpleDateFormat();
		dateFormat2 = new SimpleDateFormat();
		dateFormatDayOne = new SimpleDateFormat();

		dateFormat1.applyPattern("yyyy/MM/dd");
		dateFormat2.applyPattern("yyyy/MM/dd（E）");
		dateFormatDayOne.applyPattern("yyyy年M月d日");
	}

	public final Date date;

	/**
	 * 日付の割り当て。
	 * @param dateString 日付
	 * @throws ParseException
	 */
	public SearchResultDay(String dateString)
		throws ParseException
	{
		this.date = dateFormat1.parse(dateString);
	}

	/**
	 * 日付の割り当て。
	 * @param date 日付
	 * @throws ParseException
	 */
	public SearchResultDay(Date date)
		throws ParseException
	{
		this.date = date;
	}

	/**
	 * 日付を文字列として取得。
	 * @return 文字列形式による日付
	 */
	public String date()
	{
		return dateFormat2.format(date);
	}

	/**
	 * 日付を文字列として取得。
	 * @return 文字列形式による日付
	 */
	public String date1()
	{
		return dateFormat1.format(date);
	}

	/**
	 * 日付を文字列として取得。
	 * @return 文字列形式による日付
	 */
	public String getDate()
	{
		return date();
	}

	/**
	 * 日付を文字列として取得。
	 * @return 文字列形式による日付
	 */
	public String getDate1()
	{
		return date1();
	}

	/**
	 * 日付を文字列として取得。
	 * @return 文字列形式による日付
	 */
	public String getDateForDayOne()
	{
		return dateFormatDayOne.format(date);
	}

	/**
	 * 今日であるかを判定。
	 * @param minus 0=今日／1=昨日／…
	 * @return true=今日である
	 */
	public boolean isToday(int minus)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DAY_OF_MONTH, minus);

		return
			calendar1.get(Calendar.YEAR) ==
			calendar2.get(Calendar.YEAR) &&
			calendar1.get(Calendar.MONTH) ==
			calendar2.get(Calendar.MONTH) &&
			calendar1.get(Calendar.DAY_OF_MONTH) ==
			calendar2.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString()
	{
		String ret = dateFormat2.format(date) + "\n";

		for (int i=0 ; i<size() ; i++)
		{
			SearchResult result = get(i);
			ret += result;
		}

		return ret;
	}
}
