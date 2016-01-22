package ks.minaserver;
import java.util.ArrayList;
import java.util.Map;


import ks.mina.bitmapstore.BitmapStore;
import ks.mina.encipher.EncipherAndDecipher;
import ks.mina.globalcontent.GlobalContent;
import ks.mina.jdbc.database.DataBaseRW;
import ks.mina.jdbc.database.JDBC_OPT;
import ks.mina.jdbc.database.UserLogInJudge;
import ks.mina.jdbc.database.UserLogInResult;
import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;
import ks.mina.zipbytes.ZipAndUnzipBytes;
import ks.minaserver.connectsessions.ConnectSessions;

import org.apache.mina.core.future.IoFuture;	
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;




//��һ��û���ף�Ϊʲô���������е����Ӷ�ʹ���������������ã�������ÿ���߳��½�һ����
//�����̹߳������ݣ����鷳��һ���̸߳ı����ݣ��������ӵ�����Ҳ����
 class ServerHandler extends IoHandlerAdapter {
	 
	 private final String TAG="ServerHandler";
   private int picturetimes=0;//������һ��ʱ�������ݿ�洢һ��ͼƬ
   ArrayList<Map<String,String>> myresult=new ArrayList<Map<String,String>>();
   
   
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		cause.printStackTrace();//�ڿ���̨����Ͽ�ԭ��
		/*
    	 * �������е������ʾ�ͻ����Ѿ��˳���һ���ǿͻ��˱������˳���
    	 * ����һ�������������������ͻȻ������
    	 * 
    	 * ��������ʾ�û����ͻ����쳣�˳�����������������
    	 */
		System.out.println("�����쳣����,�����쳣������");
		if(NetConnect.isConnect())//�������������������˵���ǿͻ����˳���
		{
		System.out.println("�Ϳͻ��˶Ͽ����ӣ�ò�ƿͻ��˹��ˡ�����");
		}
		else
		{
			System.out.println("���ո�ȥ�������������ˡ�����");
		}
		ConnectSessions.delectUnuserSessions();
		//ConnectSessions.deleteCloseSocketInfo(session.getRemoteAddress().toString());
		//���ԣ��ڹرպ������Ƿ��ܹ���ý�Ҫ�Ͽ��ĻỰ��Ϣ
		//System.out.println("ɾ������������Ϣ�ǣ�"+session.getRemoteAddress().toString());
		session.close(true);
		
	}
	

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
		//ɾ�����رյ�����
		ConnectSessions.printLength();
		ConnectSessions.delectUnuserSessions();
		super.sessionClosed(session);
		System.out.println("��ǰ�����TCP�߳��˳�������");
		ConnectSessions.printLength();
		
	}


	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("���µ����ӡ�����");
		super.sessionOpened(session);
	}

