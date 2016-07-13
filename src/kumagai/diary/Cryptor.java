package kumagai.diary;

import java.io.*;
import ktool.crypt.*;
import sun.misc.*;

/**
 * 暗号化。
 * @author kumagai
 */
public class Cryptor
{
	/**
	 * 暗号化ユーティリティ。
	 * @param args [0]=パスワード [1]=フレーズ
	 */
	public static void main(String[] args)
		throws Exception
	{
		DesEncryptCipher cipher =
			new DesEncryptCipher(new DesKeyAndIVByMD5(args[0]));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream out2 = cipher.createOutputStream(out);
		out2.write(args[1].getBytes());
		out2.close();
		out.close();
		byte [] crypted = out.toByteArray();
		String encoded = new BASE64Encoder().encode(crypted);
		System.out.println(encoded);
	}
}
