package kumagai.diary;

import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;
import javax.crypto.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.*;
import ktool.crypt.*;
import ktool.datetime.*;

/**
 * 日記ドキュメントコレクション。
 * @author kumagai
 */
public class DiaryDocumentCollectionFromDirectory
	extends DiaryDocumentCollection
{
	/**
	 * 検索テスト。
	 * @param args なし
	 * @throws Exception
	 */
	static public void main(String [] args)
		throws Exception
	{
		DiaryDocumentCollectionFromDirectory documents =
			new DiaryDocumentCollectionFromDirectory(
				"C:\\Users\\kumagai\\Documents\\Nefertiti の文書\\Private\\log",
				null,
				"xxx",
				new DateRange(2012, 2012));

		for (DiaryDocumentCore document : documents)
		{
			System.out.println(document);
		}
	}

	/**
	 * 指定のフォルダ下の日記ファイルを読み込み、コレクションする。
	 * @param diaryPath 日記ファイルがあるフォルダ
	 * @param imagePath 画像があるフォルダ
	 * @param password 暗号を復号化するパスワード
	 * @param range 対象範囲
	 * @throws ParserConfigurationException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 */
	public DiaryDocumentCollectionFromDirectory
		(String diaryPath, String imagePath, String password, DateRange range)
		throws
			ParserConfigurationException,
			InvalidKeyException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidAlgorithmParameterException,
			SAXException,
			IOException,
			TransformerConfigurationException,
			TransformerFactoryConfigurationError
	{
		DesDecryptCipher cipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		File [] files = new File(diaryPath).listFiles();

		for (int i=0 ; i<files.length ; i++)
		{
			if (files[i].getName().length() == (6 + 1 + 3))
			{
				// ファイル名の長さは正しい。

				try
				{
					int year =
						Integer.parseInt(files[i].getName().substring(0, 4));
					int month =
						Integer.parseInt(files[i].getName().substring(4, 6));

					boolean greater = false;

					if (year == range.startYear)
					{
						// 指定の開始年。

						if (month >= range.startMonth)
						{
							// 月は指定の開始月以上。

							greater = true;
						}
					}
					else if (year > range.startYear)
					{
						// 指定の開始年より後。

						greater = true;
					}

					boolean less = false;

					if (year == range.endYear)
					{
						// 指定の終了年。

						if (month <= range.endMonth)
						{
							// 月は指定の終了月。

							less = true;
						}
					}
					else if (year < range.endYear)
					{
						// 指定の終了年より前。

						less = true;
					}

					if (less & greater)
					{
						// 範囲内。

						try
						{
							InputStream stream =
								new FileInputStream(files[i].getPath());

							add(
								new DiaryDocument(
									cipher.createInputStream(stream),
									imagePath));

							stream.close();
						}
						catch (SAXException exception)
						{
						}
					}
				}
				catch (NumberFormatException exception)
				{
				}
			}
		}
	}

	/**
	 * 指定のフォルダ下の日記ファイルを読み込み、コレクションする。
	 * @param diaryPath 日記ファイルがあるフォルダ
	 * @param imagePath 画像があるフォルダ
	 * @param password 暗号を復号化するパスワード
	 * @param yearMonths 対象範囲
	 * @throws ParserConfigurationException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 */
	public DiaryDocumentCollectionFromDirectory(String diaryPath, String imagePath,
		String password, String [] yearMonths)
		throws
			ParserConfigurationException,
			InvalidKeyException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidAlgorithmParameterException,
			SAXException,
			IOException,
			TransformerConfigurationException,
			TransformerFactoryConfigurationError
	{
		DesDecryptCipher cipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		File [] files = new File(diaryPath).listFiles();

		for (int i=0 ; i<files.length ; i++)
		{
			if (files[i].getName().length() == (6 + 1 + 3))
			{
				// ファイル名の長さは正しい。

				for (int j=0 ; j<yearMonths.length ; j++)
				{
					if (files[i].getName().equals(yearMonths[j] + ".xml"))
					{
						// 指定の年月である。

						try
						{
							InputStream stream =
								new FileInputStream(files[i].getPath());

							add(
								new DiaryDocument(
									UtfBomUtility.createInputStreamWithoutBom(
									cipher.createInputStream(stream)),
									imagePath));

							stream.close();
						}
						catch (SAXException exception)
						{
							System.out.println
								(yearMonths[j] + exception.getMessage());
						}

						break;
					}
				}
			}
		}
	}

	/**
	 * 指定の月のドキュメントのみ読み込みコレクションを構築する。
	 * @param diaryPath 日記ファイルがあるフォルダ
	 * @param imagePath 画像があるフォルダ
	 * @param password 暗号を復号化するパスワード
	 * @param startYear 扱う対象とする始めの年
	 * @param endYear 扱う対象とする終わりの年
	 * @param targetMonth 扱う対象とする月
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws TransformerConfigurationException
	 * @throws FileNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 */
	public DiaryDocumentCollectionFromDirectory(String diaryPath, String imagePath,
		String password, int startYear, int endYear, int targetMonth)
		throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			TransformerConfigurationException, FileNotFoundException,
			ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError
	{
		DesDecryptCipher cipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		File [] files = new File(diaryPath).listFiles();

		for (int i=0 ; i<files.length ; i++)
		{
			int year = Integer.parseInt(files[i].getName().substring(0, 4));
			int month = Integer.parseInt(files[i].getName().substring(4, 6));

			if (year >= startYear && year <= endYear && targetMonth == month)
			{
				// 対象の月のドキュメント。

				InputStream stream = new FileInputStream(files[i].getPath());

				add(
					new DiaryDocument
						(cipher.createInputStream(stream), imagePath));

				stream.close();
			}
		}
	}

	/**
	 * コレクションしている全日記ドキュメントについて日ごとの日記の分量を求め
	 * る。
	 * @return 日ごとの日記の分量
	 * @throws ParseException
	 */
	public HashMap <Calendar, Integer> getVolumePerDay()
		throws ParseException
	{
		HashMap <Calendar, Integer> ret = new HashMap<Calendar, Integer>();

		for (DiaryDocument document : this)
		{
			ret.putAll(document.getDensity());
		}

		return ret;
	}

	/**
	 * 全ドキュメントの全カテゴリ文字列を取得
	 * @return 全ドキュメントの全カテゴリ文字列
	 */
	public StringRanking getCategories()
	{
		StringRanking ret = new StringRanking();

		for (DiaryDocument document : this)
		{
			ret.addAll(document.getCategories());
		}

		return ret;
	}

	/**
	 * 空データ検索。
	 * @return 日付コレクション
	 * @throws ParseException
	 */
	public ArrayList<Date> getEmptyDay()
		throws ParseException
	{
		ArrayList<Date> ret = new ArrayList<Date>();

		for (DiaryDocument document : this)
		{
			ret.addAll(document.getEmptyDay());
		}

		return ret;
	}

	/**
	 * 誤りの可能性がある「へぺべ」文字検索。
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> searchHagyou()
		throws ParseException
	{
		ArrayList<SearchResultDay> ret = new ArrayList <SearchResultDay>();

		for (DiaryDocument document : this)
		{
			ArrayList <SearchResultDay> recv = document.searchHagyou();

			ret.addAll(recv);
		}

		return ret;
	}

	/**
	 * 同じ日を検索。
	 * @param calendar
	 * @return 検索結果コレクション
	 * @throws ParseException
	 */
	public ArrayList<SearchResultDay> getSameDay(Calendar calendar)
		throws ParseException
	{
		ArrayList <SearchResultDay> ret = new ArrayList <SearchResultDay>();

		for (DiaryDocument document : this)
		{
			SearchResultDay recv = document.getOneDay(new DateTime(calendar), null);

			if (recv != null)
			{
				// 見つけた。

				ret.add(0, recv);
			}
		}

		return ret;
	}
}
