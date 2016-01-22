package ks.mina.bitmapstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author ks
 * �������ڴ�洢���ƣ����ö�д������
 * ����ͬʱ���ж�����Ĳ�������ֻ����һ��д�Ĳ���,�����ܻ�����ʲ���
 *���й������ֱ�Ӹ�ֵ�������ã���������ʱ��һ��Ҫ��¡��������
 */
public class BitmapStore {

	
	// ��ʼ��һ�� ReadWriteLock
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

	
	private static byte[] bitmapByte=new byte[1];
	
	
	
	//Ϊ�˳���Ľ�׳�ԣ���Ҫʹ��null��Ϊ����ֵ
	public  static byte[] getBitmapByte()
	{
		 // �õ� readLock ������
	    Lock readLock = lock.readLock();
	    readLock.lock();
	    try {
	    
	         // �����Ĺ���
	    	if(bitmapByte!=null)
	    	{
	          byte[] retByte=bitmapByte;
	    	  return retByte;
	    	}
	    	else
	    	{
	    		byte[] retByte=new byte[1];
	    		return retByte;
	    	}
	      
	    } finally {
	      readLock.unlock();//һ��Ҫ��֤�����ͷ�
	    }

		
		
		
	}
	public static  void setBitmapByte(byte[] bitmapBytes)
	{
		 // �õ� writeLock ������
	    Lock writeLock = lock.writeLock();
	    writeLock.lock();

	    try
	    {
	    	if(bitmapBytes!=null)
	    	{
		      bitmapByte=bitmapBytes;
	    	}
	    
		
	    }
		 finally 
		 {
		   writeLock.unlock();//һ��Ҫ��֤�����ͷ�
		 }

	}

}
