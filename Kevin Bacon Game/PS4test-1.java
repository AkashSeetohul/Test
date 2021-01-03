import java.util.HashSet;
import java.util.List;

public class PS4test extends GraphLib{
	
	public static void main(String [] args) {
		Graph<String, HashSet<String>> ActorsMap = new AdjacencyMapGraph<String, HashSet<String>>();
	
		ActorsMap.insertVertex("Kevin Bacon");
		ActorsMap.insertVertex("Alice");
		ActorsMap.insertVertex("Bob");
		ActorsMap.insertVertex("Charlie");
		ActorsMap.insertVertex("Dartmouth (Earl thereof)");
		ActorsMap.insertVertex("Nobody");
		ActorsMap.insertVertex("Nobody's Friend");
		
		
		HashSet<String> KevinAlice = new HashSet<String>();
		KevinAlice.add("A Movie");
		KevinAlice.add("E movie");
		
		HashSet<String> AliceCharlie = new HashSet<String>();
		AliceCharlie.add("D Movie");
		
		HashSet<String> AliceBob = new HashSet<String>();
		AliceBob.add("A Movie");
		
		HashSet<String> KevinBob = new HashSet<String>();
		KevinBob.add("A Movie");
		
		HashSet<String> CharlieBob = new HashSet<String>();
		CharlieBob.add("C Movie");
		
		HashSet<String> CharlieDartmouth = new HashSet<String>();
		CharlieDartmouth.add("B Movie");
		
		HashSet<String> NobodyFriend = new HashSet<String>();
		NobodyFriend.add("F Movie");
		
		ActorsMap.insertUndirected("Kevin Bacon", "Alice", KevinAlice);
		ActorsMap.insertUndirected("Alice", "Charlie", AliceCharlie);
		ActorsMap.insertUndirected("Kevin Bacon", "Bob", KevinBob);
		ActorsMap.insertUndirected("Alice", "Bob", AliceBob);
		ActorsMap.insertUndirected("Bob", "Charlie", CharlieBob);
		ActorsMap.insertUndirected("Charlie", "Dartmouth (Earl thereof)", CharlieDartmouth);
		ActorsMap.insertUndirected("Nobody", "Nobody's Friend", NobodyFriend);
		
		List<String> VerticesByDegree = verticesByInDegree(ActorsMap);
		
		String Center = VerticesByDegree.get(0);
		System.out.println(Center);
		
		Graph<String, HashSet<String>> BFSMap= bfs(ActorsMap, Center);
		
		List<String> path = getPath(BFSMap, "Dartmouth (Earl thereof)");
		System.out.println(path);
		
		double AverageSeparation = averageSeparation(BFSMap, Center);
		System.out.println(AverageSeparation);
	}
}
