package kumagai.diary;

/**
 * 年ごとの件数情報。
 * @author kumagai
 */
public class MonthlyCountTableRow
{
	public final int year;
	public final int [] count;

	/**
	 * 配列初期化。
	 * @param year 年
	 */
	public MonthlyCountTableRow(int year)
	{
		this.year = year;
		this.count = new int [12];
	}

	/**
	 * カウントをセット。
	 * @param month 月
	 * @param count カウント
	 */
	public void setCount(int month, int count)
	{
		this.count[month - 1] = count;
	}

	/**
	 * カウント合計を取得。
	 * @return カウント合計
	 */
	public int getTotalCount()
	{
		int total = 0;

		for (int i=0 ; i<count.length ; i++)
		{
			total += count[i];
		}

		return total;
	}
}
