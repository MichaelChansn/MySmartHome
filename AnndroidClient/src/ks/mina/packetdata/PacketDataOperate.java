package ks.mina.packetdata;

public class PacketDataOperate {
	/**
	 * 
	 * @param messages
	 * �ַ�����
	 * @param bitmapBytes
	 * ͼƬ����
	 * @return
	 * PacketData��һ��ʵ��
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
	 * �ַ�������
	 * @return
	 * PacketData��ʵ��
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
