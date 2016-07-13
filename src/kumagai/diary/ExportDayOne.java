package kumagai.diary;

import java.io.*;
import java.text.*;
import java.util.*;

public class ExportDayOne
{
	static public final SimpleDateFormat dateFormat;

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyyMM");
	}

	/**
	 * 検索テスト。
	 * @param args [0]=変換元ファイルパス [1]=p [2]=開始年 [3]=終了年 [4]=変換先ファイルパス
	 * @throws Exception
	 */
	static public void main(String [] args)
		throws Exception
	{
		DiaryDocumentCollectionFromDirectory documents =
			new DiaryDocumentCollectionFromDirectory(
				args[0],
				null,
				args[1],
				new DateRange(
					Integer.valueOf(args[2]),
					Integer.valueOf(args[3])));

		PrintStream stream = new PrintStream(args[4], "utf-8");

		for (DiaryDocument document : documents)
		{
			ArrayList<SearchResultDay> results = document.getAllNoTag();

			for (SearchResultDay resultDay : results)
			{
				for (SearchResult result : resultDay)
				{
					stream.printf
						("\nDate: %s 12:00\n\n", resultDay.getDateForDayOne());

					stream.println("#" + result.category);

					for (String line : result.lines)
					{
						if (line.startsWith("\t- "))
						{
							line = line.substring(1);
						}

						stream.println(line);
					}
				}
			}
		}

		stream.close();
	}
}
