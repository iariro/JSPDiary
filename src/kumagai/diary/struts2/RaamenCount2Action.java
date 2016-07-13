package kumagai.diary.struts2;

import java.util.*;
import javax.servlet.*;
import com.sun.org.apache.xerces.internal.impl.io.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.datetime.*;
import kumagai.diary.*;

/**
 * ラーメン屋に行った回数カウントアクション。
 * @author kumagai
 */
@Namespace("/diary")
@Results
({
	@Result(name="success", location="/diary/raamencount2.jsp"),
	@Result(name="error", location="/diary/error.jsp")
})
public class RaamenCount2Action
{
	public String password;
	public int startYear;
	public int endYear;

	public ArrayList<MiseAndCountCollection> countCollection =
		new ArrayList<MiseAndCountCollection>();

	public int [] sum;
	public Exception exception;
	public String message;

	/**
	 * カウントコレクション追加。
	 * @param countCollection カウントコレクション
	 */
	public void addCountCollection(MiseAndCountCollection countCollection)
	{
		this.countCollection.add(countCollection);
	}

	/**
	 * 年数を取得。
	 * @return 年数
	 */
	public int getYearCount()
	{
		return countCollection.size();
	}

	/**
	 * ラーメン屋に行った回数カウントアクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("raamencount2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String diaryFolder = context.getInitParameter("diaryPath");
		String imagePath = context.getInitParameter("imagePath");
		String [] raamenyaList =
			context.getInitParameter("RaamenyaList").split(",");

		DateTime today = new DateTime();

		sum = new int [raamenyaList.length];

		try
		{
			for (int i=startYear ; i<=endYear ; i++)
			{
				int endMonth = 12;

				if (i == today.getYear())
				{
					// 今年。

					endMonth = today.getMonth();
				}

				// 日記ファイル読み込み。
				DiaryDocumentCollectionFromDirectory diaries =
					new DiaryDocumentCollectionFromDirectory(
						diaryFolder,
						imagePath,
						password,
						new DateRange(i, 1, i, endMonth));

				MiseAndCountCollection countCollection =
					new MiseAndCountCollection(i);

				for (int j=0 ; j<raamenyaList.length ; j++)
				{
					ArrayList<SearchResultDay> results =
						diaries.search("ラーメン", raamenyaList[j], true);

					countCollection.add
						(new MiseAndCount(raamenyaList[j], results.size()));

					sum[j] += results.size();
				}

				addCountCollection(countCollection);
			}

			return "success";
		}
		catch (MalformedByteSequenceException exception)
		{
			this.message = "パスワードが違うかファイルが壊れています";
			this.exception = exception;

			return "error";
		}
	}
}
