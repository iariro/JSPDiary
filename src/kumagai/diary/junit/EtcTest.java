package kumagai.diary.junit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;
import ktool.datetime.DateTime;

public class EtcTest
	extends TestCase
{
	static SimpleDateFormat dateFormat1 = new SimpleDateFormat();

	static
	{
		dateFormat1.applyPattern("yyyy/MM/dd");
	}

	public void test1()
	{
		Assert.assertEquals("ラーメン", "・ラーメン".substring(1));
	}

	public void test2()
	{
		System.out.println(dateFormat1.format(new Date()));
	}

	public void test3()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -20);
		System.out.println(dateFormat1.format(calendar.getTime()));
	}

	public void test4()
		throws ParseException
	{
		DateTime date = new DateTime("2012/03/04 00:00:00");
		System.out.println(date);
	}

	public void testPattern()
	{
		Pattern categoryPattern = Pattern.compile("..鑑賞");
		Assert.assertTrue(categoryPattern.matcher("xxx鑑賞").find());
	}
}
