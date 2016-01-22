package ks.mina.objectfilter;

import ks.mina.packetdata.PacketData;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

public class PacketProtocol extends DemuxingProtocolCodecFactory{


	//private static final Charset charset=Charset.forName("UTF-8");
    private MessageDecoder decoder; 
	private MessageEncoder<PacketData> encoder;
	

	 public PacketProtocol(MessageDecoder decoder,MessageEncoder<PacketData> encoder) 
	 {
	        this.decoder = decoder;
	        this.encoder = encoder;
	        addMessageDecoder(this.decoder);
	        addMessageEncoder(PacketData.class, this.encoder);
	 }
	


}























