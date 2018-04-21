import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class readFile {
	private String fileName;
	readFile(String fileName) {
		this.fileName = fileName;	
	}
	readFile() {
	}
	
	public String read() throws IOException{
		  String data = null;
		  //System.out.println("readfile:"+fileName);
		  BufferedReader br = new BufferedReader(new FileReader(fileName));
		  StringBuilder sb = new StringBuilder();
          String line = br.readLine();
	        try {
	        	
	                

	                while (line != null) {
	                    sb.append(line);
	                    sb.append("\n");
	                    line = br.readLine();
	                }

           
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }
	        
	       // System.out.println(sb.toString().replaceAll("\n", "\\\\n"));
	        br.close();
	        return sb.toString();
			
	    }
	
	public String readLine() throws IOException{
		  String data = null;
		  

		  BufferedReader br = new BufferedReader(new FileReader(fileName));
		  StringBuilder sb = new StringBuilder();
		  String line = br.readLine();
	        try {

	                while (line != null) {
	                    sb.append(line);
	                    sb.append("\n");
	                    line = br.readLine();
	                }

         
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }
	        br.close();
	        return sb.toString();
			
	    }

	
	

	
	public ArrayList<String> fileToPackets(String data, int packetSize,String window) throws IOException{
		//String fileName = myClient.getFileName();
		ArrayList<String> packets = new ArrayList<String>() ;
		String splitData=null;
		int fileSize = (int) data.length();
		int k = 0;
		int remainingPart;
		int outerLoop = (int) Math.ceil((float)fileSize/packetSize);
		
		int i;
		for(i =0; i < outerLoop; i++){
		//	System.out.println("dakhal"+ i );
			if(fileSize > packetSize){
				 remainingPart = packetSize;
			}
			else{
				remainingPart = fileSize;
				//System.out.println("rem part:"+ remainingPart);
			}
			fileSize -= packetSize;
			//System.out.println("filesize:"+fileSize);
			for(int j =0; j< remainingPart; j++){
				
				if(splitData==null){
					splitData=(Character.toString(data.charAt(k)));
				}
				else{
					
					//System.out.println(j);
					splitData+=( Character.toString(data.charAt(k)) );
					k++;
				}
			}
			//System.out.println(splitData);
			String seqnum = Integer.toString(i);
			String checksum = Integer.toString(computeChecksum(splitData));
			
			packets.add(splitData+"-q-"+seqnum+"-q-"+checksum+"-q-"+window);
			//System.out.println("Packet:"+i + ":"+packets.get(i));
			splitData = null;
			
		}
		
		packets.add("end"+"-q-"+ i +"-q-"+computeChecksum("end")+"-q-"+window);
		System.out.println("Number of packets:"+i);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

		return packets;
		
	}
	
	
	static int computeChecksum(String packetOfData) {
        int allSum = 0;
        byte[] packet = packetOfData.getBytes(Charset.forName("UTF-8"));
        for(byte b : packet)
            allSum += b;
        int cuttedSum = allSum & 0x000000FF;
        int remaining  = allSum >> 8;
        while (remaining != 0){
            cuttedSum += (remaining & 0x000000FF);
            while ((cuttedSum & 0x0000FF00) != 0){
                int nextByte = (cuttedSum & 0x0000FF00) >> 8;
                cuttedSum &= 0x000000FF;
                cuttedSum += nextByte;
            }
            remaining = remaining >> 8;
        }
 
        return cuttedSum ^ 0xFF;
    }
	
	
	
	
	public byte[] readImg(String name) throws IOException{
		File imgPath = new File(name);
		 BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		 WritableRaster raster = bufferedImage .getRaster();
		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

		 return ( data.getData() );
	}
	
}



