package kumagai.diary;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import ktool.crypt.*;
import ktool.io.*;

/**
 * ファイル暗号化。
 */
public class Encrypt
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
		if(args.length >= 3)
		{
			// 引数は最低限ある。

			new StreamProcessor(
				new CipherInputStream(
					new FileInputStream(args[1]),
					new DesEncryptCipher(new DesKeyAndIVByMD5(args[0])).getCipher()),
				new FileOutputStream(args[2])).process();
		}
		else
		{
			// 引数は最低限ない。

			System.out.println("だめ");
		}
	}
}
