package kumagai.diary.junit;

import junit.framework.*;
import kumagai.diary.*;

public class DateRangeTest
	extends TestCase
{
	public void test1()
	{
		DateRange dateRange = new DateRange(2013, 11, 2014, 3);

		String [] yearMonths = dateRange.getYearMonths();
		assertEquals(5, yearMonths.length);
		assertEquals("201311", yearMonths[0]);
		assertEquals("201312", yearMonths[1]);
		assertEquals("201401", yearMonths[2]);
		assertEquals("201402", yearMonths[3]);
		assertEquals("201403", yearMonths[4]);
	}

	public void test2()
	{
		DateRange dateRange = new DateRange(2012, 2013);

		String [] yearMonths = dateRange.getYearMonths();
		assertEquals(24, yearMonths.length);
	}
}
