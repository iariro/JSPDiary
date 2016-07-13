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
 * 月ごと件数グラフ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/monthlygraph2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class MonthlyGraph2Action
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
	@Action("monthlygraph2")
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
					new DateRange(startYear, endYear));

			ArrayList<SearchResultDay> results =
				diaries.search(category, phrase, true);

			MonthlyCount monthly =
				new MonthlyCount(results, startYear, endYear);

			densityDocument = new MonthlyCountGraphDocument(monthly, startYear);

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
