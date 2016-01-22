package ks.mina.jdbc.database;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

//数据库读写类。数据库表头一定不要使用中文，linux有时候后不识别，整个程序崩溃
public class DataBaseRW  {

	
	/**
	 * "TEST_DO_NOT_DELETE"是MYSQL中一个空的数据库，用来作为参考使用（动态创建和删除数据库的参考），名字必须是TEST_DO_NOT_DELETE，不要弄错
	 */
	private static final String TEST_DATABASE="TEST_DO_NOT_DELETE";
	public DataBaseRW() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 * @param dbName
	 * 表示要操作的数据库名字
	 * @param sql
	 * 表示插入语句SQL标准形式
	 * 
	 * 
	 * 方法executeQuery 
	 *用于产生单个结果集的语句，
	 *例如 SELECT 语句。 
	 *被使用最多的执行 SQL 语句的方法是 executeQuery。
	 *这个方法被用来执行 SELECT 语句，
	 *它几乎是使用最多的 SQL 语句。
	 *
	 *
	 *方法executeUpdate 
     *用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句，
     *例如 CREATE TABLE 和 DROP TABLE。
     *INSERT、UPDATE 或 DELETE 语句的效果是修改表中零行或多行中的一列或多列。
     *executeUpdate 的返回值是一个整数，指示受影响的行数（即更新计数）。
     *对于 CREATE TABLE 或 DROP TABLE 等不操作行的语句，executeUpdate 的返回值总为零。 

     *使用executeUpdate方法是因为在 createTableCoffees 中的 SQL 语句是 DDL （数据定义语言）语句。
     *创建表，改变表，删除表都是 DDL 语句的例子，要用 executeUpdate 方法来执行。
     *你也可以从它的名字里看出，方法 executeUpdate 也被用于执行更新表 SQL 语句。
     *实际上，相对于创建表来说，executeUpdate 用于更新表的时间更多，因为表只需要创建一次，但经常被更新
	 */
	public static void insert(String dbName,String  sql)
	{
		
		Connection conn=null;
		Statement statement=null;
	//	conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
			if(!conn.isClosed())
			{
				System.out.println("成功连接到数据库。。。");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("插入数据失败。。。");
					//关闭数据库
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		//完成操作之后关闭数据库
		JDBC_OPT.closedb(conn, null,statement);
		
		
	}
	
	public static void delect(String dbName,String  sql)
	{
		Connection conn=null;
		Statement statement=null;
		//conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
			if(!conn.isClosed())
			{
				System.out.println("成功连接到数据库。。。");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("删除数据失败。。。");
					//关闭数据库
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		      //完成操作之后关闭数据库
				JDBC_OPT.closedb(conn, null,statement);
	}
	
	
	//创建数据库，首先连接到任意数据库，然后再创建需要的数据库
	public static void createDB(String sqlString)
	{
		update(TEST_DATABASE, sqlString);
	}
	
	//删除数据库，原理和创建一样
	public static void deleteDB(String sqlString)
	{
		update(TEST_DATABASE, sqlString);
	}
	
	public static void update(String dbName,String  sql)
	{
		
		Connection conn=null;
		Statement statement=null;
		//conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
			if(!conn.isClosed())
			{
				System.out.println("成功连接到数据库。。。");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("更改数据库失败。。。");
					//关闭数据库
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		      //完成操作之后关闭数据库
				JDBC_OPT.closedb(conn, null,statement);
	}
	/**
	 * 
	 * @param dbName
	 * 数据库的名字
	 * @param sql
	 * 查询语句，命令
	 * @param tabletitles
	 * 表头集合比如，name age birth。。。。形成的集合
	 * @return 
	 * 结果集合以arraylist的形式返回
	 * @throws SQLException
	 * 
	 * 把整个数据库查询结果放到ArrayList数组里，一个Map可以存放数据库的一行（以键值对的形式存在的）
	 * 一个ArrayList存放有所需要的Map个数，根据嵌套可以任选一的提取需要数据
	 * 
	 * 
	 */
	
	public static ArrayList<Map<String, String>> select(String dbName,String  sql,ArrayList<String> tabletitles) throws SQLException
	{
		//String result=null;
		ArrayList<Map<String, String>> maplist=new ArrayList<Map<String, String>>();
		Connection conn=null;
		Statement statement=null;
		ResultSet rs=null;
		//conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
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
					 return null;
				}
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, rs,statement);
				 return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, rs,statement);
			 return null;
		}
		while(rs.next()) {

			
			
			Map<String, String> selResult=new HashMap<String, String>();
			for(int i=0;i<tabletitles.size();i++)
			{
				selResult.put((String)tabletitles.get(i), rs.getString((String)tabletitles.get(i)).toString());
			}
		       maplist.add(selResult);
			}
		
		      //完成操作之后关闭数据库
			JDBC_OPT.closedb(conn, rs,statement);
				return maplist;
		
	}
	
	
	/**
	 * 
	 * @param dbName
	 * 数据库的名字
	 * @param tableName
	 * 表的名字
	 * @param pictByte
	 * 图片的字节数组
	 */
	public static void insertPicture(String dbName,String tableName,byte[] pictByte)
	{

		Connection conn=null;
		//Statement statement=null;
		PreparedStatement ps=null;
		//InputStream in=new ByteArrayInputStream(pictByte); 
		//conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
			if(!conn.isClosed())
			{
				System.out.println("成功连接到数据库。。。");
				// statement = (Statement) conn.createStatement();
				 ps=conn.prepareStatement("UPDATE "+tableName+" SET PICTURE = ? WHERE ID=?;");  
		            
		            ps.setInt(2, 1);  
		            ps.setBytes(1, pictByte);//(1, in, in.available());  
		            ps.executeUpdate(); 
		            System.out.println("图片插入成功。。。");
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, null,(Statement)ps);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, null,(Statement)ps);
			 return;
		}
		//完成操作之后关闭数据库
		JDBC_OPT.closedb(conn, null,(Statement)ps);
	}
	
	
	
	public static byte[] selectPicture(String dbName,String tableName) throws IOException 
	{
		//String result=null;
	
		Connection conn=null;
		//Statement statement=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//InputStream in=null;
		//conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
		try {
			conn=JDBC_OPT.connectdb(JDBC_OPT.url+dbName, JDBC_OPT.user, JDBC_OPT.password);
			if(!conn.isClosed())
			{
				System.out.println("成功连接到数据库。。。");
				
				 ps=conn.prepareStatement("select PICTURE from "+tableName+" where ID=?;");  
		            
				  
		            ps.setInt(1, 1);  
		            rs= ps.executeQuery();
		            System.out.println("读取图片成功。。。");
		            rs.next();
		           byte[] data = rs.getBytes("PICTURE");
		          
		           return data;

				 
			}
			else
			{
				//关闭数据库
				JDBC_OPT.closedb(conn, rs,(Statement)ps);
				 return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//关闭数据库
			JDBC_OPT.closedb(conn, rs,(Statement)ps);
			 return null;
		}
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
