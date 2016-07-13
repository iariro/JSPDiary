package kumagai.diary.junit;

import java.util.*;
import junit.framework.*;
import kumagai.diary.*;
import kumagai.diary.struts2.*;

public class OneDay2ActionTest
	extends TestCase
{
	String path = "C:/Users/kumagai/Documents/Nefertiti の文書/Private/log/";
	String category = "c";
	String p = "x";

	public void test1()
		throws Exception
	{
		ArrayList<SearchResultDay> results =
			OneDay2Action.search
				("2012/09/21-2012/09/22,2014/05/24", path, "", category, p);

		for (SearchResultDay result : results)
		{
			System.out.println(result);
		}
	}
}
