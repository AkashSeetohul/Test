import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Viterbi extends Trainer{
	
	public ArrayList<String> ViterbiDecoding(String Sentence){
		
		// create array of observations
		Sentence = Sentence.toLowerCase();
		String[] StrArray = Sentence.split(" ");
		
		int length = StrArray.length;
		
		ArrayList<HashMap<String, String>> backtrace = new ArrayList<HashMap<String, String>>();
		
		// initializing a backtrace
		for (int i = 0; i < length + 1; i++) {
			backtrace.add(i, new HashMap<String, String>());
		}
		
		HashMap<String, Double> CurrScores = new HashMap<String, Double>();
		CurrScores.put("#", Double.valueOf(0));
		
		double Penalize = -10;
		
		// looping through all the observations
		for (int z = 0; z < length; z++) {
			
			HashMap<String, Double> nextScores = new HashMap<String, Double>();
			
			//  looping through each current state
			for (String current: CurrScores.keySet()) {
				
				if (TrainingMap.get(current) != null) {
					
				// for each transition currState >> NextState
				for (String x: TrainingMap.get(current).keySet()) {
					double nextScore = Double.MIN_VALUE;
					
					// if the state is present over there, add score
					if (EmissionMap.get(x).containsKey(StrArray[z])) {
						
						double ObsScore = 0.0;
						
						// start not present in the emission map and other mising from map
						if (current != "#") {
							ObsScore = EmissionMap.get(x).get(StrArray[z]).doubleValue();
						}
						
						
						nextScore = CurrScores.get(current).doubleValue() + TrainingMap.get(current).get(x).doubleValue() + ObsScore;
					} else {
						nextScore = CurrScores.get(current).doubleValue() + TrainingMap.get(current).get(x).doubleValue() + Penalize;
					}
				
				// if not yet explored, or score is higher
				if (!nextScores.keySet().contains(x) || (nextScore > nextScores.get(x).doubleValue())){
					nextScores.put(x, Double.valueOf(nextScore));
					
					//remember that pred of nextState @ i is curr
					backtrace.get(z).put(x, current);
					}
				}
				}
			}
		CurrScores = nextScores;
		}
		
		
		ArrayList<String> bestSequence = new ArrayList<String>();
		
		// adding for the best last state
		double max = -Double.MAX_VALUE;
		String LastTag = null;
		for (String x: CurrScores.keySet()) {
			if (CurrScores.get(x).doubleValue() > max) {
				LastTag = x;
			}
		}
		
		bestSequence.add(LastTag);
		
		// now working back to start
		for (int z = backtrace.size() - 2; z > 0; z--) {
			
			// since list of maps, move backwards
			String currentTag = backtrace.get(z).get(LastTag);
			bestSequence.add(currentTag);
			LastTag = currentTag;
		}
		
		
		//Reversing the list
		Collections.reverse(bestSequence);
		
		return bestSequence;
	}
}
