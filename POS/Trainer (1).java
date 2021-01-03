import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Trainer {
	
	// need two arraylist to store Words and tags at common index
	ArrayList<String> Words = new ArrayList<String>();
	ArrayList<String> Tags = new ArrayList<String>();
	
	HashMap<String, HashMap<String, Double>> EmissionMap = new HashMap<String, HashMap<String, Double>>();
	HashMap<String, HashMap<String, Double>> TrainingMap = new HashMap<String, HashMap<String, Double>>();
	
	
	public void MakeWordsTags(String WordsPath, String TagsPath) throws IOException{

	try {
		// opening the files
		BufferedReader WordsIn = new BufferedReader(new FileReader(WordsPath));
		BufferedReader TagsIn = new BufferedReader(new FileReader(TagsPath));
		
		String line;
		
		//  read each line in Words File
		while ((line = WordsIn.readLine()) != null) {
			// split between whitespaces
			line = line.toLowerCase();
			String[] StrArray =line.split(" ");
			for (String s: StrArray) {
					Words.add(s);
				}
	
		}
	
		//  read each line in Words File
		while ((line = TagsIn.readLine()) != null) {

		
			// split between whitespaces
			String[] StrArray =line.split(" ");
		
			int i = -1;
			int j = i + 1;
			
		
			while (j < StrArray.length) {
			
				String from = "#";
			
				// make sure we are not at the start of a sentence
				if (i != -1) {
					from = StrArray[i];
				}
			
				String to = StrArray[j];
				Tags.add(StrArray[j]); // add remaining
			
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
		
		WordsIn.close();
		TagsIn.close();
		
	} catch (IOException e) {
		System.err.println("Error opening File!");
	} 
}
	
	public void CreateEmissionMap() {
		// looping through all the words
		for (int i = 0; i < Words.size(); i++) {
			String WordTag = Tags.get(i);
			
			if (EmissionMap.containsKey(WordTag)) {
				
				HashMap<String,Double> tempMap = EmissionMap.get(WordTag);  // inner map
				
				// check if word exists in inner map
				if (tempMap.containsKey(Words.get(i))) {
					tempMap.put(Words.get(i), Double.valueOf(EmissionMap.get(WordTag).get(Words.get(i)).doubleValue() + 1)); // updating the count of the words
					EmissionMap.put(WordTag, tempMap);
					
				// otherwise add one
				} else {
					tempMap.put(Words.get(i), Double.valueOf(1)); // updating the count of the words
					EmissionMap.put(WordTag, tempMap);
				}
				
			// add Tag to the emission map if it is absent
			} else {
				HashMap<String,Double> tempMap = new HashMap<String,Double>();
				tempMap.put(Words.get(i), Double.valueOf(1));
				EmissionMap.put(WordTag, tempMap);
			}
				
		}
	}
	
	/*
	 * 
	 * Method to Normalize the probabilities
	 * 
	 */
	public void Normalize() {
		
		// looping through all the keys for emission map
		for (String s : EmissionMap.keySet()) {
			double TotalValue = 0.0;
			
			// looping through all the inner values
			for (String x: EmissionMap.get(s).keySet()) {
				TotalValue += EmissionMap.get(s).get(x);
			}
			
			//  changing to the log probabilities
			for (String x: EmissionMap.get(s).keySet()) {
				
				double Prob = Math.log(EmissionMap.get(s).get(x).doubleValue() / TotalValue);
				EmissionMap.get(s).put(x, Prob);
			}
		}
		
		
		// looping through all the keys for training map
			for (String s : TrainingMap.keySet()) {
				double TotalValue = 0.0;
					
				// looping through all the inner values
				for (String x: TrainingMap.get(s).keySet()) {
					TotalValue += TrainingMap.get(s).get(x);
				}
					
				//  changing to the log probabilities
				for (String x: TrainingMap.get(s).keySet()) {
						
					double Prob = Math.log(TrainingMap.get(s).get(x).doubleValue() / TotalValue);
					TrainingMap.get(s).put(x, Prob);
				}
			}
	}
	
}
