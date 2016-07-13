package kumagai.diary;

import java.util.*;

/**
 * ランキング情報。
 * @author kumagai
 */
public class StringRanking
	extends ArrayList<String>
{
	/**
	 * ランキングを求める。
	 * @return ランキングデータ
	 */
	public ArrayList <Map.Entry <String, Integer>> getRanking()
	{
		// HashMapを使い出現回数を求める。
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();

		for (String s : this)
		{
			if (hashmap.get(s) == null)
			{
				// 要素なし＝初回。

				hashmap.put(s, 1);
			}
			else
			{
				// 要素あり。

				hashmap.put(s, hashmap.get(s) + 1);
			}
		}

		// Collections.sort()に渡せるようArrayListに変換。
		ArrayList <Map.Entry <String, Integer>> ret =
			new ArrayList<Map.Entry <String, Integer>>();
		ret.addAll(hashmap.entrySet());

		// 値でソートする（降順）。
		Collections.sort(
			ret,
			new Comparator<Map.Entry <String, Integer>>()
			{
				public int compare(
					Map.Entry <String, Integer> entry1,
					Map.Entry <String, Integer> entry2)
				{
					return - entry1.getValue().compareTo(entry2.getValue());
				}
			});

		return ret;
	}
}
