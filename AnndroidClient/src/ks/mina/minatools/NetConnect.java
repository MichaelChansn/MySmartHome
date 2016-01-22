package ks.mina.minatools;

import ks.mina.activitys.Main_Control;
import ks.mina.activitys.viewoperate.OPT_ActivityView_static;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


//本类用来检测手机网络的连接情况
//记得要加上权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
public class NetConnect {

	public NetConnect() {
		// TODO Auto-generated constructor stub
	}

	//判断是否有网络连接 包括wifi信号和GPRS信号
	/**
	 * 
	 * @param context
	 * 指的是当前运行的activity
	 * @return
	 * 有网络连接返回true否则返回false
	 */
	          public static boolean isNetworkConnected(Context context)
	              { 
						if (context != null) 
						{ 
						ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
						NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
							if (mNetworkInfo != null) 
								{ 
								return mNetworkInfo.isAvailable(); 
								} 
						} 
						return false; 
					}

	
	//判断WIFI网络是否可用 
	          /**
	           * 
	           * @param context
	           * 当前的activity
	           * @return
	           * 是wifi联网返回true否则返回false
	           */
	public static boolean isWifiConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		if (mWiFiNetworkInfo != null) { 
		return mWiFiNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		}


	//判断MOBILE网络是否可用
	/**
	 * 
	 * @param context
	 * 当前使用的activity
	 * @return
	 * 是移动2G 3G联网返回true，否则返回false
	 */
	public static boolean isMobileConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		if (mMobileNetworkInfo != null) { 
		return mMobileNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		}

   //判断网络连接的类型
	/*
	 * one of TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH, or other types defined by ConnectivityManager
	 */
	/**
	 * 
	 * @param context
	 * 当前的activity
	 * @return
	 * 网络连接的类型TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH, or other types defined by ConnectivityManager
	 */
	public static int getConnectedType(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { 
		return mNetworkInfo.getType(); 
		} 
		} 
		return -1; 
		}


	
	 //下面定义广播相关的处理，用来定义广播，发送广播，接收广播，处理广播，网络改变时会自动发送广播，可以接收光比作出相应的处理
	
	
	/**
	 * 注册网络广播（一般在 @Override  public void onResume()）里注册
	 * @param context
	 * 当前运行的activity
	 * 
	 */
	public void registerBroadcast(Context context)
	{
		IntentFilter intentFilter = new IntentFilter(); 
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
		context.registerReceiver(connectionReceiver, intentFilter);


	}
	
	//
	/**
	 * 解除广播（一般在onDestroy（））中使用
	 * @param context
	 * 当前运行的activity
	 */
	public void unregisterBroadcast(Context context)
	{
		if (connectionReceiver != null) { 
			context.unregisterReceiver(connectionReceiver); 
			}

	}
	
	//广播的处理程序
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() { 
		@Override 
		public void onReceive(Context context, Intent intent) { 
			
			//Toast.makeText(this, "网络连接已经改变", Toast.LENGTH_LONG);
			System.out.println("检测到网络状态发生变化。。。");
			
			
		ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected())
		{ 
		
		// 没有任何网络可用
			System.out.println("手机的所有网络都没连接。。。");
			}
		else 
		   if(mobNetInfo.isConnected() && !wifiNetInfo.isConnected())
			{ 
			// 手机网络可用
			    System.out.println("手机3G信号开启。。。");
			   } 
			else
				if(!mobNetInfo.isConnected() && wifiNetInfo.isConnected())
				{
					//wifi可用
					   System.out.println("WIFI信号开启。。。");
					   
					  }
				else
				{
				 //3g和wifi都可用
					System.out.println("WIFI和3G信号都开启。。。");
					
					
					 }
		} 
		};


}
