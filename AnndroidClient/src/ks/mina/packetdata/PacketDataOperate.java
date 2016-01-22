package ks.mina.packetdata;

public class PacketDataOperate {
	/**
	 * 
	 * @param messages
	 * 字符命令
	 * @param bitmapBytes
	 * 图片数据
	 * @return
	 * PacketData的一个实例
	 */
	public static PacketData sendStrAndPictPacketData(String messages,byte[] bitmapBytes)
	{
		PacketData pd=new PacketData();
		
		pd.setDataType(PacketDataContent.ATRINGANDPICTUREDATA);
		pd.setPictureSize(bitmapBytes.length);
		pd.setBmpByte(bitmapBytes);
		pd.setMessage(messages);
		pd.setStringLength(messages.length());
		pd.setSumDataLength(4+4+4+4+pd.getStringLength()+pd.getPictureSize());
		
		
		return pd;
		
	}
	/**
	 * 
	 * @param messages
	 * 字符串命令
	 * @return
	 * PacketData的实例
	 */
	public static PacketData sendStrPacketData(String messages)
	{
		PacketData pd=new PacketData();
		pd.setDataType(PacketDataContent.STRINGDATA);
		pd.setMessage(messages);
		pd.setStringLength(messages.length());
		pd.setSumDataLength(4+4+4+4+pd.getStringLength()+pd.getPictureSize());
		return pd;
	}

}
