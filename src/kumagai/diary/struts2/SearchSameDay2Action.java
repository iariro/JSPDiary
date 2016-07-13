package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.diary.*;

/**
 * 同じ日検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/searchsameday2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class SearchSameDay2Action
{
	public String startYear;
	public String endYear;
	public int plusminus;
	public String password;
	public ArrayList<SearchResultDay> results;
	public Exception exception;
	public String message;

	/**
	 * 同じ日検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchsameday2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imageFolder = context.getInitParameter("imagePath");

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DATE, Integer.valueOf(plusminus));

		try
		{
			DiaryDocumentCollectionFromDirectory documents =
				new DiaryDocumentCollectionFromDirectory(
					diaryFolder,
					imageFolder,
					password,
					Integer.valueOf(startYear),
					Integer.valueOf(endYear),
					calendar.get(Calendar.MONTH) + 1);

			results = documents.getSameDay(calendar);

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
