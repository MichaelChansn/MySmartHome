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
		// 报头长度==8，其中最开始的4个字节是消息类型，接着的4个字节是手动添加的数据包总长度
		//这个函数，要是网络有延时的话，一次接收不完整，就会多次接收，多次运行
		if (in.remaining() < 8) {
		return MessageDecoderResult.NEED_DATA;
		}
		int tag = in.getInt();
        
 
        if (tag == PacketDataContent.STRINGDATA || tag==PacketDataContent.ATRINGANDPICTUREDATA) 
        {
            System.out.println("请求标识符："+tag);
            System.out.println("当前程序在预解码函数中。。。");
            
        }else
        {
            System.out.println("未知标识符...");
            return MessageDecoderResult.NOT_OK;
        }
        
        //数据包的格式4 + 4 + 4 + 4 +string + bytes
       // 真实数据长度
        int len = in.getInt();
        int remainLen = in.remaining(); 
        
        if (remainLen < len-8) //已经读出了8个字节了
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
		int tag = in.getInt(); // 消息标志
		int len = in.getInt(); // 总的消息长度
		

		byte[] temp = new byte[len-8];//消息实体 的长度，去掉前两个int型所占的大小
		in.get(temp); // 数据区
		
		// ===============解析数据做准备======================
		IoBuffer buf = IoBuffer.allocate(400).setAutoExpand(true);
		buf.put(temp);
		buf.flip(); // 为获取基本数据区长度做准备
		
		 if (tag == PacketDataContent.STRINGDATA || tag==PacketDataContent.ATRINGANDPICTUREDATA) 
	        {
	            System.out.println("请求标识符："+tag);
	            System.out.println("解码中，稍等。。。");
	            
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
	            System.out.println("未知标识符...");
	            out.write(null);
	            return MessageDecoderResult.NOT_OK;
	        }
		 

	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("解码完毕。。。");
	}

}
