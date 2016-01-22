package ks.mina.bitmapstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author ks
 * 高性能内存存储机制，采用读写锁构建
 * 允许同时进行多个读的操作，但只允许一个写的操作,高性能互斥访问操作
 *所有关于类的直接赋值都是引用，读这个类的时候一定要克隆这个类才行
 */
public class BitmapStore {

	
	// 初始化一个 ReadWriteLock
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

	
	private static byte[] bitmapByte=new byte[1];
	
	
	
	//为了程序的健壮性，不要使用null作为返回值
	public  static byte[] getBitmapByte()
	{
		 // 得到 readLock 并锁定
	    Lock readLock = lock.readLock();
	    readLock.lock();
	    try {
	    
	         // 做读的工作
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
	      readLock.unlock();//一定要保证锁的释放
	    }

		
		
		
	}
	public static  void setBitmapByte(byte[] bitmapBytes)
	{
		 // 得到 writeLock 并锁定
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
		   writeLock.unlock();//一定要保证锁的释放
		 }

	}

}
