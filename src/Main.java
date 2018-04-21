import java.util.ArrayList;

public class Main {

	static int window=128;
	public static final float probabilityPercent = 5;
	final static String fileName = "test2.txt";
	final static int  serial = 512;
	final static int chunckSize = 532;
	ArrayList<Integer> seqnumArray= new ArrayList<Integer>();
	ArrayList<Integer> corruptionArray=new ArrayList<Integer>();
	private int corruptionIndex;
	
	
	public Main(ArrayList<Integer> seqnumArray, ArrayList<Integer> corruptionArray,int index  ){
		this.window = window;
		this.seqnumArray=seqnumArray;
		this.corruptionArray = corruptionArray;
		this.setCorruptionIndex(index);
	}
	
	public  void printArray(){
		for(int i = 0; i < corruptionArray.size(); i++){
			System.out.print(corruptionArray.get(i)+",");
			
		}
		System.out.println("");
	}

	public int getCorruptionIndex() {
		return corruptionIndex;
	}

	public void setCorruptionIndex(int corruptionIndex) {
		this.corruptionIndex = corruptionIndex;
	}


}

