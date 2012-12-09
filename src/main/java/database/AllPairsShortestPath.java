package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import vogella.DijkstraAlgorithm;
import vogella.Edge;
import vogella.Graph;
import vogella.Vertex;


/**
 * This uses the dijkstra classes to get the shortest path from every node in the graph to every other node
 * in the graph and stores it inside of a data structure to be used later in the routing decisions
 * @author David Rankin
 *
 */
public class AllPairsShortestPath {

	static final HashMap allPaths = new HashMap<String, HashMap>();

	public AllPairsShortestPath(Graph graph){

		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

		for (int i = 0; i < graph.getVertexes().size(); i++) {

			dijkstra.execute(graph.getVertexes().get(i));
			HashMap targetAndPath = new HashMap<String, LinkedList<Vertex>>();

			for (int a = 0; a < graph.getVertexes().size(); a++) {

				try{

					LinkedList<Vertex> path = dijkstra.getPath(graph.getVertexes().get(a));
					String nameOfTarget = graph.getVertexes().get(a).toString();
					targetAndPath.put(nameOfTarget, path);
					String startingNodeName = graph.getVertexes().get(i).toString();
					allPaths.put(startingNodeName, targetAndPath);

				}
				catch(NullPointerException e){}	
			}	
		}	
	}

	public static HashMap getAllPaths() {
		return allPaths;
	}
	
	public static LinkedList<Vertex> getPath(String startVertex, String finalVertex){
		
		HashMap temp = (HashMap) allPaths.get(startVertex);
		return (LinkedList<Vertex>) temp.get(finalVertex);
		
	}

}
