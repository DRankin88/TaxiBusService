package vogella;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VogellaMain {

	  private static List<Vertex> nodes;
	  private static List<Edge> edges;
	
	
	public static void main(String[] args) {
		
		 nodes = new ArrayList<Vertex>();
		    edges = new ArrayList<Edge>();
		    for (int i = 0; i < 5; i++) {
		      Vertex location = new Vertex("Node_" + i, "Node_" + i);
		      nodes.add(location);
		    }

		    addLane("Edge_0", 0, 4, 10);
		    addLane("Edge_1", 0, 1, 4);
		    addLane("Edge_2", 1, 3, 11);
		    addLane("Edge_3", 1, 2, 3);
		    addLane("Edge_4", 2, 4, 19);
		    addLane("Edge_5", 3, 4, 7);
		    addLane("Edge_6", 4, 0, 15);
		    addLane("Edge_7", 4, 1, 13);
		    addLane("Edge_8", 2, 3, 1);

		    // Lets check from location Loc_1 to Loc_10
		    Graph graph = new Graph(nodes, edges);
		    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		    dijkstra.execute(nodes.get(2));
		    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(4));
		    

		    
		    for (Vertex vertex : path) {
		      System.out.println(vertex);
		    }
		    
	
	}
	
	private static void addLane(String laneId, int sourceLocNo, int destLocNo,
		      int duration) {
		    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		    edges.add(lane);
		  }

}
