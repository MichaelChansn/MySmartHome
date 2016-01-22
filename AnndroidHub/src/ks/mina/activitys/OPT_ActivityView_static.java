package ks.mina.activitys;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
/**
 * 这个类专业用来操作显示activity页面上的各个控件，因为当前avtivity上的控件只能在本activity并且是主线程才能操作
 * 写这个类的目的就是为了全局访问或操作当前的activity控件
 * @author ks
 *
 */

public class OPT_ActivityView_static {
	
	//操作工程中用到的handler操作索引标记message.what
	
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
	   	   long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启   
	   	   vib.vibrate(pattern,repeat); 
	   	 
	   	 }
	*/
	
	
	/**
	 * 
	 * @param context
	 * 要操作的activity
	 */
	public static void optView(Context context)
	{
		
	}
	/**
	 * 专业用来向目的activity发送handler消息，以便在activity中使用handlerMassage接收消息并显示在控件上
	 * @param handler
	 * 要操作的activity中的handler
	 * @param str
	 * 消息内容
	 * @param index
	 * massage的索引从0开始
	 */
	public static void sendHandlerMessage(Handler handler,String str,int index)
	{
		Message msg=new Message();
		msg.obj=str;//packet.getPort();//
		msg.what=index;
		handler.sendMessage(msg);
		
	}

}
