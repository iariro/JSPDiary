package kumagai.diary.junit;

import java.text.ParseException;

import junit.framework.TestCase;
import kumagai.diary.SearchResultDay;

public class SearchResultDayTest
	extends TestCase
{
	public void test1()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/15");
		assertTrue(day.isToday(0));
	}

	public void test2()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/14");
		assertTrue(day.isToday(-1));
	}

	public void test3()
		throws ParseException
	{
		SearchResultDay day = new SearchResultDay("2012/09/12");
		assertFalse(day.isToday(0));
	}
}
