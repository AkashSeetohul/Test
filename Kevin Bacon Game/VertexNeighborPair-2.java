
public class VertexNeighborPair<V> {
	public V Vertex;
	public int NumNeighbors;
	
	public VertexNeighborPair(V a, int b){
		this.Vertex = a;
		this.NumNeighbors = b;
	}
	
	public V getVertex() {
		return Vertex;
	}
	
	public int getNumNeighbors() {
		return NumNeighbors;
	}
}
