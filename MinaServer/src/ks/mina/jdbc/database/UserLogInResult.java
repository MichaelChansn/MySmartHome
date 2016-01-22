package ks.mina.jdbc.database;

public class UserLogInResult {
	
	public String userName=null;
	public String password=null;
	public String homeID=null;
	public String logInResult=null;
	public boolean isLogInOK=false;
	public UserLogInResult(String homeID,String userName,String password)
	{
		this.userName=userName;
		this.password=password;
		this.homeID=homeID;
		this.logInResult="Unknown Error!";
		this.isLogInOK=false;
		
	}

}
