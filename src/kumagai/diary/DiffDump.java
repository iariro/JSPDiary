package kumagai.diary;

import java.io.*;
import java.util.*;
import ktool.datetime.*;

/**
 * 内容比較。
 */
public class DiffDump
{
	/**
	 * 内容比較。
	 * @param args [0]=XMLファイルパス [1]=ZIPファイルパス [2]=Tempパス [3]=p [4]=行モード
	 * @throws Exception
	 */
	public static void main(String[] args)
		throws Exception
	{
		if (args.length >= 4)
		{
			// 引数は最低限指定されている。

			String xmlPath = args[0];
			String zipPath = args[1];
			String tempPath = args[2];
			String p = args[3];

			DateTime today = new DateTime();

			// 先月分を残し、当月分を除外。
			String excludeMonth = null;

			if (today.getDay() < 15)
			{
				// 月前半。

				excludeMonth = String.format("%02d", today.getMonth());
			}
			else
			{
				// 月後半。

				today.addMonth(1);
				excludeMonth = String.format("%02d", today.getMonth());
			}

			if (args.length >= 5)
			{
				// 第６パラメータあり。

				if (! Boolean.valueOf(args[4]))
				{
					// 無視なしの指定。

					excludeMonth = null;
				}
			}

			// Tempフォルダのファイルをあるだけ検索。
			DiaryFileCollection tempFiles = new DiaryFileCollection(tempPath);

			String [] files = tempFiles.getYearMonthFromFileName();

			diff(xmlPath, zipPath, files, p, excludeMonth);
		}
		else
		{
			// 最低限の引数が指定されていない。

			System.out.printf
				("[0]=XMLファイルパス [1]=ZIPファイルパス [2]=Tempパス [3]=p");
		}
	}

	/**
	 * 内容比較実処理。
	 * @param xmlPath XMLファイル配置パス
	 * @param zipPath ZIPファイルパス
	 * @param yearMonths 対象年月
	 * @param p p
	 * @param excludeMonth 除外月（ファイル名月部分文字列）／null=除外しない
	 * @throws Exception
	 */
	private static void diff(String xmlPath, String zipPath,
		String [] yearMonths, String p, String excludeMonth)
		throws Exception
	{
		DiaryDocumentCollectionFromZip zipDocuments =
			new DiaryDocumentCollectionFromZip(zipPath, p, yearMonths);

		ArrayList<DiaryDocument> plainDocuments =
			new DiaryDocumentCollectionFromDirectory(xmlPath, null, p, yearMonths);

		for (int i=0 ; i<zipDocuments.size() || i<plainDocuments.size() ; i++)
		{
			if (i < zipDocuments.size() && ! zipDocuments.completeFlags.get(i))
			{
				// 最後の＝書き途中のエントリ。

				System.out.printf(
					"%s/%s : skipped %d:%d:%d\n",
					yearMonths[i].substring(0, 4),
					yearMonths[i].substring(4, 6),
					i,
					plainDocuments.size(),
					zipDocuments.size());

				continue;
			}

			if (excludeMonth != null &&
				yearMonths[i].substring(4, 6).equals(excludeMonth))
			{
				// 除外する月である。

				System.out.printf(
					"%s/%s : skipped\n",
					yearMonths[i].substring(0, 4),
					yearMonths[i].substring(4, 6));

				continue;
			}

			if (i<zipDocuments.size() && i<plainDocuments.size())
			{
				// どちらも読み込み済み。

				DiaryDocument zipDocument = zipDocuments.get(i);
				DiaryDocument plainDocument = plainDocuments.get(i);

				ArrayList<String> lines1 =
					zipDocument.getXmlLinesWithIndent();
				ArrayList<String> lines2 =
					plainDocument.getXmlLinesWithIndent();

				String filepath =
					String.format("c:/temp/diary/before/%s.xml", yearMonths[i]);

				PrintStream stream = new PrintStream(filepath, "utf-8");

				for (String line : lines1)
				{
					stream.println(line);
				}

				stream.close();

				String filepath2 =
					String.format("c:/temp/diary/after/%s.xml", yearMonths[i]);

				PrintStream stream2 = new PrintStream(filepath2, "utf-8");

				for (String line : lines2)
				{
					stream2.println(line);
				}

				stream2.close();
			}
			else if (i<zipDocuments.size() || i<plainDocuments.size())
			{
				// どちらか読み込み済みではない。

				System.out.printf("%s/%s : %s only\n",
					yearMonths[i].substring(0, 4),
					yearMonths[i].substring(4, 6),
					i<zipDocuments.size() ? "zip" : "plain");
			}
			else
			{
				// どちらも読み込み済みではない。

				System.out.printf("%s/%s : -\n",
					yearMonths[i].substring(0, 4),
					yearMonths[i].substring(4, 6));
			}
		}
	}
}
