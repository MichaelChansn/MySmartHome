package ks.mina.activitys;

import ks.anndroidmina.R;
import ks.mina.bitmapstore.BitmapStore;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.minatools.Tcp_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Surface_view extends Activity {

	
	private Button button_stopvedio;
   
    private boolean isrun=false;
    private SurfaceView surfacevedio;
    private SurfaceHolder surfaceHolder;
    
    private LinearLayout ll;
    private EditText commendText;
    private Button   sendCommendButton;
    private Button   exitCommendButton;
    private CheckBox checkBoxLight;
    private CheckBox checkBoxMusic;
    private CheckBox checkBoxVibrate;
   
  
   
   
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    
    
    //控制图像缩放
 
    private Matrix matrix = new Matrix();  
 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//屏蔽Home键
		setContentView(R.layout.activity_surfaceviewplan1);
		
		
		button_stopvedio=(Button)findViewById(R.id.button_video_stop);
		
		
		/**
		 * 切记，绝对不要在xml里指定surfaceview的背景颜色，要是制定了，他会一直显示背景色，所有的绘画都无效
		 */
		surfacevedio=(SurfaceView)findViewById(R.id.surfaceView1);
		surfaceHolder=surfacevedio.getHolder();
		
		isrun=false;
		
		surfacevedio.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				   switch (event.getAction() & MotionEvent.ACTION_MASK) {  
				   
				   case MotionEvent.ACTION_DOWN:  //第一个触点按下
					   
			            mStartPoint.set(event.getX(), event.getY());  
			            
			            break;
			      
				 

			        case MotionEvent.ACTION_MOVE: 
			        	
			         
			        	 dragAction(event);
			           
			        	break;
			        
			        default:  
			            break;  
			        }  
			  
			        return true;  
				
			}
		});
				
	
	    Animation myanim=AnimationUtils.loadAnimation(this, R.anim.popin);
	    button_stopvedio.setAnimation(myanim);
		
		button_stopvedio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(button_stopvedio.getText().equals("停止视频"))
				{
				//	issend=false;
					PacketData pd=PacketDataOperate.sendStrPacketData("STOPCAMERA");
					Tcp_static.cf.getSession().write(pd);
					isrun=false;
				    // isrun=false;
				button_stopvedio.setText("打开视频");
				}
				else
					if(button_stopvedio.getText().equals("打开视频"))
				{
					
					
						PacketData pd=PacketDataOperate.sendStrPacketData("STARTCAMERA");
						Tcp_static.cf.getSession().write(pd);
						// issend=true;
						 isrun=true;
						 new BitmapThread().start();
						button_stopvedio.setText("停止视频");
					
				
					
				}
			}
		});
		
	
		

		ll=(LinearLayout)findViewById(R.id.linear_commend);
		commendText=(EditText)findViewById(R.id.text_commend);
		sendCommendButton=(Button)findViewById(R.id.button_sendcommend);
		exitCommendButton=(Button)findViewById(R.id.button_exitcommend);
		
		hideLinearLayout();

		sendCommendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//只能输入中文命令。
				String str = "";
				str = commendText.getText().toString();
				
				if(str.trim()!="")
				{
					System.out.println(str);
					PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+str);
					Tcp_static.cf.getSession().write(pd);
				}
			}
		});
		exitCommendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideLinearLayout();
			}
		});
		
		
		checkBoxLight=(CheckBox) findViewById(R.id.checkBox_Light);
		checkBoxMusic=(CheckBox)findViewById(R.id.checkBox_Music);
		checkBoxVibrate=(CheckBox)findViewById(R.id.checkBox_Vibrate);
		
		checkBoxLight.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"openlight");
					Tcp_static.cf.getSession().write(pd);
				}
				else
				{
					PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"closelight");
					Tcp_static.cf.getSession().write(pd);
				}
			}
		});
		
		checkBoxMusic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked)
						{
							PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"playmusic");
							Tcp_static.cf.getSession().write(pd);
						}
						else
						{
							PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"stopmusic");
							Tcp_static.cf.getSession().write(pd);
						}
					}
				});
				
		
		checkBoxVibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"openvibrate");
					Tcp_static.cf.getSession().write(pd);
				}
				else
				{
					PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND+GlobalContent.separator+"closevibrate");
					Tcp_static.cf.getSession().write(pd);
				}
			}
		});
		
		
		
		
		
		
	}

	
	void showLinearLayout()
	{
		ll.setVisibility(View.VISIBLE);
//		commendText.setEnabled(true);
//		commendText.setFocusable(true);
//		commendText.setClickable(true);
//		
//		sendCommendButton.setFocusable(true);
//		sendCommendButton.setEnabled(true);
//		sendCommendButton.setClickable(true);
//		exitCommendButton.setClickable(true);
//		exitCommendButton.setFocusable(true);
//		exitCommendButton.setEnabled(true);
	}
	
	void hideLinearLayout()
	{
		ll.setVisibility(View.GONE);
//		commendText.setFocusable(false);
//		commendText.setClickable(false);
//		commendText.setEnabled(false);
//		sendCommendButton.setFocusable(false);
//		sendCommendButton.setEnabled(false);
//		sendCommendButton.setClickable(false);
//		exitCommendButton.setClickable(false);
//		exitCommendButton.setFocusable(false);
//		exitCommendButton.setEnabled(false);
	}
	
	private PointF mStartPoint=new PointF(0, 0);
	private PointF mPoint=new PointF(0, 0);
    private void dragAction(MotionEvent event) {  
    	  
         
            PointF currentPoint = new PointF();  
            currentPoint.set(event.getX(), event.getY());  
            int offsetX = (int) currentPoint.x - (int) mStartPoint.x;  
            int offsetY = (int) currentPoint.y - (int) mStartPoint.y;
            mPoint.x+=offsetX;
            mPoint.y+=offsetY;
          
            	
            showBitmap();
            mStartPoint.set(currentPoint);
        
    } 
    
    private  Bitmap mBitmap=null;
    private synchronized void  showBitmap() {  
        
    	
        	 byte[] bitbyte=BitmapStore.getBitmapByte();
        	 if(bitbyte.length>1)
        	 {
            try
            {
        	  mBitmap =BitmapFactory.decodeByteArray(bitbyte, 0,  bitbyte.length); 
        	  
        	
             
             
              if(mBitmap.getWidth()<surfacevedio.getWidth())
              {
            	  //缩放倍数
            	  float fx=(float)surfacevedio.getWidth()/mBitmap.getWidth();
            	  float fy=(float)mBitmap.getHeight()*fx;
            	  //在x轴和y轴上都放大fx倍，等比缩放
            	  matrix.setScale(fx, fx, 0, 0);
            	  Bitmap bitmap =Bitmap.createBitmap(surfacevedio.getWidth(), (int) fy,  Config.ARGB_8888); // 背景图片  
            	         Canvas canvas = new Canvas(bitmap); // 新建画布  
            	          canvas.drawBitmap(mBitmap, matrix, null); // 画图片  
            	          canvas.save(Canvas.ALL_SAVE_FLAG); // 保存画布  
            	         canvas.restore();  
            	         mBitmap=bitmap;

              }
              if(mPoint.x>0)
                	mPoint.x=0;
                if(mPoint.y>0)
                	mPoint.y=0;
                int x=mBitmap.getWidth()-surfacevedio.getWidth();
                if(x>0)
                {
                if(mPoint.x<-(mBitmap.getWidth()-surfacevedio.getWidth()))
              	  mPoint.x=-(mBitmap.getWidth()-surfacevedio.getWidth());
                }
                else
                {
              	  mPoint.x=0;
                }
                int y=mBitmap.getHeight()-surfacevedio.getHeight();
                if(y>0)
                {
                if(mPoint.y<-(mBitmap.getHeight()-surfacevedio.getHeight()))
              	  mPoint.y=-(mBitmap.getHeight()-surfacevedio.getHeight());
                }
                else
                {
              	  mPoint.y=0;
                }
               
                
              //将图片移动到mPoint.x, mPoint.y坐标
              matrix.setTranslate(mPoint.x, mPoint.y);
              
              
            Canvas c = surfaceHolder.lockCanvas();  
            if (c != null && mBitmap != null) {  
                c.drawColor(Color.GRAY);  
              //  c.drawBitmap(mBitmap, null, new Rect(this.surfacevedio.getLeft(),this.surfacevedio.getTop(), this.surfacevedio.getRight(), this.surfacevedio.getBottom()), null);
                //画图像
                c.drawBitmap(mBitmap, matrix, null);
                //画预览
                Paint pant=new Paint();
                pant.setColor(Color.RED);
                pant.setStrokeWidth(3);
                pant.setStyle(Style.STROKE);//空心矩形框  
                //预览大小
                //Rect rect=new Rect(0, surfacevedio.getHeight()-surfacevedio.getWidth()*1/3, surfacevedio.getWidth()*1/3, surfacevedio.getHeight());
                Rect rect=new Rect(0, 0, surfacevedio.getWidth()*1/4, surfacevedio.getWidth()*1/4);
                
                c.drawBitmap(mBitmap, null, rect, null);
                c.drawRect(rect ,pant);
                surfaceHolder.unlockCanvasAndPost(c);  
               } 
            }catch(Exception e)
            {
            	Log.i("system.out", "还没连接服务器，得不到图片");
            }
           
        	}
        	
    	
         
    }  
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.surface_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		switch (item.getItemId()) 
		{
		case R.id.openCommend:
			
			showLinearLayout();
			return true;
	

		default:
			return false;
		}
	}
	
	
	   
		/*// 屏蔽home键和返回键
			 @Override
			 public void onAttachedToWindow() {
			  // TODO Auto-generated method stub
			  this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
			  super.onAttachedToWindow();
			 }*/
			 @Override
			 public boolean onKeyDown(int keyCode, KeyEvent event) {
			  // TODO Auto-generated method stub
			  switch(keyCode) {
			  
			  case KeyEvent.KEYCODE_BACK:
				  System.out.println("按下了返回键。。。");
				try {
					
					isrun=false;
				
				    // isrun=false;
					//finalize();//垃圾收集器
					finish();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("销毁Activity失败。。。");
				}
			
				  return true;
			  
			  case KeyEvent.KEYCODE_HOME:
				  return false;

			  }
			  return super.onKeyDown(keyCode, event);
			 } 
	
	
	private class BitmapThread extends Thread
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if(Tcp_static.isTcpConnect)
			{
			 PacketData pd=PacketDataOperate.sendStrPacketData("GETPICTURE");
			 Tcp_static.cf.getSession().write(pd);
			
			
			 //注意，线程里也绝对不允许while（1）空转
			
				while(isrun)
				{
				
					//Tcp_static.cf.getSession().write(pd);
				/*try {
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("靠，线程休眠居然失败了。。。");
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				*/
				
				
				
				
				if(BitmapStore.getisGetPicture())
				{
			
				 BitmapStore.setisGetPicture(false);
				 Tcp_static.cf.getSession().write(pd);
				 
				
				 
				 
				 showBitmap();
			    }
				
				
			}
		}
			else
			{
				  Canvas c = surfaceHolder.lockCanvas();  
		            if (c != null) {  
		                c.drawColor(Color.WHITE); 
		                Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    
		                mTextPaint.setColor(Color.RED);    
		                mTextPaint.setTextSize(35);//设置字体大小
		                mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体类型
		                // Define the string.    
		                String displayText ="亲，您还没连接到服务器呢！";   
		                // Measure the width of the text string.    
		                int textWidth = (int)mTextPaint.measureText(displayText);  
		                
		                c.drawText(displayText, (surfacevedio.getWidth()-textWidth)/2,surfacevedio.getHeight()/2 , mTextPaint);  
		                surfaceHolder.unlockCanvasAndPost(c);  
		               }  
		        	
			}
	 }
		
	}
	
	
}
