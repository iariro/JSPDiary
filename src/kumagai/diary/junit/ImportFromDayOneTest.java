package kumagai.diary.junit;

import java.util.regex.*;
import junit.framework.*;

public class ImportFromDayOneTest
	extends TestCase
{
	static private final Pattern datePattern =
		Pattern.compile("\tDate:\t([0-9]*)年([0-9]*)月([0-9]*)日 ([0-9:]*) .*");

	public void test1()
		throws Exception
	{
		String line = "	Date:	2016年3月20日 12:00:00 JST";
		Matcher dateMatcher = datePattern.matcher(line);

		if (dateMatcher.find())
		{
			System.out.println(dateMatcher.group(1));
			System.out.println(dateMatcher.group(2));
			System.out.println(dateMatcher.group(3));
			System.out.println(dateMatcher.group(4));
		}
	}
}
