package kumagai.diary;

import java.text.*;
import java.util.*;

/**
 * 日記ドキュメントコレクション基底部。
 * @author kumagai
 */
public class DiaryDocumentCollection
	extends ArrayList<DiaryDocument>
{
	/**
	 * カテゴリー，フレーズを指定しての検索。
	 * @param category カテゴリー
	 * @param phrase フレーズ
	 * @param topicOnly true=トピックのみ絞り込む／false=日全体を対象にする
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> search
		(String category, String phrase, boolean topicOnly)
		throws ParseException
	{
		return search(category, phrase, false, topicOnly);
	}

	/**
	 * カテゴリー，フレーズを指定しての検索。１行目のみ対象指定付き。
	 * @param category カテゴリー
	 * @param phrase フレーズ
	 * @param top true=１行目のみ対象／false=制限なし
	 * @param topicOnly true=トピックのみ絞り込む／false=日全体を対象にする
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> search
		(String category, String phrase, boolean top, boolean topicOnly)
		throws ParseException
	{
		ArrayList <SearchResultDay> ret = new ArrayList <SearchResultDay>();

		for (DiaryDocument document : this)
		{
			ArrayList <SearchResultDay> recv =
				document.search(category, phrase, top, topicOnly);

			ret.addAll(recv);
		}

		return ret;
	}
}
