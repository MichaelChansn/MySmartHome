package ks.mina.encipher;

import java.io.UnsupportedEncodingException;

public class EncipherAndDecipher {


	/**
	 * �Լ���д�ļ����㷨������������ͬ���ķ������н���
	 * ��ԭ�����ַ�����һ���򵥼��ܼ���
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
	 * �����㷨
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
