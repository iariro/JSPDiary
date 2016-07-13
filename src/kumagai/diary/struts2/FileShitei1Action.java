package kumagai.diary.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.crypt.*;
import kumagai.diary.*;

/**
 * １ファイル分の日記表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/onemonthview.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class FileShitei1Action
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
	 * １ファイル分の日記表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("fileshitei1")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String imagePath = context.getInitParameter("imagePath");

		DesDecryptCipher desDecryptCipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		try
		{
			InputStream stream = new FileInputStream(filepath);

			DiaryDocument document =
				new DiaryDocument(
					desDecryptCipher.createInputStream(stream),
					imagePath);

			stream.close();

			results =
				document.getAll
					(true, DiaryDocument.getMonthFromFilename(filepath), true);

			return "success";
		}
		catch (FileNotFoundException exception)
		{
			this.message =
				String.format("ファイル%sが見つかりません", filepath);

			this.exception = exception;

			return "error";
		}
		catch (MalformedByteSequenceException exception)
		{
			this.message =
				String.format(
					"パスワードが違うかファイル%sが壊れています",
					filepath);

			this.exception = exception;

			return "error";
		}
	}
}
