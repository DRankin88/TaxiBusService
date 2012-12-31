package vogella;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public Vertex getVertex(String vertexName){

		Vertex vertex = null;

		for (int i = 0; i < vertexes.size(); i++){

			if (vertexes.get(i).getName().equals(vertexName)){

				vertex = vertexes.get(i);

			}

		}

		return vertex;

	}

	public Edge getEdgeBetweenVertices(Vertex firstVertex, Vertex secondVertex){
		
		Edge returnEdge = null;
		
		for (int i = 0; i < edges.size(); i++){
			
			Edge edge = edges.get(i);
			Vertex vertexA = edge.getSource();
			Vertex vertexB = edge.getDestination();
			
			if (vertexA.equals(firstVertex) && vertexB.equals(secondVertex)){
				
				returnEdge = edge;
				
			}
			
		}
		
		return returnEdge;
		
	}

} 