package graph;


/**
 * Class represents an Edge in the graph. In the bus routes model this is like a road
 * @author David Rankin
 *
 */
public class Edge {
	
	public Edge (String edgeName, double weight, Vertex startingVertex, Vertex finishingVertex){
		
		this.edgeName = edgeName;
		this.weight = weight;
		this.startingVertex = startingVertex;
		this.finishingVertex = finishingVertex;
		
	}
	
	private String edgeName;
	private double weight;
	private Vertex startingVertex;
	private Vertex finishingVertex;
	
	public String getEdgeName() {
		return edgeName;
	}
	public void setEdgeName(String edgeName) {
		this.edgeName = edgeName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Vertex getStartingVertex() {
		return startingVertex;
	}
	public void setStartingVertex(Vertex startingVertex) {
		this.startingVertex = startingVertex;
	}
	public Vertex getFinishingVertex() {
		return finishingVertex;
	}
	public void setFinishingVertex(Vertex finishingVertex) {
		this.finishingVertex = finishingVertex;
	}


	
	

}
