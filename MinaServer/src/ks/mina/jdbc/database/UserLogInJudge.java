package ks.mina.jdbc.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ks.mina.globalcontent.GlobalContent;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class UserLogInJudge {
	
	public static UserLogInResult UserLogInJudgeFromDataBase(String homeID,String userNames,String password)
	{
		UserLogInResult userresult=new UserLogInResult(homeID, userNames, password);
		
		
		       String sql="SELECT PASSWORD FROM userinfo WHERE USERNAME='"+userNames+"'";
		
				//ArrayList<Map<String, String>> maplist=new ArrayList<Map<String, String>>();
				Connection conn=null;
				Statement statement=null;
				ResultSet rs=null;
				
				
				boolean ishomeIDExist=false;
				ishomeIDExist=JDBC_OPT.isDataBaseNameExist(homeID);
				if(!ishomeIDExist)
				{
					userresult.isLogInOK=false;
					userresult.logInResult=GlobalContent.LOGIN_ERROR_HOMEID;
					
					
					return userresult;
				}
				
				
				
				
				try
				{
				 conn=JDBC_OPT.connectdb(JDBC_OPT.url+homeID, JDBC_OPT.user, JDBC_OPT.password);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
					userresult.isLogInOK=false;
					userresult.logInResult=GlobalContent.LOGIN_ERROR_HOMEID;
					
					JDBC_OPT.closedb(conn, rs,statement);
					return userresult;
					
				}
				try {
					//conn=JDBC_OPT.connectdb(JDBC_OPT.url+homeID, JDBC_OPT.user, JDBC_OPT.password);
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
							 userresult.isLogInOK=false;
							 userresult.logInResult=GlobalContent.LOGIN_ERROR_USERNAME;
							 return userresult;
						}
					}
					else
					{
						//�������ݿ�ʧ�ܣ��ر����ݿ�
						userresult.isLogInOK=false;
						userresult.logInResult=GlobalContent.LOGIN_ERROR_HOMEID;
						
						JDBC_OPT.closedb(conn, rs,statement);
						
						 return userresult;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//�������ݿ�ʧ�ܣ��ر����ݿ�
					userresult.isLogInOK=false;
					userresult.logInResult=GlobalContent.LOGIN_ERROR_HOMEID;
					
					JDBC_OPT.closedb(conn, rs,statement);
					
					 return userresult;
				}
				
				  try {
					  
					  if(rs!=null)
					  if(rs.next())
						  
					  {
						  //rs.next();
						String passwordFromDataBase=rs.getString("PASSWORD");
						
						if(password.equals(passwordFromDataBase))
						{
							userresult.isLogInOK=true;
							userresult.logInResult="LOGINOKOKOK";
							  //��ɲ���֮��ر����ݿ�
							JDBC_OPT.closedb(conn,rs,statement);
							return userresult;
						}
						else
						{
							userresult.isLogInOK=false;
							userresult.logInResult=GlobalContent.LOGIN_ERROR_PASSWORD;
							JDBC_OPT.closedb(conn, rs,statement);
							return userresult;
						}
					  }
					  else
					  {
						  JDBC_OPT.closedb(conn, rs,statement);
							 userresult.isLogInOK=false;
							 userresult.logInResult=GlobalContent.LOGIN_ERROR_USERNAME;
							 return userresult;
						  
					  }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("��ѯ���������");
					JDBC_OPT.closedb(conn,rs,statement);
					e.printStackTrace();
				}
				
				      //��ɲ���֮��ر����ݿ�
					JDBC_OPT.closedb(conn,rs,statement);

		return userresult;
	}

}
