package kumagai.diary;

import java.io.*;

/**
 * UTF BOMユーティリティ。
 * @author kumagai
 */
public class UtfBomUtility
{
	/**
	 * BOMなしのバイト列入力ストリームを生成。
	 * @param inputStream BOMがありうるバイト列入力ストリーム
	 * @return BOMなしのバイト列入力ストリーム
	 * @throws IOException
	 */
	static public InputStream createInputStreamWithoutBom
		(InputStream inputStream)
		throws IOException
	{
		int received;
		boolean bom = false;
		byte [] buffer = new byte [512];
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		for (int i=0 ; (received = inputStream.read(buffer)) > 0 ; i++)
		{
			if (i == 0)
			{
				// 先頭部分。

				bom =
					buffer[0] == (byte)0xef &&
					buffer[1] == (byte)0xbb &&
					buffer[2] == (byte)0xbf;
			}
			outputStream.write(buffer, 0, received);
		}

		byte [] buffer2 = outputStream.toByteArray();

		InputStream inputStream2;

		if (bom)
		{
			// BOMあり。

			inputStream2 =
				new ByteArrayInputStream(buffer2, 3, buffer2.length - 3);
		}
		else
		{
			// BOMなし。

			inputStream2 =
				new ByteArrayInputStream(buffer2, 0, buffer2.length);
		}

		return inputStream2;
	}
}
