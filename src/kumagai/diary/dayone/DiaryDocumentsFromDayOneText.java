package kumagai.diary.dayone;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import kumagai.diary.*;

/**
 * テキスト形式のエクスポートファイルから生成可能な日記ドキュメントのコレクション
 */
public class DiaryDocumentsFromDayOneText
	extends HashMap<String, DiaryDocument>
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
	 * テキスト形式のエクスポートファイルから日記ドキュメントのコレクションを生成
	 * @param stream 読み込みストリーム
	 * @param outLocation true=位置を出力する
	 * @param outTime true=時刻を出力する
	 */
	public DiaryDocumentsFromDayOneText(InputStream stream, boolean outLocation, boolean outTime)
		throws Exception
	{
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(stream, "utf-8"));

		String tag = null;
		String line;
		String date = null;
		String date2 = null;
		String time = null;
		String month = null;
		String month2 = null;
		String location = null;
		ArrayList<String> lines = null;

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
						put(month, document);
					}

					if ((date2 != null) && (! date.equals(date2)))
					{
						// 日の変わり目。

						DiaryDocument document = get(month2);
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
								"@image(%s)",
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

		DiaryDocument document = get(month);
		if (document == null)
		{
			document = new DiaryDocument();
			put(month, document);
			document = get(month);
		}
		document.setOneDay(date, lines);
	}
}
