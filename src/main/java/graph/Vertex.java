package graph;

import java.util.ArrayList;

/**
 * Object representing a vertex of the graph. For the bus routes implementation this is a bus stop.
 * @author David Rankin
 *
 */
public class Vertex {
	
	public Vertex(String name) {
		
		this.name = name;
		outwardEdges = new ArrayList<Edge>();
		
	}
	
	private String name;
	private ArrayList<Edge> outwardEdges;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addOutwardEdge(Edge edge){
		
		outwardEdges.add(edge);
		
	}
	
	/**
	 * Loops through the list of outward Edges connected to the Vertex and returns the one with least cost
	 * @return
	 */
	public Edge getLowestCostOutwardEdgeFromVertex(){
		
		Edge leastCostOutwardEdge = outwardEdges.get(0);
		
		for (int i = 0; i < outwardEdges.size(); i++){
			
			if (outwardEdges.get(i).getWeight() < leastCostOutwardEdge.getWeight()){
				
				leastCostOutwardEdge = outwardEdges.get(i);
				
			}
			
		}
		
		return leastCostOutwardEdge;
	}
	

}
