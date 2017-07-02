package Connected;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/* 
* A Class representing of a Graph (undirected)
* This Graph implementation is using an Adjacency list to store vertices, such that every vertex stores a list of adjacent vertices. 
* This data structure allows the storage of additional data on the vertices (String representing the name of cities).
* @author Gil Skeif
*/
class Graph {

	// C:\YOUR\WORKSPACE> java your.package.YouMainClass
	static String workSpacePath = "D:\\workspace2017\\GraphAlgorithms\\src\\Connected\\";

	// graphAdjacencyList
	private HashMap<String, ArrayList<String>> graphAdjacencyList;

	// Constructor
	public Graph() {
		graphAdjacencyList = new HashMap<String, ArrayList<String>>();
	}

	/*
	 * Instance method reading file, and writing it to graph
	 * 
	 * @param String fileName
	 * 
	 * @throws FileNotFoundException
	 */
	public void addFileDataToGraph(String fileName) {
		File file = new File(workSpacePath + fileName);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				// Read one line at a time from source file
				String[] edeges = scanner.nextLine().split(",");
				for (int i = 0; i < edeges.length; i++) {
					// Parse all the edges per line
					edeges[i] = edeges[i].trim().replaceAll("\n ", "");
					// Add vertex to the Graph
					addVertex(edeges[i]);
				}
				// Add edge to graph
				addEdge(edeges[0], edeges[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * instance method to add vertex to graph
	 * 
	 * @param String vertex
	 */
	public void addVertex(String vertex) {
		if (!graphAdjacencyList.containsKey(vertex)) {
			graphAdjacencyList.put(vertex, new ArrayList<String>());
		}
	}

	/*
	 * instance method to add a non directed edge (v,w) into the graph
	 * 
	 * @param String v representing edge vertex
	 * 
	 * @param String w representing edge vertex
	 */
	public void addEdge(String v, String w) {
		// Adding a path from v to w
		graphAdjacencyList.get(v).add(w);
		// Adding a path from w to v
		graphAdjacencyList.get(w).add(v);
	}

	/*
	 * @param String source vertex
	 * 
	 * @param String target vertex method implementing BFS algorithm on an
	 * undirected graph used to determine if there is a valid path between
	 * Source to Target O(E + V) runtime
	 */
	public void breathFirstSearch(String source, String target) {
		// if source or target vertex was not added to graph from file it is not
		// connected
		if (graphAdjacencyList.get(source) == null || graphAdjacencyList.get(target) == null) {
			System.out.println("no");
		} else {
			// source or target vertex were added to graph, check if Connected
			boolean isConnected = false;

			// Mark all the vertices as not visited(By default set as false)
			ArrayList<String> visited = new ArrayList<String>();
			// Create a queue for BFS
			LinkedList<String> queue = new LinkedList<String>();

			// Mark the current node as visited and enqueue it
			visited.add(source);
			queue.add(source);

			while (queue.size() != 0 && !isConnected) {
				// Dequeue a vertex from queue and print it
				source = queue.poll();
				if (source.equals(target)) {
					isConnected = true;
					System.out.println("yes");
				}
				// Get all adjacent vertices of the dequeued vertex
				// if an adjacent vertex has not been visited yet mark it
				// visited and enqueue it
				for (String vertex : graphAdjacencyList.get(source)) {
					if (!visited.contains(vertex)) {
						visited.add(vertex);
						queue.add(vertex);
					}
				}
			}
			if (!isConnected) {
				System.out.println("no");
			}
		}
	}

	public static void main(String args[]) {
		connected(args[0], args[1], args[2]);
	}

	/*
	 * @param String fileName
	 * 
	 * @param String source vertex
	 * 
	 * @param String target vertex Static method that reads in such a file and
	 * outputs whether two specified cities are connected.
	 * 
	 * @ Linear runtime: O(N)
	 */
	public static void connected(String fileName, String cityName01, String cityName02) {
		Graph g = new Graph();
		g.addFileDataToGraph(fileName);
		g.breathFirstSearch(cityName01, cityName02);
	}
}
