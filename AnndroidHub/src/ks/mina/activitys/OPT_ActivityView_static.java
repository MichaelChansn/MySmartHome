package ks.mina.activitys;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
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
	
	public final static int INDEX_JUMPTOCAMERA=3;
	
	public final static int INDEX_JUMPTOSTREAM=5;
	
	public final static int INDEX_COMMEND=4;
	
	public final static int INDEX_SENDMESSAGETOBLUETOOTH=6;
	
	
	
	
	
	
	
	 public static void Vibrate(final Activity activity, long milliseconds) {
   	  Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
   	  vib.vibrate(milliseconds);
   	 }
	
	
	/* public static void Vibrate(final Activity activity, int repeat) {
	   	   Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
	   	   long [] pattern = {100,400,100,400};   // ֹͣ ���� ֹͣ ����   
	   	   vib.vibrate(pattern,repeat); 
	   	 
	   	 }
	*/
	
	
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
