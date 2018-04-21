
public class Packet implements Comparable {
	
	int seqnum;
	String data;
	int checksum;
	Packet(int seqnum, String data,int checksum){
		this.seqnum = seqnum;
		this.data = data;
		this.checksum = checksum;
	}
	@Override
	public int compareTo(Object o) {
		Packet p = (Packet)o;
		if (this.seqnum>p.seqnum)
			return 1;
		else if (this.seqnum<p.seqnum)
			return -1;
		return 0;
	}
}
