package kumagai.diary.struts2;

/**
 * int値と表示用文字列の対。
 * @author kumagai
 */
public class IntAndString
{
	public int value;
	public String displayString;

	/**
	 * 指定の値をメンバーに割り当て。
	 * @param value 年
	 * @param displayString 表示用文字列
	 */
	public IntAndString(int value, String displayString)
	{
		this.value = value;
		this.displayString = displayString;
	}
}
