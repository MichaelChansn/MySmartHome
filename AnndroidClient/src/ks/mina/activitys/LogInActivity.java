package ks.mina.activitys;

import ks.anndroidmina.R;
import ks.mina.activity.application.KsApplication;
import ks.mina.minatools.Tcp_static;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LogInActivity extends Activity {

	
	private String homeID=null;
	
	private ImageView imageView_UserImage;
	private EditText editText_UserName;
	private EditText editText_PassWord;
	private EditText editText_HomeID;
	private Button button_LogIn;
	private CheckBox checkBox_IsRemember;
	private CheckBox checkBox_IsAutoLogIn;
	private TextView textView_SignIn;
	private TextView textView_ForgetPassword;
	private KsApplication application;
	private Tcp_static tcpStatic=null;
	private final String TAG="LogInActivity";
	private String loginInfo=null;
	private TextView textView_LoginInfo;
	private Handler handler=null;
	
	private ProgressDialog dialog=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_login);
		
		findViews();
		setFlags();
		setViewFunctions();
	}

	
	
	void setFlags()
	{
		 application.getLastUserInfo(this);
		if(application.flagIsRememberPassword &&application.flagIsAutoLogIn)
		{
			checkBox_IsRemember.setChecked(true);
			checkBox_IsAutoLogIn.setChecked(true);
			checkBox_IsAutoLogIn.setTextColor(Color.WHITE);
			checkBox_IsAutoLogIn.setClickable(true);
			if(application.userName!=null && application.passWord!=null)
			{
			editText_UserName.setText(application.userName);
			editText_PassWord.setText(application.passWord);
			editText_HomeID.setText(application.homeID);
			}
			
			
		}
		else
			if(application.flagIsRememberPassword)
			{
				checkBox_IsRemember.setChecked(true);
				checkBox_IsAutoLogIn.setChecked(false);
				checkBox_IsAutoLogIn.setTextColor(Color.WHITE);
				checkBox_IsAutoLogIn.setClickable(true);
				if(application.userName!=null && application.passWord!=null)
				{
				editText_UserName.setText(application.userName);
				editText_PassWord.setText(application.passWord);
				editText_HomeID.setText(application.homeID);
				}
			}
			else
			{
				checkBox_IsRemember.setChecked(false);
				checkBox_IsAutoLogIn.setChecked(false);
				checkBox_IsAutoLogIn.setTextColor(Color.GRAY);
				checkBox_IsAutoLogIn.setClickable(false);
				if(application.userName!=null && application.passWord!=null)
				{
				editText_UserName.setText(application.userName);
				editText_HomeID.setText(application.homeID);
				}
			}
	}
	
	void findViews()
	{
		application=KsApplication.getAppContext();
		tcpStatic=Tcp_static.getInstance();
		homeID=application.homeID;
		
		Intent intent=getIntent();
		loginInfo=intent.getStringExtra("LOGIN_FAIL");
	
		textView_LoginInfo=(TextView) findViewById(R.id.textView_LoginInfo);
		imageView_UserImage=(ImageView) findViewById(R.id.imageView_UserImage);
		editText_UserName=(EditText) findViewById(R.id.editText_UserName);
		editText_PassWord=(EditText) findViewById(R.id.editText_PassWord);
		editText_HomeID=(EditText) findViewById(R.id.editText_HomeID);
		button_LogIn=(Button) findViewById(R.id.button_LogIn);
		checkBox_IsRemember=(CheckBox) findViewById(R.id.checkBox_IsRemember);
		checkBox_IsAutoLogIn=(CheckBox) findViewById(R.id.checkBox_IsAutoLogIn);
		textView_SignIn=(TextView) findViewById(R.id.textView_SignIn);
		textView_ForgetPassword=(TextView) findViewById(R.id.textView_ForgetPassword);
		checkBox_IsAutoLogIn.setClickable(false);
		checkBox_IsAutoLogIn.setTextColor(Color.GRAY);
		handler=new Handler(LogInActivity.this.getMainLooper())
    	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what)
				{
				case 0:
					new AlertDialog
					 .Builder(LogInActivity.this)
					 .setIcon(android.R.drawable.stat_notify_error)
					 .setTitle("登录错误！")
					 .setMessage(application.logInInfor)
					 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						
						}
					}).create().show();
					break;
				case 1:
					
					dialog=ProgressDialog.show(LogInActivity.this, "正在登陆", "请稍后...");
					
					break;
				case 2:
					if(dialog!=null)
					{
						dialog.dismiss();
						dialog=null;
					}
					break;
				}
			
			
			}
    		
    	};
		
	}
	
	void setViewFunctions()
	{
		
		
		if(loginInfo.equalsIgnoreCase("LOGINFAIL"))
		{
			textView_LoginInfo.setText("登录失败，请重试！");
		}
		else
			if(loginInfo.equalsIgnoreCase("NOLOGIN"))
		{
			textView_LoginInfo.setText("请登录...");
		}
		
		button_LogIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				if(editText_HomeID.getText().toString().length()>0 && editText_UserName.getText().toString().trim().length()>0 && editText_PassWord.getText().toString().trim().length()>0)
				{
					Log.e(TAG, "**********");
					 new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							//显示登陆progress
							handler.sendEmptyMessage(1);
					    if(application.LogIn(editText_HomeID.getText().toString(),editText_UserName.getText().toString(),editText_PassWord.getText().toString()))
					    {
						  application.setFlags(LogInActivity.this, checkBox_IsRemember.isChecked(), checkBox_IsAutoLogIn.isChecked());
						  application.setLastUserInfo(LogInActivity.this, editText_UserName.getText().toString(), editText_PassWord.getText().toString(),editText_HomeID.getText().toString());
						 
						  //取消progress
						  handler.sendEmptyMessage(2);
						  jumpToMainControlActivity();
						  
						  
					    }
					    else
					    {
					    	//取消progress
							  handler.sendEmptyMessage(2);
					    	tcpStatic.cleanSocket();
					    	
					    	//通知登陆错误
					    	handler.sendEmptyMessage(0);
					    
					    	
					    }
						
						}}).start();
				}
				else
				{
					Log.e(TAG, "############");
					
					
					 new AlertDialog
					 .Builder(LogInActivity.this)
					 .setIcon(android.R.drawable.stat_notify_error)
					 .setTitle("用户名或密码不能为空")
					 .setMessage("请输入正确的用户名和密码")
					 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						
						}
					}).create().show();
					                          
				}
			}
		});
		
		checkBox_IsRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					checkBox_IsAutoLogIn.setClickable(true);
					checkBox_IsAutoLogIn.setTextColor(Color.WHITE);
				}
				else
				{
					checkBox_IsAutoLogIn.setChecked(false);
					checkBox_IsAutoLogIn.setClickable(false);
					checkBox_IsAutoLogIn.setTextColor(Color.GRAY);
				}
			}
		});
		
		textView_SignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToSignInActivity();
				
				
				
				
			}
		});
		textView_ForgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToFindPasswordActivity();
				
			}
		});
		
		
	}
	
	
	void jumpToMainControlActivity()
	{
		
		Intent intent=new Intent();
		intent.setClass(this, Main_Control.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		
	}
	
	void jumpToSignInActivity()
	{
		
		
		
		
		
		
		LogInActivity.this.finish();
	}
	

	void jumpToFindPasswordActivity()
	{
		
		
		
		
		LogInActivity.this.finish();
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//回到桌面，相当于按下HOME按键
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}
	
	
	
	
	
	
}
