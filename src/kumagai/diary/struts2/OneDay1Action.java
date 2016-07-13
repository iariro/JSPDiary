package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * １日分のみ表示受付アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/oneday1.jsp")
public class OneDay1Action
{
	public String date;
	public String category;

	/**
	 * １日分のみ表示受付アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("oneday1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
