package vogella;

import java.util.List;
import java.util.Scanner;

import scenarios.InputParser;
import utilities.Parser;
import algorithms.DynamicReroutingCostMinimization;
import algorithms.MinimalRerouting;
import algorithms.NoReroutingAllowed;
import controller.BusCentralDatabase;
import controller.TimeStepper;
import database.AllPairsShortestPath;

public class VogellaMain {

	private static List<Vertex> nodes;
	private static List<Edge> edges;
	public static String outputFile;

	public static void main(String[] args) throws Exception {

		// graph,scenariom,savelocation
		Scanner myScanner = new Scanner(System.in);
		String input = myScanner.nextLine();
		String[] inputStringArray = input.split(" ");
		Parser graphParser = new Parser("./graphs/" + inputStringArray[0] + ".xml");
		Graph busGraph = graphParser.parseFile();
		final AllPairsShortestPath allPairsShortestPath = new AllPairsShortestPath(busGraph);
		
		InputParser scenario = new InputParser("./src/main/resources/" + inputStringArray[1] + ".txt");
		TimeStepper timeStepper = new TimeStepper(busGraph,scenario, allPairsShortestPath);

		outputFile = "L:/SimulationOutput/FINAL/" + inputStringArray[2] + ".xls";

		DynamicReroutingCostMinimization dynAlgo = new DynamicReroutingCostMinimization();
		NoReroutingAllowed noReAlgo = new NoReroutingAllowed();
		MinimalRerouting minReAlgo = new MinimalRerouting();
		BusCentralDatabase.setGraph(busGraph);

		while (true){
				noReAlgo.doAlgorithm(busGraph, allPairsShortestPath);
//				dynAlgo.doAlgorithm(busGraph, allPairsShortestPath);
//		  	    minReAlgo.doAlgorithm(busGraph, allPairsShortestPath);
			//	AssignClosestBus.doAlgorithm();
		}
	}

}
