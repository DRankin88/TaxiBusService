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

public class NoReroutingAllowed {

	private static int count = 0;
	private Graph busGraph;
	private AllPairsShortestPath allPairsShortestPath;

	public void doAlgorithm(Graph busGraph, AllPairsShortestPath allPairsShortestPath){

		TimeStepper.step();
		this.busGraph = busGraph;
		this.allPairsShortestPath = allPairsShortestPath;

		if(count < 650) {

			count++;
			System.out.println("TimeStep " + count);
			BusCentralDatabase.printStateOfWorld();

		}

		if (BusCentralDatabase.getBusesInTheWorld().size() > 0){

			// Firstly any bus that is at a stop in which a passenger could be dropped off should do so
			dropOffPassengers();

			// If there are unallocated passengers then we may need to change the paths that buses are currently moving along
			if (BusCentralDatabase.getUnallocatedPassengers().size() > 0){

				if (assignIfRouteIsTrivial() == false){

					// If it wasn't trivial then we have to do some more difficult appending


				}
			}

			// Pick up passenger if bus is at destination
			pickupPassengers();

			// Tell all the buses to move along their routes to the next location
			incrimentBuses();

		}
	}

	/**
	 * This has to loop over all of the buses and see if it would be trivial to assign the passenger to that bus. 
	 * @return
	 */
	private boolean assignIfRouteIsTrivial(){

		// Use this to place the bus and it's candidate path
		HashMap<Bus, Integer> busesAndCosts = new HashMap<Bus, Integer>();

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
			LinkedList<Vertex> busPath = thisBus.getPath();



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
			LinkedList<Vertex> subPath = (LinkedList<Vertex>) busPath.clone();

			subPath = (LinkedList<Vertex>) subPath.subList(0, subPath.indexOf(dropOffStop));

			int thisCost = sizeOfPath(subPath);

			if (thisCost < cost){

				cost = thisCost;

			}

			busesAndCosts.put(thisBus, thisCost);

		}

		if (busesAndCosts.size() > 0) {

			Bus bestBus = null;

			// Having completed this operation we now need to assign the passenger to the bus that can do it with the lowest cost
			Iterator it = busesAndCosts.entrySet().iterator();
			while (it.hasNext()){

				Map.Entry pairs = (Map.Entry)it.next();

				if ((Integer)pairs.getValue() == cost){

					bestBus = (Bus)pairs.getKey();

				}
			}

			// Now that we know the bus we just need to assign the passenger to it.

			bestBus.assignPassenger(passenger);
			return true;
		}
		// If we get to the end and no one was assigned the passenger then the method just returns false
		return false;

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

				bus.pickupPassengers();

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


