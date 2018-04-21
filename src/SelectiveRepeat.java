import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class SelectiveRepeat implements Runnable  {
	private DatagramPacket packet;
	private DatagramSocket socket;
	private int id;
	String fileName;
	Main main;
	
	public SelectiveRepeat(Main main,DatagramPacket packet,DatagramSocket socket, int id){
		this.packet = packet;
		this.socket = socket;
		this.id = id;
		this.main = main;
	}
	
	@Override
	public void run(){
		//window
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
			//System.out.print("read data:"+serverDataString);
			ArrayList<String> packets = rf.fileToPackets(serverDataString,main.serial,Integer.toString(Main.window));
			SelectiveRepeatServer sr = new SelectiveRepeatServer( main, packets, socket,packet.getLength(), packet.getAddress(), packet.getPort(),id ,Main.window);
			sr.runServer();
			
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		System.out.println("CLIENT:"+id+":DONE");

	
		
	}
}
