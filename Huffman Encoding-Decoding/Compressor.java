/*
 * 
 * Author Tooryanand Seetohul with partner Arun Maganti
 * 
 */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;


public class Compressor{
	public TreeMap<Character, Integer> frequencyMap = new TreeMap<Character, Integer>();
	public PriorityQueue<BinaryTree<CharFreq>> treePriority;
	public BinaryTree<CharFreq> HuffmanTree;
	public TreeMap<Character, String> CodeMap = new TreeMap<Character,String>();
	public BinaryTree<CharFreq> getHuffmanTree() {
			return HuffmanTree;
		}
	
	
	public TreeMap<Character, String> getCodeMap() {
		return CodeMap;
	}
	
	/*
	 * method to open and read file and make a character frequency map
	 */
	public void frequencyMap(String pathName) throws IOException {
		BufferedReader input;
		

		// opening the file
		input = new BufferedReader(new FileReader(pathName));
		
		try {
			int c = input.read();
			while (c != -1) {
				// reading a character at a time
				char CharacterPrimitive = (char) c;
				Character KeyCharacter = Character.valueOf(CharacterPrimitive);
			
				// incrementing existing value or adding new Key-Value in the Map
				if (frequencyMap.containsKey(KeyCharacter)) {
					frequencyMap.put(KeyCharacter, frequencyMap.get(KeyCharacter).intValue() + 1);
				} else {
					frequencyMap.put(KeyCharacter, Integer.valueOf(1));
				}
				c = input.read();
			}
			
		} finally {
			input.close();
		}
	}
	
	/*
	 * Generating a minimum priority queue
	 */
	public void generateQueue() throws Exception{
		
		if (frequencyMap.size() == 1) {
			System.err.println("File contains only one character!");
		} else {
			try {
				TreeComparator comparator = new TreeComparator();
				treePriority = new PriorityQueue<BinaryTree<CharFreq>>(frequencyMap.size(), comparator);
		
				//looping through  the map
				for (Map.Entry<Character,Integer> entry : frequencyMap.entrySet()) {
					// making a new binary tree for every CharFreq
					BinaryTree<CharFreq> tempBT = new BinaryTree<CharFreq>(new CharFreq(entry.getKey(), entry.getValue()));
					// adding the tree to the priority queue
					treePriority.add(tempBT);
					}
			} catch(Exception e) {
				System.err.println("Frequency table is empty. File was empty!");
			}
		}
	}
	
	/*
	 * 
	 * Create and return a Huffman Code Tree
	 */
	public void generateHuffmanTree(){
		
		Character dummy = Character.valueOf('z');
		
		while (treePriority.size() > 1) {
			BinaryTree<CharFreq> T1 = treePriority.remove();
			BinaryTree<CharFreq> T2 = treePriority.remove();
			
			int T1freq = T1.getData().getFrequency().intValue();
			int T2freq = T2.getData().getFrequency().intValue();
			Integer Rfreq = Integer.valueOf(T1freq + T2freq);
			
			// new Binary Tree R with sum of frequencies and T1 and T2 as children
			BinaryTree<CharFreq> R = new BinaryTree<CharFreq>(new CharFreq(dummy, Rfreq), T1, T2);
			treePriority.add(R);
		}
		if (treePriority.size() == 1) {
			HuffmanTree = treePriority.remove();
		}
	}
	
	/*
	 * Create a code map for every character in the code tree
	 * 
	 */
	public void generateCodeMap(BinaryTree<CharFreq> tree, String path) {
		
		// if the current tree is a leaf, add to the CodeMap
		if (tree.isLeaf()) {
				CodeMap.put(tree.getData().getCharacter(), path);
		}
		// if it has a left, add to the left leaf
		if (tree.hasLeft()) {
			generateCodeMap(tree.getLeft(), path + "0");
		}
		// if it has a right, add to the right leaf
		if (tree.hasRight()) {
			generateCodeMap(tree.getRight(), path + "1");
		}
	}
	
