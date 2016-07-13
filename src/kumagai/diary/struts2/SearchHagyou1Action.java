package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * は行検索トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/searchhagyou1.jsp")
public class SearchHagyou1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * は行検索トップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchhagyou1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
