package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.string.*;
import ktool.datetime.*;
import kumagai.diary.*;

/**
 * 月ごと件数グラフ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/oneday2.jsp")
public class OneDay2Action
{
	public String date;
	public String password;
	public String category;
	public String useTag;
	public ArrayList<SearchResultDay> results;

	/**
	 * 月ごと件数グラフ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("oneday2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		results = search(date, diaryFolder, imagePath, category, password);

		return "success";
	}

	/**
	 * 検索処理。複数日指定可能。
	 * @param dates 対象日文字列
	 * @param diaryFolder ドキュメントファイルパス
	 * @param imagePath 画像ファイルパス
	 * @param category カテゴリ
	 * @param password パスワード
	 * @return 検索結果
	 * @throws Exception
	 */
	static public ArrayList<SearchResultDay> search(String dates,
		String diaryFolder, String imagePath, String category, String password)
		throws Exception
	{
		// 短縮形式日付文字列を展開。
		DateCollection dateCollection =
			DateCollection.fromHyphenAndComma(dates);

		// 日付コレクションから年月コレクションを生成。
		ArrayList<String> yearMonths = new ArrayList<String>();

		for (String date : dateCollection)
		{
			String yearMonth = date.substring(0, 4) + date.substring(5, 7);

			boolean find = false;

			for (String yearMonth2 : yearMonths)
			{
				if (yearMonth.equals(yearMonth2))
				{
					// 年月が同じ。

					find = true;
					break;
				}
			}

			if (! find)
			{
				// 初登場。

				yearMonths.add(yearMonth);
			}
		}

		String [] yearMonths2 = yearMonths.toArray(new String [] {});

		// 年月コレクション分の日記ドキュメントコレクションを生成。
		DiaryDocumentCollectionFromDirectory diaryDocuments =
			new DiaryDocumentCollectionFromDirectory(
				diaryFolder,
				imagePath,
				password,
				yearMonths2);

		ArrayList<SearchResultDay> results = new ArrayList<SearchResultDay>();

		// 検索。
		for (DiaryDocument diary : diaryDocuments)
		{
			boolean find = false;

			for (String date : dateCollection)
			{
				String dateShort = date.substring(0, 4) + date.substring(5, 7);

				if (diary.getYearAndMonth().equals(dateShort))
				{
					// 年月は一致。

					SearchResultDay result =
						diary.getOneDay(DateTime.parseDateString(date), category);

					if (result != null)
					{
						// 見つかった。

						results.add(result);

						find = true;
					}
				}
				else
				{
					// 年月は一致しない。

					if (find)
					{
						// 一致部分を通り過ぎた。

						break;
					}
				}
			}
		}

		return results;
	}
}
