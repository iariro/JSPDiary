package kumagai.diary.struts2;

/**
 * 店とカウント情報。
 * @author kumagai
 */
public class MiseAndCount
{
	public final String mise;
	public final int count;

	/**
	 * 値をメンバーに割り当てる。
	 * @param mise 店名
	 * @param count カウント
	 */
	public MiseAndCount(String mise, int count)
	{
		this.mise = mise;
		this.count = count;
	}
}
