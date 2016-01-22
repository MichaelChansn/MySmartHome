package ks.mina.minatools;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

import ks.mina.keepalive.KeepLiveFactory;
import ks.mina.keepalive.KeepLiveTimeOutHandler;
import ks.mina.objectfilter.PacketDecoder;
import ks.mina.objectfilter.PacketEncoder;
import ks.mina.objectfilter.PacketProtocol;
import ks.mina.packetdata.PacketData;
import ks.mina.reconnect.ReconnectTypesContent;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.util.Log;

//本类用来管理TCP连接的各种数据，以用于其他不同类的直接使用TCP通信
public class Tcp_static {
	
	

	private final static String TAG="Tcp_static";
	//服务器端口
	public static   int PORT=8000;
	//阿里云服务器地址
	public static  String ServerIP="192.168.253.1";//"42.96.205.175";"172.27.35.1";
	//保存连接的服务器地址键
	public static InetSocketAddress ServerAddress;
	//保存连接的socket
	public static NioSocketConnector connector=null;
	//保存连接的socket属性
	/**
	 * 注意，这个值必须在确认连接上了以后才能使用，否则程序崩溃
	 */
	public static ConnectFuture cf=null;
	public static IoSession session=null;


	private static final int KEEPLIVEINTERVAL=35;//心跳包发送时间间隔35秒
	private static final int KEEPLIVETIMEOUT=15;//心跳超时15秒
	
	public static final int IDEL_TIME=60;//设置空闲时间为60秒
	
	public static final int IDEL_OUT=60;//设置idel超时时间，用于自动关闭连接
	
	
   //设置tcp连接状态标志位，true表示tcp连接成功，fasle表示连接失败,用在其他类来判断连接情况
	public static boolean isTcpConnect=false;

	private static Tcp_static tcpInstance=null;
	
	public static Tcp_static getInstance()
	{
		if(tcpInstance==null)
			tcpInstance=new Tcp_static();
		return tcpInstance;
	}

	//连接之前先设置连接属性，智能设置一遍
	public static void setupTcp_staticConnectorParam()
	{
		
		if(Tcp_static.connector==null)
		{
				
				// TODO Auto-generated method stub
				Tcp_static.connector = new NioSocketConnector(); 
				Tcp_static.connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
				
				Tcp_static.connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new PacketProtocol(new PacketDecoder(Charset.forName("utf-8")), new PacketEncoder(Charset.forName("utf-8")))));
				
				//线程池
				ExecutorService filterExecutor = new OrderedThreadPoolExecutor();
				Tcp_static.connector.getFilterChain().addLast("threadPool",new ExecutorFilter(filterExecutor));
				
				
		         //设置心跳，保持和判断连接的稳定性
				//服务器采用没动接收，接收到就给客户端返回，超时时间没接收就说明客户端掉线了，关闭session
				 /** 主角登场 */
	
		        KeepAliveMessageFactory heartBeatFactory = new KeepLiveFactory();
		        KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepLiveTimeOutHandler();
		        
		        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
		        /** 是否回发 */
		        //idle事件回发  当session进入idle状态的时候 依然调用handler中的idled方法
		        /*说明：尤其 注意该句话，使用了 KeepAliveFilter之后，IoHandlerAdapter中的 sessionIdle方法默认是不会再被调用的！ 所以必须加入这句话 sessionIdle才会被调用*/
		        //heartBeat.setForwardEvent(true);
		        //本程序不在需要空闲函数，所以不在调用空闲函数
		        heartBeat.setForwardEvent(false);
		        
		        
		        /** 发送频率 */
		        /**
		         *   说明：设置心跳包请求时间间隔，其实对于被动型的心跳机制来说，
		         *   设置心跳包请求间隔貌似是没有用的，因为它是不会发送心跳包的，
		         *   但是它会触发 sessionIdle事件， 我们利用该方法，
		         *   可以来判断客户端是否在该时间间隔内没有发心跳包，
		         *   一旦 sessionIdle方法被调用，则认为 客户端丢失连接并将其踢出 。
		         *   因此其中参数 heartPeriod其实就是服务器对于客户端的IDLE监控时间。
		         */
		        
		        //KEEPLIVEINTERVAL时间内没接收信号就看是进入空闲函数
		        heartBeat.setRequestInterval(KEEPLIVEINTERVAL);
		        
		        //被动的不需要进入超时，因为本来就没有发送request，但是这里不使用空闲判断，还是使用timeout判断失联
		        heartBeat.setRequestTimeout(KEEPLIVETIMEOUT); 
		        
		    
		      //这句话是比服务器多加上的，要主动发送keep信号
		        Tcp_static.connector.getSessionConfig().setKeepAlive(true);
		        
		        
		        Tcp_static.connector.getFilterChain().addLast("heartbeat", heartBeat);
		        
		        
	
