package kumagai.diary;

import java.awt.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import ktool.xml.*;

/**
 * 日ごとの日記の分量をSVG形式でグラフ化。
 */
public class DensityDocument
	extends KDocument
{
	/**
	 * １日を表すますのサイズ。
	 */
	static private final Dimension size;

	/**
	 * 表の左上隅の座標。
	 */
	static private final Point location;

	/**
	 * 静的メンバの初期化。
	 */
	static
	{
		size = new Dimension(2, 15);
		location = new Point(50, 20);
	}

	/**
	 * 指定の計算結果を受けSVG文書を構築。
	 * @param density 日ごとの日記の分量
	 * @param start 開始年
	 * @param end 終了年
	 * @throws ParserConfigurationException
	 */
	public DensityDocument
		(HashMap <Calendar, Integer> density, int start, int end)
		throws ParserConfigurationException,
		TransformerConfigurationException
	{
		// トップ要素。
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		// 枠線。
		Element element = createElement("rect");
		element.setAttribute
			("x", String.valueOf(location.x - 1));
		element.setAttribute
			("y", String.valueOf(location.y - 1));
		element.setAttribute
			("width", String.valueOf(size.width * 366 + 2));
		element.setAttribute
			("height", String.valueOf(size.height * (end - start + 1) + 2));

		element.setAttribute("fill", "none");
		element.setAttribute("stroke", "black");
		top.appendChild(element);

		for (int i=0 ; i<=end-start ; i++)
		{
			// 年数。
			element = createElement("text");
			element.setAttribute
				("x", String.valueOf(location.x - 35));
			element.setAttribute
				("y", String.valueOf(location.y + 12 + size.height * i));
			element.appendChild
				(createTextNode(String.valueOf(start + i)));
			top.appendChild(element);

			// 日付オブジェクト生成。
			GregorianCalendar calendar = new GregorianCalendar(i + start, 0, 1);

			// うるう年計算。
			int dayOfYear = calendar.isLeapYear(i + start) ? 366 : 365;

			for (int j=0 ; j<dayOfYear ; j++)
			{
				if (density.containsKey(calendar))
				{
					// 指定の日はある。

					// その日の値を取得。
					int count = density.get(calendar);

					if (count > 0)
					{
						// １文字でも書いている。falseになることは無いはず。

						element = createElement("rect");
						element.setAttribute
							("x", String.valueOf(location.x + size.width * j));
						element.setAttribute
							("y", String.valueOf(location.y + size.height * i));
						element.setAttribute
							("width", String.valueOf(size.width));
						element.setAttribute
							("height", String.valueOf(size.height));

						if (count >= 100)
						{
							// 100以上。

							element.setAttribute("fill", "#cc0000");
						}
						else if (count >= 50)
						{
							// 50以上。

							element.setAttribute("fill", "#ff7777");
						}
						else
						{
							// 50未満。

							element.setAttribute("fill", "#ffcccc");
						}

						top.appendChild(element);
					}
				}

				// 次の日へ。
				calendar.add(Calendar.DATE, 1);
			}
		}
	}
}
