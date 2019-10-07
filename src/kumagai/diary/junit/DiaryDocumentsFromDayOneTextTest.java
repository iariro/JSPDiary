package kumagai.diary.junit;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;
import kumagai.diary.DiaryDocument;
import kumagai.diary.SearchResultDay;
import kumagai.diary.dayone.DiaryDocumentsFromDayOneText;

public class DiaryDocumentsFromDayOneTextTest
	extends TestCase
{
	public void test()
		throws Exception
	{
		FileInputStream inStream = new FileInputStream("testdata/2019-10-07-Journal-TXT.txt");
		HashMap<String, DiaryDocument> documents =
			new DiaryDocumentsFromDayOneText(inStream, true, true);

		DiaryDocument diary201909 = documents.get("201909");
		ArrayList<SearchResultDay> all = diary201909.getAllNoTag();
		SearchResultDay day = all.get(0);
		assertEquals("2019/09/01（日）", day.getDate());
		assertEquals("音楽", day.get(0).category);
		assertEquals("買ってきたPMA-5で遊ぶ。", day.get(0).lines.get(0));
		assertEquals("", day.get(0).lines.get(1));
		assertEquals("7:56:38 - 前原東2丁目10-11, 船橋市, 千葉県, 日本", day.get(0).lines.get(2));

		assertEquals("ラーメン", day.get(1).category);
		assertEquals("川崎めんりゅう。新規。", day.get(1).lines.get(0));
		assertEquals("", day.get(1).lines.get(1));
		assertEquals("12:08:24", day.get(1).lines.get(2));

		day = all.get(1);
		assertEquals("2019/09/13（金）", day.getDate());
		assertEquals("お出かけ", day.get(0).category);
		assertEquals("ハードオフ西那須野店。", day.get(0).lines.get(0));
		assertEquals("@image(a5421d82cfe0b83f016496acdc737fdd.jpeg)", day.get(0).lines.get(1));
		assertEquals("結構いいものあるじゃない。", day.get(0).lines.get(2));
		assertEquals("", day.get(0).lines.get(3));
		assertEquals("16:16:24 - 南郷屋3丁目141-11, 那須塩原市, 栃木県, 日本", day.get(0).lines.get(4));

		DiaryDocument diary201910 = documents.get("201910");
		all = diary201910.getAllNoTag();
		day = all.get(0);
		assertEquals("2019/10/04（金）", day.getDate());
		assertEquals("盗撮", day.get(0).category);
		assertEquals("@image(6b4aa6c673262ed02d7b62d12e09d62a.jpeg)", day.get(0).lines.get(0));
		assertEquals("これスカートたくし上げてるのか。いいよな。", day.get(0).lines.get(1));
		assertEquals("@image(af9c8c903ef427f1a76c361688cc10d3.jpeg)", day.get(0).lines.get(2));
		day = all.get(1);
		assertEquals("2019/10/05（土）", day.getDate());
		assertEquals("盗撮", day.get(0).category);
		assertEquals("@image(927de822e7e20947c7bc78d9baa7c2ff.jpeg)", day.get(0).lines.get(0));
		assertEquals("@image(a1894f21530735992906df91e754c6fc.jpeg)", day.get(0).lines.get(1));
		assertEquals("@image(094858a0a4e8230b914e6cd15b9da245.jpeg)", day.get(0).lines.get(2));
		assertEquals("@image(2a45325ce78be959be9e58c848b7b80b.png)", day.get(0).lines.get(3));
		assertEquals("@image(5f92e62a4bd3a7c450f3905c1715dfb5.jpeg)", day.get(0).lines.get(4));
		assertEquals("今日暑かったからな、10月なのに薄着の女子が多い多い。", day.get(0).lines.get(5));
		assertEquals("14:00:39 - 前原東2丁目10-11, 船橋市, 千葉県, 日本", day.get(0).lines.get(6));
	}
}
