package kumagai.diary.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import org.xml.sax.*;
import ktool.crypt.*;
import ktool.datetime.*;
import kumagai.diary.*;

/**
 * 今月分の日記表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/onemonthview.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class CurrentMonthDiaryViewAction
{
	public String filepath;
	public String password;
	public ArrayList<SearchResultDay> results;
	public Exception exception;
	public String message;

	/**
	 * ファイル名から年月文字列を取得。
	 * @return 年月文字列
	 */
	public String getYearMonthFromFilename()
	{
		String filename = new File(filepath).getName();

		return
			String.format(
				"%s年%s月",
				filename.substring(0, 4),
				filename.substring(4, 6));
	}

	/**
	 * カテゴリー一覧表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("currentmonthdiaryview")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		DesDecryptCipher desDecryptCipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		DateTime today = new DateTime();

		filepath =
			new File(
				diaryFolder,
				String.format(
					"%04d%02d.xml",
					today.getYear(),
					today.getMonth())).getPath();

		try
		{
			if (new File(filepath).exists())
			{
				// ファイルは存在する。

				InputStream stream = new FileInputStream(filepath);

				DiaryDocument document =
					new DiaryDocument(
						desDecryptCipher.createInputStream(stream),
						imagePath);

				stream.close();

				results = document.getAll(true, today, false);
			}
			else
			{
				// ファイルは存在しない。

				// 新規作成分。

				results = new ArrayList<SearchResultDay>();

				for (int i=1 ; i<=today.getDay() ; i++)
				{
					DateTime date = new DateTime(today);
					date.setDay(i);
					results.add(new SearchResultDay(date.getDate()));
				}
			}

			return "success";
		}
		catch (SAXParseException exception)
		{
			this.message = String.format("ファイル%sが壊れています", filepath);
			this.exception = exception;
		}
		catch (MalformedByteSequenceException exception)
		{
			this.message =
				String.format(
					"パスワードが違うかファイル%sが壊れています",
					filepath);
			this.exception = exception;
		}

		return "error";
	}
}
