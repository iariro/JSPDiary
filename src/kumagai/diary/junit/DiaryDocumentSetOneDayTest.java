package kumagai.diary.junit;


import junit.framework.*;
import java.io.*;
import java.util.*;
import ktool.crypt.*;
import kumagai.diary.*;

public class DiaryDocumentSetOneDayTest
	extends TestCase
{
	static private String diaryPath =
		"C:\\Users\\kumagai\\Documents\\Nefertiti の文書\\Private\\log\\";

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

		InputStream stream = new FileInputStream(diaryPath + "201209.xml");

		document =
			new DiaryDocument(
				desDecryptCipher.createInputStream(stream),
				imagePath);

		stream.close();
	}

	public void tearDown()
	{
		System.out.println(document.getDocumentElement());
	}

	public void testReplace()
		throws Exception
	{
		ArrayList<String> lines = new ArrayList<String>();

		lines.add("・abc");
		lines.add("def");
		lines.add("・123");
		lines.add("456");

		document.setOneDay("2012/09/08", lines);
	}

	public void testAppend()
		throws Exception
	{
		ArrayList<String> lines = new ArrayList<String>();

		lines.add("・abc");
		lines.add("def");
		lines.add("・123");
		lines.add("456");

		document.setOneDay("2012/09/12", lines);
	}

	public void testInsert()
		throws Exception
	{
		ArrayList<String> lines = new ArrayList<String>();

		lines.add("・abc");
		lines.add("def");
		lines.add("・123");
		lines.add("456");

		document.setOneDay("1990/01/01", lines);
	}
}
