package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 空内容検索トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/searchempty1.jsp")
public class SearchEmpty1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 空内容検索トップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchempty1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
