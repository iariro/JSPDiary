package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.datetime.*;
import kumagai.diary.*;

/**
 * 間隔検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/searchinterval2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class SearchInterval2Action
{
	public String category;
	public String phrase;
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;
	public String password;
	public ArrayList<DateAndInterval> dateAndIntervalCollection;
	public Exception exception;
	public String message;

	/**
	 * 間隔検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchinterval2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imageFolder = context.getInitParameter("imagePath");

		try
		{
			DiaryDocumentCollectionFromDirectory documents =
				new DiaryDocumentCollectionFromDirectory(
					diaryFolder,
					imageFolder,
					password,
					new DateRange(startYear, startMonth, endYear, endMonth));

			ArrayList<SearchResultDay> results =
				documents.search(category, phrase, true);

			dateAndIntervalCollection = new ArrayList<DateAndInterval>();

			SearchResultDay date2 = null;

			for (int i=0 ; i<results.size() ; i++)
			{
				SearchResultDay resultDay = results.get(i);

				if (i <= 0)
				{
					// 初回。

					dateAndIntervalCollection.add(
						new DateAndInterval(
							resultDay.date(),
							new String()));
				}
				else
				{
					// ２回目以降。

					DateTime date12 = new DateTime(resultDay.date);
					DateTime date22 = new DateTime(date2.date);

					TimeSpan span = date12.diff(date22);

					dateAndIntervalCollection.add(
						new DateAndInterval(
							resultDay.date(),
							Integer.toString(span.getDay())));
				}

				date2 = resultDay;
			}

			DateTime date12 = new DateTime();
			DateTime date22 = new DateTime(date2.date);

			TimeSpan span = date12.diff(date22);

			// 今日分追加。
			dateAndIntervalCollection.add(
				new DateAndInterval("今日", Integer.toString(span.getDay())));

			return "success";
		}
		catch (MalformedByteSequenceException exception)
		{
			this.message = "パスワードが違うかファイルが壊れています";
			this.exception = exception;

			return "error";
		}
	}
}
