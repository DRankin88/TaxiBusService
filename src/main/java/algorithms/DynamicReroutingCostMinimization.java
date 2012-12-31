package algorithms;

import hamilton.HamiltonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import passengers.Passenger;
import sun.awt.image.ImageWatched.Link;
import vogella.Edge;
import vogella.Graph;
import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;
import database.AllPairsShortestPath;

/**
 * This is the algorithm described in my emails to Murray Cole
 * @author David Rankin
 *
 */
public class DynamicReroutingCostMinimization {

	private static int count = -1;
	private Graph busGraph;
	private AllPairsShortestPath allPairsShortestPath;

	public void doAlgorithm(Graph busGraph, AllPairsShortestPath allPairsShortestPath){

		this.busGraph = busGraph;
		this.allPairsShortestPath = allPairsShortestPath;

		if(count < 120) {
			count++;
			System.out.println("TimeStep " + count);
			BusCentralDatabase.printStateOfWorld();

		}
		if (BusCentralDatabase.getBusesInTheWorld().size() > 0){
			// Firstly any bus that is at a stop in which a passenger could be dropped off should do so
			//		dropOffPassengers();

			// If there are unallocated passengers then we may need to change the paths that buses are currently moving along
			if (BusCentralDatabase.getUnallocatedPassengers().size() > 0){
				dynamicReroutingAndPassengerAllocation();
			}

			// TODO Pick up passenger if bus is at destination
			//		pickupPassengers();

			// Tell all the buses to move along their routes to the next location
			//		incrimentBuses();
		}
		TimeStepper.step();



	}

