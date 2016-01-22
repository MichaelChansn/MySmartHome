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


//mina��ܲ�֧�����ģ����Բ�Ҫʹ�����Ĵ���

//minaĬ����60��û���κ����ݵĽ��ջ��;��Զ��Ͽ�����
public class TCPServer {
	private static final int TCP_PORT = 8000;
	private static final int KEEPLIVEINTERVAL=50;//����������ʱ����50��
	//private static final int KEEPLIVETIMEOUT=10;//������ʱ10��,���������ò���
	public static final int IDEL_TIME=60;//���ö�ȡ����ʱ��Ϊ60��
	
	public static final int IDEL_OUT=60;//����idel��ʱʱ�䣬�����Զ��ر�����
	


	public TCPServer() {
		// TODO Auto-generated constructor stub
		//���캯������ʱ��ʹ�á�
	}
	public static boolean createTCPServer()
	{
		
		//���ȣ�����Ϊ����˴���IoAcceptor��NioSocketAcceptor�ǻ���NIO�ķ���˼�����
		IoAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		
		//���ţ���Acceptor��IoHandler֮�佫����һϵ�е�Fliter
		//������¼�������ͱ���������������TextLineCodecFactory��mina�Դ����ı��������
		//logger����������־��صĴ���
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		
		acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new PacketProtocol(new PacketDecoder(Charset.forName("utf-8")), new PacketEncoder(Charset.forName("utf-8")))));
		//�̳߳�
		ExecutorService filterExecutor = new OrderedThreadPoolExecutor();
		acceptor.getFilterChain().addLast("threadPool",new ExecutorFilter(filterExecutor));
		
		
		
		/**
		 * ��һ��һ��Ҫע�⣬���������keepalive���ƣ�ֻҪ����60��֮��û�����κδ���mina�ͻ��Զ��ر�����
		 * ���ԣ����еĴ���һ��Ҫ��60������ɣ�����һ��������keeplive��ʱʱ�䣬Ĭ���ǲ����ٽ���handler�Ŀ��к�����
		 * ��������heartBeat.setForwardEvent(true);���Խ�����к��������к����ĳ�ʱʱ��Ҳ�ͱ����keepalive�ĳ�ʱʱ��
		 */
		
		//�������������ֺ��ж����ӵ��ȶ���
		//����������û�����գ����յ��͸��ͻ��˷��أ���ʱʱ��û���վ�˵���ͻ��˵����ˣ��ر�session
		 /** ���ǵǳ� */

        KeepAliveMessageFactory heartBeatFactory = new KeepLiveFactory();
        KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepLiveTimeOutHandler();
        
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
        /** �Ƿ�ط� */
        //idle�¼��ط�  ��session����idle״̬��ʱ�� ��Ȼ����handler�е�idled����
        /*˵�������� ע��þ仰��ʹ���� KeepAliveFilter֮��IoHandlerAdapter�е� sessionIdle����Ĭ���ǲ����ٱ����õģ� ���Ա��������仰 sessionIdle�Żᱻ����*/
        heartBeat.setForwardEvent(true);
        //�������ڿͻ��˲�����Ҫ���к��������Բ��ڵ��ÿ��к���
        //heartBeat.setForwardEvent(false);
        
        
        /** ����Ƶ�� */
        /**
         *   ˵������������������ʱ��������ʵ���ڱ����͵�����������˵��
         *   ����������������ò����û���õģ���Ϊ���ǲ��ᷢ���������ģ�
         *   �������ᴥ�� sessionIdle�¼��� �������ø÷�����
         *   �������жϿͻ����Ƿ��ڸ�ʱ������û�з���������
         *   һ�� sessionIdle���������ã�����Ϊ �ͻ��˶�ʧ���Ӳ������߳� ��
         *   ������в��� heartPeriod��ʵ���Ƿ��������ڿͻ��˵�IDLE���ʱ�䡣
         */
        
        //KEEPLIVEINTERVALʱ����û�����źžͿ��ǽ�����к���
        heartBeat.setRequestInterval(KEEPLIVEINTERVAL);
        
        //�����Ĳ���Ҫ���볬ʱ����Ϊ������û�з���request���������ﲻʹ�ÿ����жϣ�����ʹ��timeout�ж�ʧ��
       //heartBeat.setRequestTimeout(KEEPLIVETIMEOUT); //���������ò���
        //((SocketSessionConfig) acceptor.getSessionConfig()).setKeepAlive(true);
         acceptor.getFilterChain().addLast("heartbeat", heartBeat);

        /** *********************** */
		
		
		
		
		
		
		
		
		
		
		
		//����Buffer�Ļ�������С
		acceptor.getSessionConfig().setReadBufferSize(100*1024);
	
		
		
		((SocketSessionConfig) acceptor.getSessionConfig()).setReceiveBufferSize(100*1024);
		((SocketSessionConfig) acceptor.getSessionConfig()).setSendBufferSize(100*1024);
		//����Ϊ���ӳٷ��ͣ�Ϊtrue����װ�ɴ�����ͣ��յ��������Ϸ���
		((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
		//���õ�ַ���ã�һ��ʹ����TCP��P2P��͸��
		((NioSocketAcceptor)acceptor).setReuseAddress(true);		
		
		//���ö೤ʱ���û�ж�Ҳû��д���ݣ�����handler.sessionIdle()����
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDEL_TIME);
		
		//30��д��ʱʱ��
		acceptor.getSessionConfig().setWriteTimeout(30);
		
		//����������Handler��������ת��TimeServerHandler����
		acceptor.setHandler(new ServerHandler());
		
		
		try {
			//�󶨶˿�
			acceptor.bind(new InetSocketAddress(TCP_PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("TCP�������󶨳������⣬�˿ڿ��ܱ�ռ�á�����");
			acceptor.dispose();
			return false;
			
		}
		
		System.out.println("TCP�������Ѿ�����������");
		return true;
	}
	
	

}
