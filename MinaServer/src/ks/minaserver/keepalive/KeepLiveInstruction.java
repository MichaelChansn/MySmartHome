package ks.minaserver.keepalive;

public class KeepLiveInstruction {
	
	/*MINA本身提供了一个过滤器类: org.apache.mina.filter.keepalive . KeepAliveFilter ,该过滤器用于在IO空闲的时候发送并且反馈心跳包(keep-alive request/response)。 

	说到KeepAliveFilter这个类有必要先说一说其构造函数，即实例化该类需要些什么，该类构造函数中参数有三个分别是： 
	（1）KeepAvlieMessageFactory:   该实例引用用于判断接受与发送的包是否是心跳包，以及心跳请求包的实现 
	（2）IdleStatus:                              该过滤器所关注的空闲状态，默认认为读取空闲。 即当读取通道空闲的时候发送心跳包 
	（3）KeepAliveRequestTimeoutHandler： 心跳包请求后超时无反馈情况下的处理机制  默认为CLOSE  即关闭连接 

	首先需要实现接口   KeepAliveMessageFactory 。   该接口中的抽象方法有： 
	Modifier and Type	 Method and Description
	Object	getRequest(IoSession session)
	Returns a (new) keep-alive request message.
	Object	getResponse(IoSession session, Object request)
	Returns a (new) response message for the specified keep-alive request.
	boolean	isRequest(IoSession session, Object message)
	Returns true if and only if the specified message is a keep-alive request message.
	boolean	isResponse(IoSession session, Object message)
	Returns true if and only if the specified message is a keep-alive response message;

	一般来说心跳机制主要分为以下四类： 
	1， active 活跃型：  当读取通道空闲的时候发送心跳请求，一旦该心跳请求被发送，那么需要在keepAliveRequestTimeout时间内接收到心跳反馈，否则KeepAliveRequestTimeoutHandler将会被调用，当一个心跳请求包被接受到后，那么心跳反馈也会立即发出。 
	针对活跃型心跳机制：  KeepAliveMessageFactory 类的实现方法中： getRequest ( IoSession  session)与getResponse ( IoSession  session,  Object  request)必须返回非空。

	2， semi-active 半活跃型：当读取通道空闲的时候发送心跳请求，然而并不在乎心跳反馈有没有，当一个心跳请求包被接收到后，那么心跳反馈也会立即发出。 
	针对半活跃型心跳机制：   KeepAliveMessageFactory 类的实现方法中： getRequest ( IoSession  session)与getResponse ( IoSession  session,  Object  request)必须返回非空。并且心跳包请求后超时无反馈的处理机制设置为KeepAliveRequestTimeoutHandler.NOOP（不做任何处理）, KeepAliveRequestTimeoutHandler.LOG(只输出警告信息不做其他处理)

	3， passive 被动型：当前IO不希望主动发送心跳请求，但是当接受到一个心跳请求后，那么该心跳反馈也会立即发出。 
	针对被动型心跳机制：  KeepAliveMessageFactory 类的实现方法中： getRequest ( IoSession  session)必须反馈null 与 getResponse ( IoSession  session,  Object  request)必须反馈non-null. 

	4， deaf speaker 聋子型： 当前IO会主动发送心跳请求，但是不想发送任何心跳反馈。 
	 针对聋子型心跳机制：  KeepAliveMessageFactory 类的实现方法中： getRequest ( IoSession  session)必须反馈non-null与 getResponse ( IoSession  session,  Object  request)必须反馈null，将KeepAliveRequestTimeoutHandler 设置为DEAF_SPEAKER. 

	5， sient-listener 持续监听型：既不想发送心跳请求也不想发送心跳反馈。 
	针对持续监听型心跳机制：  KeepAliveMessageFactory 类的实现方法中： getRequest ( IoSession  session)必须反馈null 与 getResponse ( IoSession  session,  Object  request)必须反馈null.


	心跳包请求超时后的处理机制：接口 KeepAliveRequestTimeoutHandler ,一般该处理主要是针对能够发送心跳请求的心跳机制。
	1.CLOSE:关闭连接
	2，LOG：输出 警告信息
	3，NOOP：不做任何处理  
	4，EXCEPTION：抛出异常 
	5，DEAF_SPEAKER:一个特殊的处理，停止当前过滤器对对心跳反馈监听，因此让过滤器丢失请求超时的侦测功能。（让其变成聋子）  
	6，keepAliveRequestTimeout(KeepAliveFilter filter, IoSession session);   自定义处理 



	下面对客户端与服务端和分别举个例子： 
	服务器：
	以被动型心跳机制为例，服务器在接受到客户端连接以后被动接受心跳请求，当在规定时间内没有收到客户端心跳请求时 将客户端连接关闭 
	主要代码如下：  
	KeepAliveMessageFactoryImpl kamfi = new KeepAliveMessageFactoryImpl(); 
	        实现 方法： public boolean isRequest(IoSession session, Object message)：判断是否心跳请求包  是的话返回true 
	                             public boolean isResponse(IoSession session, Object message)：由于被动型心跳机制，没有请求当然也就不关注反馈 因此直接返回false
	                              public Object getRequest(IoSession session)： 被动型心跳机制无请求  因此直接返回null
	                              public Object getResponse(IoSession session, Object request) : 根据心跳请求request 反回一个心跳反馈消息 non-nul 
	        说明：  KeepAliveMessageFactoryImpl  为 KeepAliveMessageFactory的一个实现类，其中的实现方法满足被动型心跳机制。
	 
	KeepAliveFilter kaf = new KeepAliveFilter(kamfi, IdleStatus.BOTH_IDLE);
	       说明：实例化一个  KeepAliveFilter  过滤器，传入 KeepAliveMessageFactory引用，IdleStatus参数为 BOTH_IDLE,及表明如果当前连接的读写通道都空闲的时候在指定的时间间隔getRequestInterval后发送出发Idle事件。
	 
	kaf.setForwardEvent(true); //idle事件回发  当session进入idle状态的时候 依然调用handler中的idled方法
	      说明：尤其 注意该句话，使用了 KeepAliveFilter之后，IoHandlerAdapter中的 sessionIdle方法默认是不会再被调用的！ 所以必须加入这句话 sessionIdle才会被调用
	 
	kaf.setRequestInterval(heartPeriod);  //本服务器为被定型心跳  即需要每10秒接受一个心跳请求  否则该连接进入空闲状态 并且发出idled方法回调
	      说明：设置心跳包请求时间间隔，其实对于被动型的心跳机制来说，设置心跳包请求间隔貌似是没有用的，因为它是不会发送心跳包的，但是它会触发 sessionIdle事件， 我们利用该方法，可以来判断客户端是否在该时间间隔内没有发心跳包，一旦 sessionIdle方法被调用，则认为 客户端丢失连接并将其踢出 。因此其中参数 heartPeriod其实就是服务器对于客户端的IDLE监控时间。
	 
	//kaf.setRequestTimeout(5); //超时时间   如果当前发出一个心跳请求后需要反馈  若反馈超过此事件 默认则关闭连接
	acceptor.getFilterChain().addLast("heart", kaf); 
	     说明： 该过滤器加入到整个通信的过滤链中。




	客户端： 
	客户端会定时发送心跳请求（注意定时时间必须小于，服务器端的IDLE监控时间）,同时需要监听心跳反馈，以此来判断是否与服务器丢失连接。对于服务器的心跳请求不给与反馈。
	主要代码如下： 
	 ClientKeepAliveFactoryImpl ckafi = new ClientKeepAliveFactoryImpl(); 
	   实现 方法：     public boolean isRequest(IoSession session, Object message)： 服务器不会给客户端发送请求包，因此不关注请求包，直接返回false 
	                            public boolean isResponse(IoSession session, Object message)：客户端关注请求反馈，因此判断mesaage是否是反馈包
	                              public Object getRequest(IoSession session)： 获取心跳请求包 non-null
	                              public Object getResponse(IoSession session, Object request) : 服务器不会给客户端发送心跳请求，客户端当然也不用反馈  该方法返回null
	       说明：  ClientKeepAliveFactoryImpl  为 KeepAliveMessageFactory的一个实现类。
	  
	 KeepAliveFilter kaf = new KeepAliveFilter(ckafi, IdleStatus.READER_IDLE,KeepAliveRequestTimeoutHandler.CLOSE); 
	  说明：实例化一个  KeepAliveFilter  过滤器，传入 KeepAliveMessageFactory引用，IdleStatus参数为 READER_IDLE ,及表明如果当前连接的读通道空闲的时候在指定的时间间隔getRequestInterval后发送出心跳请求，以及发出Idle事件。 KeepAliveRequestTimeoutHandler设置为CLOS表明，当发出的心跳请求在规定时间内没有接受到反馈的时候则调用CLOSE方式 关闭连接 
	  
	  kaf.setForwardEvent(true); 
	  说明：继续调用 IoHandlerAdapter 中的 sessionIdle时间 

	  kaf.setRequestInterval(HEART_INTERVAL); 
	   说明：设置当连接的读取通道空闲的时候，心跳包请求时间间隔 

	  kaf.setRequestTimeout(HEART_TIMEOUT); 
	  说明：设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler.CLOSE 
	  
	  connector.getFilterChain().addLast("heart", kaf);  
	      说明： 该过滤器加入到整个通信的过滤链中。 
	zz:http://my.oschina.net/yjwxh/blog/174633
*/
}
