package ks.mina.minatools;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

import ks.mina.objectfilter.PacketDecoder;
import ks.mina.objectfilter.PacketEncoder;
import ks.mina.objectfilter.PacketProtocol;
import ks.mina.reconnect.ReconnectTypesContent;
import ks.minaserver.keepalive.KeepLiveFactory;
import ks.minaserver.keepalive.KeepLiveTimeOutHandler;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.content.res.Resources;

//������������TCP���ӵĸ������ݣ�������������ͬ���ֱ��ʹ��TCPͨ��
public class Tcp_static {

	//�������˿�
	public static  int PORT=8000;
	//�����Ʒ�������ַ
	public static  String ServerIP="42.96.205.175";
	//�������ӵķ�������ַ��
	public static InetSocketAddress ServerAddress;
	//�������ӵ�socket
	public static NioSocketConnector connector;
	//�������ӵ�socket����
	public static ConnectFuture cf;
	//���ö�ȡ����ʱ��Ϊ10��
	private static final int KEEPLIVEINTERVAL=35;//����������ʱ����40��
	private static final int KEEPLIVETIMEOUT=15;//������ʱ15��
	
	public static final int IDEL_TIME=60;//���ÿ���ʱ��Ϊ70��
	
	public static final int IDEL_OUT=60;//����idel��ʱʱ�䣬�����Զ��ر�����
	
	//����ɨ���ֻ�����״̬ʹ�õ������Ļ��������ݲ�ͬ��activity����ͬ��ֵ��������onresum���������︴�ƣ���֤context���ǵ�ǰactivity
	public static Context netContext;
	
   //����tcp����״̬��־λ��true��ʾtcp���ӳɹ���fasle��ʾ����ʧ��,�������������ж��������
	public static boolean isTcpConnect=false;
    
	
	 public static Resources res=null;
	
	
	
	//����֮ǰ�������������ԣ���������һ��
		public static void setupTcp_staticConnectorParam()
		{
			
			if(Tcp_static.connector==null)
			{
					
					// TODO Auto-generated method stub
					Tcp_static.connector = new NioSocketConnector(); 
					Tcp_static.connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
					
					Tcp_static.connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new PacketProtocol(new PacketDecoder(Charset.forName("utf-8")), new PacketEncoder(Charset.forName("utf-8")))));
					
					//�̳߳�
					ExecutorService filterExecutor = new OrderedThreadPoolExecutor();
					Tcp_static.connector.getFilterChain().addLast("threadPool",new ExecutorFilter(filterExecutor));
					
					
					         //�������������ֺ��ж����ӵ��ȶ���
							//����������û�����գ����յ��͸��ͻ��˷��أ���ʱʱ��û���վ�˵���ͻ��˵����ˣ��ر�session
							 /** ���ǵǳ� */

					        KeepAliveMessageFactory heartBeatFactory = new KeepLiveFactory();
					        KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepLiveTimeOutHandler();
					        
					        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
					        /** �Ƿ�ط� */
					        //idle�¼��ط�  ��session����idle״̬��ʱ�� ��Ȼ����handler�е�idled����
					        /*˵�������� ע��þ仰��ʹ���� KeepAliveFilter֮��IoHandlerAdapter�е� sessionIdle����Ĭ���ǲ����ٱ����õģ� ���Ա��������仰 sessionIdle�Żᱻ����*/
					        //heartBeat.setForwardEvent(true);
					        //����������Ҫ���к��������Բ��ڵ��ÿ��к���
					        heartBeat.setForwardEvent(false);
					        
					        
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
					        heartBeat.setRequestTimeout(KEEPLIVETIMEOUT); 
					        
					    
					      //��仰�Ǳȷ���������ϵģ�Ҫ��������keep�ź�
					        Tcp_static.connector.getSessionConfig().setKeepAlive(true);
					        
					        
					        Tcp_static.connector.getFilterChain().addLast("heartbeat", heartBeat);
					        
					        

					        /** *********************** */
							
							
					
					
					
					
					
