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
	 * 线程同步模块，进行高性能的互斥访问
	 * http://blog.csdn.net/fw0124/article/details/6672522
	 * ReentrantLock 比 synchronized 性能更好
	Lock lock = new ReentrantLock();
	lock.lock();
	try { 
	  // update object state
	}
	finally {
	  lock.unlock(); 
	}
	*/
	//数据库地址，使用各式是url+数据库名称 如：url+"ks"表示jdbc:mysql://127.0.0.1:3306/ks找哥哥数据库
	public final static String url="jdbc:mysql://127.0.0.1:3306/";
	
	//数据库用户名
	public final static String user="root";
	
	//数据库密码
	public final static String password="MySQLMicroController";
	
	

	
	//这个常量是数据库的表头标签，根据需要初始化
	/**
	 * 要查找的数据表头
	 */
	public  static ArrayList<String> tabletitles= new ArrayList<String>();

	
	//设置想要查找的表中数据的表头
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
				System.out.println("成功连接到数据库。。。");
				 statement = (Statement) conn.createStatement();
				try
				{
					rs = statement.executeQuery(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("查询数据库失败。。。");
					//关闭数据库
					 JDBC_OPT.closedb(conn, rs,statement);
					 
					 return false;
				}
			}
			else
			{
				//连接数据库失败，关闭数据库
				
				
				JDBC_OPT.closedb(conn, rs,statement);
				
				 return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//连接数据库失败，关闭数据库
			
			
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
		
		      //完成操作之后关闭数据库
			JDBC_OPT.closedb(conn,rs,statement);
		
		
		
		
		return isExist;
		
	}
	
	
	
	//连接数据库，返回值是Connection对象；
	public static Connection  connectdb(String str_url,String str_user,String str_password)
	{
		        //java的mysql访问驱动
				String driver="com.mysql.jdbc.Driver";
				//要访问的数据库的地址
				String url=str_url;//"jdbc:mysql://127.0.0.1:3306/ks";
				//MySQL配置时的用户名
				String user=str_user;
				// MySQL配置时的密码
				String password=str_password;
				// 加载驱动程序
				try {
					Class.forName(driver);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 连续数据库
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
		// 判断声明是否为空，如果不为空，关闭清空
		if (rs != null) {
			try {
			rs.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
			rs = null;
			}
			// 判断声明是否为空，如果不为空，关闭清空
			if (statement != null) {
			try {
			statement.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
			statement = null;
			}
			// 判断连接是否为空，如果不为空，关闭清空
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
			// statement用来执行SQL语句
		 statement = (Statement) conn.createStatement();
			if(isselect)//查询操作
			{
				rs = statement.executeQuery(sql);
				//rs指向当前结果数据表第一行的前一行，先rs.next()；在使用结果数据
			}
			else //其他操作
			{
				
				 * 
				 * executeUpdate 的返回值是一个整数，指示受影响的行数（即更新计数）。
				 * 对于 CREATE TABLE 或 DROP TABLE 等不操作行的语句，
				 * executeUpdate 的返回值总为零。
				 
				statement.executeUpdate(sql);
				//getGeneratedKeys()方法获取最后插入记录的自增id值的示例：
				rs=statement.getGeneratedKeys();
				
				rs.next();
				int id = rs.getInt(1);//取得id的值
				
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
