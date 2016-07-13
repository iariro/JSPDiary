package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 間隔検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/searchinterval1.jsp")
public class SearchInterval1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 間隔検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchinterval1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
