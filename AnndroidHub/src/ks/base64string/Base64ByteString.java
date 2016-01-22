package ks.base64string;
import org.apache.mina.util.Base64;


public class Base64ByteString {
	
	
	public static String base64FromByte2String(byte[] bytes)
	{
		String str=new String(Base64.encodeBase64(bytes));
		return str;
		
	}
	
	public static byte[] base64FromString2bytes(String str)
	{
		byte[] bytes=Base64.decodeBase64(str.getBytes());
		return bytes;
	}
	
	

}
