package ks.mina.activitys.vlc;

import ks.anndroidmina.R;
import ks.mina.activity.application.KsApplication;
import ks.mina.activitys.LogInActivity;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.minatools.Tcp_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class VLCplan2Activity extends Activity {

	protected static final String TAG = "VLCplan2Activity";
	private LibVLC mLibVLC = null;
	private ImageButton imageButton_PlayVLC;
	private Handler handler ;
	private Handler playVideoHandler;

	private KsApplication application;
	private EventHandler em;
	private ProgressDialog dialog=null;
	private int tryTimes=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vlcplan2);
		
		findViews();

		

	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		em= EventHandler.getInstance();
		em.addHandler(handler);
		try
		{
		mLibVLC = Util.getLibVlcInstance();
		mLibVLC.init(getApplicationContext());
		} catch (LibVlcException e) {
			e.printStackTrace();
		}
		super.onStart();
	}


	private void playVideo(String strPathUri)
	{
		String pathUri = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
		

		

		if (mLibVLC != null) 
		{
			mLibVLC.restart(KsApplication.getAppContext());//保证每次播放前都是新的实例
			if(strPathUri!=null)
			{
				pathUri=strPathUri;
			}
			Log.e(TAG, "$$$$$$$$$$$$$$");
		
			
			mLibVLC.playMyMRL(pathUri);
			
		}
	}
	
	private void findViews()
	{
		application=KsApplication.getAppContext();
		imageButton_PlayVLC=(ImageButton) findViewById(R.id.imageButton_playVlc);
		imageButton_PlayVLC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new Thread(new  Runnable() {
					public void run() {
						playVideoHandler.sendEmptyMessage(1);
						synchronized (application) {
							try {
								PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.GETVIDEOSTREAM);
								Tcp_static.sendMessage(pd);
								application.wait(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								playVideoHandler.sendEmptyMessage(2);
							}	
						}
						Log.e(TAG, "跨过wait()了");
						playVideo(application.videoAddress);
						//startOtherSoftPlayVideo(application.videoAddress);
						//playVideoHandler.sendEmptyMessage(2);
						
					}
				}).start();
				
				
			}
		});
		
		handler= new Handler() {
			public void handleMessage(Message msg) {
				Log.d(TAG, "Event = " + msg.getData().getInt("event"));
				switch (msg.getData().getInt("event")) {
				
				case EventHandler.MediaPlayerPaused:

					break;
				
				case EventHandler.MediaPlayerEndReached:

					playVideoHandler.sendEmptyMessage(2);
					break;
				case EventHandler.MediaPlayerPlaying:
				case EventHandler.MediaPlayerVout:
				
					//if (msg.getData().getInt("data") > 0) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								VLCVideoPlayerActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						playVideoHandler.sendEmptyMessage(2);
						startActivity(intent);
						//VLCplan2Activity.this.finish();
					//}
					break;
				case EventHandler.MediaPlayerPositionChanged:
					playVideoHandler.sendEmptyMessage(2);
					break;
				case EventHandler.MediaPlayerStopped:
					playVideoHandler.sendEmptyMessage(2);

					break;
				case EventHandler.MediaPlayerEncounteredError://没有进入播放地址
					
					
					if(tryTimes<3)
					{
						tryTimes++;
					playVideo(application.videoAddress);
					}
					else
					{
						playVideoHandler.sendEmptyMessage(2);
					tryTimes=0;
					AlertDialog dialog = new AlertDialog.Builder(VLCplan2Activity.this)
							.setTitle("提示信息")
							.setMessage("无法连接到摄像头，请确保手机已经连接到摄像头所在的wifi热点")
							.setNegativeButton("知道了",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											VLCplan2Activity.this.finish();
										}
									}).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
					}
					break;
					
				default:
					Log.d(TAG, "Event not handled ");
					playVideoHandler.sendEmptyMessage(2);
					break;
				}
			}
		};
		
		
		playVideoHandler=new Handler(VLCplan2Activity.this.getMainLooper())
    	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what)
				{
				case 0:
					new AlertDialog
					 .Builder(VLCplan2Activity.this)
					 .setIcon(android.R.drawable.stat_notify_error)
					 .setTitle("播放错误！")
					 .setMessage("请重试")
					 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						
						}
					}).create().show();
					break;
				case 1:
					
					dialog=ProgressDialog.show(VLCplan2Activity.this, "正在打开", "请稍后...");
					dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			            
			            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			                // TODO Auto-generated method stub
			                // Cancel task.
			                if (keyCode == KeyEvent.KEYCODE_BACK) {
			                	dialog.dismiss();
			                	PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.STREAMOUT);
			            		Tcp_static.sendMessage(pd);

			                }
			                return false;
			            }
			        });
					
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		EventHandler em = EventHandler.getInstance();
		em.removeHandler(handler);
		super.onStop();
	}








}
