package ks.mina.packetdata;

import java.io.Serializable;


/**
 * 
 * @author ks
 * 注意：
 *     1.对象的每个值都必须有，绝对不能传送null
 *     2.对象格式，客户端和服务端必须一样，包名也必须一样
 *     3.对象格式说明：字节数：4 + 4 + 4 + 4 + string.length + picture.length
 *     结构体结构如下：
 *     一。消息头4+4=8个字节，包括消息类型和总体的消息长度，防止粘包和片段接收，用来接收指定大小的数据，保证稳定接收
 *     ①消息类型int型
 *     ②消息总长度int型
 *     二。消息体
 *     ③字符串长度int型
 *     ④图片长度int型
 *     ⑤字符串内容，一般是指令协议，自定义的
 *     ⑥图片数据，字节数组
 *     
 *     
 *
 */
public class PacketData implements Serializable{

	/**
	 * 串行化ID
	 */
	private static final long serialVersionUID = 3584822359096741188L;
	
	
	private int dataType;//消息类型，定义在PacketDataContent中
	private int sumdataLength;//数据包的总长度,包括消息类型和当前变量所占的8个字节长度
	private int stringLength;//字符串的长度
	private int pictureSize;//图片数据的大小
	private String message;//字符串内容
	private byte[] bmpByte;//图片的字节数组
	
	public PacketData()
	{
		dataType=PacketDataContent.STRINGDATA;//消息类型，定义在PacketDataContent中
		 sumdataLength=0;//数据包的总长度
		 stringLength=4;//字符串的长度
		 pictureSize=1;//图片数据的大小
		 message="init";//字符串内容
		 bmpByte=new byte[1];//图片的字节数组
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
