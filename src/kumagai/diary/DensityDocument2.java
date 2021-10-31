package kumagai.diary;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ktool.xml.KDocument;

/**
 * 日ごとの日記の分量をSVG形式でグラフ化。
 * 同じ密度の連続をまとめて扱えるようにした改良版。
 */
public class DensityDocument2
	extends KDocument
{
	/**
	 * １日を表すますのサイズ。
	 */
	static private Dimension rectSize;

	/**
	 * 表の左上隅の座標。
	 */
	static private Point origin;

	/**
	 * フォント名。
	 */
	static private final String fontFamily = "MS-Mincho";

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		rectSize = new Dimension(2, 15);
		origin = new Point(75, 20);
	}

	/**
	 * オブジェクトの構築とともに、指定の計算結果を受けSVG文書を構築。
	 * @param volumePerDay 日ごとの日記の分量
	 * @param startYear 開始年
	 * @param endYear 終了年
	 * @throws ParserConfigurationException
	 */
	public DensityDocument2
		(HashMap <Calendar, Integer> volumePerDay, int startYear, int endYear)
		throws ParserConfigurationException,
		TransformerConfigurationException
	{
		DensityRectCollection densityRectCollection =
			new DensityRectCollection(volumePerDay, startYear, endYear);

		// トップ要素。
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);

		Text text = createTextNode("日記密度グラフ");
		element.appendChild(text);

		// 枠線。
		element = createElement("rect");

		element.setAttribute(
			"x",
			String.valueOf(origin.x - 1));
		element.setAttribute(
			"y",
			String.valueOf(origin.y - 1));
		element.setAttribute(
			"width",
			String.valueOf(rectSize.width * 366 + 2));
		element.setAttribute(
			"height",
			String.valueOf(rectSize.height * (endYear - startYear + 1) + 2));

		element.setAttribute("fill", "none");
		element.setAttribute("stroke", "black");
		top.appendChild(element);

		// 年数。
		for (int i=0 ; i<=endYear-startYear ; i++)
		{
			element = createElement("text");

			element.setAttribute(
				"x",
				String.valueOf(origin.x - 65));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + 12 + rectSize.height * i));
			element.setAttribute("font-family", fontFamily);
			element.appendChild
				(createTextNode(String.format("%d年", startYear + i)));

			top.appendChild(element);
		}

		// 密度データ。
		for (DensityRect rect : densityRectCollection)
		{
			element = createElement("rect");

			element.setAttribute(
				"x",
				String.valueOf(origin.x + rectSize.width * rect.origin.x));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + rectSize.height * rect.origin.y));
			element.setAttribute(
				"width",
				String.valueOf(rectSize.width * rect.width));
			element.setAttribute(
				"height",
				String.valueOf(rectSize.height));
			element.setAttribute(
				"fill",
					rect.density == 1 ? "#ccccff" :
					rect.density == 2 ? "#7777ff" :
					rect.density == 3 ? "#0000cc" :
					rect.density == 4 ? "#000077" : null);

			top.appendChild(element);
		}
	}
}
