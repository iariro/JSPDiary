package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.diary.*;

/**
 * は行検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/searchhagyou2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class SearchHagyou2Action
{
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;
	public String password;
	public ArrayList<SearchResultDay> results;
	public Exception exception;
	public String message;

	/**
	 * は行検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchhagyou2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		try
		{
			DiaryDocumentCollectionFromDirectory documents =
				new DiaryDocumentCollectionFromDirectory(
					diaryFolder,
					imagePath,
					password,
					new DateRange(startYear, startMonth, endYear, endMonth));

			results = documents.searchHagyou();

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
