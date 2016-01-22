package ks.mina.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;



/***

 * @Description: 当心跳超时时的处理，也可以用默认处理 这里like
 *               KeepAliveRequestTimeoutHandler.LOG的处理
 * 
 */
 public class KeepLiveTimeOutHandler implements KeepAliveRequestTimeoutHandler
{

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler#
     * keepAliveRequestTimedOut
     * (org.apache.mina.filter.keepalive.KeepAliveFilter,
     * org.apache.mina.core.session.IoSession)
     */
    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception 
    {
        System.out.println("《*客户端*》心跳包发送超时处理(及长时间没有发送（接受）心跳包)");
        //ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;
        session.close(true);
        
        
       
        
    }

}
