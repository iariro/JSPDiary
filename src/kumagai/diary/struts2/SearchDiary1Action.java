package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 日記検索アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/searchdiary1.jsp")
public class SearchDiary1Action
{
	public String category;
	public String phrase;
	public String startYear;
	public String startMonth;
	public String endYear;
	public String endMonth;

	/**
	 * 日記検索アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("searchdiary1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
