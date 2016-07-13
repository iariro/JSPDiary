package kumagai.diary;

import java.util.*;
import difflib.*;
import difflib.Delta.*;
import ktool.datetime.*;

/**
 * 内容比較。
 */
public class Diff
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

			boolean lineMode = false;

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
				// 第５パラメータあり。

				lineMode = Boolean.valueOf(args[4]);
			}

			if (args.length >= 6)
			{
				// 第６パラメータあり。

				if (! Boolean.valueOf(args[5]))
				{
					// 無視なしの指定。

					excludeMonth = null;
				}
			}

			// Tempフォルダのファイルをあるだけ検索。
			DiaryFileCollection tempFiles = new DiaryFileCollection(tempPath);

			String [] files = tempFiles.getYearMonthFromFileName();

			diff(xmlPath, zipPath, files, p, lineMode, excludeMonth);
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
	 * @param lineMode true=差分をもとの行数で表示／false=差分を１行ずつで表示
	 * @param excludeMonth 除外月（ファイル名月部分文字列）／null=除外しない
	 * @throws Exception
	 */
	private static void diff(String xmlPath, String zipPath,
		String [] yearMonths, String p, boolean lineMode, String excludeMonth)
		throws Exception
	{
		DiaryDocumentCollectionFromZip zipDocuments =
			new DiaryDocumentCollectionFromZip(zipPath, p, yearMonths);

		ArrayList<DiaryDocument> plainDocuments =
			new DiaryDocumentCollectionFromDirectory(xmlPath, null, p, yearMonths);

		for (int i=0 ; i<zipDocuments.size() || i<plainDocuments.size() ; i++)
		{
			if (i > 0)
			{
				// ２件目以降。

				System.out.println();
			}

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

				Patch patch = DiffUtils.diff(lines1, lines2);

				System.out.printf(
					"%s/%s : %d件\n",
					yearMonths[i].substring(0, 4),
					yearMonths[i].substring(4, 6),
					patch.getDeltas().size());

				if (lineMode)
				{
					// 差分をもとの行数で表示。

					for (Delta delta : patch.getDeltas())
					{
						System.out.println(delta.getType());

						for (Object line : delta.getOriginal().getLines())
						{
							System.out.println(line);
						}

						if (delta.getType() == TYPE.CHANGE)
						{
							// 変更の場合。

							System.out.print("    ");
							for (int j=0 ; j<76 ; j++)
							{
								System.out.print("-");
							}
							System.out.println();
						}

						for (Object line : delta.getRevised().getLines())
						{
							System.out.println(line);
						}
					}
				}
				else
				{
					// 差分を１行ずつで表示。

					for (Delta delta : patch.getDeltas())
					{
						System.out.printf(
							"%s\n\t%s\n\t%s\n",
							delta.getType(),
							delta.getOriginal(),
							delta.getRevised());
					}
				}
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
