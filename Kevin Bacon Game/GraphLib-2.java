import java.util.*;
import java.lang.Math;
/**
 * Library for graph analysis
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author Tooryanand Seetohul
 */
public class GraphLib {
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		// if V is not in the graph, return null
		if (!g.hasVertex(start)) {
			return null;
		}
		ArrayList<V> path = new ArrayList<V>();
		
		if (steps == 0) {
			path.add(start);
			return path;
		}
		
		V current = start;
		for (int x = 0; x < steps; x++) {
			// check if current has out edges
			if (g.outDegree(current) > 0 && !path.contains(current)) {
				//  add current to path
				path.add(current);
				
				Iterator<V> NextNeighbors = g.outNeighbors(current).iterator();
				// loop until a random neighbor
				for (int z = 0; z < (int) (Math.random() * (g.outDegree(current)+1)); z++) {
					while (NextNeighbors.hasNext()) {
						current = NextNeighbors.next();
					}
				}
			} else {
				// if there is no more out-edges, return path
				return path;
			}
		}
		return path;
	}
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		
		Iterator<V> vertices = g.vertices().iterator();
		ArrayList<VertexNeighborPair<V>> VertexPair = new ArrayList<VertexNeighborPair<V>>();
		ArrayList<V> SortedVertices = new ArrayList<V>();
		
		// looping through all vertices and pairing each with inDegree number
		while (vertices.hasNext()) {
			V vertex = vertices.next();
			VertexPair.add(new VertexNeighborPair<V>(vertex, g.inDegree(vertex)));
		}
		
		// sorting in descending order for in-degree
		VertexPair.sort(new VerticesComparator<V>());
		
		// add to Sorted Vertices list
		for (VertexNeighborPair<V> a: VertexPair) {
			SortedVertices.add(a.getVertex());
		}
		
		return SortedVertices;
		
	}
	
	/*
	 * Using bfs to find a shortest path tree for a current center of the universe
	 * 
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
		
		Graph<V,E> pathTree = new AdjacencyMapGraph<V,E>();
		pathTree.insertVertex(source);
		Set<V> visited = new HashSet<V>();
		Queue<V> queue = new LinkedList<V>();
		
		queue.add(source);
		visited.add(source);
		
		while (!queue.isEmpty()) {
			V u = queue.remove();
			for (V v :g.outNeighbors(u)) {
				if (!visited.contains(v)) {
					visited.add(v);
					queue.add(v);
					
					// insert vertex to graph
					pathTree.insertVertex(v);
					pathTree.insertDirected(v, u, g.getLabel(v, u));
				}
			}
		}
		
		return pathTree;
	}
	
	/*
	 * 
	 * given a shortest path tree and a vertex, construct a path from vertex back to center of universe
	 * 
	 */
	public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
		List<V> path = new ArrayList<V>();
		
		// check if tree contains vertex
		if (!tree.hasVertex(v)) {
			return null;
		}
		
		path.add(v);
		
		Iterator<V> neighbor = tree.outNeighbors(v).iterator();
		V u = neighbor.next();
		
		// stops looping when center is reached
		while (tree.outDegree(u)!=0) {
			path.add(u);
			neighbor = tree.outNeighbors(u).iterator();
			
			if(neighbor==null) break;
			
			u = neighbor.next(); // update u

		}
		
		path.add(u); // adding the center to the path
		return path;
	}
	
	/*
	 * Given a graph and a subgraph (here shortest path tree),
	 *  determine which vertices are in the graph 
	 *  but not the subgraph (here, not reached by BFS).
	 * 
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
		Set<V> MissingVertices = new HashSet<V>();
		
		Iterable<V> GraphVertices = graph.vertices();
		
		for (V u: GraphVertices) {
			if (!subgraph.hasVertex(u)) {
				MissingVertices.add(u);
			}
		}
		
		return MissingVertices;
	}

	/*
	 * 
	 * Find the average distance-from-root in a shortest path tree
	 * 
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		
		Iterable<V> NeighborList = tree.inNeighbors(root);
		double TotalDistance = 0.0;
		
		for (V u: NeighborList) {
			TotalDistance += TotalDistance(tree, u);
			
		}
		
		// each edge represents a path
		return TotalDistance/tree.inDegree(root);
	}
	
	// helper method for recursion along a path
	public static <V,E> double TotalDistance(Graph<V,E> tree, V root) {
		
		double TotalDistance = 0.0;
		
		// if it is an inner node
		if (tree.inDegree(root) == 1) {
			Iterable<V> NeighborList = tree.outNeighbors(root);
			for (V u: NeighborList) {
					TotalDistance += TotalDistance(tree, u);
				}
			}
		return (TotalDistance + 1);
	}
}