package ks.mina.objectfilter;

import java.nio.charset.Charset;

import ks.mina.packetdata.PacketData;
import ks.mina.packetdata.PacketDataContent;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class PacketDecoder implements MessageDecoder {

	 private Charset charset;
	 
	    public PacketDecoder(Charset charset) {
	        this.charset = charset;
	    }
	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		// TODO Auto-generated method stub
		// ��ͷ����==8�������ʼ��4���ֽ�����Ϣ���ͣ����ŵ�4���ֽ����ֶ���ӵ����ݰ��ܳ���
		//���������Ҫ����������ʱ�Ļ���һ�ν��ղ��������ͻ��ν��գ��������
		if (in.remaining() < 8) {
		return MessageDecoderResult.NEED_DATA;
		}
		int tag = in.getInt();
        
 
        if (tag == PacketDataContent.STRINGDATA || tag==PacketDataContent.ATRINGANDPICTUREDATA) 
        {
            System.out.println("�����ʶ����"+tag);
            System.out.println("��ǰ������Ԥ���뺯���С�����");
            
        }else
        {
            System.out.println("δ֪��ʶ��...");
            return MessageDecoderResult.NOT_OK;
        }
        
        //���ݰ��ĸ�ʽ4 + 4 + 4 + 4 +string + bytes
       // ��ʵ���ݳ���
        int len = in.getInt();
        int remainLen = in.remaining(); 
        
        if (remainLen < len-8) //�Ѿ�������8���ֽ���
        {
        return MessageDecoderResult.NEED_DATA;
        }
        return MessageDecoderResult.OK;
 
        
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		PacketData pd=new PacketData();
		int tag = in.getInt(); // ��Ϣ��־
		int len = in.getInt(); // �ܵ���Ϣ����
		

		byte[] temp = new byte[len-8];//��Ϣʵ�� �ĳ��ȣ�ȥ��ǰ����int����ռ�Ĵ�С
		in.get(temp); // ������
		
		// ===============����������׼��======================
		IoBuffer buf = IoBuffer.allocate(400).setAutoExpand(true);
		buf.put(temp);
		buf.flip(); // Ϊ��ȡ����������������׼��
		
		 if (tag == PacketDataContent.STRINGDATA || tag==PacketDataContent.ATRINGANDPICTUREDATA) 
	        {
	            System.out.println("�����ʶ����"+tag);
	            System.out.println("�����У��Եȡ�����");
	            
	            pd.setDataType(tag);
	            pd.setSumDataLength(len);
	            
	            int strLength=buf.getInt();
	            pd.setStringLength(strLength);
	            int picSize=buf.getInt();
	            pd.setPictureSize(picSize);
	            
	            
	            pd.setMessage(buf.getString(strLength, charset.newDecoder()));
	             
	            
	            byte[] bmpByte = new byte[picSize];
	            buf.get(bmpByte, 0, picSize);
	            pd.setBmpByte(bmpByte);
	            out.write(pd);  
	            return MessageDecoderResult.OK;
	        }else
	        {
	            System.out.println("δ֪��ʶ��...");
	            out.write(null);
	            return MessageDecoderResult.NOT_OK;
	        }
		 

	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("������ϡ�����");
	}

}
