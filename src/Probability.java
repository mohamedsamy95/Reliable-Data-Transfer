import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

public class Probability {

	float percent;
	int packetsLength;
	public Probability(float percent, int packetsLength){
		this.percent = percent;
		this.packetsLength = packetsLength;
	}
	ArrayList<Integer> corruption(){
		ArrayList<Integer> corruptionArray= new ArrayList<Integer>();
		int corrupted = (int) Math.ceil(percent*packetsLength/100);
		
		for(int i =0; i< corrupted; i++ ){
			
				corruptionArray.add(0);
			
		}
		for(int i =0; i< packetsLength-corrupted; i++ ){
			
			corruptionArray.add(1);
			
		}
		
		
		
		shuffleList(corruptionArray);
	for(int i =0; i< corrupted+1; i++ ){
				
				corruptionArray.add(1);
			
		}
		//System.out.println(corruptionArray);
		return corruptionArray;
		
	}


	    public void shuffleList(ArrayList<Integer> a) {
	        int n = a.size();
	        Random random = new Random();
	        random.nextInt();
	        for (int i = 0; i < n; i++) {
	            int change = i + random.nextInt(n - i);
	            swap(a, i, change);
	        }
	    }
	
	    private void swap(ArrayList<Integer> a, int i, int change) {
	        int helper = a.get(i);
	        a.set(i, a.get(change));
	        a.set(change, helper);
	    }
	
}