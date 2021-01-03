import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDrivenTest extends Viterbi{
	
	public double AccuracyTest(String WordsPath, String TagsPath) throws IOException {
		
		BufferedReader CorrectIN = new BufferedReader(new FileReader(WordsPath));
		BufferedReader CorrectTags = new BufferedReader(new FileReader(TagsPath));
		
		String line;
		
		ArrayList<String> TestTags = new ArrayList<String>();
		ArrayList<String> CTags = new ArrayList<String>();
		
		//  going through every words in test tag and forming
		//  arraylist of tags
		while ((line = CorrectIN.readLine()) != null) {
			ArrayList<String> Test = ViterbiDecoding(line);
			
			for (String x: Test) {
				TestTags.add(x);
			}
		}
		
		//  going through every words in test tag and forming
		//  arraylist of tags
		while ((line = CorrectTags.readLine()) != null) {
			
			String[] StrArray = line.split(" ");
			
			for (String x: StrArray) {
				CTags.add(x);
			}
		}
		
		CorrectIN.close();
			CorrectTags.close();
		
			double bad = 0.0;
		
			//  checking for bad tags
			for (int i = 0; i < CTags.size(); i++) {
				if (CTags.get(i) != TestTags.get(i)) {
					bad++;
				}
			}
		 return bad;

		}
	
	public void ConsoleTest() {
		Scanner in = new Scanner(System.in);
		String line = " ";
	
		while (line != "q") {
		System.out.println("Enter sentences: ");
		line = in.nextLine();
		
		ArrayList<String> bestSequence = ViterbiDecoding(line);
		System.out.println("Best sequece of POS tag: " + bestSequence);
		
	}
	
	in.close();
	}

	public static void main(String[] args) throws IOException {
		FileDrivenTest test = new FileDrivenTest();
		test.MakeWordsTags("inputs/simple-train-sentences.txt","inputs/simple-train-tags.txt");
		test.CreateEmissionMap();
		test.Normalize();
		System.out.println(test.AccuracyTest("inputs/simple-test-sentences.txt","inputs/simple-test-tags.txt"));;
		
		
		FileDrivenTest test1 = new FileDrivenTest();
		
		test1.MakeWordsTags("inputs/brown-train-sentences.txt","inputs/brown-train-tags.txt");
		test1.CreateEmissionMap();
		test1.Normalize();
		System.out.println(test1.AccuracyTest("inputs/brown-test-sentences.txt","inputs/brown-test-tags.txt"));
		
		test1.ConsoleTest();
	}

}
