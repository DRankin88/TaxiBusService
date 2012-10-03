package search;

import java.util.ArrayList;

import graph.BusRouteGraph;
import graph.Vertex;

/**
 * Will hold search algorithms for graph objects.
 * @author David Rankin
 *
 */
public class PathFinder {

	public PathFinder (BusRouteGraph graph){
		
		this.busRouteGraph = graph;
		
	}
	
	private BusRouteGraph busRouteGraph;
	
	/**
	 * Finds the shortest path between a start and finish Vertex via a brute force search 
	 * @param startVertex the starting vertex to be searched from
	 * @param finishVertex the goal vertex we are looking to reach
	 * @return List of Vertices in order that should be traversed with get(0) being the start
	 */
	public ArrayList<Vertex> bruteForceSearch(Vertex startVertex, Vertex finishVertex){
		
		ArrayList<Vertex> pathList = new ArrayList<Vertex>();
		return pathList;
		
	}
	
	
	
	/**
	 * Finds the shortest path between two vertices using an A star search algorithm.
	 * @param startVertex Initial Vertex in the path
	 * @param finishVertex The final Vertex looking to be reached
	 * @return List of Vertices in order that should be traversed with get(0) being the start
	 */
	public ArrayList<Vertex> aStarSearch(Vertex startVertex, Vertex finishVertex) {
		
		ArrayList<Vertex> pathList = new ArrayList<Vertex>();
		
		ArrayList<Vertex> closedSet = new ArrayList<Vertex>();
		ArrayList<Vertex> openSet = new ArrayList<Vertex>();
		
		
		
		
		return pathList;
		
		
	}
	
	
	
	
}
