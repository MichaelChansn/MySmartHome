package ks.minaserver;
import java.io.IOException;

import ks.mina.jdbc.database.JDBC_OPT;


/*
 * mina服务端和客户端的连接中断有三种情况
 * 1.客户端正常退出
 * 这种情况很好处理，只要在推出前发送退出信息，服务端接收到退出信息，关闭连接即可
 * 2.客户端程序突然推出
 * 这种情况服务端会检测到连接中断异常
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		cause.printStackTrace();//在控制台输出断开原因
		System.out.println("和客户端断开连接，貌似客户端跪了。。。");
		session.close(false);
		
	}
 * 在这个函数中关闭连接即可
 * 3.由于网络信号不好引起的数据传送失败，此时服务端检测不到客户端掉线，必须使用其他方式判断
 * 这种情况比较麻烦，一般采用客户端定时给服务器发送特殊数据，服务器接收后返回特殊数据，
 * 客户端如果没接受到数据就说明断开连接了，此时关闭连接。同理服务端长时间没有收到数据也认为断开连接，
 * 进行连接关闭处理
 * 
 * 
 */
public class minaserver {
		public static void main(String[] args) throws IOException {
			
			//以下用来自动获取ip地址，仅限于局域网，采用UDP广播形式
			UDPIP.UDPIPServer();
			
			//创建TCP服务端
			if(TCPServer.createTCPServer())
			{
				System.out.println("TCP服务器创建成功。。。");
				
				//初始化数据库表头信息
				//JDBC_OPT.initTableTitles();
			}
			else
			{
				System.out.println("TCP服务器创建失败，程序给跪了。。。");
				System.exit(0);
			}
			
		}
		
	

}
