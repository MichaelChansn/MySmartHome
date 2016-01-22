package ks.mina.packetdata;

import java.io.Serializable;


/**
 * 
 * @author ks
 * ע�⣺
 *     1.�����ÿ��ֵ�������У����Բ��ܴ���null
 *     2.�����ʽ���ͻ��˺ͷ���˱���һ��������Ҳ����һ��
 *     3.�����ʽ˵�����ֽ�����4 + 4 + 4 + 4 + string.length + picture.length
 *     �ṹ��ṹ���£�
 *     һ����Ϣͷ4+4=8���ֽڣ�������Ϣ���ͺ��������Ϣ���ȣ���ֹճ����Ƭ�ν��գ���������ָ����С�����ݣ���֤�ȶ�����
 *     ����Ϣ����int��
 *     ����Ϣ�ܳ���int��
 *     ������Ϣ��
 *     ���ַ�������int��
 *     ��ͼƬ����int��
 *     ���ַ������ݣ�һ����ָ��Э�飬�Զ����
 *     ��ͼƬ���ݣ��ֽ�����
 *     
 *     
 *
 */
public class PacketData implements Serializable{

	/**
	 * ���л�ID
	 */
	private static final long serialVersionUID = 3584822359096741188L;
	
	
	private int dataType;//��Ϣ���ͣ�������PacketDataContent��
	private int sumdataLength;//���ݰ����ܳ���,������Ϣ���ͺ͵�ǰ������ռ��8���ֽڳ���
	private int stringLength;//�ַ����ĳ���
	private int pictureSize;//ͼƬ���ݵĴ�С
	private String message;//�ַ�������
	private byte[] bmpByte;//ͼƬ���ֽ�����
	
	public PacketData()
	{
		dataType=PacketDataContent.STRINGDATA;//��Ϣ���ͣ�������PacketDataContent��
		 sumdataLength=0;//���ݰ����ܳ���
		 stringLength=4;//�ַ����ĳ���
		 pictureSize=1;//ͼƬ���ݵĴ�С
		 message="init";//�ַ�������
		 bmpByte=new byte[1];//ͼƬ���ֽ�����
	}
	public void setDataType(int dataType)
	{
		this.dataType=dataType;
	}
	public int getDataType()
	{
		return dataType;
	}
	
	
	public void setSumDataLength(int sumdataLength)
	{
		this.sumdataLength=sumdataLength;
	}
	public int getSumDataLength()
	{
		return sumdataLength;
	}
	
	
	public void setStringLength(int stringLength)
	{
		if(stringLength!=0)
		this.stringLength=stringLength;
	}
	public int getStringLength()
	{
		return stringLength;
	}
	
	
	public void setPictureSize(int pictureSize)
	{
		if(pictureSize!=0)
		this.pictureSize=pictureSize;
	}
	public int getPictureSize()
	{
		return pictureSize;
	}
	


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getBmpByte() {
		return bmpByte;
	}
	public void setBmpByte(byte[] bmpByte) {
		if(bmpByte!=null)
		this.bmpByte = bmpByte;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "The packet data is this:\n type="+this.dataType+" sumdatalenght="+this.sumdataLength+" stringLength="+this.stringLength+"\n"+ 
				"pictureSize="+this.pictureSize+" message="+this.message+" bmpSize="+this.bmpByte.length;
	}
	
}
