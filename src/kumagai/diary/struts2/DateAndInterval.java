package kumagai.diary.struts2;

/**
 * 日付と間隔情報。
 * @author kumagai
 */
public class DateAndInterval
{
	public String date;
	public String interval;

	/**
	 * 値をメンバに割り当てる。
	 * @param date 日付
	 * @param interval 間隔
	 */
	public DateAndInterval(String date, String interval)
	{
		this.date = date;
		this.interval = interval;
	}
}
