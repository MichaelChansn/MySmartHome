package ks.mina.activitys;




import freescale.ks.remoteeye.ui.Activity_VideoStream;
import ks.anndroidmina.R;
import ks.mina.activitys.bluetooth.BluetoothCommService;
import ks.mina.activitys.bluetooth.ScanDeviceActivity;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.minatools.NetConnect;
import ks.mina.minatools.Tcp_static;
import ks.mina.minatools.Udp_GetIP_static;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import ks.mina.reconnect.ReconnectTypesContent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ��תactivity��ʱ����Ա�֤Ҫ��ת��activityֻ��һ��ʵ����Ҳ����ֻ��һ������
 *  Intent intent = new Intent(ReorderFour.this, ReorderTwo.class);   
 *  intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);   
 *  startActivity(intent);
 *  
 */
public class Main_Control extends Activity {

	
	   //�������Ϣ���������������������ഫ��������Ϣ
	   @SuppressLint("HandlerLeak")
	   public static Handler handler;
	   
	   private EditText ipET;
	   private EditText portET;
	   
	   private Button button_con;
	   private Button button_getip;
	   private Button button_bluetooth;
	   private TextView textout;
	   
	   
	   //�������
	   
	   // Debugging
	    private static final String TAG = "BluetoothComm";
	    private static final boolean D = true;
	    //������������requestCode
		static final int REQUEST_ENABLE_BT = 1;
		//�������ӵ�requestCode
		static final int REQUEST_CONNECT_DEVICE = 2;
		//bluetoothCommService ��������Ϣ״̬
	    public static final int MESSAGE_STATE_CHANGE = 1;
	    public static final int MESSAGE_READ = 2;
	    public static final int MESSAGE_WRITE = 3;
	    public static final int MESSAGE_DEVICE_NAME = 4;
	    public static final int MESSAGE_TOAST = 5;
		
	    // Key names received from the BluetoothChatService Handler
	    public static final String DEVICE_NAME = "device_name";
	    public static final String TOAST = "toast";
	    //�����豸
		private BluetoothDevice device = null;
		
		
		
		//��������������
		private BluetoothAdapter bluetooth;
		//����һ���������ڷ������
		private BluetoothCommService mCommService = null;
		
		private StringBuffer mOutStringBuffer = new StringBuffer("");
		
	    private String mConnectedDeviceName = null;
	   
	 
	   
	
	   PowerManager.WakeLock wakeLock=null;
	
	   private void acquireWakeLock() {
		   if (wakeLock ==null) {
		  
		   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		   wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
		   wakeLock.acquire();
		   }

		   }


		   private void releaseWakeLock() {
		   if (wakeLock !=null&& wakeLock.isHeld()) {
		   wakeLock.release();
		   wakeLock =null;
		   }

		   }
	
