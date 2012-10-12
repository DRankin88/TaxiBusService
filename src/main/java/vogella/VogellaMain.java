package vogella;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import sun.awt.image.ImageWatched.Link;
import utilities.Parser;

import database.AllPairsShortestPath;

public class VogellaMain {

	private static List<Vertex> nodes;
	private static List<Edge> edges;

	public static void main(String[] args) throws Exception {

		Parser parser = new Parser(args[0]);
		Graph busGraph = parser.parseFile();
		AllPairsShortestPath allPairsShortestPath = new AllPairsShortestPath(busGraph);
		LinkedList<Vertex> path = allPairsShortestPath.getPath("A", "D");

	}

}
