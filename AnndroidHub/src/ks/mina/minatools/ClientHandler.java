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
	
	
	//private  boolean isright=false;//用来判断是不是正确的连接上了，暗号对接上，表示成功连接，可以防止恶意连接

	//连接关闭的时候调用此函数
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		
		
		System.out.println("当前TCP客户端退出。。。");
		
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "当前客户端退出", OPT_ActivityView_static.INDEX_TEXTVIEW);
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "连接服务器",OPT_ActivityView_static.INDEX_BUTTONTEXT );
		Tcp_static.isTcpConnect=false;
		//判断是否需要重自动新连接服务器
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

	//连接创建的时候调用此函数
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	//连接创建成功的时候调用此函数
	@Override
	public void sessionOpened(final IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
		 //向服务器注册身份
          PacketData pd=PacketDataOperate.sendStrPacketData("THISISTERMINAL"+GlobalContent.separator+GlobalContent.HOMEID);
          writedata(session,pd);
		 
		 
		 
		 writedata(session, pd);
		
		super.sessionOpened(session);
	}

	//连接异常的时候调用此函数
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		/*
    	 * 程序运行到这里表示服务器已经退出，1.一般是服务器崩溃，退出了
    	 * 2.还有一种情况，客户端突然断网
    	 * 
    	 * 在这里提示用户服务器崩溃，或手机网络突然中断，是否重连。
    	 * 可以判断一下手机网络情况作出具体处理
    	 * 
    	 * 
    	 * 
    	 */
		
		cause.printStackTrace();
		
		
		System.out.println("进入异常函数");
		
		if(NetConnect.isNetworkConnected(Tcp_static.netContext))//手机网络正常，表示服务器程序退出
		{
			System.out.println("和服务器连接断开，貌似服务器给跪了。。。");
			//ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;//手机正常，需要重新连接
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "和服务器连接断开，貌似服务器给跪了。。。", OPT_ActivityView_static.INDEX_TEXTVIEW);
		}
		else//手机网络断开引起的连接中断
		{
			System.out.println("手机网络已断开，请重新连接网络。。。");
			
			//ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_NO;//手机没有网，不需要重新连接
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "手机网络已断开，请重新连接网络。。。。", OPT_ActivityView_static.INDEX_TEXTVIEW);
			
		}
		session.close(true);
	}



	//收到数据的时候调用此函数
	public void messageReceived(IoSession session, Object message)throws Exception 
	{
		
		if (message instanceof PacketData) {
            PacketData pd = (PacketData) message;
            System.out.println("客户器端 获取成功 :"+pd.toString() );
            //session.write("success rescive");
            String[] strmessages=pd.getMessage().split(GlobalContent.separator);
            if (strmessages[0].trim().equalsIgnoreCase(GlobalContent.LOGOUT)) {
				System.out.println("当前连接正常退出。。。");
				//正常退出不需要再连接服务器
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
            System.out.println("获取失败");
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
  }
	
	
	
	//读写空闲的时候调用此函数，本链接使用的是读超时，一段时间没有读到数，就调用此函数
	//z这个函数就是tcp连接建立时设置的
	//Tcp_static.cf.getSession().getConfig().setIdleTime(IdleStatus.READER_IDLE, Tcp_static.IDEL_TIME);
	////设置10秒没有接收进入idle状态，可以用来判断连接中断
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception 
	{
		System.out.println("IDLE " + session.getIdleCount(status));
		
		
		//每一个handler.sendMessage()都要定义一个新的Message，放到消息处理队列里等待处理
		//如果使用同一个Message如果原来的没被处理，又使用的话，整个程序崩溃
	
		OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "空闲时间-->"+(session.getIdleCount(status)*Tcp_static.IDEL_TIME+"秒"), OPT_ActivityView_static.INDEX_TEXTVIEW);
		if(session.getIdleCount(status)*Tcp_static.IDEL_TIME>=Tcp_static.IDEL_OUT)
		{
			/**************************************************/
			/*程序运行到这里说明网络连接中断，信号不好，一般需要重连*/
			/**/
			/*在这里提示用户，连接已断开，需要重新连接，登陆*/
			/**/
			/**************************************************/
			
			
			System.out.println("已经"+Tcp_static.IDEL_OUT+"秒没收到数据了，连接貌似断开了，马上关闭当前连接。。。");
			
			OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler,"已经"+Tcp_static.IDEL_OUT+"秒没收到数据了，连接貌似断开了，马上关闭当前连接。。。", OPT_ActivityView_static.INDEX_TEXTVIEW);
			session.close(true);
			
			
			
		}
	}
	
	/**
	 * session.write()是把数据写到缓冲区，不能保证写到输出通道
	 * 如果连接已经断开，开始可以写到缓冲区的，但是没法写到输出通道
	 * 使用本方法可以判断监听缓冲区的数据是否写到了输出通道，
	 * 可以用来判断和对方大连接是否中断，准确的说是判断对方是不是崩溃了，异常退出。
	 * 
	 * 如果写入失败，一定会触发异常 exceptionCaught()函数
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
				        	 * 程序运行到这里表示服务器已经退出，一般是服务器崩溃，退出了
				        	 */
				        	// 写入失败，说明连接断开，关闭session
				        	System.out.println("写入失败，服务器线程已经退出了，貌似服务器崩溃了");
				        	
							OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "写入失败，服务器线程已经退出，貌似服务器崩溃了。。。", OPT_ActivityView_static.INDEX_TEXTVIEW);
				        	session.close(true);
				        
				      
				        }
				              
					
				}  
			}); 
	}
}
