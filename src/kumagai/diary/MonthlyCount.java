package kumagai.diary;

import java.io.*;
import java.util.*;
import sun.misc.*;
import ktool.crypt.*;

/**
 * 月ごとのカウント配列。
 */
public class MonthlyCount
	extends ArrayList<Integer>
{
	/**
	 * MonthlyCountテストプログラム。
	 * @param args 0=path/1=password/2=startYear/3=endYear/4=category
	 */
	public static void main(String[] args)
		throws Exception
	{
		int startYear = Integer.parseInt(args[2]);
		int endYear = Integer.parseInt(args[3]);

		// 日記ファイル読み込み。
		DiaryDocumentCollectionFromDirectory diaries =
			new DiaryDocumentCollectionFromDirectory(
				args[0],
				null,
				args[1],
				new DateRange(startYear, endYear));

		DesDecryptCipher cipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(args[1]));
		byte [] categoryBytes = new BASE64Decoder().decodeBuffer(args[4]);
		InputStream stream = new ByteArrayInputStream(categoryBytes);
		InputStream stream2 = cipher.createInputStream(stream);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream2));
		String category = reader.readLine();

		ArrayList<SearchResultDay> results =
			diaries.search(category, null, true);

		MonthlyCount monthly = new MonthlyCount(results, startYear, endYear);

		int year = startYear;
		int month = 1;

		System.out.printf("     ");
		for (int i=1 ; i<=12 ; i++)
		{
			System.out.printf(" %2d月", i);
		}
		System.out.println();

		int maxOnYear = 0;

		for (int i=0 ; i<monthly.size() ; i++)
		{
			int count = monthly.get(i);

			if (month == 1)
			{
				// １月。

				maxOnYear = 0;

				for (int j=0 ; j<12 ; j++)
				{
					if (monthly.get(i + j) > 0)
					{
						// １件でもある。

						if (maxOnYear < j)
						{
							// 現在の最大を超える。

							maxOnYear = j;
						}
					}
				}

				System.out.printf("%4d ", year);
			}

			if (count > 0)
			{
				// １件でもある。

				System.out.printf("  %3d", count);
			}
			else
			{
				// １件もない。

				if (i % 12 < maxOnYear)
				{
					// 最大月以内。

					System.out.print("     ");
				}
			}

			month++;

			if (month > 12)
			{
				// １２月を越えた。

				System.out.println();

				year++;
				month = 1;
			}
		}
	}

	/**
	 * 月ごとのカウント配列を生成。
	 * @param data 集計対象
	 * @param startYear 開始年
	 * @param endYear 終了年
	 */
	public MonthlyCount
		(ArrayList<SearchResultDay> data, int startYear, int endYear)
	{
		for (int i=startYear ; i<=endYear ; i++)
		{
			for (int j=0 ; j<12 ; j++)
			{
				add(0);
			}
		}

		GregorianCalendar calendar = new GregorianCalendar();

		for (int i=0 ; i<data.size() ; i++)
		{
			calendar.setTime(data.get(i).date);

			int month =
				(calendar.get(Calendar.YEAR) - startYear) * 12 +
				calendar.get(Calendar.MONTH);

			set(month, get(month) + 1);
		}
	}
}
