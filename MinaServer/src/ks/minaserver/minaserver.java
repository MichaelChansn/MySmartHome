package ks.minaserver;
import java.io.IOException;

import ks.mina.jdbc.database.JDBC_OPT;


/*
 * mina����˺Ϳͻ��˵������ж����������
 * 1.�ͻ��������˳�
 * ��������ܺô���ֻҪ���Ƴ�ǰ�����˳���Ϣ������˽��յ��˳���Ϣ���ر����Ӽ���
 * 2.�ͻ��˳���ͻȻ�Ƴ�
 * �����������˻��⵽�����ж��쳣
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		cause.printStackTrace();//�ڿ���̨����Ͽ�ԭ��
		System.out.println("�Ϳͻ��˶Ͽ����ӣ�ò�ƿͻ��˹��ˡ�����");
		session.close(false);
		
	}
 * ����������йر����Ӽ���
 * 3.���������źŲ�����������ݴ���ʧ�ܣ���ʱ����˼�ⲻ���ͻ��˵��ߣ�����ʹ��������ʽ�ж�
 * ��������Ƚ��鷳��һ����ÿͻ��˶�ʱ�������������������ݣ����������պ󷵻��������ݣ�
 * �ͻ������û���ܵ����ݾ�˵���Ͽ������ˣ���ʱ�ر����ӡ�ͬ�����˳�ʱ��û���յ�����Ҳ��Ϊ�Ͽ����ӣ�
 * �������ӹرմ���
 * 
 * 
 */
public class minaserver {
		public static void main(String[] args) throws IOException {
			
			//���������Զ���ȡip��ַ�������ھ�����������UDP�㲥��ʽ
			UDPIP.UDPIPServer();
			
			//����TCP�����
			if(TCPServer.createTCPServer())
			{
				System.out.println("TCP�����������ɹ�������");
				
				//��ʼ�����ݿ��ͷ��Ϣ
				//JDBC_OPT.initTableTitles();
			}
			else
			{
				System.out.println("TCP����������ʧ�ܣ���������ˡ�����");
				System.exit(0);
			}
			
		}
		
	

}
