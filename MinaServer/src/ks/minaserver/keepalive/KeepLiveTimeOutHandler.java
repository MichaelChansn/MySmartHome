package ks.minaserver.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;



/***

 * @Description: 当心跳超时时的处理，也可以用默认处理 这里like
 *               KeepAliveRequestTimeoutHandler.LOG的处理
 *               本程序服务器采用被动方式，
 *               服务器端用不到，直接在空闲函数处理即可
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
        System.out.println("《*服务器端*》心跳包发送超时处理(及长时间没有发送（接受）心跳包)");
        session.close(true);
    }

}
