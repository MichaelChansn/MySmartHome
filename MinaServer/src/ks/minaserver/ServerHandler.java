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




//有一点没明白，为什么服务器所有的连接都使用这个处理类的引用，而不是每个线程新建一个类
//所有线程共享数据，很麻烦，一个线程改变数据，其他连接的数据也变了
 class ServerHandler extends IoHandlerAdapter {
	 
	 private final String TAG="ServerHandler";
   private int picturetimes=0;//用来过一定时间向数据库存储一张图片
   ArrayList<Map<String,String>> myresult=new ArrayList<Map<String,String>>();
   
   
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		cause.printStackTrace();//在控制台输出断开原因
		/*
    	 * 程序运行到这里表示客户端已经退出，一般是客户端崩溃，退出了
    	 * 还有一种特殊情况，服务器端突然断网。
    	 * 
    	 * 在这里提示用户，客户端异常退出。。。。。。。。
    	 */
		System.out.println("进入异常函数,连接异常。。。");
		if(NetConnect.isConnect())//如果服务器联网正常，说明是客户端退出了
		{
		System.out.println("和客户端断开连接，貌似客户端跪了。。。");
		}
		else
		{
			System.out.println("我勒个去，服务器断网了。。。");
		}
		ConnectSessions.delectUnuserSessions();
		//ConnectSessions.deleteCloseSocketInfo(session.getRemoteAddress().toString());
		//测试，在关闭函数中是否能够获得将要断开的会话信息
		//System.out.println("删除掉的连接信息是："+session.getRemoteAddress().toString());
		session.close(true);
		
	}
	

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
		//删除掉关闭的连接
		ConnectSessions.printLength();
		ConnectSessions.delectUnuserSessions();
		super.sessionClosed(session);
		System.out.println("当前服务端TCP线程退出。。。");
		ConnectSessions.printLength();
		
	}


	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("有新的连接。。。");
		super.sessionOpened(session);
	}

/**
 * 一般是接收到一次就发送一次，如果发送次数多于接收次数会造成指数式发散传送，
 * 比如每次接收一次就发送3次，那么用于接受的线程明显多于发送，会造成接收通道的数据是
 * 发送通道的3倍以上，也就是说接收会有延时，以至于服务端断开，客户端还有一批数据要接受
 * 结果是，延时很久才接受完早就发送的数据
 */

	public void messageReceived(IoSession session, Object message) throws Exception 
	{
		String str=null;
		if (message instanceof PacketData) {
            PacketData pd = (PacketData) message;
            System.out.println("服务器端 获取成功 :"+pd.toString() );
            str=pd.getMessage();
            System.out.println(str);
            String[] stringMesages=str.split(GlobalContent.separator);
            
            if (stringMesages[0].trim().equalsIgnoreCase(GlobalContent.LOGIN))
            {
            	//查询数据库验证密码
           
            	String[] strings=EncipherAndDecipher.decipher(stringMesages[1]).split(GlobalContent.separator);
            	
            	UserLogInResult logResult=UserLogInJudge.UserLogInJudgeFromDataBase(strings[0], strings[1], strings[2]);
            	
            	
            	
            	if(logResult.isLogInOK==true)//登陆成功
            	{
            		
            		String IP=session.getRemoteAddress().toString();
            		//注册用户信息
            		ConnectSessions.addSessionInfo(IP, session);
            		ConnectSessions.adduserInfo(IP, strings[0]+":"+strings[1]);
            		System.out.println("客户端注册成功");
            		PacketData pd2=PacketDataOperate.sendStrPacketData(GlobalContent.LOGIN_OK+"<##>OK");
					writedata(session,pd2);
					return;
            	}
            	else//登陆失败，返回失败的原因
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
            	System.out.println("当前连接正常退出。。。");
				session.close(true);
				return;
			}
            
				else
					if(stringMesages[0].trim().equalsIgnoreCase("THISISTERMINAL"))//终端信息注册
					{
						String IP=session.getRemoteAddress().toString();
						ConnectSessions.addTerminalIP(stringMesages[1],IP);
						ConnectSessions.addSessionInfo(IP, session);
						System.out.println("存储终端信息完毕。。。");
						ConnectSessions.isTerminalConnect=true;
						
						//System.out.println(ConnectSessions.getTerminalIP());
						return;
					}

						else
							if(stringMesages[0].trim().equalsIgnoreCase("GETPICTURE"))
						    {
								
					 
								//从缓存里提取图片
								 byte[] bitByte=BitmapStore.getBitmapByte();
								 PacketData pd3=null;
								 if(bitByte.length>1)//要是有缓存的话就使用缓存
								 {
								 pd3=PacketDataOperate.sendStrAndPictPacketData("this is your picture", bitByte);
								 //清空缓存，为了省流量，不更新不发送
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
								System.out.println("开始存储图片。。。");
								
								
								System.out.println("图片大小是："+pd.getPictureSize());
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
											System.out.println("终端还没有连接！！！");
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
												System.out.println("终端还没有连接！！！");
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
													System.out.println("终端还没有连接！！！");
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
															System.out.println("终端还没有连接！！！");
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
																System.out.println("终端还没有连接！！！");
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
																System.out.println("终端还没有连接！！！");
															}
															
															return;
														}
													
									
						
					
            
            
            
            
            
            
            
            
            
            
				
             
        } else 
        {
            System.out.println("获取失败");
            return;
        }

	
	}
	
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("SERVER-->IDLE :" + (session.getIdleCount(status)*TCPServer.IDEL_TIME)+"秒");
		//如果6*10秒内没有接收或发送数据就认为客户端断开了，关闭连接
		if(session.getIdleCount(status)*TCPServer.IDEL_TIME>=TCPServer.IDEL_OUT)
		{
			/**************************************************/
			/*程序运行到这里说明网络连接中断，信号不好，通信中断*/
			/*提示用户，连接已经中断，需要重新连接，登陆*/
			/**************************************************/
		
		System.out.println("服务器已经"+TCPServer.IDEL_OUT+"秒没有收到任何数据了，和客户端貌似断开了，关闭本次服务。。。");
		session.close(true);
		}
	}
	
	
	/**
	 * session.write()是把数据写到缓冲区，不能保证写到输出通道
	 * 如果连接已经断开，开始可以写到缓冲区的，但是没法写到输出通道
	 * 使用本方法可以判断监听缓冲区的数据是否写到了输出通道，
	 * 可以用来判断和对方大连接是否中断，准确的说是判断对方是不是崩溃了，异常退出。
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
				        // 写入成功  
				        if(wfuture.isWritten())
				        {  
				            return;  
				        }  
				        else
				        {
				        	/*
				        	 * 程序运行到这里表示客户端已经退出，一般是客户端崩溃，退出了
				        	 */
				        	// 写入失败，说明连接断开，关闭session
				        	System.out.println("写入失败，客户端已经退出了，貌似客户端异常退出。。。崩溃了？？？");
				        	session.close(true);
				        	
				        }
				              
					
				}  
			}); 
	}
	
	
}