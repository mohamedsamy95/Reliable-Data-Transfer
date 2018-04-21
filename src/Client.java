import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
public class Client  {
	public static final int PORT = 1234;
	private DatagramPacket packet;
	private DatagramSocket socket;
	public Client(DatagramPacket packet, DatagramSocket socket){
		
		this.packet = packet;
		socket = this.socket;
	}
	
	public static void main(String[] args)throws Exception {
			DatagramSocket clientSocket = new DatagramSocket();
			int packetSize = Main.chunckSize;
			byte [] file = Main.fileName.getBytes();
			Server server = new Server();
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket clientData = new DatagramPacket ( file, file.length, ip, PORT);
			clientSocket.send(clientData);
			//System.out.println("new Client");

			//STOP AND WAIT----------------------------------------------------------------------------
			/*double time_before = System.currentTimeMillis();
			StopAndWaitClient saw = new StopAndWaitClient(ip,PORT,clientSocket,packetSize);
			try {
				ArrayList<Packet> ReceivedData = saw.runClient();
				double time_after = System.currentTimeMillis();
				System.out.println("Throughput: " + (time_after - time_before)/1000  + "s");
				PrintWriter writer = new PrintWriter("Received.txt", "UTF-8");
				for (Packet P : ReceivedData)
				writer.write(P.data);
				writer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//-------------------------------------------------------------------------------------------
			
			//SELECTIVE REPEAT-------------------------------------------------------------------------------
			double time_before = System.currentTimeMillis();
			 SelectiveRepeatClient sr = new SelectiveRepeatClient(ip,PORT,clientSocket,packetSize,server.ID,Main.window);
			try {
				ArrayList<Packet> ReceivedData=sr.runClient();
				double time_after = System.currentTimeMillis();
				System.out.println("Throughput: " + (time_after - time_before)/1000  + "s");
				PrintWriter writer = new PrintWriter("Received.txt", "UTF-8");
				for (Packet P : ReceivedData)
				writer.write(P.data);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//--------------------------------------------------------------------------------------------
			
			//GBN
			/*GBNClient gbnclient = new GBNClient(ip,PORT,clientSocket,packetSize,server.ID,Main.window);
			try {
				gbnclient.runClient();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//...........................................................
			
			
	}


}

