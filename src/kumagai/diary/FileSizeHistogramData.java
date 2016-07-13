package kumagai.diary;

import java.util.*;

/**
 * ファイルサイズヒストグラム情報。
 * @author kumagai
 */
public class FileSizeHistogramData
{
	/**
	 * テストコード。
	 * @param args [0]=ファイル配置パス
	 */
	public static void main(String[] args)
	{
		if (args.length >= 1)
		{
			ArrayList<FileNameAndSize> fileNameAndSizeCollection =
				new DiaryFileCollection(args[0]).getFileNameAndSizeCollection();

			FileSizeHistogramData histogram =
				new FileSizeHistogramData(fileNameAndSizeCollection);

			for (int i=0 ; i<histogram.countPer1Killobyte.length ; i++)
			{
				System.out.printf("%3dKB : ", i);

				for (int j=0 ; j<histogram.countPer1Killobyte[i] ; j++)
				{
					System.out.print("*");
				}

				System.out.println();
			}

			System.out.println();

			for (int i=0 ; i<histogram.countPer10Killobyte.length ; i++)
			{
				System.out.printf("%3d0KB : ", i);

				for (int j=0 ; j<histogram.countPer10Killobyte[i] ; j++)
				{
					System.out.print("*");
				}

				System.out.println();
			}
		}
	}

	public int [] countPer1Killobyte;
	public int [] countPer10Killobyte;

	/**
	 * ファイルサイズヒストグラム情報を生成。
	 * @param fileNameAndSizeCollection ファイル名とサイズ情報のコレクション
	 */
	public FileSizeHistogramData
		(ArrayList<FileNameAndSize> fileNameAndSizeCollection)
	{
		int max = 0;

		for (FileNameAndSize fileNameAndSize : fileNameAndSizeCollection)
		{
			if (max < fileNameAndSize.size)
			{
				// 最大値を超える。

				max = (int)fileNameAndSize.size;
			}
		}

		countPer1Killobyte = new int [max / 1024 + 1];
		countPer10Killobyte = new int [max / 10240 + 1];

		for (FileNameAndSize fileNameAndSize : fileNameAndSizeCollection)
		{
			countPer1Killobyte[(int)fileNameAndSize.size / 1024]++;
			countPer10Killobyte[(int)fileNameAndSize.size / 10240]++;
		}
	}
}
