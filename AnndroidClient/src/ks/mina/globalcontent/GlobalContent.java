package ks.mina.globalcontent;

import android.content.Context;
import android.content.res.Resources;

//用来存储整个程序的全局变量
public class GlobalContent {

	//设置扫描手机网络状态使用的上下文环境，根据不同的activity幅不同的值，必须在onresum（）函数里复制，保证context就是当前activity
		public static Context netContext;

		public static Resources res=null;
		 

		 public static final String separator="<##>";
		
		
		//登录服务器的返回信息
		 
		//登陆成功
		public static final String LOGIN_OK="LOGINOK";
		//登陆失败
		public static final String LOGIN_FAIL="LOGFAIL";
		
		
		public static final String LOGIN_ERROR_HOMEID="homeIDerror";
		public static final String LOGIN_ERROR_USERNAME="usernameerror";
		public static final String LOGIN_ERROR_PASSWORD="passworderror";
		public static final String LOGIN_ERRPR_OTHER="Unknown Error!";
		
		
		
		
		//整个系统的通信协议：
		public static final String LOGIN="LOGIN";
		public static final String LOGOUT="LOGOUT";
		
		
		public static final String CLIENT="CLIENT";
		public static final String TERMINAL="TERMINAL";
		
		public static final String GETVIDEOSTREAM="GETVIDEOSTREAM";
		public static final String YOURVIDEOSTREAM="YOURVIDEOSTREAM";
		public static final String STREAMOUT="STREAMOUT";
		
		public static final String COMMEND="COMMEND";
		
		public static final String COMMEND_BLUETOOTH="COMMEND_BLUETOOTH";
		
		
}