/**
 * һ���ǽ��յ�һ�ξͷ���һ�Σ�������ʹ������ڽ��մ��������ָ��ʽ��ɢ���ͣ�
 * ����ÿ�ν���һ�ξͷ���3�Σ���ô���ڽ��ܵ��߳����Զ��ڷ��ͣ�����ɽ���ͨ����������
 * ����ͨ����3�����ϣ�Ҳ����˵���ջ�����ʱ�������ڷ���˶Ͽ����ͻ��˻���һ������Ҫ����
 * ����ǣ���ʱ�ܾòŽ�������ͷ��͵�����
 */

	public void messageReceived(IoSession session, Object message) throws Exception 
	{
		String str=null;
		if (message instanceof PacketData) {
            PacketData pd = (PacketData) message;
            System.out.println("�������� ��ȡ�ɹ� :"+pd.toString() );
            str=pd.getMessage();
            System.out.println(str);
            String[] stringMesages=str.split(GlobalContent.separator);
            
            if (stringMesages[0].trim().equalsIgnoreCase(GlobalContent.LOGIN))
            {
            	//��ѯ���ݿ���֤����
           
            	String[] strings=EncipherAndDecipher.decipher(stringMesages[1]).split(GlobalContent.separator);
            	
            	UserLogInResult logResult=UserLogInJudge.UserLogInJudgeFromDataBase(strings[0], strings[1], strings[2]);
            	
            	
            	
            	if(logResult.isLogInOK==true)//��½�ɹ�
            	{
            		
            		String IP=session.getRemoteAddress().toString();
            		//ע���û���Ϣ
            		ConnectSessions.addSessionInfo(IP, session);
            		ConnectSessions.adduserInfo(IP, strings[0]+":"+strings[1]);
            		System.out.println("�ͻ���ע��ɹ�");
            		PacketData pd2=PacketDataOperate.sendStrPacketData(GlobalContent.LOGIN_OK+"<##>OK");
					writedata(session,pd2);
					return;
            	}
            	else//��½ʧ�ܣ�����ʧ�ܵ�ԭ��
            	{
            		PacketData pd2=PacketDataOperate.sendStrPacketData(GlobalContent.LOGIN_FAIL+"<##>"+logResult.logInResult);
					writedata(session,pd2);
					
					PacketData pd3=PacketDataOperate.sendStrPacketData(GlobalContent.LOGOUT);
					writedata(session, pd3);
					session.close(false);
					return;
            		
            	}
            	
            
            }
            else
            if (stringMesages[0].trim().equalsIgnoreCase(GlobalContent.LOGOUT)) {
            	System.out.println("��ǰ���������˳�������");
				session.close(true);
				return;
			}
            
				else
					if(stringMesages[0].trim().equalsIgnoreCase("THISISTERMINAL"))//�ն���Ϣע��
					{
						String IP=session.getRemoteAddress().toString();
						ConnectSessions.addTerminalIP(stringMesages[1],IP);
						ConnectSessions.addSessionInfo(IP, session);
						System.out.println("�洢�ն���Ϣ��ϡ�����");
						ConnectSessions.isTerminalConnect=true;
						
						//System.out.println(ConnectSessions.getTerminalIP());
						return;
					}

						else
							if(stringMesages[0].trim().equalsIgnoreCase("GETPICTURE"))
						    {
								
					 
								//�ӻ�������ȡͼƬ
								 byte[] bitByte=BitmapStore.getBitmapByte();
								 PacketData pd3=null;
								 if(bitByte.length>1)//Ҫ���л���Ļ���ʹ�û���
								 {
								 pd3=PacketDataOperate.sendStrAndPictPacketData("this is your picture", bitByte);
								 //��ջ��棬Ϊ��ʡ�����������²�����
								 BitmapStore.setBitmapByte(new byte[1]);
								 }
								 
								 else
								 {
									 pd3=PacketDataOperate.sendStrAndPictPacketData("this is your picture", new byte[1]);
								 }
								 writedata(session,pd3);
								 return;
								
								
						    }
							else
								if(stringMesages[0].trim().equalsIgnoreCase("GIVEYOURPICTURE"))
							{
								picturetimes++;
								System.out.println(picturetimes);
								System.out.println("��ʼ�洢ͼƬ������");
								
								
								System.out.println("ͼƬ��С�ǣ�"+pd.getPictureSize());
								BitmapStore.setBitmapByte(pd.getBmpByte());
							
								return;
							}
								else
									if(stringMesages[0].trim().equalsIgnoreCase("STARTCAMERA"))
									{
										if(ConnectSessions.isTerminalConnect==true)
										{
										PacketData pd2=pd;
										writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd2);
										}
										else
										{
											System.out.println("�ն˻�û�����ӣ�����");
										}
										return;
									}
									else
										if(stringMesages[0].trim().equalsIgnoreCase("STOPCAMERA"))
										{
											if(ConnectSessions.isTerminalConnect==true)
											{
											PacketData pd3=pd;
											writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd3);
											}
											else
											{
												System.out.println("�ն˻�û�����ӣ�����");
											}
											return;
										}
										else
											if(stringMesages[0].trim().equalsIgnoreCase(GlobalContent.GETVIDEOSTREAM))
											{
												if(ConnectSessions.isTerminalConnect==true)
												{
													PacketData pd4=PacketDataOperate.sendStrPacketData(stringMesages[0]+GlobalContent.separator+session.getRemoteAddress().toString());
													writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd4);
													
												//String strAddress="rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
												//PacketData pd3=PacketDataOperate.sendStrPacketData(GlobalContent.YOURVIDEOSTREAM+GlobalContent.separator+strAddress);
												//writedata(session,pd3);
													return;
												}
												else
												{
													System.out.println("�ն˻�û�����ӣ�����");
												}
												return;
												
											}
											else
												if(stringMesages[0].trim().equalsIgnoreCase(GlobalContent.YOURVIDEOSTREAM))
												{
													String strAddress=stringMesages[2];//"rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
													PacketData pd3=PacketDataOperate.sendStrPacketData(GlobalContent.YOURVIDEOSTREAM+GlobalContent.separator+strAddress);
													writedata(ConnectSessions.getSessionByKeyIP(stringMesages[1]),pd3);
													return;
												}
												else
													if(stringMesages[0].trim().equalsIgnoreCase(GlobalContent.STREAMOUT))
													{
														if(ConnectSessions.isTerminalConnect==true)
														{
														PacketData pd3=pd;
														writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd3);
														}
														else
														{
															System.out.println("�ն˻�û�����ӣ�����");
														}
														return;
													}
													else
														if(stringMesages[0].trim().equalsIgnoreCase(GlobalContent.COMMEND_BLUETOOTH))
							            				{
							            					
															if(ConnectSessions.isTerminalConnect==true)
															{
															 PacketData pd3=pd;
															 writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd3);
															}
															else
															{
																System.out.println("�ն˻�û�����ӣ�����");
															}
															
															return;
							            					
										            	}
														
													else
														if(stringMesages[0].trim().equalsIgnoreCase(GlobalContent.COMMEND))
														{
															if(ConnectSessions.isTerminalConnect==true)
															{
															 PacketData pd3=pd;
															 writedata(ConnectSessions.getSessionByKeyIP(ConnectSessions.getTerminalIP("Micro")),pd3);
															}
															else
															{
																System.out.println("�ն˻�û�����ӣ�����");
															}
															
															return;
														}
													
									
						
					
            
            
            
            
            
            
            
            
            
            
				
             
        } else 
        {
            System.out.println("��ȡʧ��");
            return;
        }

	
	}
	
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("SERVER-->IDLE :" + (session.getIdleCount(status)*TCPServer.IDEL_TIME)+"��");
		//���6*10����û�н��ջ������ݾ���Ϊ�ͻ��˶Ͽ��ˣ��ر�����
		if(session.getIdleCount(status)*TCPServer.IDEL_TIME>=TCPServer.IDEL_OUT)
		{
			/**************************************************/
			/*�������е�����˵�����������жϣ��źŲ��ã�ͨ���ж�*/
			/*��ʾ�û��������Ѿ��жϣ���Ҫ�������ӣ���½*/
			/**************************************************/
		
		System.out.println("�������Ѿ�"+TCPServer.IDEL_OUT+"��û���յ��κ������ˣ��Ϳͻ���ò�ƶϿ��ˣ��رձ��η��񡣡���");
		session.close(true);
		}
	}
	
	
	/**
	 * session.write()�ǰ�����д�������������ܱ�֤д�����ͨ��
	 * ��������Ѿ��Ͽ�����ʼ����д���������ģ�����û��д�����ͨ��
	 * ʹ�ñ����������жϼ����������������Ƿ�д�������ͨ����
	 * ���������жϺͶԷ��������Ƿ��жϣ�׼ȷ��˵���ж϶Է��ǲ��Ǳ����ˣ��쳣�˳���
	 * 
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
				        	 * �������е������ʾ�ͻ����Ѿ��˳���һ���ǿͻ��˱������˳���
				        	 */
				        	// д��ʧ�ܣ�˵�����ӶϿ����ر�session
				        	System.out.println("д��ʧ�ܣ��ͻ����Ѿ��˳��ˣ�ò�ƿͻ����쳣�˳������������ˣ�����");
				        	session.close(true);
				        	
				        }
				              
					
				}  
			}); 
	}
	
	
}