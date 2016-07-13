package kumagai.diary.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 密度ビュートップページ表示アクション。
 * @author kumagai
 */
@Namespace("/diary")
@Result(name="success", location="/diary/densityview1.jsp")
public class DensityView1Action
{
	public YearMonthSelection yearMonthSelection = new YearMonthSelection();

	/**
	 * 密度ビュートップページ表示アクション。
	 * @return 処理結果
	 * @throws Exception
	 */
	@Action("densityview1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
