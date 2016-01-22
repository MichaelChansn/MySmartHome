package ks.minaserver;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;


import ks.mina.objectfilter.PacketDecoder;
import ks.mina.objectfilter.PacketEncoder;
import ks.mina.objectfilter.PacketProtocol;
import ks.minaserver.keepalive.KeepLiveFactory;
import ks.minaserver.keepalive.KeepLiveTimeOutHandler;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


//mina框架不支持中文，所以不要使用中文传送

//mina默认是60秒没有任何数据的接收或发送就自动断开连接
public class TCPServer {
	private static final int TCP_PORT = 8000;
	private static final int KEEPLIVEINTERVAL=50;//心跳包发送时间间隔50秒
	//private static final int KEEPLIVETIMEOUT=10;//心跳超时10秒,服务器端用不到
	public static final int IDEL_TIME=60;//设置读取空闲时间为60秒
	
	public static final int IDEL_OUT=60;//设置idel超时时间，用于自动关闭连接
	


	public TCPServer() {
		// TODO Auto-generated constructor stub
		//构造函数，暂时不使用。
	}
	public static boolean createTCPServer()
	{
		
		//首先，我们为服务端创建IoAcceptor，NioSocketAcceptor是基于NIO的服务端监听器
		IoAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		
		//接着，在Acceptor和IoHandler之间将设置一系列的Fliter
		//包括记录过滤器和编解码过滤器。其中TextLineCodecFactory是mina自带的文本解编码器
		//logger用来设置日志相关的处理
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		
		acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new PacketProtocol(new PacketDecoder(Charset.forName("utf-8")), new PacketEncoder(Charset.forName("utf-8")))));
		//线程池
		ExecutorService filterExecutor = new OrderedThreadPoolExecutor();
		acceptor.getFilterChain().addLast("threadPool",new ExecutorFilter(filterExecutor));
		
		
		
		/**
		 * 有一点一定要注意，如果设置了keepalive机制，只要是在60秒之内没有做任何处理，mina就会自动关闭连接
		 * 所以，所有的处理一定要在60秒内完成，而且一旦设置了keeplive超时时间，默认是不会再进入handler的空闲函数。
		 * 就算设置heartBeat.setForwardEvent(true);可以进入空闲函数，空闲函数的超时时间也就变成了keepalive的超时时间
		 */
		
		//设置心跳，保持和判断连接的稳定性
		//服务器采用没动接收，接收到就给客户端返回，超时时间没接收就说明客户端掉线了，关闭session
		 /** 主角登场 */

        KeepAliveMessageFactory heartBeatFactory = new KeepLiveFactory();
        KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepLiveTimeOutHandler();
        
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
        /** 是否回发 */
        //idle事件回发  当session进入idle状态的时候 依然调用handler中的idled方法
        /*说明：尤其 注意该句话，使用了 KeepAliveFilter之后，IoHandlerAdapter中的 sessionIdle方法默认是不会再被调用的！ 所以必须加入这句话 sessionIdle才会被调用*/
        heartBeat.setForwardEvent(true);
        //本程序在客户端不在需要空闲函数，所以不在调用空闲函数
        //heartBeat.setForwardEvent(false);
        
        
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
       //heartBeat.setRequestTimeout(KEEPLIVETIMEOUT); //服务器端用不到
        //((SocketSessionConfig) acceptor.getSessionConfig()).setKeepAlive(true);
         acceptor.getFilterChain().addLast("heartbeat", heartBeat);

        /** *********************** */
		
		
		
		
		
		
		
		
		
		
		
		//配置Buffer的缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(100*1024);
	
		
		
		((SocketSessionConfig) acceptor.getSessionConfig()).setReceiveBufferSize(100*1024);
		((SocketSessionConfig) acceptor.getSessionConfig()).setSendBufferSize(100*1024);
		//设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
		((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
		//设置地址重用，一般使用在TCP的P2P穿透上
		((NioSocketAcceptor)acceptor).setReuseAddress(true);		
		
		//设置多长时间既没有读也没有写数据，调用handler.sessionIdle()方法
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDEL_TIME);
		
		//30秒写超时时间
		acceptor.getSessionConfig().setWriteTimeout(30);
		
		//配置事务处理Handler，将请求转由TimeServerHandler处理。
		acceptor.setHandler(new ServerHandler());
		
		
		try {
			//绑定端口
			acceptor.bind(new InetSocketAddress(TCP_PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("TCP服务器绑定出现问题，端口可能被占用。。。");
			acceptor.dispose();
			return false;
			
		}
		
		System.out.println("TCP服务器已经启动。。。");
		return true;
	}
	
	

}
