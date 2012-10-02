package graph;

import java.util.ArrayList;

/**
 * Manages anything and everything to do with the static graph that is used for the bus routes
 * @author David Rankin
 *
 */
public class BusRouteGraph {

	public BusRouteGraph(String name){
		
		this.name = name;
		listOfVertices = new ArrayList<Vertex>();
		listOfEdges = new ArrayList<Edge>();
	}

	private String name;
	private ArrayList<Vertex> listOfVertices;
	private ArrayList<Edge> listOfEdges;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Vertex> getListOfVertices() {
		return listOfVertices;
	}
	public void setListOfVertices(ArrayList<Vertex> listOfVertices) {
		this.listOfVertices = listOfVertices;
	}
	public ArrayList<Edge> getListOfEdges() {
		return listOfEdges;
	}
	public void setListOfEdges(ArrayList<Edge> listOfEdges) {
		this.listOfEdges = listOfEdges;
	}
	
	
	public void addVertex(Vertex vertex){
		
		listOfVertices.add(vertex);
		
	}
	
	public void addEdge(Edge edge){
		
		listOfEdges.add(edge);
		edge.getStartingVertex().addOutwardEdge(edge);
		
	}

	
}
