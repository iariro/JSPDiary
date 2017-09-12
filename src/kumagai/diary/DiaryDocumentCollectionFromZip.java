package kumagai.diary;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ktool.crypt.DesDecryptCipher;
import ktool.crypt.DesKeyAndIVByMD5;

/**
 * PDA同期対象ファイルZIPから構築可能な日記ドキュメントコレクション。
 * @author kumagai
 */
public class DiaryDocumentCollectionFromZip
	extends DiaryDocumentCollection
{
	/**
	 * テストコード。
	 * @param args 未使用
	 * @throws Exception
	 */
	public static void main(String[] args)
		throws Exception
	{
		ArrayList<DiaryDocument> documents =
			new DiaryDocumentCollectionFromZip(
				"C:/Backup/Nefertiti20130213.zip",
				"xxx",
				new String [] { "201209" });

		for (DiaryDocument document : documents)
		{
			ArrayList<SearchResultDay> searchResultDayCollection =
				document.getAll(false, null, true);

			System.out.println(searchResultDayCollection.get(0).date1());
		}
	}

	public final ArrayList<Boolean> completeFlags = new ArrayList<Boolean>();

	/**
	 * PDA同期対象ファイルZIPから日記ドキュメントコレクションを構築。
	 * @param zipPath ZIPファイルのパス
	 * @param password 日記ファイルのパスワード
	 * @param yearMonths 対象年月
	 * @throws Exception
	 */
	public DiaryDocumentCollectionFromZip
		(String zipPath, String password, String [] yearMonths)
		throws Exception
	{
		DesDecryptCipher cipher =
			new DesDecryptCipher(new DesKeyAndIVByMD5(password));

		ZipFile zip = new ZipFile(zipPath);

		for (Enumeration<? extends ZipEntry> e = zip.entries() ;
				e.hasMoreElements() ; )
		{
			try
			{
				String name = e.nextElement().toString();
				ZipEntry entry = zip.getEntry(name);

				if (entry != null)
				{
					// 存在する。

					if (! entry.isDirectory())
					{
						// ディレクトリではない＝ファイルである。

						if (entry.getName().startsWith("Private/log/"))
						{
							// 所定のフォルダ内である。

							for (int i=0 ; i<yearMonths.length ; i++)
							{
								if (entry.getName().endsWith(yearMonths[i] + ".xml"))
								{
									// 対象ファイルである。

									try
									{
										add(
											new DiaryDocument(
												UtfBomUtility.createInputStreamWithoutBom(
												cipher.createInputStream
													(zip.getInputStream(entry))),
												null));

										// 最後のファイル扱い。
										completeFlags.add(false);
									}
									catch (Exception exception)
									{
										exception.printStackTrace();
									}

									break;
								}
								else
								{
									// 対象ファイルではない。

									if (completeFlags.size() > 0)
									{
										// １個でもある。

										// 最後の要素について最後のファイル扱い
										// を解除。
										completeFlags.set
											(completeFlags.size() - 1, true);
									}
								}
							}
						}
					}
				}
			}
			catch (IllegalArgumentException exception)
			{
			}
		}
		zip.close();
	}
}
