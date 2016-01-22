package ks.minaserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

//本类用来判断本机的网络连接是不是正常
public class NetConnect {

	public NetConnect() {
		// TODO Auto-generated constructor stub
	}

	
	public static boolean isConnect(){
		URL url = null;
		try {
			url = new URL("http://www.baidu.com");//以百度的网址为检验标准
			try {
				InputStream in = url.openStream();
				in.close();
				System.out.println("网络连接正常！");
				return true;
			} catch (IOException e) {
				System.out.println("网络连接失败！");
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
}
