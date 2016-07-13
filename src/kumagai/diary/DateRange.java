package kumagai.diary;

import java.util.*;

/**
 * 年・月による期間指定。
 * @author kumagai
 */
public class DateRange
{
	public final int startYear;
	public final int startMonth;
	public final int endYear;
	public final int endMonth;

	/**
	 * 年による期間指定を行う。
	 * @param startYear はじめの年
	 * @param endYear 終わりの年
	 */
	public DateRange(int startYear, int endYear)
	{
		this.startYear = startYear;
		this.startMonth = 1;
		this.endYear = endYear;
		this.endMonth = 12;
	}

	/**
	 * 年・月による期間指定を行う。
	 * @param startYear はじめの年
	 * @param startMonth はじめの月
	 * @param endYear 終わりの年
	 * @param endMonth 終わりの月
	 */
	public DateRange(int startYear, int startMonth, int endYear, int endMonth)
	{
		this.startYear = startYear;
		this.startMonth = startMonth;
		this.endYear = endYear;
		this.endMonth = endMonth;
	}

	/**
	 * オブジェクトの構築とともに、年・月による期間指定を行う。
	 * @param startYear はじめの年
	 * @param startMonth はじめの月
	 * @param endYear 終わりの年
	 * @param endMonth 終わりの月
	 */
	public DateRange
		(String startYear, String startMonth, String endYear, String endMonth)
	{
		Calendar calendar = Calendar.getInstance();

		this.startYear =
			startYear != null ?
				Integer.parseInt(startYear) :
				calendar.get(Calendar.YEAR);

		this.startMonth =
			startMonth != null ?
				Integer.parseInt(startMonth) :
				calendar.get(Calendar.MONTH) + 1;

		this.endYear =
			endYear != null ?
				Integer.parseInt(endYear) :
				calendar.get(Calendar.YEAR);

		this.endMonth =
			endMonth != null ?
				Integer.parseInt(endMonth) :
				calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return startYear + "/" + startMonth + " " + endYear + "/" + endMonth;
	}

	/**
	 * 開始・終了年月から範囲内の全年月を取得。
	 * @return 範囲内の全年月
	 */
	public String [] getYearMonths()
	{
		ArrayList<String> yearMonths = new ArrayList<String>();

		int year = startYear;
		int month = startMonth;

		for (;;)
		{
			yearMonths.add(String.format("%04d%02d", year, month));

			month++;

			if (month > 12)
			{
				// 繰り上がり。

				year++;
				month = 1;
			}

			if ((year == endYear && month > endMonth) || (year > endYear))
			{
				// 終了年月を超えた。

				break;
			}
		}

		return yearMonths.toArray(new String [] {});
	}
}
