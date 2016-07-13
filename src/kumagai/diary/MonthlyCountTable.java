package kumagai.diary;

import java.util.*;

/**
 * 月毎の件数情報。
 */
public class MonthlyCountTable
	extends ArrayList<MonthlyCountTableRow>
{
	public int [] monthlySum = new int [12];

	/**
	 * 月毎の件数情報を構築。
	 * @param monthlyCount 月毎カウント配列
	 * @param startYear 開始年
	 */
	public MonthlyCountTable(MonthlyCount monthlyCount, int startYear)
	{
		int year = startYear;

		for (int i=0 ; i<monthlyCount.size() ; i+=12)
		{
			MonthlyCountTableRow monthCount = new MonthlyCountTableRow(year);
			add(monthCount);

			for (int j=0 ; j<12 ; j++)
			{
				monthCount.setCount(j + 1, monthlyCount.get(i + j));
				monthlySum[j] += monthlyCount.get(i + j);
			}

			year++;
		}
	}
}