					 //Tcp_static.connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue()))); //���ñ�������� 
					Tcp_static.connector.setConnectTimeoutMillis(5*1000); //5������ӳ�ʱʱ��
					Tcp_static.connector.setConnectTimeoutCheckInterval(3*1000);//ÿ3����һ��
					Tcp_static.connector.getSessionConfig().setReuseAddress(true);
					Tcp_static.connector.getSessionConfig().setWriteTimeout(30);
					Tcp_static.connector.getSessionConfig().setReceiveBufferSize(100*1024);
					Tcp_static.connector.getSessionConfig().setSendBufferSize(100*1024);
					Tcp_static.connector.getSessionConfig().setTcpNoDelay(true);
					Tcp_static.connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,Tcp_static.IDEL_TIME);
					Tcp_static.connector.setHandler(new ClientHandler());//�����¼������� 
					//Tcp_static.ServerAddress=new InetSocketAddress(Tcp_static.ServerIP, Tcp_static.PORT);//�������� 
					//Tcp_static.connector.setDefaultRemoteAddress(Tcp_static.ServerAddress);
					
			}
			else
			{
				System.out.println(" �Ѿ����ú����������ԣ�����Ҫ��������");
			}
					
		}
		
		
	
	
		//������������tcp�����ӵĸ������ԣ����ݳ�����Ҫ��ӣ�һ��Ϊstatic������ȫ�ֱ���
		
		public static boolean connectServer()
		{

			
			if(!Tcp_static.isTcpConnect)
			{
			
			//����������������Ҫ���������ӷ�����
			ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;
			Tcp_static.ServerAddress=new InetSocketAddress(Tcp_static.ServerIP, Tcp_static.PORT);//�������� 
			
			
			Tcp_static.cf = Tcp_static.connector.connect(Tcp_static.ServerAddress);
			
			
			Tcp_static.cf.awaitUninterruptibly();//�ȴ����Ӵ������ 
			
			//Tcp_static.cf.getSession().write("hello");//������Ϣ 
			if(Tcp_static.cf.isConnected())
			{
				
			
			     System.out.println("TCP���ӳɹ�������");
			     
			     //����tcp����״̬��־λ��true��ʾtcp���ӳɹ���fasle��ʾ����ʧ��
			     Tcp_static.isTcpConnect=true;
			     
			
			     
			   /**
			    * ��Ϊ���Ҫʹ�������������ԾͲ�����ʹ������������  
			    */
			/*
			//������������
			Tcp_static.cf.getSession().getCloseFuture().awaitUninterruptibly();//�ȴ����ӶϿ� 
			System.out.println("���е����ʾ��ǰ�����˳��ˡ�����");
			Tcp_static.isTcpConnect=false;
			//����connectorʹ�õ�������Դ������connecter�߳��˳�
			//connector��������һ���ڲ���Worker�������̣߳���������Щ���ӡ�
			//�����ǹر�ĳ��session֮��ֻ�ǹر���ĳ�����󣬹����߳���ʵ��û�б��رգ����Գ��ֳ���û��ֹͣ������
			//connector��dispose()�������÷���ͨ������ExecutorService��shutdown()����ֹͣҵ�����̣߳�
			//�������ڲ�disposed��־λ��ʶ��Ҫֹͣ���ӹ�������Worker�߳�ͨ���ñ�ʶֹͣ��
			Tcp_static.connector.dispose();//һ����connector����Դȫ���ͷŵ���connectorҲ�����null
			
			System.out.println("�ͻ���TCP�߳���ȫ�˳���������");*/
			return true;
			}
			else
			{
				System.out.println("û�����ӳɹ�������");
				System.out.println("�����������ߣ�û����������");
				Tcp_static.isTcpConnect=false;
				/*
				 * ��������ʾ�û��������������ߣ��������쳣������
				 * 
				 * ˵����
				 * 1.�ֻ�û�������Ļ���������ӷ������������ӳɹ����������ϾͶϿ���Ҳ���Ǿ�����session���������ӵĹ��̡�
				 * 
				 * 2.�ֻ������Ļ���������ӷ���������ֱ����ʾ���Ӳ��ɹ���ֱ�����Ӿܾ���
				 * 
				 * ����ֻҪ�ǳ������е��⣬��˵��������û����
				 */
				/**
				 * Ҫʹ���������ܣ����β����ͷ�connector����Դ
				 */
				//Tcp_static.connector.dispose();
				return false;
			}
			
			
			}
			else
			{
				System.out.println("�����Ѿ�����������Ҫ�ٴν�����");
				return false;
				
			}
		}

}
