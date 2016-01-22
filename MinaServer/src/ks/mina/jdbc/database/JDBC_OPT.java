package ks.mina.jdbc.database;
import java.awt.image.TileObserver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ks.mina.globalcontent.GlobalContent;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class JDBC_OPT {


	/*
	 * �߳�ͬ��ģ�飬���и����ܵĻ������
	 * http://blog.csdn.net/fw0124/article/details/6672522
	 * ReentrantLock �� synchronized ���ܸ���
	Lock lock = new ReentrantLock();
	lock.lock();
	try { 
	  // update object state
	}
	finally {
	  lock.unlock(); 
	}
	*/
	//���ݿ��ַ��ʹ�ø�ʽ��url+���ݿ����� �磺url+"ks"��ʾjdbc:mysql://127.0.0.1:3306/ks�Ҹ�����ݿ�
	public final static String url="jdbc:mysql://127.0.0.1:3306/";
	
	//���ݿ��û���
	public final static String user="root";
	
	//���ݿ�����
	public final static String password="MySQLMicroController";
	
	

	
	//������������ݿ�ı�ͷ��ǩ��������Ҫ��ʼ��
	/**
	 * Ҫ���ҵ����ݱ�ͷ
	 */
	public  static ArrayList<String> tabletitles= new ArrayList<String>();

	
	//������Ҫ���ҵı������ݵı�ͷ
	public static void setSeekTitles(String[] titles)
	{
		for(int i=0,j=titles.length;i<j;i++)
		{
			JDBC_OPT.tabletitles.add(titles[i]);
		}
		
		
//		JDBC_OPT.tabletitles.add("ID");
//		JDBC_OPT.tabletitles.add("ROOM1");
//		JDBC_OPT.tabletitles.add("ROOM2");
//		JDBC_OPT.tabletitles.add("ROOM3");
//		JDBC_OPT.tabletitles.add("ROOM4");
//		JDBC_OPT.tabletitles.add("ROOM5");
	}
	
	
	public static boolean isDataBaseNameExist(String daName)
	{
		boolean isExist=false;
		
		String sql="SELECT * FROM information_schema.SCHEMATA where SCHEMA_NAME='"+daName+"'";
		
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		try
		{
		 conn=JDBC_OPT.connectdb(JDBC_OPT.url, JDBC_OPT.user, JDBC_OPT.password);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			JDBC_OPT.closedb(conn, rs,statement);
			return false;
			
		}
		
		
		
		try {
			
			if(!conn.isClosed())
			{
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				 statement = (Statement) conn.createStatement();
				try
				{
					rs = statement.executeQuery(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("��ѯ���ݿ�ʧ�ܡ�����");
					//�ر����ݿ�
					 JDBC_OPT.closedb(conn, rs,statement);
					 
					 return false;
				}
			}
			else
			{
				//�������ݿ�ʧ�ܣ��ر����ݿ�
				
				
				JDBC_OPT.closedb(conn, rs,statement);
				
				 return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//�������ݿ�ʧ�ܣ��ر����ݿ�
			
			
			JDBC_OPT.closedb(conn, rs,statement);
			
			 return false;
		}
		
		  try {
			  
			  if(rs!=null)
			  if(rs.next())  
			  {
				  JDBC_OPT.closedb(conn,rs,statement);
				  return true;
			  }
			  else
			  {
				  JDBC_OPT.closedb(conn,rs,statement);
				  return false;
				  
			  }
		} catch (SQLException e) {
			
			JDBC_OPT.closedb(conn,rs,statement);
			e.printStackTrace();
		}
		
		      //��ɲ���֮��ر����ݿ�
			JDBC_OPT.closedb(conn,rs,statement);
		
		
		
		
		return isExist;
		
	}
	
	
	
	//�������ݿ⣬����ֵ��Connection����
	public static Connection  connectdb(String str_url,String str_user,String str_password)
	{
		        //java��mysql��������
				String driver="com.mysql.jdbc.Driver";
				//Ҫ���ʵ����ݿ�ĵ�ַ
				String url=str_url;//"jdbc:mysql://127.0.0.1:3306/ks";
				//MySQL����ʱ���û���
				String user=str_user;
				// MySQL����ʱ������
				String password=str_password;
				// ������������
				try {
					Class.forName(driver);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// �������ݿ�
				Connection conn=null;
				try {
					 conn = (Connection) DriverManager.getConnection(url, user, password);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  return conn;
	}
	
	public static void closedb(Connection conn,ResultSet rs,Statement statement)
	{
		// �ж������Ƿ�Ϊ�գ������Ϊ�գ��ر����
		if (rs != null) {
			try {
			rs.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
			rs = null;
			}
			// �ж������Ƿ�Ϊ�գ������Ϊ�գ��ر����
			if (statement != null) {
			try {
			statement.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
			statement = null;
			}
			// �ж������Ƿ�Ϊ�գ������Ϊ�գ��ر����
			if (conn != null) {
			try {
			conn.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
			conn = null;
			}
		
	}

	/*public static ResultSet optdb(Connection conn,Statement statement,boolean isselect,String sql)
	{
		ResultSet rs=null;
		try {
			if(!conn.isClosed())
			{
			System.out.println("Succeeded connecting to the Database!");
			// statement����ִ��SQL���
		 statement = (Statement) conn.createStatement();
			if(isselect)//��ѯ����
			{
				rs = statement.executeQuery(sql);
				//rsָ��ǰ������ݱ��һ�е�ǰһ�У���rs.next()����ʹ�ý������
			}
			else //��������
			{
				
				 * 
				 * executeUpdate �ķ���ֵ��һ��������ָʾ��Ӱ��������������¼�������
				 * ���� CREATE TABLE �� DROP TABLE �Ȳ������е���䣬
				 * executeUpdate �ķ���ֵ��Ϊ�㡣
				 
				statement.executeUpdate(sql);
				//getGeneratedKeys()������ȡ�������¼������idֵ��ʾ����
				rs=statement.getGeneratedKeys();
				
				rs.next();
				int id = rs.getInt(1);//ȡ��id��ֵ
				
				//return null;
			}
			//statement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	*/
	
	
	
}
