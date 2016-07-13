package kumagai.diary;

import java.io.*;
import java.util.*;
import ktool.datetime.*;

/**
 * 日記ファイルのコレクション。
 * @author kumagai
 */
public class DiaryFileCollection
	extends ArrayList<DiaryFile>
{
	/**
	 * テストコード。
	 * @param args [0]=ファイルパス
	 * @throws IOException
	 */
	public static void main(String[] args)
		throws IOException
	{
		String [] files =
			new DiaryFileCollection(args[0]).getYearMonthFromFileName();

		for (String file : files)
		{
			System.out.println(file);
		}
	}

	/**
	 * 日記ファイルのコレクションを構築する。
	 * @param path 対象パス
	 */
	public DiaryFileCollection(String path)
	{
		for (String filename : new File(path).list())
		{
			if (filename.length() == (6 + 1 + 3))
			{
				// ファイル名の長さは正しい。

				try
				{
					Integer.parseInt(filename.substring(0, 4));
					Integer.parseInt(filename.substring(4, 6));

					add(new DiaryFile(path, filename));
				}
				catch (NumberFormatException exception)
				{
				}
			}
		}
	}

	/**
	 * ファイル名とサイズ情報を取得する。
	 * @return ファイル名とサイズ情報コレクション
	 */
	public ArrayList<FileNameAndSize> getFileNameAndSizeCollection()
	{
		ArrayList<FileNameAndSize> fileNameAndSizeCollection =
			new ArrayList<FileNameAndSize>();

		for (DiaryFile file : this)
		{
			fileNameAndSizeCollection.add(
				new FileNameAndSize(
					file.filename,
					new File(file.path, file.filename).length()));
		}

		return fileNameAndSizeCollection;
	}

	/**
	 * ファイル名から年月のリストを作成し返却する。
	 * @return ファイル名から取得した年月のリスト
	 */
	public String [] getYearMonthFromFileName()
	{
		ArrayList<String> yearMonth = new ArrayList<String>();

		for (DiaryFile file : this)
		{
			DateTime date = DiaryDocument.getMonthFromFilename(file.filename);

			yearMonth.add(
				String.format("%4d%02d", date.getYear(), date.getMonth()));
		}

		return yearMonth.toArray(new String [] {});
	}

	/**
	 * 同じバイトの繰り返しを検索する。
	 * @throws IOException
	 */
	public void searchRepeatBytes()
		throws IOException
	{
		for (DiaryFile file : this)
		{
			System.out.printf
				("%s in %s\n", file.searchRepeatBytes(), file.filename);
		}
	}
}
