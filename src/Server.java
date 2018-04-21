import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class Server {
	public static final int PORT = 1234;
	public int ID = 0;
	Server(){
		
	}
	public static void main(String[ ]args) throws IOException{
		new Server().runServer();
	}
	
	
	public void runServer() throws IOException{
		
		while (true){
			try
			{
				ArrayList<Integer> c = new ArrayList<Integer>() ;
				ArrayList<Integer> seqnumArray = new ArrayList<Integer>() ;
				Main main = new Main(seqnumArray,c,0);
				DatagramSocket serverSocket = new DatagramSocket(PORT);
				int packetSize = Main.chunckSize;
				byte [] clientReq = new byte[packetSize];
				DatagramPacket packet = new DatagramPacket(clientReq, packetSize);
				serverSocket.receive(packet);
				DatagramSocket threadSocket = new DatagramSocket();
				
				//STOP AND WAIT
				//Thread t = new Thread(new StopAndWait(main,packet,threadSocket, ID++));
				//t.start();
				
				//SELECTIVE REPEAT
				Thread t = new Thread(new SelectiveRepeat(main,packet,threadSocket, ID++));
				t.start();
				
				//GBN
				//Thread t = new Thread(new GBN(main,packet,threadSocket, ID++));
				//t.start();

			}
			catch(Exception e){
				
			}
		}
		
	}
}
