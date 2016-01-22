package ks.mina.minatools;


import ks.mina.activity.application.KsApplication;
import ks.mina.activitys.Main_Control;
import ks.mina.activitys.Surface_view;
import ks.mina.activitys.viewoperate.OPT_ActivityView_static;
import ks.mina.bitmapstore.BitmapStore;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import ks.mina.reconnect.ReconnectServer;
import ks.mina.reconnect.ReconnectTypesContent;
import ks.mina.zipbytes.ZipAndUnzipBytes;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.annotation.SuppressLint;
import android.os.Message;
import android.util.Log;

public class ClientHandler extends IoHandlerAdapter {
	
	
	private String TAG="ClientHandler";
	private KsApplication application=KsApplication.getAppContext();
	//private  boolean isright=false;//�����ж��ǲ�����ȷ���������ˣ����ŶԽ��ϣ���ʾ�ɹ����ӣ����Է�ֹ��������

	//���ӹرյ�ʱ����ô˺���
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Log.d(TAG,"��ǰTCP�ͻ����˳�������������еĺ�������");//���������������е�
		
		Tcp_static.isTcpConnect=false;
		//ReconnectServer.reconnectServer();
		// TODO Auto-generated method stub
		//�ж��Ƿ���Ҫ���Զ������ӷ�����
		switch(ReconnectTypesContent.connectTypes)
		{
		case ReconnectTypesContent.CONNECT_YES:
			//ReconnectServer.reconnectServer();
			break;
		case ReconnectTypesContent.CONNECT_NO:
			break;
		default:
			break;
		}
	}

	//���Ӵ�����ʱ����ô˺���
	@Override
	public void sessionCreated(final IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
		
		
	}

	//���Ӵ����ɹ���ʱ����ô˺���
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
		
		 
		 Log.d(TAG,"�Ѿ����ӵ�������������");
		super.sessionOpened(session);
	}

	//�����쳣��ʱ����ô˺���
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		/*
    	 * �������е������ʾ�������Ѿ��˳���1.һ���Ƿ������������˳���
    	 * 2.����һ��������ͻ���ͻȻ����
    	 * 
    	 * ��������ʾ�û����������������ֻ�����ͻȻ�жϣ��Ƿ�������
    	 * �����ж�һ���ֻ���������������崦��
    	 * 
    	 * 
    	 * 
    	 */
		
		cause.printStackTrace();
		Log.d(TAG,"�����쳣����,�����쳣������");
		
		
		
		if(NetConnect.isNetworkConnected(GlobalContent.netContext))//�ֻ�������������ʾ�����������˳�
		{
			Log.e(TAG,"�ͷ��������ӶϿ���ò�Ʒ����������ˡ�����");
			}
		else//�ֻ�����Ͽ�����������ж�
		{
			Log.d(TAG,"�ֻ������ѶϿ����������������硣����");
			
			
		}
		session.close(true);
	}



	//�յ����ݵ�ʱ����ô˺���
	@SuppressLint("NewApi")
	public void messageReceived(IoSession session, Object message)throws Exception 
	{
		
		
		if (message instanceof PacketData) {
            PacketData pd = (PacketData) message;
            Log.d(TAG,"�������� ��ȡ�ɹ� :"+pd.toString() );
            //session.write("success rescive");
            
            String[] strmessages=pd.getMessage().split(GlobalContent.separator);
            if (strmessages[0].trim().equalsIgnoreCase(GlobalContent.LOGOUT)) {
            	Log.d(TAG,"��ǰ���������˳�������");
            	
				
				//�����˳�����Ҫ�����ӷ�����
				ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_NO;
				session.close(true);
				return;
			}
            else
					if(strmessages[0].trim().equalsIgnoreCase("this is your picture"))
				    {

						Log.d(TAG,"��ʼ�ӷ������õ�ͼƬ������");
						
						 BitmapStore.setBitmapByte(ZipAndUnzipBytes.unzipBytes(pd.getBmpByte()));
						 BitmapStore.setisGetPicture(true);
						 
						 return;
						
				    }
					else
            if(strmessages[0].trim().equalsIgnoreCase(GlobalContent.YOURVIDEOSTREAM))
            {
            	synchronized (application)
            	{
            		application.videoAddress=strmessages[1];
            		application.notify();
            	}
            }
					
					
				
					
				
             
        } else 
        {
        	Log.d(TAG,"��ȡʧ��");
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	
  }
	
	
	
	//��д���е�ʱ����ô˺�����������ʹ�õ��Ƕ���ʱ��һ��ʱ��û�ж��������͵��ô˺���
	//z�����������tcp���ӽ���ʱ���õ�
	//Tcp_static.cf.getSession().getConfig().setIdleTime(IdleStatus.READER_IDLE, Tcp_static.IDEL_TIME);
	////����10��û�н��ս���idle״̬�����������ж������ж�
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception 
	{
		System.out.println("IDLE " + session.getIdleCount(status));
		
		
		//ÿһ��handler.sendMessage()��Ҫ����һ���µ�Message���ŵ���Ϣ���������ȴ�����
		//���ʹ��ͬһ��Message���ԭ����û��������ʹ�õĻ��������������
	
		if(session.getIdleCount(status)*Tcp_static.IDEL_TIME>=Tcp_static.IDEL_OUT)
		{
			/**************************************************/
			/*�������е�����˵�����������жϣ��źŲ��ã�һ����Ҫ����*/
			/**/
			/*��������ʾ�û��������ѶϿ�����Ҫ�������ӣ���½*/
			/**/
			/**************************************************/
			
			
			System.out.println("�Ѿ�"+Tcp_static.IDEL_OUT+"��û�յ������ˣ�����ò�ƶϿ��ˣ����Ϲرյ�ǰ���ӡ�����");
			
			session.close(true);
			
			
			
		}
	}
	
	/**
	 * session.write()�ǰ�����д�������������ܱ�֤д�����ͨ��
	 * ��������Ѿ��Ͽ�����ʼ����д���������ģ�����û��д�����ͨ��
	 * ʹ�ñ����������жϼ����������������Ƿ�д�������ͨ����
	 * ���������жϺͶԷ��������Ƿ��жϣ�׼ȷ��˵���ж϶Է��ǲ��Ǳ����ˣ��쳣�˳���
	 * 
	 * ���д��ʧ�ܣ�һ���ᴥ���쳣 exceptionCaught()����
	 */
	@SuppressWarnings("rawtypes")
	public void writedata(final IoSession session, PacketData data)
	{
			WriteFuture writeResult=session.write(data);  
			writeResult.addListener(new IoFutureListener() {  
				@Override
				public void operationComplete(IoFuture future) {
					// TODO Auto-generated method stub
					 WriteFuture wfuture=(WriteFuture)future;  
				        // д��ɹ�  
				        if(wfuture.isWritten())
				        {  
				            return;  
				        }  
				        else
				        {
				        	/*
				        	 * �������е������ʾ�������Ѿ��˳���һ���Ƿ������������˳���
				        	 */
				        	// д��ʧ�ܣ�˵�����ӶϿ����ر�session
				        	System.out.println("д��ʧ�ܣ��������߳��Ѿ��˳��ˣ�ò�Ʒ�����������");
				        	
							session.close(true);
				        
				      
				        }
				              
					
				}  
			}); 
	}
}
