import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Semaphore;

public class sendPacketThreadSelective implements Runnable {
	private DatagramPacket packet ;
	private DatagramSocket socket;
	private int seqnum;
	Semaphore lock;
	Main main;
	int packetsize;
	int id;
	sendPacketThreadSelective(Main main,DatagramPacket packet,DatagramSocket socket,int seqnum, Semaphore lock, int size,int id){
		this.main = main;
		this.packet = packet;
		this.socket = socket;
		this.seqnum = seqnum;
		this.lock = lock;
		this.packetsize=size;
		this.id=id;
	}
	@Override
	public void run() {
		String newLine = System.getProperty("line.separator");
		int flag = 1;
		//System.out.println("corruption index:" + main.getCorruptionIndex());
		while(flag ==1 ){
			if(seqnum!=packetsize-1){
			if(main.getCorruptionIndex()<main.corruptionArray.size()){
				if(main.corruptionArray.get(main.getCorruptionIndex())!=0){
					
						try {
								socket.send(packet);
								//System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"packet : " + seqnum + " :sent"+newLine+"________________________________________________");
								
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						
				}
				else{
					
					//System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"packet : " + seqnum + " :DROPPED"+newLine+"________________________________________________");
				}
			
				try {
					lock.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				main.setCorruptionIndex(main.getCorruptionIndex() + 1);
				lock.release();
			
			}
		}
		else
		{
			while(true){
				if(main.seqnumArray.size()==packetsize-1){
					try {
						socket.send(packet);
						//System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"packet : " + seqnum + " :sent"+newLine+"________________________________________________");
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			break;
		}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(main.seqnumArray.contains(seqnum)){
				flag=0;
			}
			else{
				
				//System.out.println("________________________________________________"+newLine+"CLIENT:"+id+newLine+"packet : " + seqnum + " : is RESENDING "+newLine+"________________________________________________");

			}
			
		}
		
	}

	
		
}
