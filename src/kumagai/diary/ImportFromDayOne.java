package kumagai.diary;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import ktool.crypt.*;

/**
 * DayOneのテキスト形式のエクスポートファイルをインポート。
 */
public class ImportFromDayOne
{
	static private final Pattern datePattern =
		Pattern.compile("\tDate:\t([0-9]*)年([0-9]*)月([0-9]*)日 .*");
	static private final Pattern topicHeaderPattern =
		Pattern.compile("\t[A-Z][a-z]*:\t");
	static private final Pattern locationPattern =
		Pattern.compile("\tLocation:\t(.*)");
	static private final Pattern photoPattern =
		Pattern.compile("!\\[\\]\\(photos/(.*) \\\"\\\"\\)");

	/**
	 * @param args [0]=エクスポートテキストファイル [1]=出力先 [2]=パスワード [3]=-lp:l=location/p=plain
	 */
	public static void main(String[] args)
		throws Exception
	{
		if (args.length >= 3)
		{
			FileInputStream inputStream =
				new FileInputStream(args[0]);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

			String tag = null;
			String line;
			String date = null;
			String date2 = null;
			String month = null;
			String month2 = null;
			String location = null;
			boolean outLocation = false;
			boolean crypt = true;
			ArrayList<String> lines = null;
			HashMap<String, DiaryDocument> documents =
				new HashMap<String, DiaryDocument>();

			if (args.length >= 4)
			{
				outLocation = args[3].indexOf('l') >= 0;
				crypt = !(args[3].indexOf('p') >= 0);
			}

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

							lines.add(location);
						}
					}
				}
			}
			inputStream.close();

			DiaryDocument document = documents.get(month);
			if (document == null)
			{
				document = new DiaryDocument();
				documents.put(month, document);
				document = documents.get(month);
			}
			document.setOneDay(date, lines);

			for (Map.Entry<String, DiaryDocument> entry : documents.entrySet())
			{
				File outFilePath = new File(args[1], entry.getKey() + ".xml");

				OutputStream stream =
					new FileOutputStream(outFilePath.getPath());

				if (crypt)
				{
					DesEncryptCipher desEncryptCipher =
						new DesEncryptCipher(new DesKeyAndIVByMD5(args[2]));

					stream = desEncryptCipher.createOutputStream(stream);
				}

				entry.getValue().write(new OutputStreamWriter(stream, "utf-8"));

				stream.close();

				System.out.printf("%s written.\n", outFilePath.getPath());
			}
		}
		else
		{
			System.out.println("Usage:");
			System.out.println("[0]=textfile [1]=out dir [2]=p [3]=-lp:l=location/p=plain");
		}
	}
}
