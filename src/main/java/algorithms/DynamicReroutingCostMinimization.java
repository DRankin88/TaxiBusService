package algorithms;

import hamilton.HamiltonPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import passengers.Passenger;
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

	private static int count = 0;
	private Graph busGraph;
	private AllPairsShortestPath allPairsShortestPath;

	public void doAlgorithm(Graph busGraph, AllPairsShortestPath allPairsShortestPath){

		TimeStepper.step();

		this.busGraph = busGraph;
		this.allPairsShortestPath = allPairsShortestPath;

		if(count < 500) {
			count++;
			System.out.println("TimeStep " + count);
			BusCentralDatabase.printStateOfWorld();

		}

		if (BusCentralDatabase.getBusesInTheWorld().size() > 0){
			// Firstly any bus that is at a stop in which a passenger could be dropped off should do so
			dropOffPassengers();

			// If there are unallocated passengers then we may need to change the paths that buses are currently moving along
			if (BusCentralDatabase.getUnallocatedPassengers().size() > 0){
				dynamicReroutingAndPassengerAllocation();
			}

			// Pick up passenger if bus is at destination
			pickupPassengers();

			// Tell all the buses to move along their routes to the next location
			incrimentBuses();
		}
	}

	private void pickupPassengers(){

		ArrayList<Bus> busesInTheWorld = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);

			if (bus.getCostToNextStop() == 0){

				bus.pickupPassengers(TimeStepper.getTime());

			}	
		}		
	}

	private void dropOffPassengers(){

		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < allBuses.size(); i++){

			Bus bus = allBuses.get(i);

			if (bus.getCostToNextStop() == 0){

				bus.dropOffPassengers();

			}	
		}
	}

	// Sets the paths for all the buses.
	private void dynamicReroutingAndPassengerAllocation(){

		HashMap<Bus, LinkedList<Vertex>> BusesAndPotentialPaths = new HashMap<Bus, LinkedList<Vertex>>();

		/**
		 * We need to decide how to reroute our buses when we have new passengers added to the world. 
		 * Simply perform this computation over all of the buses and store the total cost of doing that. 
		 */
		for (int q = 0; q < BusCentralDatabase.getBusesInTheWorld().size(); q++){

			Bus firstBus = BusCentralDatabase.getBusesInTheWorld().get(q);
			// For the rootNode
			Vertex rootNode = firstBus.getCurrentStop();

			// If the bus is between stops then the rootNode will be the next node
			if (firstBus.getCostToNextStop() != 0){

				rootNode = firstBus.getPath().get(1);

			}

			// Getting the list of pickups. These are the stops the will be part of the picking up of a passenger
			ArrayList<Passenger> pickups = (ArrayList<Passenger>) BusCentralDatabase.getUnallocatedPassengers().clone();
			pickups.addAll(firstBus.getAssignedPassengers());
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

			// You can't just do this because the bus may be between some stops

			if (firstBus.getCostToNextStop() > 0){

				LinkedList<Vertex> path = new LinkedList<Vertex>();
				path.add(firstBus.getCurrentStop());
				path.addAll(optimalPath);
				optimalPath = path;
				//		firstBus.setPath(path);

			}

			else {

				//		firstBus.setPath(optimalPath);

			}


			/*
			for (int i = 0; i < numberOfPickups; i++){

				firstBus.assignPassenger(pickups.get(0));

			}*/

			BusesAndPotentialPaths.put(firstBus, optimalPath);

		}	

		int totalSystemCost = 0;

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);

			LinkedList<Vertex> path = (LinkedList<Vertex>) bus.getPath().clone();

			int pathCost = 0;

			if (bus.getCostToNextStop() > 0){
				path.remove(0);
				pathCost = sizeOfPath(path);
				pathCost += bus.getCostToNextStop();
			}
			else{
				pathCost = sizeOfPath(path);
			}

			if (pathCost > totalSystemCost){

				totalSystemCost = pathCost;

			}			
		}

		HashMap BusesAndCosts = new HashMap<Bus, Integer>();

		for (int i = 0; i < BusesAndPotentialPaths.size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			int pathCost = sizeOfPath(BusesAndPotentialPaths.get(bus));

			if (bus.getCostToNextStop() > 0){

				LinkedList<Vertex> path = (LinkedList<Vertex>) BusesAndPotentialPaths.get(bus).clone();
				path.remove(0);
				pathCost = sizeOfPath(path);
				pathCost += bus.getCostToNextStop();

			}

			BusesAndCosts.put(bus, pathCost);

		}



		int min = Collections.min(BusesAndCosts.values());



		Iterator it = BusesAndCosts.entrySet().iterator();
		Bus bus = null;
		while (it.hasNext()){

			Map.Entry<Bus, Integer> pairs = (Map.Entry<Bus, Integer>)it.next();

			if (pairs.getValue() == min){

				bus = pairs.getKey();
				break;

			}

		}

		bus.setPath(BusesAndPotentialPaths.get(bus));

		int numberOfPickups = BusCentralDatabase.getUnallocatedPassengers().size();

		for (int i = 0; i < numberOfPickups; i++){

			bus.assignPassenger(BusCentralDatabase.getUnallocatedPassengers().get(0));

		}

		//		System.out.println("TEST");

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
		for (int i = 2, j = 0; j < pickups.size(); i++, j++){

			map.put(i, pickups.get(j));

		}

		int terminator = map.size() + 1 + dropOffs.size();

		// place the dropoffs
		for (int i = map.size() + 1, j = 0; j < dropOffs.size(); i++, j++){

			map.put(i, dropOffs.get(j));

		}

		ArrayList<LinkedList<Vertex>> possiblePaths = new ArrayList<LinkedList<Vertex>>();

		// Cull the paths that are not possible because the drop off stop is being visited before the pickup stop
		for (int i = 0; i < enumeratedPaths.size(); i++){

			LinkedList<Vertex> path = hamToPath(enumeratedPaths.get(i), map);
			path = convertToRealPath(path);

			if (!happensBeforeViolation(path, pairs)){

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

				finalPath.remove(j);

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
			int dropoffIndex = path.lastIndexOf(dropoff);

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

	private void incrimentBuses(){

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			bus.moveAlongPath();

		}
	}
}
