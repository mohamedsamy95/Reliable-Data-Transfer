import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

public class GBNserver {

	private static ArrayList<String> packets;
	private DatagramSocket socket;
	private int length;
	private InetAddress IP;
	private int port;
	public static final float probabilityPercent = 20;
	private int id;
	private Main main;
	int window;
	
	public GBNserver(Main main,ArrayList<String> packets, DatagramSocket socket, int length, InetAddress IP, int port, int id, int window){
		this.packets = packets;
		this.socket = socket;
		this.length = length;
		this.IP=IP;
		this.port=port;
		this.id = id;
		this.window=window;
		this.main = main;
	}
	public void runServer() throws IOException, InterruptedException{
		Semaphore lock = new Semaphore(1);
		String newLine = System.getProperty("line.separator");
		int seqnum = 0;
		int base = 0;
		int i=0;
		Probability p = new Probability(probabilityPercent, packets.size());
		main.corruptionArray = p.corruption();
		System.out.println(main.corruptionArray);

		do{
			if(i==0){
				for(i =0; i< window; i++){

					DatagramPacket packet = new DatagramPacket(packets.get(i).getBytes(),packets.get(i).getBytes().length,IP, port);
					socket.send(packet);
					
				}
			}
			byte[] ackByte = new byte[2];
			DatagramPacket ack = new DatagramPacket(ackByte,ackByte.length);
			socket.receive(ack);
			String serverDataTemp = new String (ackByte);
			String serverData = ack.getData().toString();
			for(int j =0; j< ack.getLength()  ; j++)
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
			
			seqnum = Integer.parseInt(serverData);
			//acquire
			System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"ack:"+ seqnum +":received"+newLine+"________________________________________________");
			if(!main.seqnumArray.contains(seqnum)){
				main.seqnumArray.add(seqnum);
			}
			Collections.sort(main.seqnumArray);
			int k =0;
			for(k =0 ; k < main.seqnumArray.size() ; k++ ){
				if(k!=main.seqnumArray.get(k)){
					break;
				}
				
			}
			base = k;
			System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"base : "+base+newLine+"________________________________________________");

					
	
					if(i - base < window){
						if(i<packets.size()){
							DatagramPacket packet = new DatagramPacket(packets.get(i).getBytes(),packets.get(i).getBytes().length,IP, port);
							Thread t = new Thread(new sendPacketThreadSelective(main,packet,socket,i,lock,packets.size(),id));
							t.start();
							i++;
						}
						else{
							break;
						}
				
						
				}
					
			
		}while(true);
		
		
}
	
}