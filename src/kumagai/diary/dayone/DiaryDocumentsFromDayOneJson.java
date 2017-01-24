package kumagai.diary.dayone;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import net.arnx.jsonic.*;
import kumagai.diary.*;

/**
 * JSON形式のエクスポートファイルから生成可能な日記ドキュメントのコレクション
 */
public class DiaryDocumentsFromDayOneJson
	extends HashMap<String, DiaryDocument>
{
	static private final Pattern datePattern =
		Pattern.compile("([0-9]*)-([0-9]*)-([0-9]*)T([0-9]*:[0-9]*:[0-9]*)Z");
	static private final Pattern photoPattern =
		Pattern.compile("!\\[\\]\\(dayone-moment://(.*)\\)");

	/**
	 * テストコード
	 * @param args 未使用
	 */
	public static void main(String[] args)
		throws IOException
	{
		FileInputStream stream = new FileInputStream("C:/Users/w81515sr/Downloads/繧ｷ繧吶Ε繝ｼ繝翫Ν.json");
		DayOneJson json = JSON.decode(stream, DayOneJson.class);
		stream.close();

		for (Entry entry : json.entries)
		{
			for (String tag : entry.tags)
			{
				System.out.print(tag);
			}
			System.out.println();
			System.out.println(entry.creationDate);
			System.out.println(entry.location);

			BufferedReader reader = new BufferedReader(new StringReader(entry.text));

			int count = 1;
			String line;
			while ((line = reader.readLine()) != null)
			{
				System.out.printf("%d:", count);
				System.out.println(line);
				count++;
			}
		}
	}

	/**
	 * JSON形式のエクスポートファイルから日記ドキュメントのコレクションを生成
	 * @param stream 読み込みストリーム
	 * @param outLocation true=位置を出力する
	 * @param outTime true=時刻を出力する
	 */
	public DiaryDocumentsFromDayOneJson(InputStream stream, boolean outLocation, boolean outTime)
		throws Exception
	{
        DayOneJson json = JSON.decode(stream, DayOneJson.class);

		String tag = null;
		String date = null;
		String date2 = null;
		String time = null;
		String month = null;
		String month2 = null;
		String location = null;
		String attributeLine = null;
		ArrayList<String> lines = null;

    	for (Entry entry : json.entries)
    	{
	        if (outTime)
	        {
		        date = entry.creationDate;
	        }

	        if (outLocation && entry.location != null)
	        {
		        location = entry.location.toString();
	        }

			BufferedReader reader = new BufferedReader(new StringReader(entry.text));

			Matcher dateMatcher = datePattern.matcher(entry.creationDate);

			if (dateMatcher.find())
			{
				// 日付行である。

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
			}

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
				
				if (attributeLine != null)
				{
					lines.add(attributeLine);
					attributeLine = null;
				}
				
				document.setOneDay(date2, lines);
			}

			if ((date2 == null) || (! date.equals(date2)))
			{
				// 日の変わり目。

				lines = new ArrayList<String>();
			}

			String line;
			while ((line = reader.readLine()) != null)
			{
				boolean tagLine = false;

				if (line.indexOf(0x3099) >= 0)
				{
					// 結合用濁点
					
					System.out.printf("error:%s %s %s\n", date, tag, line);
				}

				if (line.length() > 0)
				{
					if (line.charAt(0) == '#')
					{
						// タグの行である。
						
						if (tag == null)
						{							
							if (attributeLine != null)
							{
								lines.add(attributeLine);
								attributeLine = null;
							}
							
							tagLine = true;
							tag = line.substring(1);
							line = "・" + tag;
						}
					}

					Matcher matcher = photoPattern.matcher(line);

					if (matcher.find())
					{
						// 写真の行である。

						line = String.format("@image(%s)", matcher.group(1));
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
							attributeLine = String.format("%s - %s", time, location);
						}
						else
						{
							attributeLine = location;
						}
					}
				}
			}

			date2 = date;
			month2 = month;
    	}

		DiaryDocument document = get(month);
		if (document == null)
		{
			document = new DiaryDocument();
			put(month, document);
			document = get(month);
		}

		if (attributeLine != null)
		{
			lines.add(attributeLine);
		}

		document.setOneDay(date, lines);
	}
}
