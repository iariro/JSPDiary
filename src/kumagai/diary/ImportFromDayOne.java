package kumagai.diary;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import ktool.crypt.*;

/**
 * DayOneのテキスト形式のエクスポートファイルをインポート。
 */
public class ImportFromDayOne
{
	static private final Pattern datePattern =
		Pattern.compile("\tDate:\t([0-9]*)年([0-9]*)月([0-9]*)日 ([0-9:]*) .*");
	static private final Pattern topicHeaderPattern =
		Pattern.compile("\t[A-Z][a-z]*:\t");
	static private final Pattern locationPattern =
		Pattern.compile("\tLocation:\t(.*)");
	static private final Pattern photoPattern =
		Pattern.compile("!\\[\\]\\(photos/(.*) \\\"\\\"\\)");

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

						HashMap<String, DiaryDocument> documents =
							convertDayoneTextToXml
								(zipStream, outLocation, outTime);

						writeXmlFiles(documents, args[1], args[2], crypt);
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
	 * DayOneテキストファイルを読み込み・変換しXMLドキュメントを生成する。
	 * @param textStream テキストファイルストリーム
	 * @param outLocation true=Locationも出力
	 * @param outTime true=時刻も出力
	 */
	static private HashMap<String, DiaryDocument> convertDayoneTextToXml(
		InputStream textStream, boolean outLocation, boolean outTime)
		throws Exception
	{
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(textStream, "utf-8"));

		String tag = null;
		String line;
		String date = null;
		String date2 = null;
		String time = null;
		String month = null;
		String month2 = null;
		String location = null;
		ArrayList<String> lines = null;
		HashMap<String, DiaryDocument> documents =
			new HashMap<String, DiaryDocument>();

		while ((line = reader.readLine()) != null)
		{
			Matcher matcher = topicHeaderPattern.matcher(line);

			if (matcher.find())
			{
				// ヘッダ行である。

				Matcher dateMatcher = datePattern.matcher(line);
				Matcher locationMatcher = locationPattern.matcher(line);

				if (dateMatcher.find())
				{
					// 日付行である。

					location = null;

					date =
						String.format(
							"%04d/%02d/%02d",
							Integer.valueOf(dateMatcher.group(1)),
							Integer.valueOf(dateMatcher.group(2)),
							Integer.valueOf(dateMatcher.group(3)));

					month =
						String.format(
							"%04d%02d",
							Integer.valueOf(dateMatcher.group(1)),
							Integer.valueOf(dateMatcher.group(2)));

					time = dateMatcher.group(4);
					tag = null;

					if (! month.equals(month2))
					{
						// 月の変わり目。

						DiaryDocument document = new DiaryDocument();
						documents.put(month, document);
					}

					if ((date2 != null) && (! date.equals(date2)))
					{
						// 日の変わり目。

						DiaryDocument document = documents.get(month2);
						document.setOneDay(date2, lines);
					}

					if ((date2 == null) || (! date.equals(date2)))
					{
						// 日の変わり目。

						lines = new ArrayList<String>();
					}

					date2 = date;
					month2 = month;
				}
				else if (locationMatcher.find())
				{
					// 位置行である。

					location = locationMatcher.group(1);
				}
			}
			else
			{
				// ヘッダ行ではない。

				boolean tagLine = false;

				if (line.length() > 0)
				{
					if (line.charAt(0) == '#')
					{
						if (tag == null)
						{
							tagLine = true;
							tag = line.substring(1);
							line = "・" + tag;
						}
					}

					matcher = photoPattern.matcher(line);

					if (matcher.find())
					{
						// 写真の行である。

						line =
							String.format(
								"@image(\"%s\")",
								matcher.group(1));
					}
				}

				if (date != null && tag != null)
				{
					lines.add(line);

					if (outLocation && tagLine)
					{
						// 位置情報出力指定あり。

						if (outTime)
						{
							lines.add(String.format("%s - %s", time, location));
						}
						else
						{
							lines.add(location);
						}
					}
				}
			}
		}

		DiaryDocument document = documents.get(month);
		if (document == null)
		{
			document = new DiaryDocument();
			documents.put(month, document);
			document = documents.get(month);
		}
		document.setOneDay(date, lines);

		return documents;
	}

	/**
	 * XMLドキュメントのファイル書き出し。
	 * @param documents XMLドキュメントのコレクション
	 * @param outDirectory XMLファイル出力先
	 * @param password パスワード
	 * @param crypt true=暗号化する
	 */
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
