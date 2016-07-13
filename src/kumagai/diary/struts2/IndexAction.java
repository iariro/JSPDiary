package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 日記トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/index.jsp")
public class IndexAction
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 日記トップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("index")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
