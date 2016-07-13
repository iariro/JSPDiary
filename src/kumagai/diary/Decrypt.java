package kumagai.diary;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import ktool.crypt.*;

/**
 * 暗号復号化。
 */
public class Decrypt
{
	/**
	 * @param args [0]=p / [1]=入力ファイル / [2]=出力ファイル
	 */
	public static void main(String[] args)
		throws
			InvalidKeyException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidAlgorithmParameterException,
			IOException
	{
		if(args.length >= 2)
		{
			// 引数は最低限ある。

			PrintStream output;

			if(args.length >= 3)
			{
				// 引数は３つ以上ある。

				output = new PrintStream(new FileOutputStream(args[2]));
			}
			else
			{
				// 引数は２つ。

				output = System.out;
			}

			BufferedReader reader =
				new BufferedReader(
					new InputStreamReader(
						new CipherInputStream(
							new FileInputStream(args[1]),
							new DesDecryptCipher(new DesKeyAndIVByMD5(args[0])).getCipher()),
						"utf-8"));

			String line;

			while((line = reader.readLine()) != null)
			{
				output.println(line);
			}

			reader.close();
		}
		else
		{
			System.out.println("password infile [outfile]");
		}
	}
}
