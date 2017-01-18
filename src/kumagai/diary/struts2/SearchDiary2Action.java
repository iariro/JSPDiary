package kumagai.diary.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.diary.*;

/**
 * 日記検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/searchdiary2.jsp"),
	@Result(name="success_digest", location="/diary/searchdiary2digest.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class SearchDiary2Action
{
	public String category;
	public String phrase;
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;
	public String password;
	public String useTag;
	public String digest;
	public String topicOnly;
	public ArrayList<SearchResultDay> results;
	public MonthlyCountTable monthlyCountTable;
	public Exception exception;
	public String message;

	/**
	 * 結果件数取得。
	 * @return 結果件数
	 */
	public int getSize()
	{
		return results.size();
	}

	/**
	 * 日記検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchdiary2")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();

			String diaryFolder = context.getInitParameter("diaryPath");
			String imagePath = context.getInitParameter("imagePath");

			DiaryDocumentCollection documents;

			File path = new File(diaryFolder);

			if (path.isFile())
			{
				// ファイルの場合。

				DateRange dateRange =
					new DateRange
						(Integer.valueOf(startYear), Integer.valueOf(endYear));

				documents =
					new DiaryDocumentCollectionFromZip(
						diaryFolder,
						password,
						dateRange.getYearMonths());
			}
			else if (path.isDirectory())
			{
				// ディレクトリの場合。

				documents =
					new DiaryDocumentCollectionFromDirectory(
						diaryFolder,
						useTag != null ? imagePath : null,
						password,
						new DateRange(startYear, startMonth, endYear, endMonth));
			}
			else
			{
				return "error";
			}

			results = documents.search(category, phrase, digest != null, topicOnly != null);

			int startYear = Integer.valueOf(this.startYear);
			int endYear = Integer.valueOf(this.endYear);

			MonthlyCount monthlyCount =
				new MonthlyCount(results, startYear, endYear);

			monthlyCountTable = new MonthlyCountTable(monthlyCount, startYear);

			if (digest != null)
			{
				// ダイジェスト指定あり。

				return "success_digest";
			}
			else
			{
				// ダイジェスト指定なし。

				return "success";
			}
		}
		catch (MalformedByteSequenceException exception)
		{
			this.message = "パスワードが違うかファイルが壊れています";
			this.exception = exception;

			return "error";
		}
	}
}
