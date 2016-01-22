package ks.minaserver.keepalive;

public class KeepLiveInstruction {
	
	/*MINA�����ṩ��һ����������: org.apache.mina.filter.keepalive . KeepAliveFilter ,�ù�����������IO���е�ʱ���Ͳ��ҷ���������(keep-alive request/response)�� 

	˵��KeepAliveFilter������б�Ҫ��˵һ˵�乹�캯������ʵ����������ҪЩʲô�����๹�캯���в����������ֱ��ǣ� 
	��1��KeepAvlieMessageFactory:   ��ʵ�����������жϽ����뷢�͵İ��Ƿ������������Լ������������ʵ�� 
	��2��IdleStatus:                              �ù���������ע�Ŀ���״̬��Ĭ����Ϊ��ȡ���С� ������ȡͨ�����е�ʱ���������� 
	��3��KeepAliveRequestTimeoutHandler�� �����������ʱ�޷�������µĴ������  Ĭ��ΪCLOSE  ���ر����� 

	������Ҫʵ�ֽӿ�   KeepAliveMessageFactory ��   �ýӿ��еĳ��󷽷��У� 
	Modifier and Type	 Method and Description
	Object	getRequest(IoSession session)
	Returns a (new) keep-alive request message.
	Object	getResponse(IoSession session, Object request)
	Returns a (new) response message for the specified keep-alive request.
	boolean	isRequest(IoSession session, Object message)
	Returns true if and only if the specified message is a keep-alive request message.
	boolean	isResponse(IoSession session, Object message)
	Returns true if and only if the specified message is a keep-alive response message;

	һ����˵����������Ҫ��Ϊ�������ࣺ 
	1�� active ��Ծ�ͣ�  ����ȡͨ�����е�ʱ������������һ�����������󱻷��ͣ���ô��Ҫ��keepAliveRequestTimeoutʱ���ڽ��յ���������������KeepAliveRequestTimeoutHandler���ᱻ���ã���һ����������������ܵ�����ô��������Ҳ������������ 
	��Ի�Ծ���������ƣ�  KeepAliveMessageFactory ���ʵ�ַ����У� getRequest ( IoSession  session)��getResponse ( IoSession  session,  Object  request)���뷵�طǿա�

	2�� semi-active ���Ծ�ͣ�����ȡͨ�����е�ʱ������������Ȼ�������ں�����������û�У���һ����������������յ�����ô��������Ҳ������������ 
	��԰��Ծ���������ƣ�   KeepAliveMessageFactory ���ʵ�ַ����У� getRequest ( IoSession  session)��getResponse ( IoSession  session,  Object  request)���뷵�طǿա����������������ʱ�޷����Ĵ����������ΪKeepAliveRequestTimeoutHandler.NOOP�������κδ���, KeepAliveRequestTimeoutHandler.LOG(ֻ���������Ϣ������������)

	3�� passive �����ͣ���ǰIO��ϣ�����������������󣬵��ǵ����ܵ�һ�������������ô����������Ҳ������������ 
	��Ա������������ƣ�  KeepAliveMessageFactory ���ʵ�ַ����У� getRequest ( IoSession  session)���뷴��null �� getResponse ( IoSession  session,  Object  request)���뷴��non-null. 

	4�� deaf speaker �����ͣ� ��ǰIO�����������������󣬵��ǲ��뷢���κ����������� 
	 ����������������ƣ�  KeepAliveMessageFactory ���ʵ�ַ����У� getRequest ( IoSession  session)���뷴��non-null�� getResponse ( IoSession  session,  Object  request)���뷴��null����KeepAliveRequestTimeoutHandler ����ΪDEAF_SPEAKER. 

	5�� sient-listener ���������ͣ��Ȳ��뷢����������Ҳ���뷢������������ 
	��Գ����������������ƣ�  KeepAliveMessageFactory ���ʵ�ַ����У� getRequest ( IoSession  session)���뷴��null �� getResponse ( IoSession  session,  Object  request)���뷴��null.


	����������ʱ��Ĵ�����ƣ��ӿ� KeepAliveRequestTimeoutHandler ,һ��ô�����Ҫ������ܹ���������������������ơ�
	1.CLOSE:�ر�����
	2��LOG����� ������Ϣ
	3��NOOP�������κδ���  
	4��EXCEPTION���׳��쳣 
	5��DEAF_SPEAKER:һ������Ĵ���ֹͣ��ǰ�������Զ�������������������ù�������ʧ����ʱ����⹦�ܡ������������ӣ�  
	6��keepAliveRequestTimeout(KeepAliveFilter filter, IoSession session);   �Զ��崦�� 



	����Կͻ��������˺ͷֱ�ٸ����ӣ� 
	��������
	�Ա�������������Ϊ�����������ڽ��ܵ��ͻ��������Ժ󱻶������������󣬵��ڹ涨ʱ����û���յ��ͻ�����������ʱ ���ͻ������ӹر� 
	��Ҫ�������£�  
	KeepAliveMessageFactoryImpl kamfi = new KeepAliveMessageFactoryImpl(); 
	        ʵ�� ������ public boolean isRequest(IoSession session, Object message)���ж��Ƿ����������  �ǵĻ�����true 
	                             public boolean isResponse(IoSession session, Object message)�����ڱ������������ƣ�û������ȻҲ�Ͳ���ע���� ���ֱ�ӷ���false
	                              public Object getRequest(IoSession session)�� ��������������������  ���ֱ�ӷ���null
	                              public Object getResponse(IoSession session, Object request) : ������������request ����һ������������Ϣ non-nul 
	        ˵����  KeepAliveMessageFactoryImpl  Ϊ KeepAliveMessageFactory��һ��ʵ���࣬���е�ʵ�ַ������㱻�����������ơ�
	 
	KeepAliveFilter kaf = new KeepAliveFilter(kamfi, IdleStatus.BOTH_IDLE);
	       ˵����ʵ����һ��  KeepAliveFilter  ������������ KeepAliveMessageFactory���ã�IdleStatus����Ϊ BOTH_IDLE,�����������ǰ���ӵĶ�дͨ�������е�ʱ����ָ����ʱ����getRequestInterval���ͳ���Idle�¼���
	 
	kaf.setForwardEvent(true); //idle�¼��ط�  ��session����idle״̬��ʱ�� ��Ȼ����handler�е�idled����
	      ˵�������� ע��þ仰��ʹ���� KeepAliveFilter֮��IoHandlerAdapter�е� sessionIdle����Ĭ���ǲ����ٱ����õģ� ���Ա��������仰 sessionIdle�Żᱻ����
	 
	kaf.setRequestInterval(heartPeriod);  //��������Ϊ����������  ����Ҫÿ10�����һ����������  ��������ӽ������״̬ ���ҷ���idled�����ص�
	      ˵������������������ʱ��������ʵ���ڱ����͵�����������˵������������������ò����û���õģ���Ϊ���ǲ��ᷢ���������ģ��������ᴥ�� sessionIdle�¼��� �������ø÷������������жϿͻ����Ƿ��ڸ�ʱ������û�з���������һ�� sessionIdle���������ã�����Ϊ �ͻ��˶�ʧ���Ӳ������߳� ��������в��� heartPeriod��ʵ���Ƿ��������ڿͻ��˵�IDLE���ʱ�䡣
	 
	//kaf.setRequestTimeout(5); //��ʱʱ��   �����ǰ����һ�������������Ҫ����  �������������¼� Ĭ����ر�����
	acceptor.getFilterChain().addLast("heart", kaf); 
	     ˵���� �ù��������뵽����ͨ�ŵĹ������С�




	�ͻ��ˣ� 
	�ͻ��˻ᶨʱ������������ע�ⶨʱʱ�����С�ڣ��������˵�IDLE���ʱ�䣩,ͬʱ��Ҫ���������������Դ����ж��Ƿ����������ʧ���ӡ����ڷ��������������󲻸��뷴����
	��Ҫ�������£� 
	 ClientKeepAliveFactoryImpl ckafi = new ClientKeepAliveFactoryImpl(); 
	   ʵ�� ������     public boolean isRequest(IoSession session, Object message)�� ������������ͻ��˷������������˲���ע�������ֱ�ӷ���false 
	                            public boolean isResponse(IoSession session, Object message)���ͻ��˹�ע������������ж�mesaage�Ƿ��Ƿ�����
	                              public Object getRequest(IoSession session)�� ��ȡ��������� non-null
	                              public Object getResponse(IoSession session, Object request) : ������������ͻ��˷����������󣬿ͻ��˵�ȻҲ���÷���  �÷�������null
	       ˵����  ClientKeepAliveFactoryImpl  Ϊ KeepAliveMessageFactory��һ��ʵ���ࡣ
	  
	 KeepAliveFilter kaf = new KeepAliveFilter(ckafi, IdleStatus.READER_IDLE,KeepAliveRequestTimeoutHandler.CLOSE); 
	  ˵����ʵ����һ��  KeepAliveFilter  ������������ KeepAliveMessageFactory���ã�IdleStatus����Ϊ READER_IDLE ,�����������ǰ���ӵĶ�ͨ�����е�ʱ����ָ����ʱ����getRequestInterval���ͳ����������Լ�����Idle�¼��� KeepAliveRequestTimeoutHandler����ΪCLOS�����������������������ڹ涨ʱ����û�н��ܵ�������ʱ�������CLOSE��ʽ �ر����� 
	  
	  kaf.setForwardEvent(true); 
	  ˵������������ IoHandlerAdapter �е� sessionIdleʱ�� 

	  kaf.setRequestInterval(HEART_INTERVAL); 
	   ˵�������õ����ӵĶ�ȡͨ�����е�ʱ������������ʱ���� 

	  kaf.setRequestTimeout(HEART_TIMEOUT); 
	  ˵������������������� �ȴ�������ʱʱ�䡣 ������ʱ��������KeepAliveRequestTimeoutHandler.CLOSE 
	  
	  connector.getFilterChain().addLast("heart", kaf);  
	      ˵���� �ù��������뵽����ͨ�ŵĹ������С� 
	zz:http://my.oschina.net/yjwxh/blog/174633
*/
}
