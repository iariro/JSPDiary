package kumagai.diary.junit;

import java.io.*;
import java.util.*;
import junit.framework.*;
import kumagai.diary.*;
import ktool.string.*;
import ktool.datetime.*;

public class DiaryDocumentEtcTest
	extends TestCase
{
	public void testGetAll()
		throws Exception
	{
		String xml =
			"<diary>" +
			"<day date=\"2012/09/03\"><topic category=\"生活\"><line>テレビを処分した。</line><line>処分代4410円。</line><line>７か月放っておいてしまったがようやく処分できたぞ。</line></topic></day>" +
			"<day date=\"2012/09/05\"><topic category=\"生活\"><line>あ</line></topic></day>" +
			"</diary>";

		InputStream stream = new ByteArrayInputStream(xml.getBytes());
		DiaryDocument document = new DiaryDocument(stream, null);
		ArrayList<SearchResultDay> days = document.getAll(true, new DateTime(), true);

		for (SearchResultDay day : days)
		{
			System.out.println(day);
		}
	}

	public void testGetMonthFromFilename()
	{
		String filepath =
			"C:\\Users\\kumagai\\Documents\\Nefertiti の文書\\Private\\log\\201209.xml";

		DateTime month = DiaryDocument.getMonthFromFilename(filepath);
		assertEquals(2012, month.getYear());
		assertEquals(9, month.getMonth());
		assertEquals(1, month.getDay());
	}

	static private String diaryPath =
		"C:\\Users\\kumagai\\Documents\\Nefertiti の文書\\Private\\log\\";

	static private String imagePath =
		"C:\\Users\\kumagai\\Pictures\\diary";

	static private String password = "xxx";

	public void testGetOneDay()
		throws Exception
	{
		String dateCollectionString = "2013/05/26-2013/06/01";

		DateCollection dateCollection =
			DateCollection.fromHyphenAndComma(dateCollectionString);

		ArrayList<String> yearMonths = new ArrayList<String>();

		for (String date : dateCollection)
		{
			String yearMonth = date.substring(0, 4) + date.substring(5, 7);

			boolean find = false;

			for (String yearMonth2 : yearMonths)
			{
				if (yearMonth.equals(yearMonth2))
				{
					// 年月が同じ。

					find = true;
					break;
				}
			}

			if (! find)
			{
				// 初登場。

				yearMonths.add(yearMonth);
			}
		}

		assertEquals(2, yearMonths.size());

		String [] yearMonths2 = yearMonths.toArray(new String [] {});

		DiaryDocumentCollectionFromDirectory diaryDocuments =
			new DiaryDocumentCollectionFromDirectory(
				diaryPath,
				imagePath,
				password,
				yearMonths2);

		for (DiaryDocument diary : diaryDocuments)
		{
			boolean find = false;

			for (String date : dateCollection)
			{
				String dateShort = date.substring(0, 4) + date.substring(5, 7);

				if (diary.getYearAndMonth().equals(dateShort))
				{
					// 年月は一致。

					SearchResultDay result =
						diary.getOneDay(DateTime.parseDateString(date), "AV鑑賞");

					System.out.println(result);

					find = true;
				}
				else
				{
					// 年月は一致しない。

					if (find)
					{
						break;
					}
				}
			}
		}
	}
}
