package ks.mina.keepalive;

import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;



/***
 * 这里的执行顺序是  分两条顺序 
 * isResponse()---->getRequest();获取数据判断心跳事件（目的是判断是否触发心跳超时异常）
 * isRequest()----->getResponse(); 写回数据是心跳事件触发的数据（目的写回给服务器（客户端）心跳包）
 * 两条路线  没必要都实现行
 * 
 * 注意；服务器是被动接收，发回反馈
 * 
 * 客户端是主动，发送消息，接收回应
 */



/**
 * 严重注意：！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 * 
 * 心跳包的形式必须符合自己编写的filter数据编解码的形式才能通过，否则心跳包实现不了，不识别
 * @author asus
 *
 */
 public class KeepLiveFactory implements KeepAliveMessageFactory {

	 /** 心跳包内容 */
	    private  final String HEARTBEATREQUEST1 = "KLREQ";
	    private  final String HEARTBEATRESPONSE2 = "KLRES";
	 
	 
    /*
     * (non-Javadoc)
     * 
     *返回给对方的心跳包数据 
     */
    @Override
    public Object getRequest(IoSession session) {
    	System.out.println("在客户端getresquest函数中");
    	PacketData pd=PacketDataOperate.sendStrPacketData(HEARTBEATREQUEST1);
    	
    	return pd;//HEARTBEATREQUEST1;
    	/*return null;*/
    }

    /*
     * (non-Javadoc)
     *"接受到的回应包"，服务器一般不会有回应，直接返回null就行
     *客户端一定会受到服务器回应
     */
    @Override
    public Object getResponse(IoSession session, Object request) {
        
      /*  return HEARTBEATRESPONSE2;*/
    	System.out.println("在客户端getResponse函数中");
        return null;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * 判断是否是客户端发送来的的心跳包此判断影响 KeepAliveRequestTimeoutHandler实现类
     * 判断是否心跳包发送超时
     * 
     * 服务器是被动接收，只是用这个方法得到客户端发来的keeplive信息，然后调用getResponse（）发回反馈
     * 
     * 服务器不会主动发送信息，所以直接返回false就行了
     */
    @Override
    public boolean isRequest(IoSession session, Object message) 
    {
    	System.out.println("在客户端isRequest函数中");
       /* if(message.equals(HEARTBEATREQUEST1))
        {
            System.out.println("接收到客户端心数据包引发心跳事件，心跳数据包是==》》" + message);
            return true;
        }*/
    	
        return false;
    }

    /*
     * (non-Javadoc)
     * 
                判断发送信息是否是心跳数据包此判断影响 KeepAliveRequestTimeoutHandler实现类
     * 判断是否心跳包发送超时
     * 
     * 对服务器来讲，是被动的不会接收回应，直接返回false就行
     */
    @Override
    public boolean isResponse(IoSession session, Object message) {
    	
    	System.out.println("在客户端isResponse函数中");
    	PacketData pd=(PacketData)message;
        if(pd.getMessage().equals(HEARTBEATRESPONSE2))
        {
            System.out.println("受到服务器发送数据包中引发心跳事件: " + message);
            return true;
        }
        return false;
    }

}