	public void compress(String PathName, String CompressedPathName) throws IOException {
		
		BufferedReader input = new BufferedReader(new FileReader(PathName));
		BufferedBitWriter bitOutput = new BufferedBitWriter(CompressedPathName);
		
		try {
			int c = input.read();
			while (c != -1) {
				// reading a character at a time
				char CharacterPrimitive = (char) c;
				Character KeyCharacter = Character.valueOf(CharacterPrimitive);
			
				//looking through all characters in the map
				String codeword = CodeMap.get(KeyCharacter);
			
				// Looping through all the characters in the map
				for (char x: codeword.toCharArray()) {
					if (x == '0') {
						bitOutput.writeBit(false);
					} else if (x == '1') {
						bitOutput.writeBit(true);
					}
				}
			
				c = input.read();
			}
		} catch(NullPointerException e) {
			System.err.println("Nothing to compress/pathname incorrect");
		} finally {
			input.close();
			bitOutput.close();
		}
	}
	
	public void decompress(String compressedPathName, String decompressedPathName) throws IOException{
		BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
		BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));
		
		BinaryTree<CharFreq> tree = HuffmanTree;
		
		try {
			while(bitInput.hasNext()) {
				boolean bit = bitInput.readBit();
				if (!bit) {
					tree = tree.getLeft();
				}
				if (bit){
					tree = tree.getRight();
				}
				if (tree.isLeaf()) {
					char Character = tree.getData().getCharacter();
					output.write(Character);
					tree = HuffmanTree;
				}
			}
		} catch(NullPointerException f){
			System.err.println("Nothing to decompress/pathname incorrect");
			
		} finally {
			bitInput.close();
			output.close();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		Compressor compressor1 = new Compressor();
		compressor1.frequencyMap("inputs/USConstitution.txt");
		compressor1.generateQueue();
		compressor1.generateHuffmanTree();
		compressor1.generateCodeMap(compressor1.getHuffmanTree(), "");
		compressor1.compress("inputs/USConstitution.txt", "inputs/USConstitutionCompressed.txt");
		compressor1.decompress("inputs/USConstitutionCompressed.txt", "inputs/USConstitutionDecompressed.txt");
		
		Compressor compressor2 = new Compressor();
		compressor2.frequencyMap("inputs/WarAndPeace.txt");
		compressor2.generateQueue();
		compressor2.generateHuffmanTree();
		compressor2.generateCodeMap(compressor2.getHuffmanTree(), "");
		compressor2.compress("inputs/WarAndPeace.txt", "inputs/WarAndPeaceCompressed.txt");
		compressor2.decompress("inputs/WarAndPeaceCompressed.txt", "inputs/WarAndPeaceDecompressed.txt");
		
		Compressor compressor3 = new Compressor();
		compressor3.frequencyMap("inputs/Harness.txt");
		compressor3.generateQueue();
		compressor3.generateHuffmanTree();
		compressor3.generateCodeMap(compressor3.getHuffmanTree(), "");
		compressor3.compress("inputs/Harness.txt", "inputs/HarnessCompressed.txt");
		compressor3.decompress("inputs/HarnessCompressed.txt", "inputs/HarnessDecompressed.txt");
		
		Compressor compressor4 = new Compressor();
		compressor4.frequencyMap("inputs/empty.txt");
		compressor4.generateQueue();
		compressor4.generateHuffmanTree();
		compressor4.generateCodeMap(compressor4.getHuffmanTree(), "");
		compressor4.compress("inputs/empty.txt", "inputs/emptyCompressed.txt");
		compressor4.decompress("inputs/emptyCompressed.txt", "inputs/emptyDecompressed.txt");
		
		Compressor compressor5 = new Compressor();
		compressor5.frequencyMap("inputs/single.txt");
		compressor5.generateQueue();
		compressor5.generateHuffmanTree();
		compressor5.generateCodeMap(compressor1.getHuffmanTree(), "");
		compressor5.compress("inputs/single.txt", "inputs/singleCompressed.txt");
		compressor5.decompress("inputs/singleCompressed.txt", "inputs/singleDecompressed.txt");
		
		
	}
	
}