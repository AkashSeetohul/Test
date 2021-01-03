import java.util.Comparator;


public class VerticesComparator<V> implements Comparator<VertexNeighborPair<V>>{

	public int compare(VertexNeighborPair<V> a, VertexNeighborPair<V> b) {
		if (a.getNumNeighbors() == b.getNumNeighbors()) {
			return 0;
		}else if (a.getNumNeighbors() < b.getNumNeighbors()) {
			// 1 because we want decreasing order
			return 1;
		} else {
			// -1 because we want decreasing order
			return -1;
		}

	}
}
