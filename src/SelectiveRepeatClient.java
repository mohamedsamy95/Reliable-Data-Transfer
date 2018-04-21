import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

public class SelectiveRepeatClient {


	private DatagramSocket clientSocket;
	private int length;
	private int port;
	private InetAddress IP;
	private int id ;
	private int window;
	

	SelectiveRepeatClient(  InetAddress IP,int port, DatagramSocket clientSocket, int length,int id, int window){
		this.clientSocket = clientSocket;
		this.length = length;
		this.port = port;
		this.IP = IP;
		this.id=id;
		this.window = window;
		
	}
	
	public ArrayList<Packet> runClient() throws IOException{
		ArrayList<Integer> array = new ArrayList();
		int base =0;
		int seqnum;
		String newLine = System.getProperty("line.separator");
		ArrayList<Packet> receivedData = new ArrayList<Packet>();
		byte [] receivedDataInBytes = new byte [length];
		byte [] ackByte = new byte [2];
		String serverData = "";
		int i = 0;
		readFile rf = new readFile();
		DatagramPacket receivedPacket = new DatagramPacket(receivedDataInBytes, length);
		clientSocket.receive(receivedPacket);
		String serverDataTemp = new String ( receivedDataInBytes);
		serverData = null;
		String[]parts = null;
		int calculatedChecksum;
		DatagramPacket ack = new DatagramPacket(ackByte,ackByte.length,receivedPacket.getAddress(), receivedPacket.getPort());
			//System.out.println(ackByte + " "+ new String(ackByte, "UTF-8"));
			

		
		do{
			
			receivedPacket = new DatagramPacket(receivedDataInBytes, length);
			clientSocket.receive(receivedPacket);
			serverDataTemp = new String ( receivedDataInBytes);
			serverData = null;
			//remove null characters
			for(int j =0; j< receivedPacket.getLength()  ; j++)
			{
				
				if(j==0)
				{
					serverData = Character.toString(serverDataTemp.charAt(j));
				}
				else
				{
					serverData += Character.toString(serverDataTemp.charAt(j));
				}

			}
			parts = serverData.split("-q-"); //split data 
			ackByte = parts[1].getBytes();
			seqnum = Integer.parseInt(parts[1]);
			ack = new DatagramPacket(ackByte,ackByte.length,receivedPacket.getAddress(), receivedPacket.getPort());
			if(i==0){
				int window = Integer.parseInt(parts[3]);

			}
				//System.out.println("CLIENT:"+id);
				//System.out.println("Base: " + base);
				if(!array.contains(seqnum)){
					array.add(seqnum);
					
				}
				//System.out.println("received packets:"+array);
 				if(seqnum - base < window){
 					
					//System.out.println(ackByte + " "+ new String(ackByte, "UTF-8"));
					receivedData.add(new Packet(Integer.parseInt(parts[1]), parts[0], Integer.parseInt(parts[2])));
					calculatedChecksum = rf.computeChecksum(receivedData.get(i).data);
					//System.out.println("Client - Received Packet :"+ receivedData.get(i).seqnum +newLine+"seqnum :"+   receivedData.get(i).seqnum+newLine+"Checksum :"+   receivedData.get(i).checksum+newLine+"Calculated Checksum :"+   calculatedChecksum+ newLine+"Data:"+ receivedData.get(i).data + newLine+"________________________________________________" );
					if(calculatedChecksum== receivedData.get(i).checksum)
					{
						
						Thread t = new Thread(new ackThread(ack,clientSocket,receivedData.get(i).seqnum,window));
						t.start();
						
		
					}		
					else
					{
						//System.out.println("________________________________________________"+newLine+"CORRUPTED!!"+ newLine+"________________________________________________" );
					}
					if(receivedData.get(i).data.equals("end")){
 						break;
 					}
					
				i++;
				Collections.sort(array);
				int k =0;
				for(k =0 ; k < array.size() ; k++ ){
					if(k!=array.get(k)){
						break;
					}
					
				}
				base = k;
				
			}
	
	}while(true);
		
		receivedData.remove(receivedData.size()-1);
		Collections.sort(receivedData);
		/*for(Packet p : receivedData)
		{
			System.out.println(p.data);
		}*/
		return receivedData;
}
	

}
