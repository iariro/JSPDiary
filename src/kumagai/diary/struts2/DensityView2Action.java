package kumagai.diary.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.xml.transform.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.convention.annotation.Result;
import ktool.xml.*;
import kumagai.diary.*;

/**
 * 密度ビュー表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/densityview2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class DensityView2Action
{
	private KDocument densityDocument;

	public String category;
	public String phrase;
	public int startYear;
	public int endYear;
	public String password;
	public Exception exception;
	public String message;

	/**
	 * グラフSVGドキュメントを文字列として取得。
	 * @return 文字列によるグラフSVGドキュメント
	 */
	public String getXml()
		throws TransformerFactoryConfigurationError, TransformerException
	{
		// XML書き出し準備。
		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		StringWriter writer = new StringWriter();

		// XML書き出し。
		densityDocument.write(transformer, writer);

		return writer.toString();
	}

	/**
	 * 密度ビュー表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("densityview2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");

		try
		{
			File path = new File(diaryFolder);

			// 日記ファイル読み込み。
			DiaryDocumentCollection documents;

			if (path.isFile())
			{
				// ファイルの場合。

				documents =
					new DiaryDocumentCollectionFromZip(
						diaryFolder,
						password,
						new DateRange(startYear, endYear).getYearMonths());
			}
			else if (path.isDirectory())
			{
				// ディレクトリの場合。

				documents =
					new DiaryDocumentCollectionFromDirectory(
						diaryFolder,
						imagePath,
						password,
						new DateRange(startYear, endYear));
			}
			else
			{
				return "error";
			}

			ArrayList<SearchResultDay> results =
				documents.search(category, phrase, true);

			// 日ごとの日記の分量を計算。
			HashMap <Calendar, Integer> volumePerDay =
				new VolumePerDayFromSearchResultDay(results);

			// 計算結果をグラフ化。
			densityDocument =
				new DensityDocument2(volumePerDay, startYear, endYear);

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
