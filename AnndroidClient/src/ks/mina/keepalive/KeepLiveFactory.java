package ks.mina.keepalive;

import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataOperate;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;



/***
 * �����ִ��˳����  ������˳�� 
 * isResponse()---->getRequest();��ȡ�����ж������¼���Ŀ�����ж��Ƿ񴥷�������ʱ�쳣��
 * isRequest()----->getResponse(); д�������������¼����������ݣ�Ŀ��д�ظ����������ͻ��ˣ���������
 * ����·��  û��Ҫ��ʵ����
 * 
 * ע�⣻�������Ǳ������գ����ط���
 * 
 * �ͻ�����������������Ϣ�����ջ�Ӧ
 */



/**
 * ����ע�⣺����������������������������������������������������������������������
 * ������������������������������������������������������������������������������
 * ��������������������������������������������������������������������������������
 * 
 * ����������ʽ��������Լ���д��filter���ݱ�������ʽ����ͨ��������������ʵ�ֲ��ˣ���ʶ��
 * @author asus
 *
 */
 public class KeepLiveFactory implements KeepAliveMessageFactory {

	 /** ���������� */
	    private  final String HEARTBEATREQUEST1 = "KLREQ";
	    private  final String HEARTBEATRESPONSE2 = "KLRES";
	 
	 
    /*
     * (non-Javadoc)
     * 
     *���ظ��Է������������� 
     */
    @Override
    public Object getRequest(IoSession session) {
    	System.out.println("�ڿͻ���getresquest������");
    	PacketData pd=PacketDataOperate.sendStrPacketData(HEARTBEATREQUEST1);
    	
    	return pd;//HEARTBEATREQUEST1;
    	/*return null;*/
    }

    /*
     * (non-Javadoc)
     *"���ܵ��Ļ�Ӧ��"��������һ�㲻���л�Ӧ��ֱ�ӷ���null����
     *�ͻ���һ�����ܵ���������Ӧ
     */
    @Override
    public Object getResponse(IoSession session, Object request) {
        
      /*  return HEARTBEATRESPONSE2;*/
    	System.out.println("�ڿͻ���getResponse������");
        return null;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * �ж��Ƿ��ǿͻ��˷������ĵ����������ж�Ӱ�� KeepAliveRequestTimeoutHandlerʵ����
     * �ж��Ƿ����������ͳ�ʱ
     * 
     * �������Ǳ������գ�ֻ������������õ��ͻ��˷�����keeplive��Ϣ��Ȼ�����getResponse�������ط���
     * 
     * ��������������������Ϣ������ֱ�ӷ���false������
     */
    @Override
    public boolean isRequest(IoSession session, Object message) 
    {
    	System.out.println("�ڿͻ���isRequest������");
       /* if(message.equals(HEARTBEATREQUEST1))
        {
            System.out.println("���յ��ͻ��������ݰ����������¼����������ݰ���==����" + message);
            return true;
        }*/
    	
        return false;
    }

    /*
     * (non-Javadoc)
     * 
                �жϷ�����Ϣ�Ƿ����������ݰ����ж�Ӱ�� KeepAliveRequestTimeoutHandlerʵ����
     * �ж��Ƿ����������ͳ�ʱ
     * 
     * �Է������������Ǳ����Ĳ�����ջ�Ӧ��ֱ�ӷ���false����
     */
    @Override
    public boolean isResponse(IoSession session, Object message) {
    	
    	System.out.println("�ڿͻ���isResponse������");
    	PacketData pd=(PacketData)message;
        if(pd.getMessage().equals(HEARTBEATRESPONSE2))
        {
            System.out.println("�ܵ��������������ݰ������������¼�: " + message);
            return true;
        }
        return false;
    }

}
