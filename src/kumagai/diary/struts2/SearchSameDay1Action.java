package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 同じ日検索ページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/searchsameday1.jsp")
public class SearchSameDay1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 同じ日検索ページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchsameday1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
