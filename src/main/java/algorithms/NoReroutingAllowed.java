package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import passengers.Passenger;
import vogella.Edge;
import vogella.Graph;
import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;
import database.AllPairsShortestPath;

public class NoReroutingAllowed {

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

				Object[] trivialCost = assignIfRouteIsTrivial();

				// If it wasn't trivial then we have to do some more difficult appending
				appendWorkToBestRoute(trivialCost);

			}
		}

		// Pick up passenger if bus is at destination
		pickupPassengers();

		// If the bus has no one on it and no one assigned then just clear the route
		for (Bus bus : BusCentralDatabase.getBusesInTheWorld()){
			
			if (bus.getAssignedPassengers().isEmpty() && bus.getPassengersOnBus().isEmpty()){
				
				bus.setPath(new LinkedList<Vertex>());
				
			}
			
		}
		// Tell all the buses to move along their routes to the next location
		incrimentBuses();

	}


	/**
	 * This has to loop over all of the buses and see if it would be trivial to assign the passenger to that bus. 
	 * @return
	 */
	private Object[] assignIfRouteIsTrivial(){

		// Use this to place the bus and it's candidate path
		HashMap<Bus, Integer> busesAndCosts = new HashMap<Bus, Integer>();
		
		Bus bestBus = null;

		// Used to store the lowest cost bus
		int cost = Integer.MAX_VALUE;

		// This basically just returns the one person that is going to be assigned on this loop iteration
		ArrayList<Passenger> pickups = (ArrayList<Passenger>) BusCentralDatabase.getUnallocatedPassengers().clone();
		Passenger passenger = pickups.get(0);

		// For each bus ask if the passenger can be trivially assigned. If they can then find who can do it the cheapest.
		for (int q = 0; q < BusCentralDatabase.getBusesInTheWorld().size(); q++){

			Bus thisBus = BusCentralDatabase.getBusesInTheWorld().get(q);

			// Now we have to find out if the bus could service the passenger on its current route 
			// then keep a note of the cost of doing this
			LinkedList<Vertex> busPath = (LinkedList<Vertex>) thisBus.getPath().clone();

			// If the bus is between two stops then we need to remove the first vertex from its path
			if (thisBus.getCostToNextStop() > 0){

				busPath.removeFirst();

			}

			// Now get the pick up and the drop off for the passenger
			Vertex pickUpStop = passenger.getStartingStop();
			Vertex dropOffStop = passenger.getDestinationStop();

			if (!busPath.contains(dropOffStop) || !busPath.contains(pickUpStop)){
				// It is not possible to trivially resolve this
				continue;

			}

			// It may be possible to resolve this route trivially but only if the passenger is also not forming a HBV			
			if (busPath.indexOf(dropOffStop) < busPath.indexOf(pickUpStop)){
				// It is not possible to trivially resolve this
				continue;

			}

			// Given that there exists a possible trivial assignation for this passenger we must record the path in some sort of map
			LinkedList<Vertex> temp1 = (LinkedList<Vertex>) busPath.clone();

			List<Vertex> temp2 = temp1.subList(0, temp1.indexOf(dropOffStop) + 1);

			LinkedList<Vertex> subPath = new LinkedList<Vertex>();

			// Subpath needs to take into account the 
			// Have to fill subpath with the values of path
			for (int y = 0; y < temp2.size(); y++){

				subPath.add(temp2.get(y));

			}


			int thisCost = sizeOfPath(subPath) + thisBus.getCostToNextStop();
			
			if (thisCost < cost){

				cost = thisCost;

			}

			busesAndCosts.put(thisBus, thisCost);

		}

		if (busesAndCosts.size() > 0) {



			// Having completed this operation we now need to assign the passenger to the bus that can do it with the lowest cost
			Iterator it = busesAndCosts.entrySet().iterator();
			while (it.hasNext()){

				Map.Entry pairs = (Map.Entry)it.next();

				if ((Integer)pairs.getValue() == cost){

					bestBus = (Bus)pairs.getKey();

				}
			}

			// Now that we know the bus we just need to assign the passenger to it.

			//	bestBus.assignPassenger(passenger);

		}

		if (busesAndCosts.size() > 0){
			
			Object[] busAndCost = new Object[] {bestBus, busesAndCosts.get(bestBus)};
			return busAndCost;
			
		}
		else{

			Object[] Onlycost = new Object[] {Integer.MAX_VALUE};
			return Onlycost;

		}
	}

	/**
	 * Having found that no bus is capable of dealing with the passenger trivially we must append the work to whomever is cheapest
	 */
	private void appendWorkToBestRoute(Object[] input){

		HashMap<Bus, LinkedList<Vertex>> busesAndPaths = new HashMap<Bus, LinkedList<Vertex>>();
		int cheapestCost = Integer.MAX_VALUE;
		Passenger passenger = BusCentralDatabase.getUnallocatedPassengers().get(0);

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus thisBus = BusCentralDatabase.getBusesInTheWorld().get(i);
			LinkedList<Vertex> busRoute = (LinkedList<Vertex>) thisBus.getPath().clone();

			if (!busRoute.isEmpty()){

				busRoute.addAll(allPairsShortestPath.getPath(busRoute.getLast().toString(), passenger.getStartingStop().toString()));
				busRoute.addAll(allPairsShortestPath.getPath(passenger.getStartingStop().toString(), passenger.getDestinationStop().toString()));

			}

			else {

				busRoute.addAll(allPairsShortestPath.getPath(thisBus.getCurrentStop().toString(), passenger.getStartingStop().toString()));
				busRoute.addAll(allPairsShortestPath.getPath(passenger.getStartingStop().toString(), passenger.getDestinationStop().toString()));

			}

			// The busRoute will have some doubles that we need to get rid of
			for (int j = 0; j < busRoute.size() - 1; j++){

				if (busRoute.get(j).equals(busRoute.get(j + 1))){

					busRoute.remove(j);

				}
			}

			int cost = sizeOfPath(busRoute);

			// If the bus is between two stops then the size of path needs to be slightly altered
			if (thisBus.getCostToNextStop() > 0){

				cost = cost - busGraph.getEdgeBetweenVertices(busRoute.get(0), busRoute.get(1)).getWeight();
				cost += thisBus.getCostToNextStop();

			}

			busesAndPaths.put(thisBus, busRoute);

			if (cost < cheapestCost){

				cheapestCost = cost;

			}


		}

		Bus bestBus = null;

		// Having completed this operation we now need to assign the passenger to the bus that can do it with the lowest cost
		Iterator it = busesAndPaths.entrySet().iterator();
		while (it.hasNext()){

			Map.Entry pairs = (Map.Entry)it.next();

			int temp = sizeOfPath((LinkedList<Vertex>) pairs.getValue());

			if (((Bus) pairs.getKey()).getCostToNextStop() > 0){

				temp = temp - busGraph.getEdgeBetweenVertices(busesAndPaths.get((Bus) pairs.getKey()).get(0), busesAndPaths.get((Bus) pairs.getKey()).get(1)).getWeight();
				temp += ((Bus) pairs.getKey()).getCostToNextStop();

			}

			if (temp == cheapestCost){

				bestBus = (Bus)pairs.getKey();
				break;
			}
		}

		// Now that we know the bus we just need to assign the passenger to it.

		if (input.length < 2){
			
			bestBus.assignPassenger(passenger);
			bestBus.setPath(busesAndPaths.get(bestBus));
			return;			
			
		}
		if ((Integer)input[1] <= cheapestCost){
			// Then it is better to do the trivial assignation

			Bus bus = (Bus)input[0];
			bus.assignPassenger(passenger);

		}
		else{
			
			bestBus.assignPassenger(passenger);
			bestBus.setPath(busesAndPaths.get(bestBus));
			return;

		}


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

	private void incrimentBuses(){

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			bus.moveAlongPath();

		}
	}
}


