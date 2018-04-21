import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class StopAndWaitClient {

	private DatagramSocket clientSocket;
	private int length;
	private int port;
	private InetAddress IP;

	StopAndWaitClient(  InetAddress IP,int port, DatagramSocket clientSocket, int length){
		this.clientSocket = clientSocket;
		this.length = length;
		this.port = port;
		this.IP = IP;
		
	}
	public ArrayList<Packet> runClient() throws IOException{
		ArrayList<Packet> receivedData = new ArrayList<Packet>();
		byte [] receivedDataInBytes = new byte [length];
		byte [] ackByte = new byte [8];
		String serverData = "";
		int i = 0;
		readFile rf = new readFile();
		
		do{
			
			DatagramPacket receivedPacket = new DatagramPacket(receivedDataInBytes, length);
			clientSocket.receive(receivedPacket);
			
			String serverDataTemp = new String ( receivedDataInBytes);
			serverData = null;
			for(int j =0; j< receivedPacket.getLength()  ; j++){
				
				if(j==0){
					serverData = Character.toString(serverDataTemp.charAt(j));
				}
				else{
					serverData += Character.toString(serverDataTemp.charAt(j));
				}
	
			}

			String[]parts = serverData.split("-q-");
			if(!serverData.equals("end")){
			receivedData.add(new Packet(Integer.parseInt(parts[1]), parts[0], Integer.parseInt(parts[2])));
			//System.out.println("Client - Received Packet :"+ i );
			//System.out.println("Data:"+ receivedData.get(i).data);
			//System.out.println("seqnum :"+   receivedData.get(i).seqnum);
			////System.out.println("Checksum :"+   receivedData.get(i).checksum);
			int calculatedChecksum = rf.computeChecksum(receivedData.get(i).data);
			////System.out.println("Calculated Checksum :"+   calculatedChecksum);
			if(calculatedChecksum== receivedData.get(i).checksum){
				DatagramPacket ack = new DatagramPacket(ackByte,8,receivedPacket.getAddress(), receivedPacket.getPort());
				clientSocket.send(ack);
				//System.out.println("ack:"+ i );
				//System.out.println("________________________________________________");

			}
			else{
				//System.out.println("CORRUPTED!!" );
				//System.out.println("________________________________________________");
			}
			
			


			
			}
			if(receivedData.get(i).data.equals("end")){
					break;
				}
			i++;
		}while(true);
		receivedData.remove(receivedData.size()-1);
		//System.out.println("Recieved end packet");
		clientSocket.close();
		return receivedData;
	}
	
}
