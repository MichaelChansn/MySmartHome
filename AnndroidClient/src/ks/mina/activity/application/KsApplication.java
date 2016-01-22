package ks.mina.activity.application;


import java.io.UnsupportedEncodingException;
import java.util.Locale;

import ks.mina.globalcontent.GlobalContent;
import ks.mina.minatools.Tcp_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;

import org.apache.mina.core.future.ReadFuture;
import org.videolan.libvlc.BitmapCache;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class KsApplication extends Application {

	private final String TAG="KsApplication";
	private static KsApplication ksApplication=null;
	public static final String SHAREPREFERENCEID="KS_PRIVATE";
	private static final String KEY_ISREMEMBER="key_isremember";
	private static final String KEY_ISAUTOLOGIN="key_isautologin";
	public boolean flagIsRememberPassword=false;
	public boolean flagIsAutoLogIn=false;
	public  String userName=null;
	public  String passWord=null;
	private static final String KEY_USERNAME="key_username";
	private static final String KEY_PASSWORD="key_password";
	
	public  String homeID=null;
	private static final String KEY_HOMEID="key_homeid";
	//private static final String SERVERIP="42.96.205.175";
	//private static final int SERVERPORT=8000;
	//public final String HOMEID="Micro";
	
	public  String logInInfor=null;
	
	public String videoAddress=null;
	
	
	
	
	//VLC 的application整合
	
	    //public final static String TAG = "VLC/VLCApplication";
	   // private static VLCApplication instance;

	    public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";

	
	
	
	
	@Override
	/**
	 * 本方法在应用启动时最先运行
	 */
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(TAG, "............");
		
		
		/**
		 * VLC*****************************************************************************
		 */
		
		  // Are we using advanced debugging - locale?
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String p = pref.getString("set_locale", "");
        if (p != null && !p.equals("")) {
            Locale locale;
            // workaround due to region code
            if(p.equals("zh-TW")) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if(p.startsWith("zh")) {
                locale = Locale.CHINA;
            } else if(p.equals("pt-BR")) {
                locale = new Locale("pt", "BR");
            } else if(p.equals("bn-IN") || p.startsWith("bn")) {
                locale = new Locale("bn", "IN");
            } else {
                /**
                 * Avoid a crash of
                 * java.lang.AssertionError: couldn't initialize LocaleData for locale
                 * if the user enters nonsensical region codes.
                 */
                if(p.contains("-"))
                    p = p.substring(0, p.indexOf('-'));
                locale = new Locale(p);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

        ksApplication = this;
		/**
		 * VLC*****************************************************************************
		 */
		
		
	}
	   /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources()
    {
        if(ksApplication == null) return null;
        return ksApplication.getResources();
    }
	
	   /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "System is running low on memory");

        BitmapCache.getInstance().clear();
    }
	public static KsApplication getAppContext() {
		if(ksApplication==null)
			ksApplication=new KsApplication();
		
		return ksApplication;
	}
	
	public  void getFlags(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(KsApplication.SHAREPREFERENCEID, Context.MODE_PRIVATE);
		flagIsRememberPassword=sharedPreferences.getBoolean(KEY_ISREMEMBER, false);
		flagIsAutoLogIn=sharedPreferences.getBoolean(KEY_ISAUTOLOGIN, false);
		
	}
	
	public  void setFlags(Context context,boolean isRemberPassword,boolean isAutoLogin)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(KsApplication.SHAREPREFERENCEID, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPreferences.edit(); //编辑文件 
		edit.putBoolean(KEY_ISREMEMBER, isRemberPassword);
		edit.putBoolean(KEY_ISAUTOLOGIN, isAutoLogin);
		edit.commit();
		
	}
	
	public  void getLastUserInfo(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(KsApplication.SHAREPREFERENCEID, Context.MODE_PRIVATE);
		userName=sharedPreferences.getString(KEY_USERNAME, null);
		passWord=sharedPreferences.getString(KEY_PASSWORD, null);
		homeID=sharedPreferences.getString(KEY_HOMEID, null);
		
	}
	
	
	public  void setLastUserInfo(Context context,String username,String password,String homeID)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(KsApplication.SHAREPREFERENCEID, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPreferences.edit(); //编辑文件 
		edit.putString(KEY_USERNAME, username);
		edit.putString(KEY_PASSWORD, password);
		edit.putString(KEY_HOMEID, homeID);
		edit.commit();
		
	}
	
	
	public boolean LogIn(String homeID,String userName,String userPassWord)
	{
		Tcp_static.setupTcp_staticConnectorParam();
		if(Tcp_static.connectServer())
		{
			
			Log.e(TAG, "connectServer() OK");
			String str = null;
			try {
				//定义字符之间的分隔符为<##>
				str = GlobalContent.LOGIN+"<##>"+encipher(homeID+"<##>"+userName+"<##>"+userPassWord);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				str=null;
			}
			if(str!=null)
			{
			 PacketData pd=PacketDataOperate.sendStrPacketData(str);
			 
			 Tcp_static.sendMessage(pd);
			 
			 
			 //切换到单一读取模式，等待登录验证
			 Tcp_static.session.getConfig().setUseReadOperation(true);	 
			 ReadFuture future = Tcp_static.session.read();
			 // Wait until a message is received.
			 try {
				future.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				 
			 PacketData message = (PacketData) future.getMessage();
			
			 String[] strGetfromServer=message.getMessage().split(GlobalContent.separator);
			 if(strGetfromServer[0].equalsIgnoreCase(GlobalContent.LOGIN_OK))
			 {
				 //返回多线程处理模式
				 Tcp_static.session.getConfig().setUseReadOperation(false);	
				 
				 //向服务器注册连接信息
				//向服务器注册身份
		        // PacketData pd2=PacketDataOperate.sendStrPacketData(GlobalContent.CLIENT);
		         //Tcp_static.sendMessage(pd2);
				 
				 return true;
			 }
			 else
				if(strGetfromServer[0].equalsIgnoreCase(GlobalContent.LOGIN_FAIL))
				{
					 //返回多线程处理模式
					 Tcp_static.session.getConfig().setUseReadOperation(false);	
					 
					 String error=strGetfromServer[1];
					 if(error.equalsIgnoreCase(GlobalContent.LOGIN_ERROR_HOMEID))
					 {
						 logInInfor="家庭号输入错误，没有当前家庭！";
					 }
					 else
						 if(error.equalsIgnoreCase(GlobalContent.LOGIN_ERROR_PASSWORD))
						 {
							 logInInfor="密码错误";
						 }
						 else
							 if(error.equalsIgnoreCase(GlobalContent.LOGIN_ERROR_USERNAME))
							 {
								 logInInfor="用户名不存在";
							 }
							 else
								 if(error.equalsIgnoreCase(GlobalContent.LOGIN_ERRPR_OTHER))
								 {
									 logInInfor="未知错误";
								 }
					
					return false;
				}
				 return false;
			 
				 
			
			}
		}
		
		
		return false;
	}

	
	/**
	 * 自己编写的加密算法，服务器采用同样的方法进行解密
	 * 把原来的字符加上一，简单加密即可
	 * @param inString
	 * @return
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	private  String encipher(String inString) throws UnsupportedEncodingException 
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
	 * 解密算法
	 * @param inString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private  String decipher(String inString) throws UnsupportedEncodingException
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
