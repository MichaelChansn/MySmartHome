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

//���ݿ��д�ࡣ���ݿ��ͷһ����Ҫʹ�����ģ�linux��ʱ���ʶ�������������
public class DataBaseRW  {

	
	/**
	 * "TEST_DO_NOT_DELETE"��MYSQL��һ���յ����ݿ⣬������Ϊ�ο�ʹ�ã���̬������ɾ�����ݿ�Ĳο��������ֱ�����TEST_DO_NOT_DELETE����ҪŪ��
	 */
	private static final String TEST_DATABASE="TEST_DO_NOT_DELETE";
	public DataBaseRW() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 * @param dbName
	 * ��ʾҪ���������ݿ�����
	 * @param sql
	 * ��ʾ�������SQL��׼��ʽ
	 * 
	 * 
	 * ����executeQuery 
	 *���ڲ����������������䣬
	 *���� SELECT ��䡣 
	 *��ʹ������ִ�� SQL ���ķ����� executeQuery��
	 *�������������ִ�� SELECT ��䣬
	 *��������ʹ������ SQL ��䡣
	 *
	 *
	 *����executeUpdate 
     *����ִ�� INSERT��UPDATE �� DELETE ����Լ� SQL DDL�����ݶ������ԣ���䣬
     *���� CREATE TABLE �� DROP TABLE��
     *INSERT��UPDATE �� DELETE ����Ч�����޸ı������л�����е�һ�л���С�
     *executeUpdate �ķ���ֵ��һ��������ָʾ��Ӱ��������������¼�������
     *���� CREATE TABLE �� DROP TABLE �Ȳ������е���䣬executeUpdate �ķ���ֵ��Ϊ�㡣 

     *ʹ��executeUpdate��������Ϊ�� createTableCoffees �е� SQL ����� DDL �����ݶ������ԣ���䡣
     *�������ı��ɾ������ DDL �������ӣ�Ҫ�� executeUpdate ������ִ�С�
     *��Ҳ���Դ����������￴�������� executeUpdate Ҳ������ִ�и��±� SQL ��䡣
     *ʵ���ϣ�����ڴ�������˵��executeUpdate ���ڸ��±��ʱ����࣬��Ϊ��ֻ��Ҫ����һ�Σ�������������
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
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("��������ʧ�ܡ�����");
					//�ر����ݿ�
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		//��ɲ���֮��ر����ݿ�
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
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("ɾ������ʧ�ܡ�����");
					//�ر����ݿ�
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		      //��ɲ���֮��ر����ݿ�
				JDBC_OPT.closedb(conn, null,statement);
	}
	
	
	//�������ݿ⣬�������ӵ��������ݿ⣬Ȼ���ٴ�����Ҫ�����ݿ�
	public static void createDB(String sqlString)
	{
		update(TEST_DATABASE, sqlString);
	}
	
	//ɾ�����ݿ⣬ԭ��ʹ���һ��
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
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				 statement = (Statement) conn.createStatement();
				try
				{
				statement.executeUpdate(sql);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
					System.out.println("�������ݿ�ʧ�ܡ�����");
					//�ر����ݿ�
					 JDBC_OPT.closedb(conn, null,statement);
					 return;
				}
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, null,statement);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
			JDBC_OPT.closedb(conn, null,statement);
			 return;
		}
		      //��ɲ���֮��ر����ݿ�
				JDBC_OPT.closedb(conn, null,statement);
	}
	/**
	 * 
	 * @param dbName
	 * ���ݿ������
	 * @param sql
	 * ��ѯ��䣬����
	 * @param tabletitles
	 * ��ͷ���ϱ��磬name age birth���������γɵļ���
	 * @return 
	 * ���������arraylist����ʽ����
	 * @throws SQLException
	 * 
	 * ���������ݿ��ѯ����ŵ�ArrayList�����һ��Map���Դ�����ݿ��һ�У��Լ�ֵ�Ե���ʽ���ڵģ�
	 * һ��ArrayList���������Ҫ��Map����������Ƕ�׿�����ѡһ����ȡ��Ҫ����
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
					 return null;
				}
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, rs,statement);
				 return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
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
		
		      //��ɲ���֮��ر����ݿ�
			JDBC_OPT.closedb(conn, rs,statement);
				return maplist;
		
	}
	
	
	/**
	 * 
	 * @param dbName
	 * ���ݿ������
	 * @param tableName
	 * �������
	 * @param pictByte
	 * ͼƬ���ֽ�����
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
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				// statement = (Statement) conn.createStatement();
				 ps=conn.prepareStatement("UPDATE "+tableName+" SET PICTURE = ? WHERE ID=?;");  
		            
		            ps.setInt(2, 1);  
		            ps.setBytes(1, pictByte);//(1, in, in.available());  
		            ps.executeUpdate(); 
		            System.out.println("ͼƬ����ɹ�������");
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, null,(Statement)ps);
				 return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
			JDBC_OPT.closedb(conn, null,(Statement)ps);
			 return;
		}
		//��ɲ���֮��ر����ݿ�
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
				System.out.println("�ɹ����ӵ����ݿ⡣����");
				
				 ps=conn.prepareStatement("select PICTURE from "+tableName+" where ID=?;");  
		            
				  
		            ps.setInt(1, 1);  
		            rs= ps.executeQuery();
		            System.out.println("��ȡͼƬ�ɹ�������");
		            rs.next();
		           byte[] data = rs.getBytes("PICTURE");
		          
		           return data;

				 
			}
			else
			{
				//�ر����ݿ�
				JDBC_OPT.closedb(conn, rs,(Statement)ps);
				 return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�ر����ݿ�
			JDBC_OPT.closedb(conn, rs,(Statement)ps);
			 return null;
		}
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
