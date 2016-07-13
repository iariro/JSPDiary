package kumagai.diary;

import java.awt.*;

/**
 * 一塊の密度データ。連続する同一の密度を扱う。
 */
public class DensityRect
{
	public final Point origin;
	public final int width;
	public final int density;

	/**
	 * オブジェクトの構築を行うとともに、メンバ初期化を行う。
	 * @param origin 原点
	 * @param width 日数
	 * @param density 密度
	 */
	public DensityRect(Point origin, int width, int density)
	{
		this.origin = origin;
		this.width = width;
		this.density = density;
	}
}
