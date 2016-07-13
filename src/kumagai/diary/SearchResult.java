package kumagai.diary;

import java.util.*;

/**
 * １カテゴリー分の検索結果本文。
 * @author kumagai
 */
public class SearchResult
{
	public final String category;

	public final ArrayList<String> lines;

	/**
	 * メンバに値を割り当てる。
	 * @param category カテゴリ
	 * @param lines 検索マッチ部分
	 */
	public SearchResult(String category, ArrayList<String> lines)
	{
		this.category = category;
		this.lines = lines;
	}

	/**
	 * カテゴリー取得。
	 * @return カテゴリー
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * 本文取得。
	 * @return 本文
	 */
	public ArrayList<String> getLines()
	{
		return lines;
	}

	/**
	 * 本文１行目のみ取得。
	 * @return 本文１行目
	 */
	public String getLine1()
	{
		if (lines.size() >= 1)
		{
			// １行でもある。

			return lines.get(0);
		}
		else
		{
			// １行もない。

			return new String();
		}
	}

	/**
	 * 本文２行目のみ取得。
	 * @return 本文２行目
	 */
	public String getLine2()
	{
		if (lines.size() >= 2)
		{
			// ２行以上ある。

			return lines.get(1);
		}
		else
		{
			// ２行もない。

			return new String();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String ret = category + "\n";

		for (int i=0 ; i<lines.size(); i++)
		{
			ret += (String)lines.get(i) + "\n";
		}

		return ret;
	}
}
