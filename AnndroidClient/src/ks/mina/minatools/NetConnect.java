package ks.mina.minatools;

import ks.mina.activitys.Main_Control;
import ks.mina.activitys.viewoperate.OPT_ActivityView_static;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


//������������ֻ�������������
//�ǵ�Ҫ����Ȩ��<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
public class NetConnect {

	public NetConnect() {
		// TODO Auto-generated constructor stub
	}

	//�ж��Ƿ����������� ����wifi�źź�GPRS�ź�
	/**
	 * 
	 * @param context
	 * ָ���ǵ�ǰ���е�activity
	 * @return
	 * ���������ӷ���true���򷵻�false
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

	
	//�ж�WIFI�����Ƿ���� 
	          /**
	           * 
	           * @param context
	           * ��ǰ��activity
	           * @return
	           * ��wifi��������true���򷵻�false
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


	//�ж�MOBILE�����Ƿ����
	/**
	 * 
	 * @param context
	 * ��ǰʹ�õ�activity
	 * @return
	 * ���ƶ�2G 3G��������true�����򷵻�false
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

   //�ж��������ӵ�����
	/*
	 * one of TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH, or other types defined by ConnectivityManager
	 */
	/**
	 * 
	 * @param context
	 * ��ǰ��activity
	 * @return
	 * �������ӵ�����TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH, or other types defined by ConnectivityManager
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


	
	 //���涨��㲥��صĴ�����������㲥�����͹㲥�����չ㲥������㲥������ı�ʱ���Զ����͹㲥�����Խ��չ��������Ӧ�Ĵ���
	
	
	/**
	 * ע������㲥��һ���� @Override  public void onResume()����ע��
	 * @param context
	 * ��ǰ���е�activity
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
	 * ����㲥��һ����onDestroy��������ʹ��
	 * @param context
	 * ��ǰ���е�activity
	 */
	public void unregisterBroadcast(Context context)
	{
		if (connectionReceiver != null) { 
			context.unregisterReceiver(connectionReceiver); 
			}

	}
	
	//�㲥�Ĵ������
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() { 
		@Override 
		public void onReceive(Context context, Intent intent) { 
			
			//Toast.makeText(this, "���������Ѿ��ı�", Toast.LENGTH_LONG);
			System.out.println("��⵽����״̬�����仯������");
			
			
		ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		
		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected())
		{ 
		
		// û���κ��������
			System.out.println("�ֻ����������綼û���ӡ�����");
			}
		else 
		   if(mobNetInfo.isConnected() && !wifiNetInfo.isConnected())
			{ 
			// �ֻ��������
			    System.out.println("�ֻ�3G�źſ���������");
			   } 
			else
				if(!mobNetInfo.isConnected() && wifiNetInfo.isConnected())
				{
					//wifi����
					   System.out.println("WIFI�źſ���������");
					   
					  }
				else
				{
				 //3g��wifi������
					System.out.println("WIFI��3G�źŶ�����������");
					
					
					 }
		} 
		};


}
