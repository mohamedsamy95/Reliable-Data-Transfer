import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class StopAndWait implements Runnable {
	String fileName;
	private DatagramPacket packet;
	private int id;
	private DatagramSocket socket;
	private Main main;
	public StopAndWait(Main main,DatagramPacket packet, DatagramSocket socket, int id){
		
		this.packet = packet;
		this.id=id;
		this.socket = socket;
		this.main = main;
	}
	@Override
	public void run() {
		fileName = new String(packet.getData(),0,packet.getLength());
		String clientDataTemp = new String (fileName);

		String clientData = null;

		for(int i =0; i< packet.getLength() ; i++){
			
			if(i==0){
				clientData = Character.toString(clientDataTemp.charAt(i));
			}
			else{
				clientData += Character.toString(clientDataTemp.charAt(i));
			}

		}
		readFile rf = new readFile(clientData);
		String serverDataString;
		try {
			serverDataString = rf.read();
			ArrayList<String> packets = rf.fileToPackets(serverDataString,Main.serial, Integer.toString(Main.window));
			StopAndWaitServer saw = new StopAndWaitServer( packets, socket,packet.getLength(), packet.getAddress(), packet.getPort(),id );
			saw.runServer();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Client: "+ id +" :Done");
 catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
}
