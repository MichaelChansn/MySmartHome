package ks.mina.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;



/***

 * @Description: ��������ʱʱ�Ĵ���Ҳ������Ĭ�ϴ��� ����like
 *               KeepAliveRequestTimeoutHandler.LOG�Ĵ���
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
        System.out.println("��*�ͻ���*�����������ͳ�ʱ����(����ʱ��û�з��ͣ����ܣ�������)");
        //ReconnectTypesContent.connectTypes=ReconnectTypesContent.CONNECT_YES;
        session.close(true);
        
        
       
        
    }

}
