package ks.mina.activitys.viewoperate;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
