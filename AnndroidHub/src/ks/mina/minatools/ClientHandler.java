package ks.mina.minatools;

import java.io.ByteArrayOutputStream;

import ks.mina.activitys.Camera_View;
import ks.mina.activitys.Main_Control;
import ks.mina.activitys.OPT_ActivityView_static;
import ks.mina.bitmapstore.BitmapStore;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataContent;
import ks.mina.packetdata.PacketDataOperate;
import ks.mina.reconnect.ReconnectServer;
import ks.mina.reconnect.ReconnectTypesContent;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import freescale.ks.remoteeye.ui.Activity_VideoStream;

import android.graphics.Bitmap;

public class ClientHandler extends IoHandlerAdapter {
	
	
	//private  boolean isright=false;//�����ж��ǲ�����ȷ���������ˣ����ŶԽ��ϣ���ʾ�ɹ����ӣ����Է�ֹ��������

	//���ӹرյ�ʱ����ô˺���
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		
		
		System.out.println("��ǰTCP�ͻ����˳�������");
		
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "��ǰ�ͻ����˳�", OPT_ActivityView_static.INDEX_TEXTVIEW);
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "���ӷ�����",OPT_ActivityView_static.INDEX_BUTTONTEXT );
		Tcp_static.isTcpConnect=false;
		//�ж��Ƿ���Ҫ���Զ������ӷ�����
				switch(ReconnectTypesContent.connectTypes)
				{
				case ReconnectTypesContent.CONNECT_YES:
					ReconnectServer.reconnectServer();
					break;
				case ReconnectTypesContent.CONNECT_NO:
					break;
				default:
					break;
				}
	}

	//���Ӵ�����ʱ����ô˺���
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	//���Ӵ����ɹ���ʱ����ô˺���
	@Override
	public void sessionOpened(final IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
		 //�������ע�����
          PacketData pd=PacketDataOperate.sendStrPacketData("THISISTERMINAL"+GlobalContent.separator+GlobalContent.HOMEID);
          writedata(session,pd);
		 
		 
		 
		 writedata(session, pd);
		
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
		
		
		System.out.println("�����쳣����");
		
		if(NetConnect.isNetworkConnected(Tcp_static.netContext))//�ֻ�������������ʾ�����������˳�
		{
			System.out.println("�ͷ��������ӶϿ���ò�Ʒ����������ˡ�����");
			//ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;//�ֻ���������Ҫ��������
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "�ͷ��������ӶϿ���ò�Ʒ����������ˡ�����", OPT_ActivityView_static.INDEX_TEXTVIEW);
		}
		else//�ֻ�����Ͽ�����������ж�
		{
			System.out.println("�ֻ������ѶϿ����������������硣����");
			
			//ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_NO;//�ֻ�û����������Ҫ��������
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "�ֻ������ѶϿ����������������硣������", OPT_ActivityView_static.INDEX_TEXTVIEW);
			
		}
		session.close(true);
	}



	//�յ����ݵ�ʱ����ô˺���
	public void messageReceived(IoSession session, Object message)throws Exception 
	{
		
		if (message instanceof PacketData) {
            PacketData pd = (PacketData) message;
            System.out.println("�ͻ����� ��ȡ�ɹ� :"+pd.toString() );
            //session.write("success rescive");
            String[] strmessages=pd.getMessage().split(GlobalContent.separator);
            if (strmessages[0].trim().equalsIgnoreCase(GlobalContent.LOGOUT)) {
				System.out.println("��ǰ���������˳�������");
				//�����˳�����Ҫ�����ӷ�����
				ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_NO;
				session.close(true);
				return;
			}
            else
            	if(strmessages[0].trim().equalsIgnoreCase("STARTCAMERA"))
            	{
            		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler,"STARTCAMERA", OPT_ActivityView_static.INDEX_JUMPTOCAMERA);
            		return;
            	}
            	else
            		if(strmessages[0].trim().equalsIgnoreCase("STOPCAMERA"))
            		{
            			OPT_ActivityView_static.sendHandlerMessage(Camera_View.handler, "STOPCAMERA", OPT_ActivityView_static.INDEX_JUMPTOCAMERA);
            			return;
            		}
            
            		else
            			if(strmessages[0].trim().equalsIgnoreCase(GlobalContent.GETVIDEOSTREAM))
            			{
            				OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "STARTSTREAM"+"<##>"+strmessages[1], OPT_ActivityView_static.INDEX_JUMPTOSTREAM);
                			return;
            			}
            			else
            				if(strmessages[0].trim().equalsIgnoreCase(GlobalContent.STREAMOUT))
            				{
            					OPT_ActivityView_static.sendHandlerMessage(Activity_VideoStream.mHandler, "STREAMOUT", OPT_ActivityView_static.INDEX_JUMPTOSTREAM);
                    			return;
            				}
            
            
            
            		else
            			if(strmessages[0].trim().equalsIgnoreCase(GlobalContent.COMMEND))
            			{
            				OPT_ActivityView_static.sendHandlerMessage(Camera_View.handler, strmessages[1].toString(), OPT_ActivityView_static.INDEX_COMMEND);
                			return;
            			}
            			else
            				if(strmessages[0].trim().equalsIgnoreCase(GlobalContent.COMMEND_BLUETOOTH))
            				{
            					OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, strmessages[1].toString(), OPT_ActivityView_static.INDEX_SENDMESSAGETOBLUETOOTH);
                    			return;
            					
            				}
        } else {
            System.out.println("��ȡʧ��");
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
	
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "����ʱ��-->"+(session.getIdleCount(status)*Tcp_static.IDEL_TIME+"��"), OPT_ActivityView_static.INDEX_TEXTVIEW);
		if(session.getIdleCount(status)*Tcp_static.IDEL_TIME>=Tcp_static.IDEL_OUT)
		{
			/**************************************************/
			/*�������е�����˵�����������жϣ��źŲ��ã�һ����Ҫ����*/
			/**/
			/*��������ʾ�û��������ѶϿ�����Ҫ�������ӣ���½*/
			/**/
			/**************************************************/
			
			
			System.out.println("�Ѿ�"+Tcp_static.IDEL_OUT+"��û�յ������ˣ�����ò�ƶϿ��ˣ����Ϲرյ�ǰ���ӡ�����");
			
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler,"�Ѿ�"+Tcp_static.IDEL_OUT+"��û�յ������ˣ�����ò�ƶϿ��ˣ����Ϲرյ�ǰ���ӡ�����", OPT_ActivityView_static.INDEX_TEXTVIEW);
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
				        	
							OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "д��ʧ�ܣ��������߳��Ѿ��˳���ò�Ʒ����������ˡ�����", OPT_ActivityView_static.INDEX_TEXTVIEW);
				        	session.close(true);
				        
				      
				        }
				              
					
				}  
			}); 
	}
}
