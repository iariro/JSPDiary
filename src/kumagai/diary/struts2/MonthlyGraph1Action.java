package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 月ごと件数グラフトップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/monthlygraph1.jsp")
public class MonthlyGraph1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 月ごと件数グラフトップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("monthlygraph1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
