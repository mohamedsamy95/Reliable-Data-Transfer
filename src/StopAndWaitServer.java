import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class StopAndWaitServer {

	private ArrayList<String> packets;
	private DatagramSocket socket;
	private int length;
	private InetAddress IP;
	private int port;
	private int id;
	
	StopAndWaitServer(ArrayList<String> packets, DatagramSocket serverSocket, int length, InetAddress IP, int port, int id){
		this.packets = packets;
		this.socket = serverSocket;
		this.length = length;
		this.IP=IP;
		this.port=port;
		this.id = id;
	}
	public void runServer() throws IOException, InterruptedException{
		byte[] ackByte = new byte[8];
		//probability
		Probability p = new Probability(Main.probabilityPercent,packets.size());
		ArrayList<Integer> c = new ArrayList<Integer>();
		c = p.corruption();
		//System.out.println("Probability:" + c);
		//System.out.println("________________________");
		int j=0;
		for(int i =0; i< packets.size(); i++){
			DatagramPacket sendPacket = new DatagramPacket(packets.get(i).getBytes(),packets.get(i).getBytes().length,IP, port);
			DatagramPacket ack = new DatagramPacket(ackByte,8);

				if(c.get(j)==0){
					//System.out.println("serving Client:" + id);
			    	//System.out.println("--------------");
					//System.out.println("TIME OUT - Dropped..Packet:" + i );
					//System.out.println("________________________");
					Thread.sleep(500);
					i--;
				}
				else{
					// resend
			    	//System.out.println("serving Client:" + id);
			    	//System.out.println("--------------");
					//System.out.println("sending..Packet:" + i );
					socket.send(sendPacket);
					//System.out.println("Packet:" + i + " sent successfully ");
				    socket.receive(ack);
				    //System.out.println("Ack:" + i + " received successfully ");
				    //System.out.println("____________________________________");
				}
				
				j++;
			}
		
			
			
			
		
			
			
		
	
	}
	
}
