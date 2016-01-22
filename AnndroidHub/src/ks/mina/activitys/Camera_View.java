package ks.mina.activitys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.LogRecord;

import com.mysql.jdbc.Buffer;

import ks.anndroidmina.R;
import ks.mina.bitmapstore.BitmapStore;
import ks.mina.minatools.Tcp_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import ks.mina.zipbytes.ZipAndUnzipBytes;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;

public class Camera_View extends Activity  implements PreviewCallback{

	
	   public static Handler handler;
	   private SurfaceView surfaceViedo;
	   private SurfaceHolder surfaceHolder;
	   DrawCaptureRect mDraw ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera__view);
		
		initViews();
		  /* ��Ӻ�ɫ�����κ��View�� */
	      mDraw = new DrawCaptureRect
	     (
	       this,
	       surfaceViedo.getLeft(),surfaceViedo.getTop()+50,
	       getResources().getColor(R.color.fs)
	     );

	     /* �������ĺ�ɫ�����������Activity�� */
	     addContentView
	     (
	       mDraw,
	       new LayoutParams
	       (
	       )
	     );
		
	     handler=new Handler(){

				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					
					switch (msg.what) {
					
					case OPT_ActivityView_static.INDEX_JUMPTOCAMERA:
						if(msg.obj.toString().equalsIgnoreCase("STOPCAMERA"))
						finshThisActivity();
						break;
					case OPT_ActivityView_static.INDEX_COMMEND:
						strCommend=msg.obj.toString();
						if(msg.obj.toString().equalsIgnoreCase("openvibrate"))
						{
							OPT_ActivityView_static.Vibrate(Camera_View.this, 2000);
							//OPT_ActivityView_static.Vibrate(Camera_View.this, 2);
						}
						else
							if(msg.obj.toString().equalsIgnoreCase("openlight"))
						{
								openLight();
						}
							else
								if(msg.obj.toString().equalsIgnoreCase("closelight"))
								{
									closeLight();
								}
								else
									if(msg.obj.toString().equalsIgnoreCase("playmusic"))
									{
										playRing();
									}
									else
										if(msg.obj.toString().equalsIgnoreCase("stopmusic"))
										{
											stopRing();
										}
						break;
					default:
						break;
					}
				}
	        	
	        };
	        
	        
	        startSendPic();
			
	}

	//���ֵ�Ͳ
	
	void openLight()
	{
		
		 Parameters mParameters = mCamera.getParameters();
		 mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//��Camera.Parameters.FLASH_MODE_OFF��Ϊ�ر�
		 mCamera.setParameters(mParameters);
    	

	}
	void closeLight()
	{
		
			
		 Parameters mParameters = mCamera.getParameters();
		 mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//��Camera.Parameters.FLASH_MODE_OFF��Ϊ�ر�
		 mCamera.setParameters(mParameters);
			 
	
			
	}
	/**
	 * ��������
	 */
	MediaPlayer mMediaPlayer=null;
	void playRing()
	{
		
		
		    mMediaPlayer = MediaPlayer.create(getApplicationContext(),RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION ));  
	        mMediaPlayer.setLooping(false);  
	        mMediaPlayer.start(); 
		
	}
	void stopRing()
	{
		if(mMediaPlayer!=null)
		{
			mMediaPlayer.stop();
		}
	}
	
	
	private boolean isrun=false;
	void startSendPic()
	{
		new Thread(new Runnable() {
					
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						isrun=true;
						while(isrun)
						{
							
							try {
								Thread.sleep(80);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							 PacketData pd=PacketDataOperate.sendStrAndPictPacketData("GIVEYOURPICTURE", BitmapStore.getBitmapByte());
							 Tcp_static.cf.getSession().write(pd);
							 BitmapStore.setBitmapByte(new byte[1]);//����֮����գ�ʡ����
						}
						
					}
				}).start();
	}
	//�رձ�����
	
	void finshThisActivity()
	{
		isrun=false;
		this.finish();
	}
	
	
	   PowerManager.WakeLock wakeLock=null;
		
	   private void acquireWakeLock() {
		   if (wakeLock ==null) {
		  
		   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		   wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
		   wakeLock.acquire();
		   }

		   }
	   
	   private void releaseWakeLock() {
		   if (wakeLock !=null&& wakeLock.isHeld()) {
		   wakeLock.release();
		   wakeLock =null;
		   }

		   }
	   
	   
	   
	private void initViews()
	{
		surfaceViedo=(SurfaceView)findViewById(R.id.surfaceView1);
        surfaceHolder=surfaceViedo.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceViewCallback());
        
        acquireWakeLock();
        
	}
	
    private Camera mCamera=null;
	   
    
    
	
		@SuppressLint("NewApi")
		private void initCamera()
	    {
	    	 
	          //������� 
			int previewwidth=10000;
			int previewheigh=0;
			int storewidth=10000;
			int storeheigh=0;
	    	
	    	try 
	    	{ 
	    		System.out.println("���ڿ���������ͷ������");
	    		mCamera = Camera.open(0); 
	    		System.out.println("������ͷ�ɹ�������");
	    	} catch(Exception e) 
	    	{ 
	    		System.out.println("============����ͷ��ռ��"); 
	    	} 
	    	if (mCamera == null) 
	    	{
	    		System.out.println("============���ؽ��Ϊ��"); 
	    	     System.exit(0); 
	    	} 
	   
	    	
	      	try {
				mCamera.setPreviewDisplay(surfaceHolder);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("����Ԥ��ʧ��Ŷ������");
			}
	    	
	   
	    	System.out.println("��ʼ��������ͷ����������");
	    	Camera.Parameters parameters = mCamera.getParameters();
	    	
	    	parameters.setPictureFormat(PixelFormat.JPEG);//�������պ�洢��ͼƬ��ʽ  
	    	  
	                      //��ѯcamera֧�ֵ�picturesize��previewsize  
	                      List<Size> pictureSizes = parameters.getSupportedPictureSizes();  
	                      List<Size> previewSizes = parameters.getSupportedPreviewSizes();  
	                      
	                    
	                      for(int i=0; i<pictureSizes.size(); i++){  
	                          Size size = pictureSizes.get(i);
	                          /*��Ƭ��С��С�ֱ���
	                           * 
	                           */
	                            if(storewidth>size.width)
	                          {
	                        	  storeheigh=size.height;
	                        	  storewidth=size.width;
	                        	  
	                          }
	                          /*
	                           * ��Ƭ���ֱ���
	                           */
	                         /* if(storeheigh<size.height)
	                          {
	                        	  storeheigh=size.height;
	                        	  storewidth=size.width;
	                        	  
	                          }*/
	                          
	                          Log.i("tag","initCamera:����ͷ֧�ֵ�pictureSizes: width = "+size.width+"height = "+size.height);  
	                      } 
	                     
	                      for(int i=0; i<previewSizes.size(); i++)
	                      {  
	                          Size size = previewSizes.get(i);  
	                          /*Ԥ����С�ֱ���*/
	                            if(previewwidth>size.width)
	                          {
	                        	  previewheigh=size.height;
	                        	  previewwidth=size.width;
	                        	  
	                          }
	                            
	                            /**
	                             * Ԥ�����ֱ���
	                             */
	                          /*if(previewheigh<size.height)
	                          {
	                        	  previewheigh=size.height;
	                        	  previewwidth=size.width;
	                        	  
	                          }*/
	                          Log.i("tag", "initCamera:����ͷ֧�ֵ�previewSizes: width = "+size.width+"height = "+size.height);  
	            
	                      }  
	  
	                      
	                      //��ô�С��Ԥ������
	                      int temp=previewwidth;
	                      previewwidth=100000;
	                      for(int i=0; i<previewSizes.size(); i++)
	                      {  
	                          Size size = previewSizes.get(i);  
	                          /*Ԥ����С�ֱ���*/
	                          if(temp==size.width)
	                          {
	                        	  continue;
	                          }
	                          else
	                            if(previewwidth>size.width)
	                          {
	                        	  previewheigh=size.height;
	                        	  previewwidth=size.width;
	                        	  
	                          }
	                            
	                        
	                      }  
	                      
	                      
	                      
	                      
	                      
	                      
	                      
	                      
	                      
	  
	                    //���ô�С�ͷ���Ȳ���  
	                      parameters.setPictureFormat(PixelFormat.JPEG);  
	      	            /* ��Ƭ���� */  
	      	              //parameters.set("jpeg-quality", 100);  
	                      parameters.setPictureSize(storewidth, storeheigh);  
	                      parameters.setPreviewSize(previewwidth, previewheigh);  
	                      parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP); //Sets the image format for preview picture��Ĭ��ΪNV21
	                      parameters.setPreviewFrameRate(20);
	                   //   parameters.setPreviewFpsRange (20, 20);

	                  /*    List<int[]> fsp= parameters.getSupportedPreviewFpsRange();
	                 
	                      for(int i=0; i<fsp.size(); i++)
	                      {
	                    	  
	                              int[] r=fsp.get(i);
	                              for(int k=0;k<r.length;k++) 
	                              {
	                            	  System.out.println(r[k]);
	                                  
	                              }
	                              

	                    	  
	                      }*/
	                     
	                      
	                    
	                     
	                      parameters.setRotation(90);
	                     
	                      
	                      mCamera.setDisplayOrientation(90);
	                      mCamera.setParameters(parameters);              
	                       
	   
	    
	                      
	    	
	    	
	        
	        
	        System.out.println("���ûص�����֮ǰ������"); 
	        
	       
	          mCamera.setPreviewCallback(this);
			
	        
	          System.out.println("���ûص���ϡ�����");
	          
	         
	          System.out.println("��������ͷԤ��������");
	    
	     	  
	     	mCamera.startPreview();
	    }
	    
	
		
		
		private synchronized byte[] cameradata2bytes(byte[] data, Camera _camera ,int quality)
	    {
	    	Size size = _camera.getParameters().getPreviewSize();
	        byte[] retbyte;
	        try {
	            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
	                    size.height, null);
	            System.out.println("picturesize:"+size.width + " " + size.height);
	            if (image != null) {
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                
	                image.compressToJpeg(new Rect(0, 0, size.width, size.height), quality, stream);
	                 retbyte=stream.toByteArray();
	                stream.close();
	                /**
	                 * ��ͼ����ת90�ȣ���Ϊֱ�ӵõ���ͼ������90��ת��
	                 */
	                Bitmap bmp = BitmapFactory.decodeByteArray(
	                		retbyte, 0, retbyte.length);
	                Matrix matrix = new Matrix();
		            matrix.postRotate(90);
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		            Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true).compress(Bitmap.CompressFormat.JPEG, quality, baos);
		            retbyte=baos.toByteArray(); 
		            baos.close();
	                
	                return retbyte;
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return new byte[1];
	    }
	    
	    
	    
	    
	    public Bitmap decodeToBitMap(byte[] data, Camera _camera) {
	        Size size = _camera.getParameters().getPreviewSize();
	        Bitmap bmp=null;
	        try {
	            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
	                    size.height, null);
	            System.out.println("picturesize:"+size.width + " " + size.height);
	            if (image != null) {
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 30, stream);
	                 bmp = BitmapFactory.decodeByteArray(
	                        stream.toByteArray(), 0, stream.size());
	                 System.out.println("������ͷ�õ���ͼƬ��С��"+ bmp.getWidth() + " " + bmp.getHeight());
	                
	                
	                stream.close();
	                return bmp;
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return null;
	    }


	    
	    private final class SurfaceViewCallback implements Callback {  
	        /** 
	         * surfaceView �������ɹ�����ô˷��� 
	         */  
	        @Override  
	        public void surfaceCreated(SurfaceHolder holder) {  
	         
	          initCamera();
	        
	          
				
				
	          
	          
	        }  
	        @Override  
	        public void surfaceChanged(SurfaceHolder holder, int format, int width,  
	                int height) {  
	           
	        }  
	        /** 
	         * SurfaceView ������ʱ�ͷŵ� ����ͷ 
	         */  
	        @Override  
	        public void surfaceDestroyed(SurfaceHolder holder) 
	            {
	        	
	    		if(null != mCamera)
	    		  {
	    		   mCamera.setPreviewCallback(null); //�������������ǰ����Ȼ�˳�����
	    		   mCamera.stopPreview(); 
	    		  
	    		   mCamera.release();
	    		   mCamera = null;     
	    		  }
	    		releaseWakeLock();
	            }  
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.camera__view, menu);
		return true;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		System.out.println("��������ͷ�ص������ˡ�������");
		

		
		if(Tcp_static.isTcpConnect)
		{
	     //GetPictureFromCrame.setPicture(decodeToBitMap(data, camera));
			
			byte[] byteArray = new byte[1];
			try {
				byteArray = ZipAndUnzipBytes.zipBytes(cameradata2bytes(data,camera,80));
				BitmapStore.setBitmapByte(byteArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	       //OPT_ActivityView_static.sendHandlerMessage(handler, "ͼ���С�ǣ�"+pd.getPictureSize()+"byte", OPT_ActivityView_static.INDEX_TEXTVIEW);
	
	       mDraw.setvalue(byteArray.length);
	       mDraw.invalidate();
	       
		}
		
		//mCamera.startPreview();
	
	}
	
	
	
	/* �������Ԥ�������������˵�� */
	private String strCommend=null;
	   class DrawCaptureRect extends View
	   {
	     private int colorFill;
	     private int intLeft,intTop;

	     private int fps;
	     public DrawCaptureRect
	     (
	       Context context, int intX, int intY, int colorFill
	     )
	     {
	       super(context);
	       this.colorFill = colorFill;
	       this.intLeft = intX;
	       this.intTop = intY;
	       
	     }

	     public void setvalue(int value)
	     {
	    	 fps=value;
	     }
	     @Override
	     protected void onDraw(Canvas canvas)
	     {
	       Paint mPaint01 = new Paint();
	       mPaint01.setStyle(Paint.Style.FILL);
	       mPaint01.setColor(colorFill);
	       mPaint01.setStrokeWidth(1.0F);
	       mPaint01.setTextSize(40);//���������С
	       
	       
           
         
           
           canvas.drawText("picturesize:"+fps+"bytes",intLeft,intTop,mPaint01);  
           
          
           //�����յ�������
           if(strCommend!=null)
           {
	       mPaint01.setStyle(Paint.Style.FILL);
	       mPaint01.setColor(colorFill);
	       mPaint01.setStrokeWidth(5.0F);
	       mPaint01.setTextSize(80);//���������С
	       canvas.drawText(strCommend,50,this.getHeight()/2,mPaint01); 
           }
	       
	       super.onDraw(canvas);
	     }
	   }
	
	
	
	
	
	
	
	
	
	
	
	
	

}
