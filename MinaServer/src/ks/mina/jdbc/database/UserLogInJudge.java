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
							 userresult.isLogInOK=false;
							 userresult.logInResult=GlobalContent.LOGIN_ERROR_USERNAME;
							 return userresult;
						}
					}
					else
					{
						//连接数据库失败，关闭数据库
						userresult.isLogInOK=false;
						userresult.logInResult=GlobalContent.LOGIN_ERROR_HOMEID;
						
						JDBC_OPT.closedb(conn, rs,statement);
						
						 return userresult;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//连接数据库失败，关闭数据库
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
							  //完成操作之后关闭数据库
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
					System.out.println("查询密码出错了");
					JDBC_OPT.closedb(conn,rs,statement);
					e.printStackTrace();
				}
				
				      //完成操作之后关闭数据库
					JDBC_OPT.closedb(conn,rs,statement);

		return userresult;
	}

}
