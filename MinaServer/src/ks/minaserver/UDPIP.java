package ks.minaserver;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public  class UDPIP {

	private static final int UDP_PORT = 9000;
	public UDPIP() {
		// TODO Auto-generated constructor stub
		
	}
	public static void UDPIPServer()
		{
			new Thread(new Runnable() {
						
						@Override
						public void run() {
							while(true)
							{
							DatagramSocket socket = null;
							try {
								 socket=new DatagramSocket(UDP_PORT);
								 //socket.setSoTimeout(1000);
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("当前端口已经被使用，UDP绑定失败。。。");
								break;
							}
							
							while(true)
							{
							// TODO Auto-generated method stub
								
								byte data [] = new byte[1024];
								DatagramPacket packet = new DatagramPacket(data,data.length);
							
								try {
									System.out.println("当前UDP服务程序已经启动，正在监听连接。。。");
									socket.receive(packet);//阻塞到这
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									//socket.close();
									System.out.println("udp接收失败。。。");
									break;
								}
						
							
						  String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
							if(message.equalsIgnoreCase("SEND+SERVER+IP"))
							{
								byte b[] = ("CON_OK").getBytes();
								
								
									DatagramPacket dgPacket = null;
									try {
										dgPacket = new DatagramPacket(b,b.length,packet.getSocketAddress());
									} catch (SocketException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										System.out.println("DatagramPacket错误。。。");
										//socket.close();
										break;
									}
								
								try {
									socket.send(dgPacket);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									System.out.println("udp发送失败。。。");
									//socket.close();
									break;
								}
								
							}
							
							
							}
							socket.close();
							
							System.out.println("当前UDP服务已经退出。。。");
						
							}
							
						  //return false;
							}
					
					}).start();
		}

}
