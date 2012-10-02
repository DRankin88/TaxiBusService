package main;

import graph.BusRouteGraph;
import graph.Edge;
import graph.Vertex;

public class Main {

	public static void main(String[] args) {

		// Create a graph
		BusRouteGraph routeMap = new BusRouteGraph("Edinburgh");
		
		//Give it some vertices
		Vertex A = new Vertex("A");
		Vertex B = new Vertex("B");
		Vertex C = new Vertex("C");
		Vertex D = new Vertex("D");
		Vertex E = new Vertex("E");
		
		routeMap.addVertex(A);
		routeMap.addVertex(B);
		routeMap.addVertex(C);
		routeMap.addVertex(D);
		routeMap.addVertex(E);
		
		//Create the edges
		Edge AtoB = new Edge("AtoB", 4, A, B);
		Edge AtoE = new Edge("AtoE", 10, A, E);
		
		Edge BtoC = new Edge("BtoC", 3, B, C);
		Edge BtoD = new Edge("BtoD", 11, B, D);
		
		Edge CtoE = new Edge("CtoE", 19, C, E);
		
		Edge DtoE = new Edge("DtoE", 7, D, E);
		
		Edge EtoA = new Edge("EtoA", 15, E, A);
		Edge EtoB = new Edge("EtoB", 13, E, B);

		routeMap.addEdge(AtoB);
		routeMap.addEdge(AtoE);
		routeMap.addEdge(BtoC);
		routeMap.addEdge(BtoD);
		routeMap.addEdge(CtoE);
		routeMap.addEdge(DtoE);
		routeMap.addEdge(EtoA);
		routeMap.addEdge(EtoB);
		
		
		//Check that our Vertices can find out their least cost path
		System.out.println ("The least cost path from A is: " + A.getLowestCostOutwardEdgeFromVertex().getEdgeName());
		System.out.println ("The least cost path from B is: " + B.getLowestCostOutwardEdgeFromVertex().getEdgeName());
		System.out.println ("The least cost path from C is: " + C.getLowestCostOutwardEdgeFromVertex().getEdgeName());
		System.out.println ("The least cost path from D is: " + D.getLowestCostOutwardEdgeFromVertex().getEdgeName());
		System.out.println ("The least cost path from E is: " + E.getLowestCostOutwardEdgeFromVertex().getEdgeName());
		
		

	}

}
