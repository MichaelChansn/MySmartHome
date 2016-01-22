package ks.mina.activitys;


import ks.anndroidmina.R;
import ks.mina.activity.application.KsApplication;
import ks.mina.minatools.Tcp_static;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class EnterActivity extends Activity {
	private String TAG="EnterActivity";
	private KsApplication ksApplication;
    private Tcp_static tcpStatic;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_enter);

		ksApplication=KsApplication.getAppContext();
		tcpStatic=Tcp_static.getInstance();
     new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				jumpToMainActivity();
				/*Intent intent =new Intent();
				intent.setClass(EnterActivity.this, Main_Control.class);
				startActivity(intent);*/
				EnterActivity.this.finish();
			}
		}, 1000);
		
	}










	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		tcpStatic.cleanSocket();
		System.exit(0);
		
	}










	void jumpToMainActivity()
	{
		ksApplication.getFlags(this);
		Log.e(TAG, ""+ksApplication.flagIsRememberPassword + ksApplication.flagIsAutoLogIn);
		if(ksApplication.flagIsRememberPassword && ksApplication.flagIsAutoLogIn)
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
				
			ksApplication.getLastUserInfo(EnterActivity.this);
			
			boolean islogin=ksApplication.LogIn(ksApplication.homeID, ksApplication.userName, ksApplication.passWord);
			if(islogin)
			{
				Intent intent=new Intent();
				intent.setClass(EnterActivity.this, Main_Control.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			else
			{
				Intent intent= new Intent();
				intent.setClass(EnterActivity.this, LogInActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
				intent.putExtra("LOGIN_FAIL", "LOGINFAIL");
				startActivity(intent);
			}
				}
			}).start();
			
		}
		else
		{
			Intent intent= new Intent();
			intent.setClass(this, LogInActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
			intent.putExtra("LOGIN_FAIL", "NOLOGIN");
			startActivity(intent);
			//this.finish();
		}
	}


	

}
