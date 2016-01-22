package ks.mina.activitys;

import ks.anndroidmina.R;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.minatools.Tcp_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class BlueToothOperate extends Activity {
	
	private ImageButton imageButton_Light1;
	private ImageButton imageButton_Light2;
	private ImageButton imageButton_Light3;
	private ImageButton imageButton_Light4;
	private ImageButton imageButton_Light5;
	private ImageButton imageButton_Light6;
	private ImageButton imageButton_Light7;
	private ImageButton imageButton_Light8;
	private ImageButton imageButton_Beep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetoothoperate);
		
		findViews();
	}
	
	
	private void findViews()
	{
		imageButton_Beep=(ImageButton) findViewById(R.id.imageButton_Beep);
		imageButton_Light1=(ImageButton) findViewById(R.id.imageButton_Light1);
		imageButton_Light2=(ImageButton) findViewById(R.id.imageButton_Light2);
		imageButton_Light3=(ImageButton) findViewById(R.id.imageButton_Light3);
		imageButton_Light4=(ImageButton) findViewById(R.id.imageButton_Light4);
		imageButton_Light5=(ImageButton) findViewById(R.id.imageButton_Light5);
		imageButton_Light6=(ImageButton) findViewById(R.id.imageButton_Light6);
		imageButton_Light7=(ImageButton) findViewById(R.id.imageButton_Light7);
		imageButton_Light8=(ImageButton) findViewById(R.id.imageButton_Light8);
		
		
		
		ButonListener bt=new ButonListener();
		ButtonListener2 bt2=new ButtonListener2();
		//imageButton_Beep.setOnClickListener(bt);
		//imageButton_Light.setOnClickListener(bt);
		imageButton_Beep.setOnTouchListener(bt2);
		imageButton_Light1.setOnTouchListener(bt2);
		imageButton_Light2.setOnTouchListener(bt2);
		imageButton_Light3.setOnTouchListener(bt2);
		imageButton_Light4.setOnTouchListener(bt2);
		imageButton_Light5.setOnTouchListener(bt2);
		imageButton_Light6.setOnTouchListener(bt2);
		imageButton_Light7.setOnTouchListener(bt2);
		imageButton_Light8.setOnTouchListener(bt2);
		
	
	}
	private void sendMessageToBlueTooth(String commend)
	{
		PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.COMMEND_BLUETOOTH+GlobalContent.separator+commend);
		Tcp_static.sendMessage(pd);
		
	}

	class ButonListener implements android.view.View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.imageButton_Beep:
				
				sendMessageToBlueTooth("c");
			
				
				break;
				
			case R.id.imageButton_Light1:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light2:
				
							
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
						
							break;
			case R.id.imageButton_Light3:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light4:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light5:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light6:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light7:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			case R.id.imageButton_Light8:
				
				//a¡¡µ∆bπÿµ∆
				sendMessageToBlueTooth("a");
			
				break;
			default:
					break;
			
			}
		}

		
		
	}
	
	 class ButtonListener2 implements OnTouchListener{  
		  
	    
	  
	        public boolean onTouch(View v, MotionEvent event) {  
	          switch(v.getId())
	          {
	          case R.id.imageButton_Beep:
					
	        	  if(event.getAction()==MotionEvent.ACTION_DOWN)
	        	  {
					sendMessageToBlueTooth("a");
	        	  }
	        	  else
	        		  if(event.getAction()==MotionEvent.ACTION_UP)
	        	  {
	        		  sendMessageToBlueTooth("b");
	        	  }
					
					break;
					
				case R.id.imageButton_Light1:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
		        	  {
						sendMessageToBlueTooth("a");
		        	  }
		        	  else
		        		  if(event.getAction()==MotionEvent.ACTION_UP)
		        	  {
		        		  sendMessageToBlueTooth("b");
		        	  }
				
					break;
					
				case R.id.imageButton_Light2:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
		        	  {
						sendMessageToBlueTooth("c");
		        	  }
		        	  else
		        		  if(event.getAction()==MotionEvent.ACTION_UP)
		        	  {
		        		  sendMessageToBlueTooth("d");
		        	  }
				
					break;
				case R.id.imageButton_Light3:
		
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("e");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("f");
			    	  }
				
					break;
				case R.id.imageButton_Light4:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("g");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("h");
			    	  }
				
					break;
				case R.id.imageButton_Light5:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("i");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("j");
			    	  }
				
					break;
				case R.id.imageButton_Light6:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("k");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("l");
			    	  }
				
					break;
				case R.id.imageButton_Light7:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("m");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("n");
			    	  }
				
					break;
				case R.id.imageButton_Light8:
					
					//a¡¡µ∆bπÿµ∆
					 if(event.getAction()==MotionEvent.ACTION_DOWN)
			    	  {
						sendMessageToBlueTooth("o");
			    	  }
			    	  else
			    		  if(event.getAction()==MotionEvent.ACTION_UP)
			    	  {
			    		  sendMessageToBlueTooth("p");
			    	  }
				
					break;
				default:
						break;
	          
	          }
	            return false;  
	        }

		
	          
	 }
	




	

}
