package ks.mina.globalcontent;



//用来存储整个程序的全局变量
public class GlobalContent {

	    /**
	     * 协议说明
	     * 
	     * 协议格式:协议类型<##>家庭号<##>房间号<##>电器号<##>操作码
	     * 
	     * 协议类型:查询(GET)、更改(CHANGE)、登陆(LOGIN)
	     * 
	     * 操作码：命令1:命令2:命令3。。。
	     * 
	     */
	    
	/**
	 * 登陆协议：LOGIN<##>HOMEID<##>USERNAME<##>PASSWORD
	 * 退出协议：LOGOUT<##>OK
	 * 登陆成功：LOGINOK<##>OK
	 * 
	 */
	
	
	
		 
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
		
		public static final String GETVIDEOSTREAM="GETVIDEOSTREAM";
		public static final String YOURVIDEOSTREAM="YOURVIDEOSTREAM";
		public static final String STREAMOUT="STREAMOUT";
		
		public static final String COMMEND="COMMEND";


		public static final String COMMEND_BLUETOOTH="COMMEND_BLUETOOTH";
		
		
		
		
		
		
}
