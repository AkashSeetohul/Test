/*
 * 
 * Author Tooryanand Seetohul with partner Arun Maganti
 * 
 */


import java.util.Comparator;

public class TreeComparator implements Comparator<BinaryTree<CharFreq>>{
	
	@Override
	public int compare(BinaryTree<CharFreq> a, BinaryTree<CharFreq> b) {
		if (a.getData().getFrequency().intValue() == b.getData().getFrequency().intValue()) {
			return 0;
		} else if (a.getData().getFrequency().intValue() < b.getData().getFrequency().intValue()){
			return -1;
		} else {
			return 1;
		}
	}
}
