package algorithms;

import java.util.ArrayList;
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

public class MinimalRerouting extends BaseAlgorithmBehaviour {

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
				assignPassenger();
			}

			// Pick up passenger if bus is at destination
			pickupPassengers();

			// Tell all the buses to move along their routes to the next location
			incrimentBuses();
		}
	}

	private void assignPassenger(){

		ArrayList<Passenger> pickups = (ArrayList<Passenger>) BusCentralDatabase.getUnallocatedPassengers().clone();
		Passenger passenger = pickups.get(0);
		HashMap<Bus, LinkedList<Vertex>> busesAndPaths = new HashMap<Bus, LinkedList<Vertex>>();

		for (Bus bus : BusCentralDatabase.getBusesInTheWorld()){


			int vertexCount = bus.getPath().size();

			if(vertexCount == 0){ // The bus is currently idle

				LinkedList<Vertex> path = (LinkedList<Vertex>) bus.getPath().clone();

				LinkedList<Vertex> pathOut = (LinkedList<Vertex>) (allPairsShortestPath.getPath(bus.getCurrentStop().getName(), passenger.getStartingStop().getName())).clone();
				LinkedList<Vertex> pathBack = (LinkedList<Vertex>) (allPairsShortestPath.getPath(passenger.getStartingStop().getName(), passenger.getDestinationStop().getName())).clone();
				pathOut.addAll(pathBack);
				path.addAll(pathOut);
				// Need to remove any potential side by side duplicates
				for (int i = 0; i < path.size() - 1; i++){

					Vertex thisVertex = path.get(i);
					Vertex nextVertex = path.get(i + 1);

					if (thisVertex.equals(nextVertex)){

						path.remove(i);

					}
				}

				// Now we have the path for this bus to serve. 
				busesAndPaths.put(bus, path);

			}
			else{ // The bus was not idle and now we have to squeeze the path into the current one

				ArrayList<LinkedList<Vertex>> candidatePaths = new ArrayList<LinkedList<Vertex>>();

				int startPoint = 0;
				if (bus.getCostToNextStop() != 0){
					startPoint = 1;
				}

				for (int i = startPoint; i < vertexCount; i++) {

					LinkedList<Vertex> path = (LinkedList<Vertex>) bus.getPath().clone();

					Vertex vertex = path.get(i);
					LinkedList<Vertex> pathOut = (LinkedList<Vertex>) (allPairsShortestPath.getPath(vertex.getName(), passenger.getStartingStop().getName())).clone();
					//			path.addAll(i,pathOut);
					// If we are at the end of the route then there is no need to add the notion of a path back
					if (i != (path.size() - 1)){

						if (!pathOut.isEmpty()){
							LinkedList<Vertex> pathBack = (LinkedList<Vertex>) (allPairsShortestPath.getPath(passenger.getStartingStop().getName(), path.get(i+1).getName())).clone();
							pathOut.addAll(pathBack);
						}
						path.addAll(i+1, pathOut);

					}
					else{

						path.addAll(i,pathOut);

					}
					// Need to remove any potential side by side duplicates
					for (int j = 0; j < path.size() - 1; j++){

						Vertex thisVertex = path.get(j);
						Vertex nextVertex = path.get(j + 1);

						if (thisVertex.equals(nextVertex)){

							path.remove(j);

						}
					}

					// Now we have added the pickup at this index and have a new path. 
					// From the pickup point we need to find the best place to do the drop off

					int indexOfPickup = path.indexOf(passenger.getStartingStop());
					if (indexOfPickup < i){
						indexOfPickup = path.lastIndexOf(passenger.getStartingStop());
					}

					ArrayList<LinkedList<Vertex>> potentialFromThisPickup = new ArrayList<LinkedList<Vertex>>();

					for (int k = indexOfPickup; k < path.size(); k++){

						LinkedList<Vertex> newPath = (LinkedList<Vertex>) path.clone();

						LinkedList<Vertex> pathToDrop =  (LinkedList<Vertex>) (allPairsShortestPath.getPath(newPath.get(k).getName(), passenger.getDestinationStop().getName())).clone();
						newPath.addAll(k, pathToDrop);

						// Delete duplicates
						for (int j = 0; j < newPath.size() - 1; j++){

							Vertex thisVertex = newPath.get(j);
							Vertex nextVertex = newPath.get(j + 1);

							if (thisVertex.equals(nextVertex)){

								newPath.remove(j);

							}
						}


					// Now make sure that the route is not doing more work than needed
					// Get the last index of the last dropoff and remove everything after
					ArrayList<Passenger> allPassengers = (ArrayList<Passenger>) bus.getAssignedPassengers().clone();
					allPassengers.add(passenger);
					allPassengers.addAll(bus.getPassengersOnBus());
					int last = getIndexOfLastDropOff(allPassengers, newPath);
					for (int w = last + 1; w < newPath.size(); w++){
						
						newPath.remove(w);
						
					}
					
						// Now store this path
						potentialFromThisPickup.add(newPath);

					}

					LinkedList<Vertex> shortestPathFromThisStart = shortestPath(potentialFromThisPickup);

					candidatePaths.add(shortestPathFromThisStart);

				}

				// Now that we have all the candidate paths just pick the best one and store it with that bus
				LinkedList<Vertex> bestPath = shortestPath(candidatePaths);
				busesAndPaths.put(bus, bestPath);

			}
		}

		// Find out which bus can do it the cheapest and assign it
		Bus bestBus = bestBus(busesAndPaths);

		bestBus.assignPassenger(passenger);
		bestBus.setPath(busesAndPaths.get(bestBus));

	}

	private int getIndexOfLastDropOff(ArrayList<Passenger> passengers, LinkedList<Vertex> path){

		ArrayList<Vertex> lastStops = new ArrayList<Vertex>();
		for (Passenger passenger : passengers){
			
			lastStops.add(passenger.getDestinationStop());
			
		}
		
		int lastIndex = path.lastIndexOf(lastStops.get(0));

		for (int i = 1; i < lastStops.size(); i++){
			
			if (path.lastIndexOf(lastStops.get(i)) > lastIndex){
				
				lastIndex = path.lastIndexOf(lastStops.get(i));
				
			}
			
		}
		
		return lastIndex;
		
	}

	private Bus bestBus (HashMap<Bus, LinkedList<Vertex>> busesAndPaths){

		Iterator it = busesAndPaths.entrySet().iterator();
		Bus bestBus = null;
		int cost = Integer.MAX_VALUE;

		while(it.hasNext()){

			Map.Entry pairs = (Map.Entry)it.next();
			int pathCost = sizeOfPath((LinkedList<Vertex>) pairs.getValue());

			if (pathCost < cost){
				bestBus = (Bus) pairs.getKey();
				cost = pathCost;
			}
		}

		return bestBus;

	}

	private LinkedList<Vertex> shortestPath (ArrayList<LinkedList<Vertex>> paths){

		int cost = sizeOfPath(paths.get(0));
		LinkedList<Vertex> bestPath = paths.get(0);


		for (int i = 1; i < paths.size(); i++){

			if (cost > sizeOfPath(paths.get(i))){

				bestPath = paths.get(i);

			}
		}

		return bestPath;

	}

	private int sizeOfPath (LinkedList<Vertex> path){

		int numberOfEdges = path.size() - 1;
		int cost = 0;
		try{
			for (int i = 0; i < numberOfEdges; i++){

				Edge edge = busGraph.getEdgeBetweenVertices(path.get(i), path.get(i+1));
				cost += edge.getWeight();

			}

			return cost;
		}
		catch (Exception e){
			// The path must not exist so return huge number
			return Integer.MAX_VALUE;
		}
	}
}