		   //����������
		     // The Handler that gets information back from the BluetoothChatService
		        private final Handler mHandler = new Handler() {
		            @Override
		            public void handleMessage(Message msg) {
		                switch (msg.what) {
		                case MESSAGE_STATE_CHANGE:
		                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
		                    switch (msg.arg1) {
		                    case BluetoothCommService.STATE_CONNECTED:
		                    	//connectDevices.setText(R.string.title_connected_to);
		                    	//connectDevices.append(mConnectedDeviceName);
		                    //    mConversationArrayAdapter.clear();
		                        break;
		                    case BluetoothCommService.STATE_CONNECTING:
		                    	//connectDevices.setText(R.string.title_connecting);
		                        break;
		                    case BluetoothCommService.STATE_LISTEN:
		                    case BluetoothCommService.STATE_NONE:
		                    	//connectDevices.setText(R.string.title_not_connected);
		                        break;
		                    }
		                    break;
		                case MESSAGE_WRITE:
		                    byte[] writeBuf = (byte[]) msg.obj;
		                    // construct a string from the buffer
		                  //  String writeMessage = new String(writeBuf);
		                 //   mConversationArrayAdapter.add("Me:  " + writeMessage);
		                    break;
		                case MESSAGE_READ:
		                    byte[] readBuf = (byte[]) msg.obj;
		                    // construct a string from the valid bytes in the buffer
		                    String readMessage = new String(readBuf, 0, msg.arg1);
		                //    mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
		                   // rxEdit.append(readMessage);
		                    break;
		                case MESSAGE_DEVICE_NAME:
		                    // save the connected device's name
		                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
		                    Toast.makeText(getApplicationContext(), "Connected to "
		                                   + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
		                    break;
		                case MESSAGE_TOAST:
		                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
		                                   Toast.LENGTH_SHORT).show();
		                    break;
		                }
		            }
		        };
		        
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main__control_layout);
		    ipET = (EditText)findViewById(R.id.IpEditText);
	        portET = (EditText)findViewById(R.id.PortEditText);
	        button_con = (Button)findViewById(R.id.ConnectButton);
	        button_getip=(Button)findViewById(R.id.getserip);
	        button_bluetooth=(Button) findViewById(R.id.button_bluetooth);
	        textout=(TextView)findViewById(R.id.textView3);
	        
	        //��ñ��������豸
	        bluetooth = BluetoothAdapter.getDefaultAdapter();
	        if(bluetooth == null)
	        {//�豸û�������豸
	        	Toast.makeText(this, "û���ҵ�����������", Toast.LENGTH_LONG).show();
	        } 
	
	        
	        Tcp_static.res=getResources();
	        acquireWakeLock();
	        
	        
	        button_bluetooth.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(!bluetooth.isEnabled())
					{
			    		//����������豸
			    		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    		startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			    	} else 
			    	{
			    		if(mCommService==null) 
			    		{
			    			mCommService = new BluetoothCommService(Main_Control.this, mHandler);
			    		
			    			if (mCommService.getState() == BluetoothCommService.STATE_NONE)
			    			{
					              // Start the Bluetooth services�����������߳�
					            	mCommService.start();
			    			}
			    		}
			    		Intent serverIntent = new Intent(Main_Control.this, ScanDeviceActivity.class);
			            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			    	}
					
					
					
					
					
					
				}
			});
	        
	      
	        button_getip.setOnClickListener(new OnClickListener(){
	        	@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	        
	        new Thread(new Runnable() {
				
				@Override
				public void run() {
					Udp_GetIP_static.udpGetIP();
				}
			}).start();
			
	        
	        	}
	        });
	        
	        
	        button_con.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if(!Tcp_static.isTcpConnect)
					{
						//�����ڲ��ֻ࣬����һ�Σ��߳�ʹ��
						// �����ͻ���������. 
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								//isConnect=true;
								
								//���ܿ��̷߳��ʿؼ���Ҫʹ��handler����
								//button_con.setText("�Ͽ�������");//FUCK, CAN NOT VISIT THE CONTROLS THROUGH ANOTHER THREAD
								OPT_ActivityView_static.sendHandlerMessage(handler, "�Ͽ�������",OPT_ActivityView_static.INDEX_BUTTONTEXT );
								//�õ�������ip��ַ�Ͷ˿ں�
								Tcp_static.ServerIP=ipET.getText().toString();
								Tcp_static.PORT=Integer.parseInt(portET.getText().toString());
								
								Tcp_static.setupTcp_staticConnectorParam();
								if(Tcp_static.connectServer())
								{
									OPT_ActivityView_static.sendHandlerMessage(handler, "�Ͽ�������",OPT_ActivityView_static.INDEX_BUTTONTEXT );
									
									
									


								}
							}
						}).start();
					}
					else
					{
						//�����˳�����Ҫ��������
						ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_NO;
						PacketData pd=PacketDataOperate.sendStrPacketData(GlobalContent.LOGOUT);
						Tcp_static.cf.getSession().write(pd);
						
						
						
						Tcp_static.cf.getSession().close(true);
						
						//isConnect=true;
						//button_con.setText("���ӷ�����");
						OPT_ActivityView_static.sendHandlerMessage(handler, "���ӷ�����",OPT_ActivityView_static.INDEX_BUTTONTEXT );
					}
					
						
								
					  
					
				}
			});
	        
	        handler=new Handler(){

				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					
					switch (msg.what) {
					case OPT_ActivityView_static.INDEX_UDPGETIP:
						textout.setText(msg.obj.toString());
						String[] tem=msg.obj.toString().split("\\+");
						if(tem.length>=2)
						{
						
						ipET.setText(tem[0].substring(1));
						portET.setText(tem[1]);
						Tcp_static.ServerIP=tem[0].substring(1);
						Tcp_static.PORT= Integer.parseInt( tem[1]);
						//tcpcon.ipstore=ipET.getText().toString();
	        			//tcpcon.portstore=socketET.getText().toString();
	        			//tcpcon.textoutstore=textout.getText().toString();
						}
						break;

					case OPT_ActivityView_static.INDEX_TEXTVIEW://��ʾ������Ϣ
						textout.setText(msg.obj.toString());
						break;
						
						

					case OPT_ActivityView_static.INDEX_BUTTONTEXT://�ı䰴ť����
						button_con.setText(msg.obj.toString());
						break;
					case OPT_ActivityView_static.INDEX_JUMPTOCAMERA:
						if(msg.obj.toString().equalsIgnoreCase("STARTCAMERA"))
						jumpToCamera_View();
						break;
						
					case OPT_ActivityView_static.INDEX_JUMPTOSTREAM:
						String[] strs=msg.obj.toString().split("<##>");
						if(strs[0].equalsIgnoreCase("STARTSTREAM"))
							jumpToStream_View(strs[1]);
						break;
						
					case OPT_ActivityView_static.INDEX_SENDMESSAGETOBLUETOOTH:
						sendMessageToBlueTooth(msg.obj.toString());
					
						break;
					default:
						break;
					}
				}
	        	
	        };
	        
	
	        
	        
	}
	
	
	 /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessageToBlueTooth(String message) {
        //û�������豸�����ܷ���
        if (mCommService.getState() != BluetoothCommService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.nodevice, Toast.LENGTH_SHORT).show();
            return;
        }
		
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mCommService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }
	
	
	
	/**
     * onActivityResult������������startActivityForResult����֮����ã�
     * �����û��Ĳ�����ִ����Ӧ�Ĳ���
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 switch (requestCode) {
		 case REQUEST_ENABLE_BT:
	            if (resultCode == Activity.RESULT_OK) {
	            	if(D) Log.d(TAG, "�������豸");
	            	Toast.makeText(this, "�ɹ�������", Toast.LENGTH_SHORT).show();    
	            } else {    
	            	if(D) Log.d(TAG, "������������豸");
	                Toast.makeText(this, "���ܴ�����,���򼴽��ر�", Toast.LENGTH_SHORT).show();
	                finish();//�û������豸���������
	            }break;
		 case REQUEST_CONNECT_DEVICE:
			 // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) {//�û�ѡ�����ӵ��豸
	                // Get the device MAC address
	                String address = data.getExtras()
	                                     .getString(ScanDeviceActivity.EXTRA_DEVICE_ADDRESS);
	                // Get the BLuetoothDevice object
	                device = bluetooth.getRemoteDevice(address);
	                //���������豸
	                mCommService.connect(device);
	            }
	            break;		 
		 }
		 return;
	 }
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	//��ת�� ��Ƶ������
	void jumpToStream_View(String strIP)
	{
		Intent intent = new Intent();

		 intent.setClass(Main_Control.this, Activity_VideoStream.class);

		 intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 intent.putExtra("USERIP", strIP);
		startActivity(intent);
	}

	//��ת������ͷ����
	void jumpToCamera_View()
	{
		Intent intent = new Intent();

		 intent.setClass(Main_Control.this, Camera_View.class);

		 intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	
	NetConnect myNet=new NetConnect();
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		releaseWakeLock();
		if(bluetooth!=null)
		{
        	bluetooth.disable();
        }
		 if (mCommService != null) mCommService.stop();
		super.onDestroy();
		
		
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		myNet.unregisterBroadcast(this);
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//��֤activityÿ����ʾ��ʱ������ȷ��ֵ
		Tcp_static.netContext=Main_Control.this;
		super.onResume();
		myNet.registerBroadcast(this);
		
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main__control, menu);
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	    


	    
	    
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			//return super.onOptionsItemSelected(item);
			switch (item.getItemId()) 
			{
			case R.id.appExit:
				exitapp();
				return true;
			case R.id.appAbout:
				
				about();
				return true;

			default:
				return false;
			}
		}
		
		
		
		
		
		/**
		 * ��ʾ��������
		 */
		public void about() {
			new AlertDialog.Builder(this)
					.setTitle("��������")
					.setMessage("��ӭʹ�����ܼҾӿ���ϵͳ  \n���ߣ�FreeScale")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// finish();
						}
					})
					.setNegativeButton("����", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}

					}).show();
		}
		
		
		private void exitapp()
		 {
			// Intent intent = new Intent();
			 //intent.setAction("GlobalVarable.EXIT_ACTION"); // �˳�����
			// this.sendBroadcast(intent);// ���͹㲥
			 super.finish();
			 this.finish();
			//System.exit(0);
			 //�˳���̨�߳�,�Լ����پ�̬����
			 System.exit(0);
		 }
		 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
