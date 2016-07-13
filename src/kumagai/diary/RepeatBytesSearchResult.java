package kumagai.diary;

/**
 * 同じバイトの繰り返し検索結果。
 * @author kumagai
 */
public class RepeatBytesSearchResult
{
	public byte value;
	public int offset;
	public int count;

	/**
	 * 指定の値をメンバーに割り当て。
	 * @param value バイトの値
	 * @param offset 登場オフセット
	 * @param count 繰り返し数
	 */
	public RepeatBytesSearchResult(byte value, int offset, int count)
	{
		this.value = value;
		this.offset = offset;
		this.count = count;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("count=%d [%x] (%02x)", count, offset, value);
	}
}
