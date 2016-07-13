package kumagai.diary;

/**
 * 年とカウント情報。
 * @author kumagai
 */
public class YearCount
{
	private final int year;
	private int count;

	/**
	 * 値をメンバーに割り当てる。
	 * @param year 年
	 * @param count カウント
	 */
	public YearCount(int year, int count)
	{
		this.year = year;
		this.count = count;
	}

	/**
	 * カウントをインクリメント。
	 */
	public void incrementCount()
	{
		count++;
	}

	/**
	 * 年を取得。
	 * @return 年
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * カウントを取得。
	 * @return カウント
	 */
	public int getCount()
	{
		return count;
	}
}
