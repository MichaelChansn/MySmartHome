package ks.mina.encipher;

import java.io.UnsupportedEncodingException;

public class EncipherAndDecipher {


	/**
	 * 自己编写的加密算法，服务器采用同样的方法进行解密
	 * 把原来的字符加上一，简单加密即可
	 * @param inString
	 * @return
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public static  String encipher(String inString) throws UnsupportedEncodingException 
	{
		String str=null;
		
		byte[] bytes=inString.getBytes("UTF-8");
		int length=bytes.length;
		byte[] strBytes=new byte[length];
		for(int i=0;i<length;i++)
		{
			strBytes[i]=(byte) (bytes[i]+(byte)1);
		}
		str=new String(strBytes,"UTF-8");
		return str;
	}
	
	/**
	 * 解密算法
	 * @param inString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static  String decipher(String inString) throws UnsupportedEncodingException
	{
		String str=null;

		byte[] bytes=inString.getBytes("UTF-8");
		int length=bytes.length;
		byte[] strBytes=new byte[length];
		for(int i=0;i<length;i++)
		{
			strBytes[i]=(byte) (bytes[i]-(byte)1);
		}
		str=new String(strBytes,"UTF-8");
		return str;
		
	}

}
