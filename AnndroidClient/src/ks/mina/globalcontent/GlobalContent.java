package ks.mina.globalcontent;

import android.content.Context;
import android.content.res.Resources;

//�����洢���������ȫ�ֱ���
public class GlobalContent {

	//����ɨ���ֻ�����״̬ʹ�õ������Ļ��������ݲ�ͬ��activity����ͬ��ֵ��������onresum���������︴�ƣ���֤context���ǵ�ǰactivity
		public static Context netContext;

		public static Resources res=null;
		 

		 public static final String separator="<##>";
		
		
		//��¼�������ķ�����Ϣ
		 
		//��½�ɹ�
		public static final String LOGIN_OK="LOGINOK";
		//��½ʧ��
		public static final String LOGIN_FAIL="LOGFAIL";
		
		
		public static final String LOGIN_ERROR_HOMEID="homeIDerror";
		public static final String LOGIN_ERROR_USERNAME="usernameerror";
		public static final String LOGIN_ERROR_PASSWORD="passworderror";
		public static final String LOGIN_ERRPR_OTHER="Unknown Error!";
		
		
		
		
		//����ϵͳ��ͨ��Э�飺
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

