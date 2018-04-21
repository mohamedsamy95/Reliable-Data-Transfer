import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ackThread implements Runnable {

	private DatagramPacket ack;
	private DatagramSocket socket;
	private int seqnum;
	private int window;
	
	ackThread(DatagramPacket packet,DatagramSocket socket,int seqnum,int window){
		this.ack = packet;
		this.socket =  socket;
		this.seqnum = seqnum;
		this.window = window;
		
	}

	@Override
	public void run() {
		String newLine = System.getProperty("line.separator");


		

			try {
				socket.send(ack);
				
				//System.out.println("________________________________________________"+newLine+ "ack:"+ seqnum +":sent" + newLine+"________________________________________________");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*while(true){
				try {
					socket.receive(ack);
					String receivedAckData = new String(ack.getData(), "UTF-8");
					System.out.println("ack : " + receivedAckData + " :received");
					System.out.println("____________________________________");
					break;
				} catch (IOException e) {
					System.out.println("Timeout");
				}
		    	
			}*/
			
			
	
		
	}
}
