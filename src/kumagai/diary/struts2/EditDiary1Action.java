package kumagai.diary.struts2;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.crypt.*;
import kumagai.diary.*;

/**
 * 日記編集ページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/editdiary1.jsp")
public class EditDiary1Action
{
	static private final SimpleDateFormat dateFormat1;
	static private final SimpleDateFormat dateFormat2;

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		dateFormat1 = new SimpleDateFormat();
		dateFormat2 = new SimpleDateFormat();

		dateFormat1.applyPattern("yyyy/MM/dd");
		dateFormat2.applyPattern("yyyy/MM/dd（E）");
	}

	public String date;
	public String password;
	public String text;

	/**
	 * 曜日付きの日付取得。
	 * @return 曜日付きの日付
	 */
	public String getDateWithYoubi()
	{
		try
		{
			return dateFormat2.format(dateFormat1.parse(date));
		}
		catch (ParseException exception)
		{
			return null;
		}
	}

	/**
	 * ファイル名取得。
	 * @return ファイル名
	 */
	public String getFileName()
	{
		return
			String.format(
				"%4s%2s.xml",
				date.substring(0, 4),
				date.substring(5, 7));
	}

	/**
	 * 日記編集ページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("editdiary1")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryPath = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		DesDecryptCipher desDecryptCipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		String filePath = new File(diaryPath, getFileName()).getPath();

		ArrayList<String> lines;

		if (new File(filePath).exists())
		{
			// ファイルは存在する。

			InputStream stream = new FileInputStream(filePath);

			DiaryDocument document =
				new DiaryDocument(
					desDecryptCipher.createInputStream(stream),
					imagePath);

			stream.close();

			lines = document.getOneDayAsText(date);
		}
		else
		{
			// ファイルは存在しない。

			lines = null;
		}

		text = new String();

		if (lines != null)
		{
			// ファイルは存在し指定の日の情報はある。

			for (String line : lines)
			{
				text += line + "\r\n";
			}
		}

		return "success";
	}
}
