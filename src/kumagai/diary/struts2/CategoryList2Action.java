package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import kumagai.diary.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;

/**
 * カテゴリー一覧表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/categorylist2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class CategoryList2Action
{
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;
	public String password;
	public ArrayList <Map.Entry<String, Integer>> ranking;
	public Exception exception;
	public String message;

	/**
	 * カテゴリー一覧表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("categorylist2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		try
		{
			// 日記ファイル読み込み。
			DiaryDocumentCollectionFromDirectory diaries =
				new DiaryDocumentCollectionFromDirectory(
					diaryFolder,
					imagePath,
					password,
					new DateRange(startYear, startMonth, endYear, endMonth));

			// カテゴリー一覧を取得。
			StringRanking categories = diaries.getCategories();

			// ランキングを取得。
			ranking = categories.getRanking();

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
