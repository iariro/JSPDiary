package kumagai.diary;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.Element;
import org.xml.sax.*;
import ktool.xml.*;

/**
 * 日記ドキュメント基底部。
 * @author kumagai
 */
public class DiaryDocumentCore
	extends KDocument
{
	static protected final SimpleDateFormat dateFormat;
	static protected final Pattern imageTagPattern;
	static protected final Pattern htmlTagPattern;

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy/MM/dd");
		imageTagPattern = Pattern.compile("@image\\(([^@]*)\\)");
		htmlTagPattern = Pattern.compile("<(.*)>");
	}

	protected String imagePath;

	/**
	 * 基底クラスの初期化およびメンバへの値の割り当て。
	 * @param stream 入力ストリーム
	 * @param imagePath 画像パス
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 */
	public DiaryDocumentCore(InputStream stream, String imagePath)
		throws TransformerConfigurationException, ParserConfigurationException,
		SAXException, IOException, TransformerFactoryConfigurationError
	{
		super(stream);

		this.imagePath = imagePath;
	}

	/**
	 * 何もしないコンストラクタ。デバッグ用。
	 * @throws Exception
	 */
	public DiaryDocumentCore()
		throws Exception
	{
		// 何もしない。
	}

	/**
	 * 特殊文字エスケープおよびタグのHTMLタグへの変換処理。
	 * @param line 返還前文字列
	 * @return 変換後文字列
	 */
	public String effect(String line)
	{
		if (line.indexOf("&") >= 0)
		{
			// &あり。

			line = line.replaceAll("&", "&amp;");
		}

		if (line.indexOf("<") >= 0)
		{
			// HTMLタグあり。

			line = line.replaceAll("<", "&lt;");
		}

		if (line.indexOf(">") >= 0)
		{
			// HTMLタグあり。

			line = line.replaceAll(">", "&gt;");
		}

		if (line.indexOf("@image(") >= 0 && imagePath != null)
		{
			// @imageタグあり。

			Matcher matcher = imageTagPattern.matcher(line);
			line = matcher.replaceAll("<img src='/kumagai/image?folder=DiaryImageFolder&filename=$1'>");
		}

		if (line.indexOf("	") == 0)
		{
			// タブあり。

			if (line.indexOf("	- ") == 0)
			{
				// ハイフンあり。

				line = line.replaceAll("	- ", "<li>");
			}
			else
			{
				// ハイフンなし。

				line += "<br>";
			}
		}

		return line;
	}

	/**
	 * １日分の要素生成。
	 * @param date1 日付
	 * @param lines 本文
	 * @return １日分の要素
	 */
	protected Element createOneDayElement(String date1, ArrayList<String> lines)
	{
		Element day = createElement("day");
		day.setAttribute("date", date1);

		Element topic = null;

		String line2 = null;

		for (String line : lines)
		{
			if (line.indexOf("・") == 0)
			{
				// カテゴリ行。

				topic = createElement("topic");
				topic.setAttribute("category", line.substring(1));
				day.appendChild(topic);
			}
			else
			{
				if (line.length() > 0 && topic != null)
				{
					// 空文字列ではない。

					if (line2 != null && line2.length() == 0)
					{
						// 直前が空文字列だった。

						Element lineElement = createElement("line");
						topic.appendChild(lineElement);
						lineElement.appendChild(createTextNode(line2));
					}

					Element lineElement = createElement("line");
					topic.appendChild(lineElement);
					lineElement.appendChild(createTextNode(line));
				}
			}

			line2 = line;
		}

		return day;
	}
}
