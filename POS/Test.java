import java.util.ArrayList;
import java.util.HashMap;

public class Test extends Viterbi{
	
	public void MakeWordsTag(String line) {
		
		// split between whitespaces
		String[] StrArray =line.split(" ");
		
		for (String s: StrArray) {
			// split forward for words/tags
			String[] NLine = s.split("/");
			Words.add(NLine[0].toLowerCase());
			Tags.add(NLine[1]);
		}
	}
	
	public void Train(String line) {
			
			// split between whitespaces
			String[] Array = line.split(" ");
			
			int i = -1;
			int j = i + 1;
			
				while (j < Array.length) {
					
					String from = "#";
					
					// make sure we are not at the start of a sentence
					if (i != -1) {
						from = Array[i].split("/")[1];
					}
					
					
					String to = Array[j].split("/")[1];
					
					// if outer map already exists
					if (TrainingMap.containsKey(from)) {
							// update count if transition from-to already exists
							if (TrainingMap.get(from).containsKey(to)) {
						
									HashMap<String,Double> tempMap = TrainingMap.get(from);
									tempMap.put(to, Double.valueOf(TrainingMap.get(from).get(to).doubleValue() + 1));
									TrainingMap.put(from, tempMap);
									
							// add the new transition to the map
							} else {
								HashMap<String,Double> tempMap = TrainingMap.get(from);
								tempMap.put(to, Double.valueOf(1));
								TrainingMap.put(from, tempMap);
							}
					} else {
						// add outer map and inner map	
						HashMap<String,Double> tempMap = new HashMap<String,Double>();
						tempMap.put(to, Double.valueOf(1));
						TrainingMap.put(from, tempMap);
					}
					i++;
					j++;
					
				}
			}
	
	public static void main(String[] args) {
		
		Test test = new Test();
		test.MakeWordsTag("cat/N chase/V dog/N");
		test.MakeWordsTag("cat/N watch/V chase/NP");
		test.MakeWordsTag("chase/NP get/V watch/N");
		test.MakeWordsTag("chase/NP watch/V dog/N and/CNJ cat/N");
		test.MakeWordsTag("dog/N watch/V cat/N watch/V dog/N");
		test.MakeWordsTag("cat/N watch/V watch/N and/CNJ chase/NP");
		test.MakeWordsTag("dog/N watch/V and/CNJ chase/V chase/NP");
		
		test.Train("cat/N chase/V dog/N");
		test.Train("cat/N watch/V chase/NP");
		test.Train("chase/NP get/V watch/N");
		test.Train("chase/NP watch/V dog/N and/CNJ cat/N");
		test.Train("dog/N watch/V cat/N watch/V dog/N");
		test.Train("cat/N watch/V watch/N and/CNJ chase/NP");
		test.Train("dog/N watch/V and/CNJ chase/V chase/NP");

		
		test.CreateEmissionMap();
		test.Normalize();
		
		ArrayList<String> bestSequence = test.ViterbiDecoding("Chase Watch Dog Chase watch");
		System.out.println(bestSequence);
	}

}
