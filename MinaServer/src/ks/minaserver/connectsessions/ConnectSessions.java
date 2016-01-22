package ks.minaserver.connectsessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

/**
 * ��������������Ϣ
 * �������Ӵ����ʽ��<IP,Session>
 * �ն˴����ʽ��   <HOMEID,IP>-->��HOME�ҵ�IP������IP�ҵ�Session
 * 
 * ��Ϊ�ͻ��˿����ж���������ն�һ����ֻͥ����һ��
 * @author Eric
 *
 */
public class ConnectSessions {
	
	
	public static boolean isTerminalConnect=false;
	
	
	
	public static void printLength()
	{
		System.out.println("���������ǣ�"+connectSessions.size());
		System.out.println("�ն������ǣ�"+terminalName2IP.size());
	}
	
	
	//<"IP",IoSession>����������е����ӵ��������Ϣ
	private static Hashtable<String, IoSession> connectSessions=new Hashtable<String, IoSession>();
	
	//<��HOMEID������IP��>������ȡ�ն�IP
	private static Hashtable<String, String>  terminalName2IP=new Hashtable<String, String>();
	
	//������ӵĿͻ�����Ϣ<IP,HOMEID:USERNAME>
	private static Hashtable<String,String>  userInfoTables=new Hashtable<String, String>();
	
	
	
	public static void adduserInfo(String IP,String homeidusername)
	{
		if(!userInfoTables.containsKey(IP))
		{
			userInfoTables.put(IP, homeidusername);
		}
		else
		{
			userInfoTables.remove(IP);
			userInfoTables.put(IP, homeidusername);
		}
	}
	
	
	public static String gethomeidAndUsernameByKEYIP(String IP)
	{
		String str=null;
		if(userInfoTables.containsKey(IP))
		{
		 str=userInfoTables.get(IP);
		}
		return str;
	}
	
	
	/**
	 * ����ͳ��ʹ��ͬһ���û�����½����������IP��ַ��һ���������ն�����ʾ
	 * @param homeIDAndUserNames
	 * @return
	 */
	public static String[] getCustomerIPsByHomeIDAndUserNames(String homeIDAndUserNames)
	{
		ArrayList<String> strIPS=new ArrayList<String>();
		
		Set<String> mapSet =  userInfoTables.keySet();	//��ȡ���е�keyֵ Ϊset�ļ���
		Iterator<String> itor =  mapSet.iterator();//��ȡkey��Iterator����
		while(itor.hasNext())
		{//������һ��ֵ
			String key = itor.next();//��ǰkeyֵ
			if(userInfoTables.get(key).equalsIgnoreCase(homeIDAndUserNames))
			{//��ȡvalue �� ��֪����value�Ƚ�
				//System.out.println("��Ҫ�ҵ�key ��"+key);//������key
				strIPS.add(key);	
			}
		}
		
		return (String[]) strIPS.toArray();
		
		
	}
	
	
	//���ն˵ķ������ƺ�IP��Ӧ������ɾ���ն���Ϣ,�ն�ֵֻ��һ��
		public static void addTerminalIP(String HomeID,String IP)
		{
			if(!terminalName2IP.containsKey(HomeID))
			{
				terminalName2IP.put(HomeID, IP);
			}
			else
			{
				terminalName2IP.remove(HomeID);
				terminalName2IP.put(HomeID, IP);
			}
			
		}
		

		//�õ��ն˵�IP��ַ
		public static String getTerminalIP(String homeID)
		{
			if(terminalName2IP.containsKey(homeID))
			{
			 return terminalName2IP.get(homeID);
			}
			
			return "NO";
		}
		
		
	
		
		

		//��ӿͻ���session��Ϣ
		public static void addSessionInfo(String IP ,IoSession session)
		{
			if(!connectSessions.containsKey(IP))
			{
				connectSessions.put(IP, session);
			}
			else
			{
				connectSessions.remove(IP);
				connectSessions.put(IP, session);
				
			}
		}
		
		//�õ�ָ��key��session�������ڿ��������ݴ���
		public static IoSession getSessionByKeyIP(String IP)
		{
			if(connectSessions.containsKey(IP))
			{
			return connectSessions.get(IP);
			}
			
			return null;
		}
		
	
		
		public static String getKeyByValue(String value)
		{
			String str=null;
			Set<String> mapSet =  terminalName2IP.keySet();	//��ȡ���е�keyֵ Ϊset�ļ���
			Iterator<String> itor =  mapSet.iterator();//��ȡkey��Iterator����
			while(itor.hasNext())
			{//������һ��ֵ
				String key = itor.next();//��ǰkeyֵ
			if(terminalName2IP.get(key).equalsIgnoreCase(value))
			{//��ȡvalue �� ��֪����value�Ƚ�
				//System.out.println("��Ҫ�ҵ�key ��"+key);//������key
				str=key;
				break;
			}
			}
			return str;
		}
		
		


		
		public static void delectUnuserSessions()
		{
			Set<String> mapSet=connectSessions.keySet();
			Iterator<String> itor =  mapSet.iterator();
			while(itor.hasNext())
			{
				String key = itor.next();//��ǰkeyֵ
				if(!connectSessions.get(key).isConnected())
				{
					connectSessions.remove(key);
					String key2=getKeyByValue(key);
					if(key2!=null)
					{
						isTerminalConnect=false;
						terminalName2IP.remove(key2);
					}
					
				}
				
			}
			
			
			
		}
		
		
		

}
