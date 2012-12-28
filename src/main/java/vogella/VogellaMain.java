package vogella;

import java.util.List;

import algorithms.AssignClosestBus;
import algorithms.DynamicReroutingCostMinimization;

import scenarios.InputParser;
import utilities.Parser;
import controller.TimeStepper;
import database.AllPairsShortestPath;

public class VogellaMain {

	private static List<Vertex> nodes;
	private static List<Edge> edges;

	public static void main(String[] args) throws Exception {

		Parser graphParser = new Parser(args[0]);
		Graph busGraph = graphParser.parseFile();
		final AllPairsShortestPath allPairsShortestPath = new AllPairsShortestPath(busGraph);

		InputParser scenario = new InputParser(args[1]);
		TimeStepper timeStepper = new TimeStepper(busGraph,scenario, allPairsShortestPath);
		
		DynamicReroutingCostMinimization dynAlgo = new DynamicReroutingCostMinimization();
		
		while (true){
			dynAlgo.doAlgorithm(busGraph);
		//	AssignClosestBus.doAlgorithm();
		}
	}

}
