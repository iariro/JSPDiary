package kumagai.diary;

import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;
import javax.crypto.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.*;
import ktool.xml.*;

/**
 * 日ごとの日記の分量を求め、SVG形式でグラフ化する。
 * コマンドライン版。
 */
public class Density
{
	/**
	 * @param args [0]=日記ファイルがあるフォルダ,[1]=パスワード,[2]=開始年,[3]=終了年
	 * @throws ParserConfigurationException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParseException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 */
	public static void main(String[] args)
		throws
			ParserConfigurationException,
			InvalidKeyException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidAlgorithmParameterException,
			SAXException,
			IOException,
			ParseException,
			TransformerConfigurationException,
			TransformerException,
			TransformerFactoryConfigurationError
	{
		if (args.length == 4)
		{
			// 引数の数は正しい。

			int startYear = Integer.parseInt(args[2]);
			int endYear = Integer.parseInt(args[3]);

			// 日記ファイル読み込み。
			DiaryDocumentCollectionFromDirectory diaries =
				new DiaryDocumentCollectionFromDirectory(
					args[0],
					args[1],
					args[2],
					new DateRange(startYear, endYear));

			// 日ごとの日記の分量を計算。
			HashMap <Calendar, Integer> volumePerDay =
				diaries.getVolumePerDay();

			// 計算結果をグラフ化。
			KDocument densityDocument =
				new DensityDocument2(volumePerDay, startYear, endYear);

			// SVGファイルのファイル名を求める。
			Date calendar = new Date();
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyyMMdd");
			String fileName = format.format(calendar) + ".svg";

			// 書き出し。
			densityDocument.write(new FileWriter(fileName));
		}
		else
		{
			System.out.println("だめ");
		}
	}
}
