package kumagai.diary;

import java.awt.*;
import java.util.*;

/**
 * DensityRectオブジェクトのコレクション。
 */
public class DensityRectCollection
	extends ArrayList<DensityRect>
{
	/**
	 * オブジェクトの構築とともに、日ごとの日記の分量からDensityRectオブジェクト
	 * の生成・コレクションを行う。
	 * @param volumePerDay 日ごとの日記の分量のコレクション
	 * @param startYear 開始年
	 * @param endYear 終了年
	 */
	public DensityRectCollection
		(HashMap <Calendar, Integer> volumePerDay, int startYear, int endYear)
	{
		for (int i=0 ; i<=endYear-startYear ; i++)
		{
			boolean slide = false;

			// 日付オブジェクト生成。
			GregorianCalendar calendar =
				new GregorianCalendar(startYear + i, 0, 1);

			// うるう年計算。
			int dayOfYear = calendar.isLeapYear(startYear + i) ? 366 : 365;

			int previousDensity = 0;
			int startDay = 0;

			// 終了処理のため、1多くループさせている。
			for (int j=0 ; j<dayOfYear+1 ; j++)
			{
				int density = 0;

				if (j < dayOfYear)
				{
					// 終了処理以外。正味データについて。

					if (volumePerDay.containsKey(calendar))
					{
						// 指定の日はある。

						// その日の値を取得。
						int count = volumePerDay.get(calendar);

						if (count > 0)
						{
							// １文字でも書いている。

							if (count >= 1000)
							{
								// 1000以上。

								density = 4;
							}
							else if (count >= 100)
							{
								// 100以上。

								density = 3;
							}
							else if (count >= 50)
							{
								// 50以上。

								density = 2;
							}
							else
							{
								// 50未満。

								density = 1;
							}
						}
					}
				}

				if (j == 0)
				{
					// 元日。

					startDay = j;
					previousDensity = density;
				}
				else
				{
					// １月２日以降。

					boolean notLeapDay =
						!calendar.isLeapYear(startYear + i) &&
						calendar.get(Calendar.MONTH) == 2 &&
						calendar.get(Calendar.DAY_OF_MONTH) == 1;

					if (previousDensity != density ||
						j>=dayOfYear ||
						notLeapDay)
					{
						// 区切りポイント。
						// 密度の違い・年末・うるう年ではない時。

						if (previousDensity > 0)
						{
							// 直前のブロックの密度が０より大きい。

							add(
								new DensityRect(
									new Point(startDay + (slide ? 1 : 0), i),
									j - startDay,
									previousDensity));
						}

						startDay = j;
						previousDensity = density;
					}

					if (notLeapDay)
					{
						// うるう年ではない。

						slide = true;
					}
				}

				// 次の日へ。
				if (j+1 < dayOfYear)
				{
					// 終了処理以外。正味データ。

					calendar.add(Calendar.DATE, 1);
				}
			}
		}
	}
}
