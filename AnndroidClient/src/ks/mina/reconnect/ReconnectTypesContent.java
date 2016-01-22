package ks.mina.reconnect;

public class ReconnectTypesContent 
{
	/**
	 * 使用下列值来判断具体的时候是否需要重新连接
	 * 
	 * 程序启动后，除了正常退出（接收到EXIT_CONNECT命令）时不进行重连接
	 * 其他的所有情况只要掉线了就重连接
	 */
	
	public static final int  CONNECT_YES=1;
	public static final int  CONNECT_NO=2;
	
	
	public static int connectTypes=CONNECT_YES;

}