		        /** *********************** */
				
				
		
		
				
				
				
				 //Tcp_static.connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue()))); //设置编码过滤器 
				Tcp_static.connector.setConnectTimeoutMillis(5*1000); //5秒的连接超时时间
				Tcp_static.connector.setConnectTimeoutCheckInterval(3*1000);//每3秒检查一次
				Tcp_static.connector.getSessionConfig().setReuseAddress(true);
				Tcp_static.connector.getSessionConfig().setWriteTimeout(30);
				Tcp_static.connector.getSessionConfig().setReceiveBufferSize(100*1024);
				Tcp_static.connector.getSessionConfig().setSendBufferSize(100*1024);
				Tcp_static.connector.getSessionConfig().setTcpNoDelay(true);
				Tcp_static.connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,Tcp_static.IDEL_TIME);
				Tcp_static.connector.setHandler(new ClientHandler());//设置事件处理器 
				
				
		}
		else
		{
			Log.e(TAG,"已经设置好了连接属性，不需要再设置了");
		}
				
	}
	
	
	
	
	
	
	
	//以下用来设置tcp长连接的各种属性，根据程序需要添加，一般为static，用作全局变量
	
	public static boolean connectServer()
	{

		
		if(!Tcp_static.isTcpConnect)//没连接的话
		{
		
		//程序运行起来后需要重连接服务器
		ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;
		Tcp_static.ServerAddress=new InetSocketAddress(Tcp_static.ServerIP, Tcp_static.PORT);//建立连接 
		
		
		Log.e(TAG, ServerAddress.toString());
		Tcp_static.cf = Tcp_static.connector.connect(Tcp_static.ServerAddress);
		Tcp_static.cf.awaitUninterruptibly();//等待连接创建完成 
		
		//Tcp_static.cf.getSession().write("hello");//发送消息 
		if(Tcp_static.cf.isConnected())
		{
			
		
		     Log.d(TAG,"TCP连接成功。。。");
		     
		     //设置tcp连接状态标志位，true表示tcp连接成功，fasle表示连接失败
		     Tcp_static.isTcpConnect=true;
		     session=cf.getSession();
		
		     
		   /**
		    * 因为后边要使用重连功能所以就不能在使用阻塞函数了  
		    */
		/*
		//程序阻塞到这
		Tcp_static.cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
		System.out.println("运行到这表示当前连接退出了。。。");
		Tcp_static.isTcpConnect=false;
		//清理connector使用的所有资源，整个connecter线程退出
		//connector中启动着一个内部的Worker（工作线程）来管理这些连接。
		//当我们关闭某个session之后，只是关闭了某个请求，工作线程其实并没有被关闭，所以出现程序没有停止的现象。
		//connector的dispose()方法。该方法通过调用ExecutorService的shutdown()方法停止业务处理线程，
		//并设置内部disposed标志位标识需要停止连接管理器；Worker线程通过该标识停止。
		Tcp_static.connector.dispose();//一切与connector的资源全都释放掉，connector也变成了null
		
		System.out.println("客户端TCP线程完全退出！！！！");*/
		return true;
		}
		else
		{
			Log.e(TAG,"没有连接成功。。。");
			Log.e(TAG,"服务器不在线，没启动。。。");
			Tcp_static.isTcpConnect=false;
			/*
			 * 在这里提示用户，服务器不在线，服务器异常。。。
			 * 
			 * 说明：
			 * 1.手机没有联网的话，点击连接服务器，会连接成功，但是马上就断开，也就是经历了session建立再连接的过程。
			 * 
			 * 2.手机联网的话，点击连接服务器，会直接显示连接不成功。直接连接拒绝。
			 * 
			 * 所以只要是程序运行到这，就说明服务器没启动
			 */
			/**
			 * 要使用重连功能，绝对不能释放connector的资源
			 */
			//Tcp_static.connector.dispose();
			return false;
		}
		
		
		}
		else
		{
			Log.e(TAG,"连接已经建立，不需要再次建立了");
			return false;
			
		}
	}

	public static void sendMessage(PacketData pd)
	{
		if(session!=null && session.isConnected())
		{
		Tcp_static.cf.getSession().write(pd);
		}
	}
	

	
	
	public void cleanSocket()
	{
		
		
		if(session!=null && connector!=null && cf!=null)
		{
		Tcp_static.isTcpConnect=false;
		Tcp_static.cf.getSession().close(true);
		Tcp_static.connector.dispose();
		Tcp_static.cf=null;
		Tcp_static.connector=null;
		session=null;
		}
	}
	
	
}
