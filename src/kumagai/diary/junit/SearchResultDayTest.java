package kumagai.diary.junit;

import java.text.*;
import junit.framework.*;
import kumagai.diary.*;

public class SearchResultDayTest
	extends TestCase
{
	public void test1()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/15");
		Assert.assertTrue(day.isToday(0));
	}

	public void test2()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/14");
		Assert.assertTrue(day.isToday(-1));
	}

	public void test3()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/12");
		Assert.assertFalse(day.isToday(0));
	}
}
