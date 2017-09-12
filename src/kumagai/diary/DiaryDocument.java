package kumagai.diary;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

import ktool.crypt.DesDecryptCipher;
import ktool.crypt.DesKeyAndIVByMD5;
import ktool.datetime.DateTime;
import ktool.xml.StructElement;

/**
 * 日記ドキュメント。
 * @author kumagai
 */
public class DiaryDocument
	extends DiaryDocumentCore
{
	/**
	 * ドキュメント生成テスト。
	 * @param args [0]=DiaryDocument [1]=imagepath [2]=password
	 * @throws Exception
	 */
	public static void main(String [] args)
		throws Exception
	{
		if (args.length >= 3)
		{
			// 引数の数は足りている。

			DesDecryptCipher cipher =
				new DesDecryptCipher(new DesKeyAndIVByMD5(args[2]));

			InputStream stream = new FileInputStream(args[0]);

			DiaryDocument document =
				new DiaryDocument(
					cipher.createInputStream(stream),
					args[1]);

			stream.close();

			document.write(new OutputStreamWriter(System.out));

			ArrayList<SearchResultDay> results =
				document.getAll(false, new DateTime(), true);

			System.out.println();
			System.out.println(results.size() + "件");
		}
		else
		{
			System.out.println
				("Usage : DiaryDocument filepath imagepath password");
		}
	}

	/**
	 * ファイル名から年月を生成。
	 * @param filepath ファイル名
	 * @return 年月値
	 */
	static public DateTime getMonthFromFilename(String filepath)
	{
		String filename = new File(filepath).getName();

		return
			new DateTime(
				Integer.valueOf(filename.substring(0, 4)),
				Integer.valueOf(filename.substring(4, 6)),
				1,
				0,
				0,
				0);
	}

	/**
	 * 指定されたストリームにより日記ファイルを読み込む。
	 * @param stream XML形式の日記ファイルのストリーム
	 * @param imagePath 画像ファイルパス
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 */
	public DiaryDocument(InputStream stream, String imagePath)
		throws ParserConfigurationException,
		SAXException,
		IOException,
		TransformerConfigurationException,
		TransformerFactoryConfigurationError
	{
		super(stream, imagePath);
	}

	/**
	 * 空の日記ドキュメントを構築する。
	 * @throws Exception
	 */
	public DiaryDocument()
		throws Exception
	{
		appendChild(createElement("diary"));
	}

	/**
	 * ドキュメント内容から年月を取得。
	 * @return ドキュメント内容から取得した年月
	 */
	public String getYearMonthFromContent()
	{
		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		if (days.length > 0)
		{
			return days[0].element().getAttribute("date");
		}
		else
		{
			return null;
		}
	}

	/**
	 * 年月を取得。第１要素から取得する。
	 * @return 年月
	 */
	public String getYearAndMonth()
		throws ParseException
	{
		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		if (days.length > 0)
		{
			String date = days[0].element().getAttribute("date");

			return date.substring(0, 4) + date.substring(5, 7);
		}
		else
		{
			return null;
		}
	}

	/**
	 * すべて取得。
	 * @param insert true=飛んでいる日を補完
	 * @param month 対象月
	 * @param asc true=昇順／false=降順
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> getAll
		(boolean insert, DateTime month, boolean asc)
		throws ParseException
	{
		ArrayList <SearchResultDay> ret = new ArrayList<SearchResultDay>();

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		DateTime startDay = null;

		if (days.length > 0)
		{
			// １日分でも内容がある。

			startDay =
				DateTime.parseDateString(days[0].element().getAttribute("date"));
		}

		if (insert)
		{
			// 飛んでいる日を補完する。

			for (int i=1 ; i<startDay.getDay() ; i++)
			{
				DateTime date = month;
				date.setDay(i);
				ret.add(new SearchResultDay(date.toString()));
			}
		}

		DateTime day2 = null;

		for (int i=0 ; i<days.length ; i++)
		{
			String dayString = days[i].element().getAttribute("date");

			DateTime day1 = DateTime.parseDateString(dayString);

			if (insert && day2 != null)
			{
				// 飛んでいる日を補完する。かつ、既存の内容の後である。

				for (int j=day2.getDay()+1 ; j<day1.getDay() ; j++)
				{
					DateTime date = month;
					date.setDay(j);
					ret.add(new SearchResultDay(date.toString()));
				}
			}

			SearchResultDay dayResult = new SearchResultDay(dayString);

			StructElement [] topics = days[i].getChildElements();

			for (int j=0 ; j<topics.length ; j++)
			{
				String category = topics[j].element().getAttribute("category");
				ArrayList <String> linesString = new ArrayList<String>();

				boolean tab = false;

				StructElement [] lines = topics[j].getChildElements();

				for (int k=0 ; k<lines.length ; k++)
				{
					String line = lines[k].getContent();

					if (line == null)
					{
						// 行の内容なし。

						line = new String();
					}

					if (line.indexOf("	") == 0)
					{
						// タブから始まっている。

						if (! tab)
						{
							// タブの中ではない＝タブ開始点。

							linesString.add("<ul class='indentlist'>");
							tab = true;
						}
					}
					else
					{
						// タブから始まっていない。

						if (tab)
						{
							// タブの中である＝タブ終了点。

							linesString.add("</ul>");
							tab = false;
						}
					}

					line = effect(line);

					if (tab)
					{
						// タブの中である。

						linesString.add(line);
					}
					else
					{
						// タブの中ではない。

						linesString.add(line + "<br>");
					}
				}

				if (tab)
				{
					// タブの中である。

					linesString.add("</ul>");
					tab = false;
				}

				dayResult.add(new SearchResult(category, linesString));
			}

			ret.add(dayResult);

			day2 = day1;
		}

		DateTime today = new DateTime();

		if (insert)
		{
			// 飛んでいる日を補完する。

			for (int i=day2.getDay()+1 ; i<=today.getDay() ; i++)
			{
				DateTime date = month;
				date.setDay(i);
				ret.add(new SearchResultDay(date.toString()));
			}
		}

		// 反転処理。
		if (! asc)
		{
			// 降順指定。

			ArrayList <SearchResultDay> ret2 = new ArrayList<SearchResultDay>();

			for (int i=0 ; i <ret.size() ; i++)
			{
				ret2.add(0, ret.get(i));
			}

			ret = ret2;
		}

		return ret;
	}

	/**
	 * すべて取得。HTMLタグ付けなし。
	 * @param insert true=飛んでいる日を補完
	 * @param month 対象月
	 * @param asc true=昇順／false=降順
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> getAllNoTag()
		throws ParseException
	{
		ArrayList <SearchResultDay> ret = new ArrayList<SearchResultDay>();

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			String dayString = days[i].element().getAttribute("date");

			SearchResultDay dayResult = new SearchResultDay(dayString);

			StructElement [] topics = days[i].getChildElements();

			for (int j=0 ; j<topics.length ; j++)
			{
				String category = topics[j].element().getAttribute("category");
				ArrayList <String> linesString = new ArrayList<String>();

				StructElement [] lines = topics[j].getChildElements();

				for (int k=0 ; k<lines.length ; k++)
				{
					String line = lines[k].getContent();

					if (line == null)
					{
						// 行の内容なし。

						line = new String();
					}

					linesString.add(line);
				}

				dayResult.add(new SearchResult(category, linesString));
			}

			ret.add(dayResult);
		}

		return ret;
	}

	/**
	 * 検索。１行目のみ対象指定付き。
	 * @param searchCategory カテゴリー
	 * @param searchPhrase フレーズ
	 * @param top true=１行目のみ対象／false=制限なし
	 * @param topicOnly true=トピックのみ絞り込む／false=日全体を対象にする
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> search(String searchCategory,
		String searchPhrase, boolean top, boolean topicOnly)
		throws ParseException
	{
		Pattern categoryPattern = Pattern.compile(searchCategory);

		ArrayList <SearchResultDay> ret = new ArrayList<SearchResultDay>();

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			String day = days[i].element().getAttribute("date");

			SearchResultDay dayResult = new SearchResultDay(day);

			StructElement [] topics = days[i].getChildElements();

			// 検索
			boolean foundDay = false;

			if (! topicOnly)
			{
				// トピックの絞り込みはしない

				for (int j=0 ; j<topics.length ; j++)
				{
					String category =
						topics[j].element().getAttribute("category");

					if (searchCategory == null ||
						searchCategory.length()==0 ||
						categoryPattern.matcher(category).find())
					{
						// カテゴリは一致するまたはカテゴリ無指定。

						if (searchPhrase != null &&
							searchPhrase.length() > 0 && !foundDay)
						{
							// フレーズは指定されておりまだヒットしていない

							StructElement [] lines =
								topics[j].getChildElements();

							for (int k=0 ; k<lines.length && (!top || k<=0) ; k++)
							{
								String line = lines[k].getContent();

								if (line != null &&
									line.indexOf(searchPhrase) >= 0)
								{
									// マッチする。

									foundDay = true;
								}
							}
						}
						else
						{
							// フレーズは指定されていない

							foundDay = true;
						}
					}
				}
			}

			for (int j=0 ; j<topics.length ; j++)
			{
				String category = topics[j].element().getAttribute("category");
				ArrayList <String> linesString = new ArrayList<String>();

				boolean found;

				if (foundDay ||
					(searchCategory == null ||
					searchCategory.length()==0 ||
					categoryPattern.matcher(category).find()))
				{
					// カテゴリは一致するまたはカテゴリ無指定。

					found = false;

					boolean tab = false;

					StructElement [] lines = topics[j].getChildElements();

					for (int k=0 ; k<lines.length && (!top || k<=0) ; k++)
					{
						String line = lines[k].getContent();

						if (line == null)
						{
							// 行の内容なし。

							line = new String();
						}

						if (line.indexOf("	") == 0)
						{
							// タブから始まっている。

							if (! tab)
							{
								// タブの中ではない＝タブ開始点。

								linesString.add("<ul>");
								tab = true;
							}
						}
						else
						{
							// タブから始まっていない。

							if (tab)
							{
								// タブの中である＝タブ終了点。

								linesString.add("</ul>");
								tab = false;
							}
						}

						line = effect(line);

						if (tab)
						{
							// タブの中である。

							linesString.add(line);
						}
						else
						{
							// タブの中ではない。

							linesString.add(line + "<br>");
						}

						if (searchPhrase != null &&
							searchPhrase.length() > 0 && !found)
						{
							// フレーズは指定されておりまだヒットしていない。

							if (line.indexOf(searchPhrase) >= 0)
							{
								// マッチする。

								found = true;
							}
						}
					}

					if (tab)
					{
						// タブの中である。

						linesString.add("</ul>");
						tab = false;
					}

					if (searchPhrase == null || searchPhrase.length() == 0)
					{
						// フレーズ指定なし。

						found = true;
					}
				}
				else
				{
					// カテゴリは一致しない。

					found = false;
				}

				if (foundDay || found)
				{
					// ヒットした。

					if (searchPhrase != null && searchPhrase.length() > 0)
					{
						// フレーズ指定あり。

						for (int k=0 ; k<linesString.size() ; k++)
						{
							if (linesString.get(k).indexOf("img") < 0)
							{
								String line =
									linesString.get(k).replaceAll(
										searchPhrase,
										"<span class=\"highlight\">" + searchPhrase + "</span>");

								linesString.set(k, line);
							}
						}
					}

					dayResult.add( new SearchResult(category, linesString));
				}
			}

			if (dayResult.size() > 0)
			{
				// １個でも見つかった。

				ret.add(dayResult);
			}
		}

		return ret;
	}

	/**
	 * １日分をテキストとして取得。
	 * @param date1 対象日
	 * @return １日分の日記
	 */
	public ArrayList<String> getOneDayAsText(String date1)
		throws ParseException
	{
		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(dateFormat.parse(date1));

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			String dayString = days[i].element().getAttribute("date");
			Date date = dateFormat.parse(dayString);

			Calendar calendar2 = new GregorianCalendar();
			calendar2.setTime(date);

			if (calendar1.get(Calendar.MONTH) ==
				calendar2.get(Calendar.MONTH) &&
				calendar1.get(Calendar.DAY_OF_MONTH) ==
				calendar2.get(Calendar.DAY_OF_MONTH))
			{
				// 月日が一致。

				ArrayList<String> ret = new ArrayList<String>();

				StructElement [] topics = days[i].getChildElements();

				for (int j=0 ; j<topics.length ; j++)
				{
					String category =
						topics[j].element().getAttribute("category");

					if (ret.size() > 0)
					{
						// １行でもある＝区切りを要する。

						ret.add(new String());
					}

					ret.add("・" + category);

					StructElement [] lines = topics[j].getChildElements();

					for (int k=0 ; k<lines.length ; k++)
					{
						String line = lines[k].getContent();

						if (line != null)
						{
							// 内容あり。

							ret.add(line);
						}
						else
						{
							// 内容なし。

							ret.add(new String());
						}
					}
				}

				return ret;
			}
		}

		return null;
	}

	/**
	 * １日分を更新。
	 * @param date1 対象日
	 * @param lines 本文
	 */
	public void setOneDay(String date1, ArrayList<String> lines)
		throws ParseException
	{
		Element day = createOneDayElement(date1, lines);

		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(dateFormat.parse(date1));

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		boolean find = false;

		for (int i=0 ; i<days.length && !find ; i++)
		{
			String dayString = days[i].element().getAttribute("date");
			Date date = dateFormat.parse(dayString);

			Calendar calendar2 = new GregorianCalendar();
			calendar2.setTime(date);

			if (calendar1.get(Calendar.MONTH) ==
				calendar2.get(Calendar.MONTH) &&
				calendar1.get(Calendar.DAY_OF_MONTH) ==
				calendar2.get(Calendar.DAY_OF_MONTH))
			{
				// 月日が一致。

				getDocumentElement().replaceChild(day, days[i].element());

				find = true;
			}
			else if (calendar1.get(Calendar.MONTH) ==
				calendar2.get(Calendar.MONTH) &&
				calendar1.get(Calendar.DAY_OF_MONTH) <
				calendar2.get(Calendar.DAY_OF_MONTH))
			{
				// 指定の日を越えた。

				getDocumentElement().insertBefore(day, days[i].element());

				find = true;
			}
		}

		if (! find)
		{
			// 未挿入。

			getDocumentElement().appendChild(day);
		}
	}

	/**
	 * 同じ日を検索。
	 * @param date1 対象日
	 * @param category カテゴリ
	 * @return 検索結果
	 * @throws ParseException
	 */
	public SearchResultDay getOneDay(DateTime date1, String category)
		throws ParseException
	{
		SearchResultDay ret = null;
		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			String dayString = days[i].element().getAttribute("date");

			DateTime date2 = DateTime.parseDateString(dayString);

			if (date1.getMonth() == date2.getMonth() &&
				date1.getDay() == date2.getDay())
			{
				// 月日が一致。

				ret = new SearchResultDay(dayString);

				StructElement [] topics = days[i].getChildElements();

				for (int j=0 ; j<topics.length ; j++)
				{
					String category2 =
						topics[j].element().getAttribute("category");

					boolean add = false;

					if (category == null)
					{
						// 指定無し。

						add = true;
					}
					else if (category.length() == 0 ||
						category.equals(category2))
					{
						// 指定あり。空文字列またはカテゴリは一致する。

						add = true;
					}

					if (add)
					{
						// 追加確定。

						ArrayList <String> linesString =
							new ArrayList<String>();

						StructElement [] lines = topics[j].getChildElements();

						boolean tab = false;

						for (int k=0 ; k<lines.length ; k++)
						{
							String line = lines[k].getContent();

							if (line != null)
							{
								// 中身がある。

								if (line.indexOf("	") >= 0)
								{
									// タブから始まっている。

									if (! tab)
									{
										// タブの中ではない＝タブ開始点。

										linesString.add
											("<ul class='indentlist'>");
										tab = true;
									}
								}
								else
								{
									// タブから始まっていない。

									if (tab)
									{
										// タブの中である＝タブ終了点。

										linesString.add("</ul>");
										tab = false;
									}
								}

								line = effect(line);
							}
							else
							{
								// 中身がない。

								line = new String();
							}

							linesString.add(line + "<br>");
						}

						if (tab)
						{
							// タブの中である＝タブ終了点。

							linesString.add("</ul>");
						}

						ret.add(new SearchResult(category2, linesString));
					}
				}
			}
		}

		return ret;
	}

	/**
	 * 日ごとの日記の分量を求める。
	 * @return 日ごとの日記の分量
	 * @throws ParseException
	 */
	public HashMap <Calendar, Integer> getDensity()
		throws ParseException
	{
		HashMap <Calendar, Integer> ret = new HashMap <Calendar, Integer>();

		// 日のループ。
		StructElement top = new StructElement(getDocumentElement());
		StructElement [] days = top.getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			// トピックのループ。
			int count = 0;
			StructElement [] topics = days[i].getChildElements();

			// 行のループ。
			for (int j=0 ; j<topics.length ; j++)
			{
				StructElement [] lines = topics[j].getChildElements();

				for (int k=0 ; k<lines.length ; k++)
				{
					if (lines[k].getContent() != null)
					{
						// 内容あり。

						count += lines[k].getContent().length();
					}
				}
			}

			Date date =
				dateFormat.parse(days[i].element().getAttribute("date"));

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			ret.put(calendar, count);
		}

		return ret;
	}

	/**
	 * 全カテゴリ文字列を取得
	 * @return カテゴリ出現回数集計
	 */
	public StringRanking getCategories()
	{
		StringRanking ret = new StringRanking();
		StructElement top = new StructElement(getDocumentElement());

		StructElement [] days = top.getChildElements();

		// 日のループ。
		for (int i=0 ; i<days.length ; i++)
		{
			StructElement [] topics = days[i].getChildElements();

			// トピックのループ。
			for (int j=0 ; j<topics.length ; j++)
			{
				ret.add(topics[j].element().getAttribute("category"));
			}
		}

		return ret;
	}

	/**
	 * 空の日。
	 * @return 空の日データの日付コレクション
	 * @throws ParseException
	 */
	public ArrayList<Date> getEmptyDay()
		throws ParseException
	{
		ArrayList<Date> ret = new ArrayList<Date>();

		StructElement top = new StructElement(getDocumentElement());

		StructElement [] days = top.getChildElements();

		// 日のループ。
		for (int i=0 ; i<days.length ; i++)
		{
			StructElement [] topics = days[i].getChildElements();

			// トピックのループ。
			if (topics.length <= 0)
			{
				// 長さゼロ。

				ret.add(
					dateFormat.parse(days[i].element().getAttribute("date")));
			}
		}

		return ret;
	}

	/**
	 * 誤りの可能性がある「へぺべ」文字検索。
	 * @return 検索結果
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> searchHagyou()
		throws ParseException
	{
		Pattern [] pattern =
			new Pattern []
			{
				Pattern.compile("([あ-ん])([ヘペベ])"),	// 平仮名＋片仮名
				Pattern.compile("([ヘペベ])([あ-ん])"),	// 片仮名＋平仮名
				Pattern.compile("([ア-ン])([へぺべ])"),	// 片仮名＋平仮名
				Pattern.compile("([へぺべ])([ア-ン])"),	// 平仮名＋片仮名
				Pattern.compile("([ア-ン])([ヘペベ])"),	// 片仮名＋片仮名
				Pattern.compile("([ヘペベ])([ア-ン])")	// 片仮名＋片仮名
			};

		ArrayList <SearchResultDay> ret = new ArrayList<SearchResultDay>();

		StructElement [] days =
			new StructElement(getDocumentElement()).getChildElements();

		for (int i=0 ; i<days.length ; i++)
		{
			String day = days[i].element().getAttribute("date");

			SearchResultDay dayResult = new SearchResultDay(day);

			StructElement [] topics = days[i].getChildElements();

			for (int j=0 ; j<topics.length ; j++)
			{
				String category = topics[j].element().getAttribute("category");
				ArrayList <String> linesString = new ArrayList<String>();

				boolean found = false;

				StructElement [] lines = topics[j].getChildElements();

				for (int k=0 ; k<lines.length ; k++)
				{
					String line = lines[k].getContent();

					if (line == null)
					{
						// 行の内容なし。

						line = new String();
					}

					for (int l=0 ; l<pattern.length ; l++)
					{
						Matcher matcher = pattern[l].matcher(line);

						if (matcher.find())
						{
							// マッチする。

							line =
								matcher.replaceAll(
									matcher.group(1) +
									"<span class=\"highlight\">" +
									matcher.group(2) +
									"</span >");

							found = true;

							break;
						}
					}

					linesString.add(line);
				}

				if (found)
				{
					// マッチする。

					dayResult.add(new SearchResult(category, linesString));
				}
			}

			if (dayResult.size() > 0)
			{
				// １個でもマッチした。

				ret.add(dayResult);
			}
		}

		return ret;
	}

	/**
	 * ドキュメントの内容をXML形式の行の集合の形で取得。文書比較用。
	 * @return XML形式の行の集合
	 * @throws IOException
	 */
	public ArrayList<String> getXmlLines()
		throws IOException
	{
		String text = getDocumentElement().toString();

		BufferedReader reader = new BufferedReader(new StringReader(text));

		ArrayList<String> lines = new ArrayList<String>();

		String line;

		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}

		reader.close();

		return lines;
	}

	/**
	 * ドキュメントの内容をXML形式の行の集合の形で取得。文書比較用。
	 * @return XML形式の行の集合
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public ArrayList<String> getXmlLinesWithIndent()
		throws IOException,
			TransformerFactoryConfigurationError,
			TransformerException
	{
		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();

		//インデントを行う
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");

		//インデントの文字数
		transformer.setOutputProperty
			(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,"2");

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Writer writer = new PrintWriter(stream);
		transformer.transform
			(new DOMSource(getDocument()), new StreamResult(writer));

		byte [] buffer = stream.toByteArray();
		stream.close();

		BufferedReader reader =
			new BufferedReader
				(new InputStreamReader(new ByteArrayInputStream(buffer)));

		ArrayList<String> lines = new ArrayList<String>();

		String line;

		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}

		reader.close();

		return lines;
	}
}
