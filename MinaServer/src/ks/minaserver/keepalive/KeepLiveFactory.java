package ks.minaserver.keepalive;

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
     *���ظ��ͻ��˵����������� return ���ؽ�����ǿͻ����յ�������������
     */
    @Override
    public Object getRequest(IoSession session) {
    	/*return HEARTBEATREQUEST1;*/
    	System.out.println("�ڷ�������getRequest������");
    	return null;
    }

    /*
     * (non-Javadoc)
     *"���ܵ��Ŀͻ��˵Ļ�Ӧ��"��һ�㲻���л�Ӧ��ֱ�ӷ���null����
     */
    @Override
    public Object getResponse(IoSession session, Object request) {
        
    	System.out.println("�ڷ�������getResponse������");
        PacketData pd=PacketDataOperate.sendStrPacketData(HEARTBEATRESPONSE2);
    	
    	return pd;//HEARTBEATRESPONSE2;
        //return HEARTBEATRESPONSE2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * �ж��Ƿ��ǿͻ��˷������ĵ����������ж�Ӱ�� KeepAliveRequestTimeoutHandlerʵ����
     * �ж��Ƿ����������ͳ�ʱ
     * 
     * �������Ǳ������գ�ֻ������������õ��ͻ��˷�����keeplive��Ϣ��Ȼ�����getResponse�������ط���
     */
    @Override
    public boolean isRequest(IoSession session, Object message) 
    {
    	System.out.println("�ڷ�������isRequest������");
    	PacketData pd=(PacketData)message;
        if(pd.getMessage().equals(HEARTBEATREQUEST1))
        {
            System.out.println("���յ��ͻ��������ݰ����������¼����������ݰ���==����" + message);
            return true;
        }
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
        /*if(message.equals(HEARTBEATRESPONSE2))
        {
            System.out.println("�������������ݰ������������¼�: " + message);
            return true;
        }*/
    	System.out.println("�ڷ�������isResponse������");
        return false;
    }

}
