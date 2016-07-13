package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * ラーメン屋に行った回数カウントアクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/raamencount1.jsp")
public class RaamenCount1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * ラーメン屋に行った回数カウントアクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("raamencount1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
