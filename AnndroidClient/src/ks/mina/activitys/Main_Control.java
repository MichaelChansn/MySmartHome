package ks.mina.activitys;




import ks.anndroidmina.R;
import ks.mina.activitys.vlc.VLCplan2Activity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/*
 * 跳转activity的时候可以保证要跳转的activity只有一个实例，也就是只有一个窗口
 *  Intent intent = new Intent(ReorderFour.this, ReorderTwo.class);   
 *  intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);   
 *  startActivity(intent);
 *  
 */
public class Main_Control extends Activity {

	
	private ImageButton imageButtonPlan1;
	private ImageButton imageButtonPlan2;
	private ImageButton imageButtonBlueTooth;
	
	
	  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maincontrol);
		findViews();
	        
	}
	
	private void findViews()
	{
		imageButtonPlan1=(ImageButton) findViewById(R.id.imageButton_Plan1);
		imageButtonPlan2=(ImageButton) findViewById(R.id.imageButton_Plan2);
		imageButtonBlueTooth=(ImageButton) findViewById(R.id.imageButton_BlueTooth);
		
		ImageButtonClickListner imageButtonClickListener=new ImageButtonClickListner();
		imageButtonPlan1.setOnClickListener(imageButtonClickListener);
		imageButtonPlan2.setOnClickListener(imageButtonClickListener);
		imageButtonBlueTooth.setOnClickListener(imageButtonClickListener);
		
		
		
	}

	class ImageButtonClickListner implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			
			case R.id.imageButton_Plan1:
				jumpToActivitys(Surface_view.class);
				break;
			case R.id.imageButton_Plan2:
				jumpToActivitys(VLCplan2Activity.class);
				break;
			case R.id.imageButton_BlueTooth:
			
				//startOtherSoftPlayVideo(null);
			
				jumpToActivitys(BlueToothOperate.class);
				break;
			}
		}
		
	}
	
	private void startOtherSoftPlayVideo(String uri)
	{
		String uriPath="rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
		if(uri!=null)
		{
		  uriPath=uri;
		  
		}
		
		Intent it = new Intent(Intent.ACTION_VIEW);
		  it.setDataAndType(Uri.parse(uriPath), "video/mp4");
		  startActivity(it);
		
	}
	
	private void jumpToActivitys(Class cls)
	{
		Intent intent=new Intent();
		intent.setClass(Main_Control.this,cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		
	}
	
	
	

}
