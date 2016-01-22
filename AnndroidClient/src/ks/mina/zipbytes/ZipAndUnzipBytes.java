package ks.mina.zipbytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipAndUnzipBytes {
	
	/**
     * Answer a byte array compressed in the Zip format from bytes.
     * 
     * @param bytes
     *            a byte array
     * @param aName
     *            a String the represents a file name
     * @return byte[] compressed bytes
     * @throws IOException
     */
    public static byte[] zipBytes(byte[] bytes) throws IOException {
    	
    	if(bytes.length>1)
    	{
        /*这个是采用java的文件zip压缩，自动添加一些而外信息，效果不是很好
         * ByteArrayOutputStream tempOStream = null;
        BufferedOutputStream tempBOStream = null;
        ZipOutputStream tempZStream = null;
        
        
        ZipEntry tempEntry = null;
        byte[] tempBytes = null;

        tempOStream = new ByteArrayOutputStream(bytes.length);
        tempBOStream = new BufferedOutputStream(tempOStream);
        tempZStream = new ZipOutputStream(tempBOStream);
        
        
        tempEntry = new ZipEntry(String.valueOf(bytes.length));
        tempEntry.setMethod(ZipEntry.DEFLATED);
        tempEntry.setSize((long) bytes.length);
        
        tempZStream.setLevel(9);
        tempZStream.putNextEntry(tempEntry);
        tempZStream.write(bytes, 0, bytes.length);
        tempZStream.flush();
       
        
        
        tempBOStream.flush();
        tempOStream.flush();
        tempZStream.close();
        
        
        tempBytes = tempOStream.toByteArray();
        tempOStream.close();
        tempBOStream.close();
        return tempBytes;*/
    		
    		
		  Deflater compressor = new Deflater();
		  compressor.setLevel(Deflater.BEST_COMPRESSION);
		  compressor.setInput(bytes);
		  compressor.finish();
		  byte[] buf = new byte[1024*10];
		  ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
		  
		  while (!compressor.finished()) 
		  {
		   int count = compressor.deflate(buf);
		    bos.write(buf, 0, count);
		  }
		 
	
		  
		  byte[] retbyte=bos.toByteArray();
		  
		  bos.close();

		  return retbyte;
    		
    		
    		
    		
    	}
    	else
    	{
    		 return null;
    	}
    }


    /**
     * Answer a byte array that has been decompressed from the Zip format.
     * 
     * @param bytes
     *            a byte array of compressed bytes
     * @return byte[] uncompressed bytes
     * @throws IOException
     */
    public static byte[] unzipBytes(byte[] bytes) throws IOException {
    	if(bytes.length>1)
    	{
       /*这个是采用java的文件zip压缩，自动添加一些而外信息，效果不是很好
        *  ByteArrayInputStream tempIStream = null;
        BufferedInputStream tempBIStream = null;
        
        ZipInputStream tempZIStream = null;
        ZipEntry tempEntry = null;
        
        
        long tempDecompressedSize = -1;
        byte[] tempUncompressedBuf = null;

        byte[] retbyte = null;
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        tempIStream = new ByteArrayInputStream(bytes, 0, bytes.length);
        tempBIStream = new BufferedInputStream(tempIStream);
        
        tempZIStream = new ZipInputStream(tempBIStream);
        tempEntry = tempZIStream.getNextEntry();
        
        
        if (tempEntry != null) {
            tempDecompressedSize = tempEntry.getCompressedSize();
            if (tempDecompressedSize < 0) {
                tempDecompressedSize = Long.parseLong(tempEntry.getName());
            }

            int size = (int)tempDecompressedSize;
            tempUncompressedBuf = new byte[size];
            int num = 0, count = 0;
            while ( true ) {
                count =tempZIStream.read(tempUncompressedBuf, 0, size - num );
                num += count;
                stream.write(tempUncompressedBuf, 0, count);
               
                retbyte=stream.toByteArray();
                if ( num >= size ) break;
            }
            
           
        }
        stream.close();
        tempZIStream.close();
      
        
        return retbyte;*/
    		
    		
    		
    		
    		
    		
    		
		  Inflater decompressor = new Inflater();
		  decompressor.setInput(bytes);
		  ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
		  byte[] buf = new byte[1024*10];
		  byte[] retbyte = null;
		  
		  while (!decompressor.finished()) 
		  {
		   try 
		   {
		    int count = decompressor.inflate(buf);
		      bos.write(buf, 0, count);
		   } catch (DataFormatException e) 
		   {
		    e.printStackTrace();
		   }
		  }
		 
		  
		  retbyte = bos.toByteArray();
		   bos.close();
		  return retbyte;

    }
    	else
    	{
    		return null;
    	}
    }


    

    
}
