package ks.mina.activitys.viewoperate;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
/**
 * �����רҵ����������ʾactivityҳ���ϵĸ����ؼ�����Ϊ��ǰavtivity�ϵĿؼ�ֻ���ڱ�activity���������̲߳��ܲ���
 * д������Ŀ�ľ���Ϊ��ȫ�ַ��ʻ������ǰ��activity�ؼ�
 * @author ks
 *
 */

public class OPT_ActivityView_static {
	
	//�����������õ���handler�����������message.what
	
	public final static int INDEX_UDPGETIP=0;
	
	public final static int INDEX_TEXTVIEW=1;
	
	public final static int INDEX_BUTTONTEXT=2;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param context
	 * Ҫ������activity
	 */
	public static void optView(Context context)
	{
		
	}
	/**
	 * רҵ������Ŀ��activity����handler��Ϣ���Ա���activity��ʹ��handlerMassage������Ϣ����ʾ�ڿؼ���
	 * @param handler
	 * Ҫ������activity�е�handler
	 * @param str
	 * ��Ϣ����
	 * @param index
	 * massage��������0��ʼ
	 */
	public static void sendHandlerMessage(Handler handler,String str,int index)
	{
		Message msg=new Message();
		msg.obj=str;//packet.getPort();//
		msg.what=index;
		handler.sendMessage(msg);
		
	}

}
