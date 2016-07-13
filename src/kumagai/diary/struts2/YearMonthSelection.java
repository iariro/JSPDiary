package kumagai.diary.struts2;

import ktool.datetime.*;

/**
 * 年月ドロップダウン用。
 * @author kumagai
 */
public class YearMonthSelection
{
	public IntAndString [] years;
	public IntAndString [] months;

	public int birthYear = 1976;
	public int birthMonth = 3;
	public int beforeYear;
	public int beforeMonth;
	public int todayYear;
	public int todayMonth;

	/**
	 * データ構築。
	 */
	public YearMonthSelection()
	{
		DateTime today = new DateTime();

		// 先月を計算。
		DateTime before = new DateTime(today);
		before.setDay(1);
		before.add(TimeSpan.createByDay(-1));

		beforeYear = before.getYear();
		beforeMonth = before.getMonth();

		todayYear = today.getYear();
		todayMonth = today.getMonth();

		years = new IntAndString [todayYear - birthYear + 1];

		for (int i=0 ; i<=todayYear - birthYear ; i++)
		{
			years[i] =
				new IntAndString
					(birthYear + i, String.format("%d年", birthYear + i));
		}

		months = new IntAndString [12];

		for (int i=0 ; i<12 ; i++)
		{
			months[i] =
				new IntAndString(i + 1, String.format("%d月", i + 1));
		}
	}
}
