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
	                    	System.out.println("��ʼ��"+connectTimes+"�����ӡ�����");
	                    	if(connectTimes>CONNECTTIMES)
	                    	{
	                    		System.out.println("����"+CONNECTTIMES+"�����Ӷ����ɹ����鿴�����Ƿ�������ȷ��");
	                    		//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "���ӷ�����",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                    		break;
	                    		//return;
	                    	}
	                        Thread.sleep(3000);  
	                        
	                        if(Tcp_static.connectServer())//���������������������ǲ�����ģ������ӳɹ�֮��������ѭ����������γ�Ƕ��ִ����
	                        {
	                        	//���ӳɹ��Ļ�
	                        	//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "�Ͽ�������",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                        	break;
	                        }
	                        else
	                        {
	                        	//û�ɹ�����
	                        	//OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, "���ӷ�����",OPT_ActivityView_static.INDEX_BUTTONTEXT );
	                        }
	                        
	                    } catch (Exception ex) 
	                    {
	                    	
	                          
	                    }  
	                }  
				
			}
		}).start();
				
	}
	

}
