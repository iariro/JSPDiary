package kumagai.diary;

import java.io.*;

/**
 * 日記ファイル。
 * @author kumagai
 */
public class DiaryFile
{
	public final String path;
	public final String filename;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param path ファイルパス
	 * @param filename ファイル名
	 */
	public DiaryFile(String path, String filename)
	{
		this.path = path;
		this.filename = filename;
	}

	/**
	 * 同じバイトの繰り返しを検索する。
	 * @return 最多繰り返し箇所情報
	 * @throws IOException
	 */
	public RepeatBytesSearchResult searchRepeatBytes()
		throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(path, filename));

		int b1;
		int b2 = -1;
		int index1 = 0;
		int index2 = 0;
		int count = 1;
		int maxCount = 0;
		RepeatBytesSearchResult result = null;

		for (int i=0 ; (b1 = stream.read()) >= 0 ; i++)
		{
			if (b1 == b2)
			{
				// 前のバイトと同じ値。

				count++;
			}
			else
			{
				// 前のバイトと異なる値。

				if (count > maxCount)
				{
					// 最大カウントを上回る。

					maxCount = count;
					index2 = index1;
					result =
						new RepeatBytesSearchResult
							((byte)(b2 >= 0 ? b2 : b1), index2, count);
				}

				index1 = i;
				b2 = b1;
				count = 1;
			}
		}

		stream.close();

		return result;
	}
}
