package kumagai.diary.struts2;

import java.util.*;

/**
 * 店ごとのカウントのコレクション。
 * @author kumagai
 */
public class MiseAndCountCollection
	extends ArrayList<MiseAndCount>
{
	public final int year;

	/**
	 * 年を割り当て。
	 * @param year 年
	 */
	public MiseAndCountCollection(int year)
	{
		this.year = year;
	}
}
