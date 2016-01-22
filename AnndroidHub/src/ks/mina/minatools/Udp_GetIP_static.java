package ks.mina.minatools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ks.mina.activitys.Main_Control;
import ks.mina.activitys.OPT_ActivityView_static;

public class Udp_GetIP_static {

	public static void udpGetIP()
	{

		// TODO Auto-generated method stub
		
		DatagramSocket dgSocket = null;
		try {
			dgSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  byte b[] = null;
	
			b = ("SEND+SERVER+IP").getBytes();
		
	
		  DatagramPacket dgPacket = null;
		  byte data [] = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
		try {
			dgPacket = new DatagramPacket(b,b.length,InetAddress.getByName("255.255.255.255"),9000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			dgSocket.send(dgPacket);
			dgSocket.setSoTimeout(2000);
			dgSocket.receive(packet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		String  message = new String(packet.getData(),packet.getOffset(),packet.getLength());
			if(message.equalsIgnoreCase("CON_OK"))
			{
				
				//packet.getAddress().toString()Ϊʲô�ַ�����ǰ�߻��и�"\"�أ�
				/*Message msg=new Message();
				msg.obj=packet.getAddress().toString()+"+"+Tcp_static.PORT;//packet.getPort();//
				msg.what=0;
				handler.sendMessage(msg);*/
				OPT_ActivityView_static.sendHandlerMessage(Main_Control.handler, packet.getAddress().toString()+"+"+Tcp_static.PORT, OPT_ActivityView_static.INDEX_UDPGETIP);
				System.out.println(packet.getSocketAddress().toString());
				System.out.println("��ȡ������IP��ַ�ɹ�");
				//���������̷߳������̵߳Ŀؼ�
		       // ipET.setText(packet.getAddress().toString())	;
		          //socketET.setText(packet.getPort());
				
			}
			
		  dgSocket.close();
		  System.out.println("IP��ַ��ȡ�߳��˳�������");
		
	
	}
}
