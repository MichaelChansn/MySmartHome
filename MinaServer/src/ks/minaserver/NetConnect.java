package ks.minaserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

//���������жϱ��������������ǲ�������
public class NetConnect {

	public NetConnect() {
		// TODO Auto-generated constructor stub
	}

	
	public static boolean isConnect(){
		URL url = null;
		try {
			url = new URL("http://www.baidu.com");//�԰ٶȵ���ַΪ�����׼
			try {
				InputStream in = url.openStream();
				in.close();
				System.out.println("��������������");
				return true;
			} catch (IOException e) {
				System.out.println("��������ʧ�ܣ�");
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
}
