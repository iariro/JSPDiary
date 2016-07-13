package kumagai.diary.junit;

import junit.framework.*;
import java.io.*;
import java.util.*;
import ktool.crypt.*;
import kumagai.diary.*;

public class DiaryDocumentReplaceTest
	extends TestCase
{
	static private String diaryPath =
		"C:\\Users\\w81515sr\\Documents\\Nefertiti の文書\\Private\\log\\";

	static private String imagePath =
		"C:\\Users\\kumagai\\Pictures\\diary";

	static private String password = "xxx";

	private DiaryDocument document;

	public void setUp()
		throws Exception
	{
		DesDecryptCipher desDecryptCipher =
			new DesDecryptCipher(
				new DesKeyAndIVByMD5(password));

		InputStream stream = new FileInputStream(diaryPath + "201303.xml");

		document =
			new DiaryDocument(
				desDecryptCipher.createInputStream(stream),
				imagePath);

		stream.close();
	}

	public void testReplace()
		throws Exception
	{
		ArrayList<SearchResultDay> result =
			document.search("ラーメン", "なりたけ", true, true);

		for (SearchResultDay day : result)
		{
			System.out.println(day);
		}

		assertEquals(5, result.size());
	}
}
