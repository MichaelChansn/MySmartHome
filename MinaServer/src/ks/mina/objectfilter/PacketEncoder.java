package ks.mina.objectfilter;

import java.nio.charset.Charset;

import ks.mina.packetdata.PacketData;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

public class PacketEncoder  implements MessageEncoder<PacketData>{

	 private Charset charset;
	 public PacketEncoder()
	 {
		 this.charset=Charset.forName("UTF-8");
	 }
	public PacketEncoder(Charset charset)
	{
		this.charset = charset;
		
	}
	@Override
	public void encode(IoSession session, PacketData message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		IoBuffer buf = IoBuffer.allocate(400).setAutoExpand(true);
		if(message instanceof PacketData)
		{
			PacketData pd=(PacketData)message;
			
			buf.putInt(pd.getDataType());
			buf.putInt(pd.getSumDataLength());
			
			buf.putInt(pd.getStringLength());
			buf.putInt(pd.getPictureSize());
			buf.putString(pd.getMessage(), charset.newEncoder());
			buf.put(pd.getBmpByte());
		}
		buf.flip();
		out.write(buf);
	}
	

	

}
