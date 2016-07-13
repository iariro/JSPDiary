package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.diary.*;

/**
 * 空内容検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/searchempty2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class SearchEmpty2Action
{
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;
	public String password;
	public ArrayList<Date> results;
	public Exception exception;
	public String message;

	/**
	 * 表示内容サイズ取得。
	 * @return 表示内容サイズ
	 */
	public int getSize()
	{
		return results.size();
	}

	/**
	 * 空内容検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchempty2")
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

			results = documents.getEmptyDay();

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
