package kumagai.diary;

/**
 * ファイル名とサイズの組。
 * @author kumagai
 */
public class FileNameAndSize
{
	public String filename;
	public long size;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param filename ファイル名
	 * @param size サイズ
	 */
	public FileNameAndSize(String filename, long size)
	{
		this.filename = filename;
		this.size = size;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return String.format("%s : %d", filename, size);
	}
}