	private void dropOffPassengers(){

		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < allBuses.size(); i++) {

			Bus bus = allBuses.get(i);
			ArrayList<Passenger> busesDropOffs = bus.getPassengersWantThisStop();

			// Remove all droppable passengers from every bus
			for (int l = 0; l < busesDropOffs.size(); l++){

				busesDropOffs.get(l).setDroppedOff(true);
				int remove = BusCentralDatabase.getPassengerInTheWorld().indexOf(busesDropOffs.get(l));
				BusCentralDatabase.getPassengerInTheWorld().remove(remove);
				bus.removePassenger(busesDropOffs.get(l));

			}	
		}
	}

	// Sets the paths for all the buses.
	private void dynamicReroutingAndPassengerAllocation(){

		// For testing we deal with just the first bus
		Bus firstBus = BusCentralDatabase.getBusesInTheWorld().get(0);

		// For this bus hand the helper method the needed information to calculate the potential best path to serve all of the customers

		// For the rootNode
		Vertex rootNode = firstBus.getCurrentStop();

		// If the bus is between stops then the rootNode will be the next node
		if (firstBus.getCostToNextStop() != 0){

			rootNode = firstBus.getPath().get(1);

		}

		// Getting the list of pickups. These are the stops the will be part of the picking up of a passenger
		ArrayList<Passenger> pickups = BusCentralDatabase.getUnallocatedPassengers();
		ArrayList<Vertex> pickupStops = new ArrayList<Vertex>();
		for (int i = 0; i < pickups.size(); i++){

			if (!pickupStops.contains(pickups.get(i).getStartingStop())){

				pickupStops.add(pickups.get(i).getStartingStop());

			}
		}

		// Getting the list of dropoff. These include the destination of passengers currently on the bus and passengers 
		ArrayList<Vertex> dropoffs = new ArrayList<Vertex>();
		ArrayList<Passenger> busPassengers = firstBus.getPassengersOnBus();
		for (int j = 0; j < busPassengers.size(); j++){

			if (!dropoffs.contains(busPassengers.get(j).getDestinationStop())){

				dropoffs.add(busPassengers.get(j).getDestinationStop());

			}			
		}

		for (int i = 0; i < pickups.size(); i++){

			if (!dropoffs.contains(pickups.get(i).getDestinationStop())){

				dropoffs.add(pickups.get(i).getDestinationStop());

			}
		}

		// This should have added all pickup and dropoff stops and the root. Now must pair the pickups and dropoffs
		ArrayList<ArrayList<Vertex>> pairs = new ArrayList<ArrayList<Vertex>>();
		for (int i = 0; i < pickups.size(); i++){

			Vertex start = pickups.get(i).getStartingStop();
			Vertex finish = pickups.get(i).getDestinationStop();
			ArrayList<Vertex> tuple = new ArrayList<Vertex>();
			tuple.add(start);
			tuple.add(finish);
			pairs.add(tuple);

		}

		LinkedList<Vertex> optimalPath = bestPath(rootNode, pickupStops, dropoffs, pairs);

		System.out.println("dbdf");
	}

	/**
	 * Calculates the shortest path that hits every stop for pickups and dropoffs of a list of passengers
	 * @param bus Bus that has to do the work
	 * @param passengers Passengers it has to hit
	 */
	private LinkedList<Vertex> bestPath(Vertex rootNode, ArrayList<Vertex> pickups, ArrayList<Vertex> dropOffs, ArrayList<ArrayList<Vertex>> pairs){

		int numberOfStops = 1 + pickups.size() + dropOffs.size();

		HamiltonPath HamPath = new HamiltonPath();

		int[][] adjMatrix;

		adjMatrix = new int[numberOfStops][numberOfStops];

		for (int i = 0; i < numberOfStops; i++){
			for (int j = 0; j < numberOfStops; j++){

				adjMatrix[i][j] = 1;

			}
		}

		HamPath.HamiltonPath(adjMatrix, 1);
		ArrayList<int[]> enumeratedPaths = HamPath.getAllPaths();

		// Create a mapping of hampath nodes to graph vertices
		HashMap<Integer, Vertex> map = new HashMap<Integer, Vertex>();
		// Place the root as one
		map.put(1, rootNode);



		// place the pickups
		for (int i = 2, j = 0; i < pickups.size() + 2; i++, j++){

			map.put(i, pickups.get(j));

		}

		int terminator = map.size() + 1 + dropOffs.size();

		// place the dropoffs
		for (int i = map.size() + 1, j = 0; i < terminator; i++, j++){

			map.put(i, dropOffs.get(j));

		}

		ArrayList<LinkedList<Vertex>> possiblePaths = new ArrayList<LinkedList<Vertex>>();

		// Cull the paths that are not possible because the drop off stop is being visited before the pickup stop
		for (int i = 0; i < enumeratedPaths.size(); i++){

			LinkedList<Vertex> path = hamToPath(enumeratedPaths.get(i), map);

			if (!happensBeforeViolation(path, pairs)){

				// Before we add this path to the list of possible paths we must first convert it into a real path in the graph
				path = convertToRealPath(path);
				possiblePaths.add(path);

			}
		}

		// Now need to find the shortest path from the possible paths.

		LinkedList<Vertex> shortestPath = possiblePaths.get(0);

		for (int i = 1; i < possiblePaths.size(); i++){

			if (sizeOfPath(shortestPath) > sizeOfPath(possiblePaths.get(i))) {

				shortestPath = possiblePaths.get(i);

			}
		}

		return shortestPath;

	}
	
	private LinkedList<Vertex> convertToRealPath(LinkedList<Vertex> path){
		
		LinkedList<Vertex> finalPath = new LinkedList<Vertex>();
		
		for (int i = 0; i < path.size() - 1; i++){
			
			LinkedList<Vertex> temp = allPairsShortestPath.getPath(path.get(i).getName(), path.get(i+1).getName());
			finalPath.addAll(temp);
			
		}
		
		for (int j = 0; j < finalPath.size() - 1; j++){
			
			Vertex current = finalPath.get(j);
			Vertex next = finalPath.get(j+1);
			
			if (current.equals(next)){
				
				finalPath.remove(current);
				
			}
		}
		
		return finalPath;
		
	}

	private int sizeOfPath (LinkedList<Vertex> path){

		int numberOfEdges = path.size() - 1;
		int cost = 0;

		for (int i = 0; i < numberOfEdges; i++){

			Edge edge = busGraph.getEdgeBetweenVertices(path.get(i), path.get(i+1));
			cost += edge.getWeight();

		}

		return cost;

	}

	/**
	 * Reports true if the path in question violates the happens before relation of pickups and drop offs.
	 * @param path
	 * @param pairs
	 * @return
	 */
	private boolean happensBeforeViolation (LinkedList<Vertex> path, ArrayList<ArrayList<Vertex>> pairs){

		for (int i = 0; i < pairs.size(); i++){

			Vertex pickup = pairs.get(i).get(0);
			Vertex dropoff = pairs.get(i).get(1);

			int pickupIndex = path.indexOf(pickup);
			int dropoffIndex = path.indexOf(dropoff);

			if (dropoffIndex < pickupIndex){

				return true;

			}

		}

		return false;

	}

	/**
	 * Turns a hamiltonian path into a list of vertexs representing a bus path in the graph
	 * @param hampath the array of integers representing a hamiltonian path
	 * @return The path as a list of Vertices
	 */
	private LinkedList<Vertex> hamToPath(int[] hampath, HashMap<Integer, Vertex> mappingMap){

		LinkedList<Vertex> path = new LinkedList<Vertex>();

		for (int i = 0; i < hampath.length; i++){

			path.add(mappingMap.get(hampath[i]));

		}

		return path;

	}

}
