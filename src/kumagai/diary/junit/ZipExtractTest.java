package kumagai.diary.junit;

import java.io.*;
import java.util.zip.*;
import junit.framework.*;

public class ZipExtractTest
	extends TestCase
{
	public void test1()
	{
		decode(new File("C:/Users/w81515sr/Downloads/2016-06-30-Journal_txt.zip"));
	}

	/**
	 * Zipファイルを展開します
	 * @param aZipFile zipファイル
	 * @param aOutDir 出力先ディレクトリ
	 */
	public void decode(File aZipFile)
	{
		FileInputStream fileIn = null;

		try
		{
			// -------------------------------
			// zipファイルをオープンする
			// -------------------------------
			fileIn = new FileInputStream(aZipFile);
			ZipInputStream zipIn = new ZipInputStream(fileIn);

			ZipEntry entry;
			while ((entry = zipIn.getNextEntry()) != null)
			{
				if (! entry.isDirectory())
				{
					// ------------------------------
					// ファイルの場合は出力する
					// 出力先は、現在の outDirの下
					// ------------------------------
					String relativePath = entry.getName();
					if (! relativePath.contains("/"))
					{
						byte[] buf = new byte[(int)entry.getSize()];
						int size = zipIn.read(buf);
						if ((size) == (int)entry.getSize())
						{
						}

						System.out.println(relativePath);
					}
				}
				zipIn.closeEntry();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		finally
		{
			try
			{
				fileIn.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}
