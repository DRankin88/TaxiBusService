package algorithms;

import java.util.ArrayList;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;

/**
 * This is the algorithm described in my emails to Murray Cole
 * @author David Rankin
 *
 */
public class DynamicReroutingCostMinimization {

	private static int count = -1;

	public static void doAlgorithm(){

		count++;

		if(count < 120) {
			System.out.println("TimeStep " + count);
			BusCentralDatabase.printStateOfWorld();

		}

		// Firstly any bus that is at a stop in which a passenger could be dropped off should do so
		//		dropOffPassengers();

		dynamicReroutingAndPassengerAllocation();

	}

	private static void dropOffPassengers(){

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

	private static void dynamicReroutingAndPassengerAllocation(){

	}

	/**
	 * Calculates the shortest path that hits every stop for pickups and dropoffs of a list of passengers
	 * @param bus Bus that has to do the work
	 * @param passengers Passengers it has to hit
	 */
	private static void bestPath(Bus bus, ArrayList<Passenger> passengers){

		// The starting stop is the next node that the bus is going to be at and is the root node of the tree
		if (bus.getPath().isEmpty()){

			Vertex startingStop = bus.getCurrentStop();

		}

		else{

			Vertex startingStop = bus.getPath().get(1);

		}
		
		ArrayList<Vertex> pickUps = new ArrayList<Vertex>();
		ArrayList<Vertex> dropOffs = new ArrayList<Vertex>();
		
		for (int i = 0; i < passengers.size(); i++){
			
			pickUps.add(passengers.get(i).getStartingStop());
			dropOffs.add(passengers.get(i).getDestinationStop());
			
		}
		
		
		
	}
}
