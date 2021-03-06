package kumagai.diary.dayone;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kumagai.diary.DiaryDocument;

/**
 * テキスト形式のエクスポートファイルから生成可能な日記ドキュメントのコレクション
 */
public class DiaryDocumentsFromDayOneText
	extends HashMap<String, DiaryDocument>
{
	static private final Pattern datePattern =
		Pattern.compile("\tDate:\t\t*([0-9]*)年([0-9]*)月([0-9]*)日 ([0-9:]*) .*");
	static private final Pattern topicHeaderPattern =
		Pattern.compile("\t[A-Z][a-z]*:\t");
	static private final Pattern locationPattern =
		Pattern.compile("\tLocation:\t(.*)");
	static private final Pattern photoPattern =
			Pattern.compile("!\\[\\]\\(photos/([^\\)]*)\\)");

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

		int topicCount = 0;
		String tag = null;
		String line;
		String date = null;
		String date2 = null;
		String time = null;
		String month = null;
		String month2 = null;
		String location = null;
		String attributeLine = null;
		ArrayList<String> lines = null;
		Boolean tagLine2 = null;

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
					topicCount = 0;

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

				ArrayList<String> lines2 = new ArrayList<String>();
				matcher = photoPattern.matcher(line);
				boolean find;
				if (find = matcher.find())
				{
					// 写真エントリを含む。

					int start;
					int end = 0;
					while (find)
					{
						start = matcher.start();
						String g1 = line.substring(end, start);
						String g2 = matcher.group();
						end = matcher.end();
						if (!g1.isEmpty())
						{
							lines2.add(g1);
						}
						lines2.add(g2);
						find = matcher.find();
						if (!find)
						{
							lines2.add(line.substring(end));
						}
					}
				}
				else
				{
					// 写真エントリを含まない。

					lines2.add(line);
				}

				for (String line2 : lines2)
				{
					boolean tagLine = false;

					if (line2.length() > 0)
					{
						if (line2.charAt(0) == '#')
						{
							// タグの行である。
						
							if (attributeLine != null)
							{
								lines.add(attributeLine);
								attributeLine = null;
							}
						
							tagLine = true;
							if (line2.startsWith("# #"))
							{
								tag = line2.substring(3);
							}
							else
							{
								tag = line2.substring(1);
							}
							line2 = "・" + tag;
							topicCount++;
						}

						matcher = photoPattern.matcher(line2);
						if (matcher.find())
						{
							// 写真エントリを含む。

							line2 = String.format("@image(%s)", matcher.group(1));
						}
					}

					if (date != null && tag != null)
					{
						// 日付とタグが確定している＝日記本文である。

						if (!tagLine2 || (line2.length() > 0))
						{
							// 前の行はタグ行ではないor空行ではない
							// ＝タグ行の次の空行を無視する

							lines.add(line2);
						}

						if (tagLine)
						{
							// タグ行

							if (outLocation)
							{
								// 位置情報出力指定あり。

								if (outTime)
								{
									// 時刻出力指定あり。

									if (topicCount == 1)
									{
										// １個目のトピック

										if (location != null)
										{
											// 位置情報あり

											attributeLine = String.format("%s - %s", time, location);
										}
										else
										{
											// 位置情報なし

											attributeLine = time;
										}
									}
									else
									{
										// ２個目＝ヘッダなしで出現したトピック

										attributeLine = "--:--:-- - 場所不明";
									}
								}
								else
								{
									// 時刻出力指定なし。

									if (topicCount == 1)
									{
										// １個目のトピック

										attributeLine = location;
									}
									else
									{
										// ２個目＝ヘッダなしで出現したトピック

										attributeLine = "--:--:--";
									}
								}
							}
						}
					}

					tagLine2 = tagLine;
				}
			}
		}
		
		if (month != null)
		{
			DiaryDocument document = get(month);
			if (document == null)
			{
				document = new DiaryDocument();
				put(month, document);
				document = get(month);
			}
			
			if (lines != null)
			{
				if (attributeLine != null)
				{
					lines.add(attributeLine);
				}
	
				document.setOneDay(date, lines);
			}
		}
		else
		{
			System.out.println("month is null");
		}
	}
}
