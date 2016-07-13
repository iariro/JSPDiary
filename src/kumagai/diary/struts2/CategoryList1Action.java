package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * カテゴリー一覧トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/categorylist1.jsp")
public class CategoryList1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * カテゴリー一覧トップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("categorylist1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
