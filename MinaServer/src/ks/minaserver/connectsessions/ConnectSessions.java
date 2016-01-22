package ks.minaserver.connectsessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

/**
 * 用来保存连接信息
 * 所有连接存放形式：<IP,Session>
 * 终端存放形式：   <HOMEID,IP>-->由HOME找到IP，再由IP找到Session
 * 
 * 因为客户端可以有多个，但是终端一个家庭只能有一个
 * @author Eric
 *
 */
public class ConnectSessions {
	
	
	public static boolean isTerminalConnect=false;
	
	
	
	public static void printLength()
	{
		System.out.println("连接总数是："+connectSessions.size());
		System.out.println("终端总数是："+terminalName2IP.size());
	}
	
	
	//<"IP",IoSession>用来存放所有的连接的输出流信息
	private static Hashtable<String, IoSession> connectSessions=new Hashtable<String, IoSession>();
	
	//<“HOMEID”，“IP”>用来提取终端IP
	private static Hashtable<String, String>  terminalName2IP=new Hashtable<String, String>();
	
	//存放连接的客户端信息<IP,HOMEID:USERNAME>
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
	 * 用来统计使用同一个用户名登陆的人数，和IP地址，一般用来在终端上显示
	 * @param homeIDAndUserNames
	 * @return
	 */
	public static String[] getCustomerIPsByHomeIDAndUserNames(String homeIDAndUserNames)
	{
		ArrayList<String> strIPS=new ArrayList<String>();
		
		Set<String> mapSet =  userInfoTables.keySet();	//获取所有的key值 为set的集合
		Iterator<String> itor =  mapSet.iterator();//获取key的Iterator便利
		while(itor.hasNext())
		{//存在下一个值
			String key = itor.next();//当前key值
			if(userInfoTables.get(key).equalsIgnoreCase(homeIDAndUserNames))
			{//获取value 与 所知道的value比较
				//System.out.println("你要找的key ："+key);//相等输出key
				strIPS.add(key);	
			}
		}
		
		return (String[]) strIPS.toArray();
		
		
	}
	
	
	//把终端的服务名称和IP对应，用来删除终端信息,终端值只存一个
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
		

		//得到终端的IP地址
		public static String getTerminalIP(String homeID)
		{
			if(terminalName2IP.containsKey(homeID))
			{
			 return terminalName2IP.get(homeID);
			}
			
			return "NO";
		}
		
		
	
		
		

		//添加客户端session信息
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
		
		//得到指定key的session对象，用于跨连接数据传送
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
			Set<String> mapSet =  terminalName2IP.keySet();	//获取所有的key值 为set的集合
			Iterator<String> itor =  mapSet.iterator();//获取key的Iterator便利
			while(itor.hasNext())
			{//存在下一个值
				String key = itor.next();//当前key值
			if(terminalName2IP.get(key).equalsIgnoreCase(value))
			{//获取value 与 所知道的value比较
				//System.out.println("你要找的key ："+key);//相等输出key
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
				String key = itor.next();//当前key值
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
