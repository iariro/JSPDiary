package kumagai.diary.struts2;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;

import kumagai.diary.DateRange;
import kumagai.diary.DiaryDocumentCollection;
import kumagai.diary.DiaryDocumentCollectionFromDirectory;
import kumagai.diary.DiaryDocumentCollectionFromZip;
import kumagai.diary.MonthlyCount;
import kumagai.diary.MonthlyCountTable;
import kumagai.diary.SearchResultDay;

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
	 * 検索件数取得
	 * @return 検索件数
	 * @throws Exception
	 */
	public int getSize()
	{
		int size = 0;
		for (SearchResultDay searchResultDay : results)
		{
			size += searchResultDay.size();
		}
		return size;
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
