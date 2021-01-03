import java.util.Comparator;


public class StringDoubleComparator implements Comparator<StringDoublePair>{
	
	@Override
	public int compare(StringDoublePair a, StringDoublePair b) {
		if (a.getValue() == b.getValue()) {
			return 0;
		}else if (a.getValue() < b.getValue()) {
			return -1;
		} else {
			return 1;
		}

	}
}

