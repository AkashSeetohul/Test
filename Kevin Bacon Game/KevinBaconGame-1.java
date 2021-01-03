/*
 * 
 * PS4
 * @Author Tooryananand Seetohul
 * Based on instructions from Professor Timothy Pierson
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class KevinBaconGame extends GraphLib{
	
	public Map<Integer, String> IDActors = new HashMap<Integer, String>(); // map that stores Actor ID as key and Actor Name as value
	public Map<Integer, String> IDMovie = new HashMap<Integer, String>();  // map that stores Movie ID as key and Movie Name as value
	
	public Map<String, Double> CenterPaths = new HashMap<String, Double>();  // map that hold Actor Name as key and their Average Separation if they were centers
	public ArrayList<String> SortedCenters = new ArrayList<String>();  // sorted list of Actors by their separation (Minimum to maximum)
	
	public Map<String, ArrayList<String>> MoviesActors = new HashMap<String, ArrayList<String>>(); //// map that stores Movie ID as key and  list of co-stars as value
	
	public Graph<String, HashSet<String>> graph = new AdjacencyMapGraph<String, HashSet<String>>();
	public Graph<String, HashSet<String>> BaconGraph = new AdjacencyMapGraph<String, HashSet<String>>();
	
	/*
	 *  Method that opens file,
	 *  reads line by line,
	 *  and build ID-Object map.
	 *  
	 */
	public static void BuildMap(String path, Map<Integer, String> map){
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line;
			
			while ((line = in.readLine()) != null) {
				String[] KeyValue = line.split("\\|");
					map.put(Integer.parseInt(KeyValue[0]), KeyValue[1]);
				}
			
			in.close();
			
		} catch (IOException e){
			System.err.println("Error reading files!");
		}
	}
	
	/*
	 *  Method that opens file,
	 *  reads line by line,
	 *  and build ID-ListOfObject map.
	 *  
	 */
	public void BuildMapList(String path, Map<String, ArrayList<String>> map) throws IOException{
			
	try {
			
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
			
		while ((line = in.readLine()) != null) {
			String[] KeyValue = line.split("\\|");
			
			// if the map already has this key, add the actor to the actorlist(value)
			if (MoviesActors.containsKey(IDMovie.get(Integer.parseInt(KeyValue[0])))) {
										
					ArrayList<String> ActorList = MoviesActors.get(IDMovie.get(Integer.parseInt(KeyValue[0])));
					
					if (IDActors.get(Integer.parseInt(KeyValue[1])) != null){
						ActorList.add(ActorList.size(), IDActors.get(Integer.parseInt(KeyValue[1])));
					}
					
					MoviesActors.put(IDMovie.get(Integer.parseInt(KeyValue[0])), ActorList);
			
			// otherwise, initialize a new list, add actor and put into map for this key
			} else {

				ArrayList<String> ActorList = new ArrayList<String>();
				ActorList.add(IDActors.get(Integer.parseInt(KeyValue[1])));
					
				MoviesActors.put(IDMovie.get(Integer.parseInt(KeyValue[0])), ActorList);
			}
		}		
			in.close();
			
		} catch (IOException e) {
			System.err.println("Error reading files!");
		}
	}
	
	/*
	 * Method which builds all the essential files
	 */
	public void BuildAllMaps() throws IOException{
		BuildMap("inputs/actors.txt", IDActors);
		BuildMap("inputs/movies.txt", IDMovie);
		BuildMapList("inputs/movie-actors.txt", MoviesActors);
	}
	
	/*
	 * 
	 *  Adding all the actor names as vertices
	 */
	public void addVertices() {
		Set<Integer> Actors = IDActors.keySet();
		Iterator<Integer> ActorsIterator = Actors.iterator();

		while (ActorsIterator.hasNext()) {
			graph.insertVertex(IDActors.get(ActorsIterator.next())); // inserting to undirected  graph
		}
	}
	
	/*
	 * Looks for common movies between different actors,
	 * add them to a HashSet,
	 * Add Hashset as UndirectedEdge
	 * 
	 */
	public void addEdges() {
		Set<String> MovieActor = MoviesActors.keySet();
		Iterator<String> MovieActorIterator = MovieActor.iterator();
		
		while(MovieActorIterator.hasNext()){
			String CurrentKey = MovieActorIterator.next();
			ArrayList<String> ActorsCommon = MoviesActors.get(CurrentKey);
			
			// loop through every actor
			for (String x: ActorsCommon) {
				// add edge to every other actor in the same list
				for (String y: ActorsCommon) {
					
					if (y != x) {
						// if they already have an edge, add to the set
						if (graph.hasEdge(x, y)) {
							graph.getLabel(x, y).add(CurrentKey);
						} else {
							// add a new HashSet
							HashSet<String> Edge = new HashSet<String>();
							Edge.add(CurrentKey);
							graph.insertUndirected(x, y, Edge);
						}
					}
				}
			}
		}
	}
	
	/*
	 * Finds the average center of separation of all vertices acting as center,
	 * add to map
	 *  
	 */
	public void findLength() {
		
		// all actors which are in the bacon universe 
		Iterator<String> vertex = BaconGraph.vertices().iterator();
		Graph<String, HashSet<String>> tempBFS = new AdjacencyMapGraph<String, HashSet<String>>();
		
		while (vertex.hasNext()) {
			String x = vertex.next();
			tempBFS = bfs(graph, x);
			double averageSeparation = averageSeparation(tempBFS, x); // average separation if they were serving as center
			CenterPaths.put(x, averageSeparation);
			
		}
		
	}
	
	/*
	 * Sort the center of the universe
	 * 
	 */
	public void CenterOfUniverse() {
		Iterator<String> KeySet = CenterPaths.keySet().iterator();
		ArrayList<StringDoublePair> Pair = new ArrayList<StringDoublePair>();
		
		// pairing each actor with its average separation
		while (KeySet.hasNext()) {
			String string = KeySet.next();
			Pair.add(new StringDoublePair(string, CenterPaths.get(string)));
		}
		
		// sorting in an arraylist
		Pair.sort(new StringDoubleComparator());
		
		// add to Sorted list
		for (StringDoublePair a: Pair) {
				SortedCenters.add(a.getString());
			}
	}
	
	/*
	 * Method that initializes the game,
	 * Kevin Bacon is the center by default
	 * 
	 */
	public void initializeGame() {
		BaconGraph = bfs(graph, "Kevin Bacon");
		int NumConnected = BaconGraph.numVertices();
		System.out.println("Kevin Bacon" + " is now the center of the acting universe, connected to " + NumConnected + "/9235 actors with average separation " + averageSeparation(BaconGraph, "Kevin Bacon"));
	}
	
	/*
	 * Method to change the center of universe
	 * 
	 */
	public void changeCenter(String center) {
		// check if a valid input has been given
		if (!graph.hasVertex(center)) {
			System.err.println("Error! Invalid Actor!");	
		} else {
			BaconGraph = bfs(graph, center);
			int NumConnected = BaconGraph.numVertices();
			System.out.println(center + " is now the center of the acting universe, connected to " + NumConnected + "/9235 actors with average separation " + averageSeparation(BaconGraph, center));
		}
	}
	
	// list actors with infinite separation from the current center
	public void findInfiniteSeparation() {
		Set<String> actors = new HashSet<String>();
		actors = missingVertices(graph, BaconGraph);
		
		Iterator<String> ActorsIterator = actors.iterator();
		while (ActorsIterator.hasNext()) {
			System.out.println(ActorsIterator.next() + ", ");
		}
	}
	
	// method to print out top or bottom centers of the universe, based on average separation
	public void printCenters(int i) {
		// if input is a negative number
		if (i < 0) {
			i = i * -1; // convert to positive
			
			if (i < SortedCenters.size() - 1) {
				// print first i actors
				for (int x = 0; x < i; x++) {
					System.out.print(SortedCenters.get(x) + " ");
				}
			}
		}
		
		// if input is a positive number
		if (i < SortedCenters.size()) {
			// print last i actors
			for (int x = SortedCenters.size()-i; x < SortedCenters.size(); x++) {
				System.out.print(SortedCenters.get(x) + " ");
			}
		}
		
		
	}
	
	// find path of actor to the center of the universe
	public void findpath(String ActorName) {
		// check if input is valid
		if (!BaconGraph.hasVertex(ActorName)) {
			System.out.println("Invalid Actor");
		}
		else {
			java.util.List<String> path = getPath(BaconGraph, ActorName);
			System.out.println(ActorName + "'s number is " + (path.size()-1));
		
			// looping through the path
			int i = 0;
			int j = i + 1;
		
			while (j < path.size()) {
				HashSet<String> EdgeLabel = BaconGraph.getLabel(path.get(i), path.get(j));
				System.out.println(path.get(i) + " appeared in " + EdgeLabel + " with " + path.get(j));
				i++;
				j++;
			}
		}
	}
	

	
	
	
	/*
	 * 
	 * Method that calls all the other essential methods to start the game,
	 * calls required methods in a sequential order,
	 * also prints the commands available for the game.
	 * 
	 */
	public void StartGame() throws Exception {
		System.out.println("Commands: ");
		System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation");
		System.out.println("i: list actors with infinite separation from the current center\n" + 
				"p <name>: find path from <name> to current center of the universe\n" + 
				"u <name>: make <name> the center of the universe\n" + "q: Quit the game!\n");
		
		BuildAllMaps();
		addVertices();
		addEdges();
		initializeGame();
		findLength();
		CenterOfUniverse();
	}
	
	/*
	 * 
	 * Method that calls the approriate method whenever an input is obtained;
	 * if no such functionality exists,
	 * inform player as invalid
	 * 
	 */
	public void HandleCases(String string) {
		
		if (string.equals("i")) {
			findInfiniteSeparation();
		} else {
			String[] StrArray= string.split(" ", 2);
			if (StrArray[0].equals("p")) {
				findpath(StrArray[1]);
			} else if (StrArray[0].equals("u")) {
				changeCenter(StrArray[1]);
			} else if (StrArray[0].equals("c")) {
				printCenters(Integer.parseInt(StrArray[1]));
			} else {
				System.out.println("Invalid!");
			}
		}
	}
	
	
}
