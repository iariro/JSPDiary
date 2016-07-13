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
 * 日記更新アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/editdiary2.jsp")
public class EditDiary2Action
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
	public String result;

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
	 * 日記更新アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("editdiary2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryPath = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		DesDecryptCipher desDecryptCipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		String filePath = new File(diaryPath, getFileName()).getPath();

		DiaryDocument document;

		if (new File(filePath).exists())
		{
			// ファイルはある。

			InputStream stream = new FileInputStream(filePath);

			document =
				new DiaryDocument
					(desDecryptCipher.createInputStream(stream), imagePath);

			stream.close();
		}
		else
		{
			// ファイルはない。

			document = new DiaryDocument();
		}

		if (text != null)
		{
			// 本文あり。

			ArrayList<String> lines = new ArrayList<String>();

			for (String line : text.split("\r\n"))
			{
				lines.add(line);
			}

			document.setOneDay(date, lines);

			DesEncryptCipher desEncryptCipher =
				new DesEncryptCipher(new DesKeyAndIVByMD5(password));

			OutputStream stream =
				desEncryptCipher.createOutputStream(
					new FileOutputStream(filePath));

			document.write(new OutputStreamWriter(stream, "utf-8"));

			stream.close();

			result = "成功";

			return "success";
		}
		else
		{
			// 本文なし。

			result = "失敗 - text == null";

			return "success";
		}
	}
}
