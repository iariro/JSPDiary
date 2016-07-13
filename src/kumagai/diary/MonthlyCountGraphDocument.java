package kumagai.diary;

import java.awt.*;
import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;
import javax.crypto.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import ktool.xml.*;

/**
 * SVG形式による月ごとの日記の分量グラフドキュメント。
 */
public class MonthlyCountGraphDocument
	extends KDocument
{
	/**
	 * １月を表すますのサイズ。
	 */
	static private final Dimension size;

	/**
	 * 最大回数。
	 */
	static private final int maxCount = 30;

	/**
	 * 表の左上隅の座標。
	 */
	static private final Point origin;

	/**
	 * フォント名。
	 */
	static private final String fontFamily = "Dotum";

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		size = new Dimension(10, 10);
		origin = new Point(40, 20);
	}

	/**
	 * MonthlyCountGraphDocument生成テストプログラム。
	 * @param args 0=path/1=/2=password/3=startYear/4=endYear/5=category
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void main(String[] args)
		throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException,
		InvalidAlgorithmParameterException,
		ParserConfigurationException,
		SAXException,
		IOException,
		ParseException,
		TransformerFactoryConfigurationError, TransformerException
	{
		int startYear = Integer.parseInt(args[3]);
		int endYear = Integer.parseInt(args[4]);

		// 日記ファイル読み込み。
		DiaryDocumentCollectionFromDirectory diaries =
			new DiaryDocumentCollectionFromDirectory(
				args[0],
				args[1],
				args[2],
				new DateRange(startYear, endYear));

		ArrayList<SearchResultDay> results =
			diaries.search(args[5], null, true);

		MonthlyCount monthly = new MonthlyCount(results, startYear, endYear);
		KDocument document = new MonthlyCountGraphDocument(monthly, startYear);
		document.write(new OutputStreamWriter(System.out));
	}

	/**
	 * 指定の計算結果を受けSVG文書を構築。
	 * @param data 日ごとの日記の分量
	 * @param start 開始年
	 * @throws ParserConfigurationException
	 */
	public MonthlyCountGraphDocument(ArrayList<Integer> data, int start)
		throws ParserConfigurationException,
		TransformerConfigurationException
	{
		// トップ要素。
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);

		Text text = createTextNode("月毎グラフ");
		element.appendChild(text);

		// 枠線。
		element = createElement("rect");
		element.setAttribute
			("x", String.valueOf(origin.x - 1));
		element.setAttribute
			("y", String.valueOf(origin.y - 1));
		element.setAttribute
			("width", String.valueOf(size.width * data.size() + 2));
		element.setAttribute
			("height", String.valueOf(size.height * maxCount + 1));

		element.setAttribute("fill", "none");
		element.setAttribute("stroke", "#000000");
		top.appendChild(element);

		// 目盛り。
		for (int i=0 ; i<=maxCount ; i+=5)
		{
			element = createElement("line");
			element.setAttribute
				("x1", String.valueOf(origin.x - (i % 10 == 0 ? 10 : 5)));
			element.setAttribute
				("y1", String.valueOf(origin.y + size.height * (maxCount - i)));
			element.setAttribute
				("x2", String.valueOf(origin.x));
			element.setAttribute
				("y2", String.valueOf(origin.y + size.height * (maxCount - i)));
			element.setAttribute("stroke", "#000000");
			top.appendChild(element);

			element = createElement("text");
			element.setAttribute(
				"x",
				String.valueOf(origin.x - 20));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + size.height * (maxCount - i) + 5));
			element.setAttribute("font-family", fontFamily);
			element.setAttribute("text-anchor", "end");
			element.appendChild(createTextNode(String.valueOf(i)));
			top.appendChild(element);
		}

		for (int i=0 ; i<data.size() ; i++)
		{
			// 年を表示。
			if (i % 12 == 0)
			{
				// 年の初め。

				element = createElement("text");

				element.setAttribute(
					"x",
					String.valueOf
						(origin.x + size.width * i - 10));
				element.setAttribute(
					"y",
					String.valueOf
						(origin.y + size.height * maxCount + 20));

				element.setAttribute("font-family", fontFamily);

				element.appendChild
					(createTextNode(String.valueOf(start + i / 12)));

				top.appendChild(element);
			}

			// 棒を表示。
			element = createElement("rect");

			element.setAttribute(
				"x",
				String.valueOf(origin.x + size.width * i + 1));
			element.setAttribute(
				"y",
				String.valueOf
					(origin.y + size.height * (maxCount - data.get(i))));
			element.setAttribute(
				"width",
				String.valueOf(size.width - 2));
			element.setAttribute(
				"height",
				String.valueOf(size.height * data.get(i)));

			element.setAttribute("fill", "#ddddff");
			element.setAttribute("stroke", "black");

			top.appendChild(element);
		}
	}
}
