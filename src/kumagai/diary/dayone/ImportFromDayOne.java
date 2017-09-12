package kumagai.diary.dayone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ktool.crypt.DesEncryptCipher;
import ktool.crypt.DesKeyAndIVByMD5;
import kumagai.diary.DiaryDocument;

/**
 * DayOneのテキスト形式のエクスポートファイルをインポート。
 */
public class ImportFromDayOne
{
	/**
	 * @param args [0]=テキストエクスポートZIPファイル [1]=出力先 [2]=パスワード [3]=-lpt:l=location/p=plain/t=time
	 */
	static public void main(String[] args)
		throws Exception
	{
		if (args.length >= 3)
		{
			boolean outLocation = false;
			boolean outTime = false;
			boolean crypt = true;

			if (args.length >= 4)
			{
				outLocation = args[3].indexOf('l') >= 0;
				outTime = args[3].indexOf('t') >= 0;
				crypt = !(args[3].indexOf('p') >= 0);
			}

			FileInputStream fileStream;
			try
			{
				fileStream = new FileInputStream(args[0]);
			}
			catch (FileNotFoundException exception)
			{
				System.out.println(exception.getMessage());
				return;
			}

			ZipInputStream zipStream = new ZipInputStream(fileStream);

			ZipEntry entry;
			while ((entry = zipStream.getNextEntry()) != null)
			{
				if (! entry.isDirectory())
				{
					String relativePath = entry.getName();

					if (! relativePath.contains("/"))
					{
						// トップの階層＝テキストファイルとみなす

						System.out.printf("DayOne Text : %s\n", relativePath);

						HashMap<String, DiaryDocument> documents;

						if (relativePath.endsWith("txt"))
						{
							documents =
								new DiaryDocumentsFromDayOneText
									(zipStream, outLocation, outTime);

							writeXmlFiles(documents, args[1], args[2], crypt);
						}
						else if (relativePath.endsWith("json"))
						{
							documents =
								new DiaryDocumentsFromDayOneJson
									(zipStream, outLocation, outTime);

							writeXmlFiles(documents, args[1], args[2], crypt);
						}
					}
				}
				zipStream.closeEntry();
			}
			fileStream.close();
		}
		else
		{
			System.out.println("Usage:");
			System.out.println("[0]=zip-file [1]=out dir [2]=p [3]=-lp:l=location/p=plain");
		}
	}

	/**
	 * XMLドキュメントのファイル書き出し。
	 * @param documents XMLドキュメントのコレクション
	 * @param outDirectory XMLファイル出力先
	 * @param password パスワード
	 * @param crypt true=暗号化する
	 */
	@SuppressWarnings("resource")
	static private void writeXmlFiles(HashMap<String, DiaryDocument> documents,
		String outDirectory, String password, boolean crypt)
		throws Exception
	{
		for (Map.Entry<String, DiaryDocument> entry : documents.entrySet())
		{
			File outFilePath = new File(outDirectory, entry.getKey() + ".xml");

			try
			{
				OutputStream stream =
					new FileOutputStream(outFilePath.getPath());

				if (crypt)
				{
					DesEncryptCipher desEncryptCipher =
						new DesEncryptCipher(new DesKeyAndIVByMD5(password));

					stream = desEncryptCipher.createOutputStream(stream);
				}

				entry.getValue().write(new OutputStreamWriter(stream, "utf-8"));

				stream.close();

				System.out.printf("%s written.\n", outFilePath.getPath());
			}
			catch (Exception exception)
			{
				System.out.printf("%s\n", exception.getMessage());
			}
		}
	}
}
