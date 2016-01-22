package ks.mina.reconnect;

import ks.mina.activitys.Main_Control;
import ks.mina.activitys.viewoperate.OPT_ActivityView_static;
import ks.mina.minatools.Tcp_static;

public class ReconnectServer {
	
	
	public static void reconnectServer()
	{
		new Thread(new Runnable() {
			
			int connectTimes=0;
			final int CONNECTTIMES=5;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 for (;;) {  
	                    try
	                    {  
	                    	connectTimes++;
	                    	System.out.println("开始第"+connectTimes+"次连接。。。");
	                    	if(connectTimes>CONNECTTIMES)
	                    	{
	                    		System.out.println("尝试"+CONNECTTIMES+"次连接都不成功，查看网络是否设置正确！");
	                    		//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "连接服务器",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                    		break;
	                    		//return;
	                    	}
	                        Thread.sleep(3000);  
	                        
	                        if(Tcp_static.connectServer())//这个函数是阻塞的这绝对是不允许的，在连接成功之后必须结束循环，否则就形成嵌套执行了
	                        {
	                        	//连接成功的话
	                        	//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "断开服务器",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                        	break;
	                        }
	                        else
	                        {
	                        	//没成功连接
	                        	//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "连接服务器",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                        }
	                        
	                    } catch (Exception ex) 
	                    {
	                    	
	                          
	                    }  
	                }  
				
			}
		}).start();
				
	}
	

}
