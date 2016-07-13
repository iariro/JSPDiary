package kumagai.diary;

import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;
import javax.crypto.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.*;

/**
 * カテゴリ表示
 */
public class Category
{
	/**
	 * @param args [0]=日記ファイルパス [1]=p [2]=開始年 [3]=終了年
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws ParseException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 */
	public static void main(String[] args)
		throws InvalidKeyException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidAlgorithmParameterException,
			ParserConfigurationException,
			SAXException,
			IOException,
			ParseException,
			TransformerConfigurationException,
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
					null,
					args[1],
					new DateRange(startYear, endYear));

			// カテゴリ文字列を取得。
			StringRanking categories = diaries.getCategories();

			// ランキングを求める。
			ArrayList <Map.Entry <String, Integer>> ranking =
				categories.getRanking();

			for (Map.Entry<String, Integer> entry : ranking)
			{
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}
		else
		{
			System.out.println
				("Usage : 日記ファイルパス 画像ファイルパス 開始年 終了年");
		}
	}
}
