package controller;

import java.util.ArrayList;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;

/**
 * Models a controller back at HQ that holds information about the world as we move through time.
 * @author David Rankin
 *
 */
public class BusCentralDatabase {

	// This is a "static" class and so is non-instantiable
	private BusCentralDatabase(){};

	private static ArrayList<Passenger> passengersInTheWorld = new ArrayList<Passenger>();
	private static ArrayList<Passenger> unallocatedPassengers = new ArrayList<Passenger>();
	private static ArrayList<Passenger> passengersWaiting = new ArrayList<Passenger>();
	private static ArrayList<Bus> busesInTheWorld = new ArrayList<Bus>();
	private static ArrayList<Bus> freeBuses = new ArrayList<Bus>();

	public static void addPassengerToWaiting(Passenger passenger){

		passengersWaiting.add(passenger);

	}

	public static void removePassengerFromWaiting(Passenger passenger){

		passengersWaiting.remove(passenger);

	}

	public static void removePassengerFromUnallocated(Passenger passenger){

		unallocatedPassengers.remove(passenger);

	}

	public static void addPassengerToUnallocated(Passenger passenger){

		unallocatedPassengers.add(passenger);

	}

	public static void addBusToFreeBuses(Bus bus){

		freeBuses.add(bus);

	}

	public static ArrayList<Bus> getFreeBuses() {
		return freeBuses;
	}



	public static void setFreeBuses(ArrayList<Bus> freeBuses) {
		BusCentralDatabase.freeBuses = freeBuses;
	}



	public static ArrayList<Passenger> getPassengerInTheWorld() {
		return passengersInTheWorld;
	}



	public static ArrayList<Passenger> getUnallocatedPassengers() {
		return unallocatedPassengers;
	}



	public static void setUnallocatedPassengers(
			ArrayList<Passenger> unallocatedPassengers) {
		BusCentralDatabase.unallocatedPassengers = unallocatedPassengers;
	}



	public static ArrayList<Bus> getBusesInTheWorld() {
		return busesInTheWorld;
	}



	/**
	 * Adds a passenger to the world. Should be called whenever a new passenger is created
	 * @param passenger
	 */
	public static void addPassengerToWorld(Passenger passenger){

		passengersInTheWorld.add(passenger);

	}

	public static void addBusesToWorld(Bus bus){

		busesInTheWorld.add(bus);

	}

	/**
	 * Used by buses to find out the passengers at their stop
	 * @param currentStop Stop the bus is at
	 * @return the ArrayList of passengers that are at the currentStop
	 */
	// THIS MIGHT BE BROKEN!!!!!!
	public static ArrayList<Passenger> getPassengersAtMyStop(Vertex currentStop){

		ArrayList<Passenger> passengersAtYourStop = new ArrayList<Passenger>();

		for (int i = 0; i < passengersInTheWorld.size(); i++){

			if (passengersInTheWorld.get(i).getStartingStop().equals(currentStop))

				passengersAtYourStop.add(passengersInTheWorld.get(i));

		}

		return passengersAtYourStop;

	}

	public static void removeBusFromUnassigned(Bus bus){

		freeBuses.remove(bus);

	}

	/**
	 * If given a passenger this method returns the bus closest to them that is Unallocated
	 * @return
	 */
	public static Bus getClosestFreeBus(Passenger passenger){

		ArrayList<Bus> myFreeBuses = freeBuses;

		Bus firstBus = myFreeBuses.get(0);

		Bus closestBus = firstBus;
		for (int i = 1; i < myFreeBuses.size(); i++){

			Bus bus = myFreeBuses.get(i);
			if (bus.getCurrentStop().equals(passenger.getStartingStop())){

				return bus;

			}

			if (closestBus.distance(passenger.getStartingStop()) > bus.distance(passenger.getStartingStop())){

				closestBus = bus;

			}
		}

		return closestBus;

	}

	/**
	 * Finds the bus that the passenger is riding in
	 * @param passenger
	 * @return
	 */
	public static Bus getBusFromPassenger(Passenger passenger){

		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);

			if (bus.getPassengersOnBus().contains(passenger)){

				return bus;

			}

		}

		return null;

	}
	
	public static void removePassengersFromTheWorld (ArrayList<Passenger> passengers){
		
		passengersInTheWorld.removeAll(passengers);
		
	}

	public static void printStateOfWorld(){

		//Print the location of all buses and who they are assigned to and where they are going
		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);
			StringBuffer output = new StringBuffer();


			if (bus.getCostToNextStop() == 0){

				output.append("Bus named " + bus.getName() + " is currently at stop " + bus.getCurrentStop());

			}

			else {

				output.append("Bus named " + bus.getName() + " is between stops " + bus.getCurrentStop() + " " + bus.getPath().get(1) + " and will get there in " + bus.getCostToNextStop() + " steps");

			}

			if (!bus.getPath().isEmpty()){

				output.append(", is moving along the path " + bus.getPath());

			}

			if (!bus.getAssignedPassengerName().equals("No One")){

				output.append(" and is assigned to pick up passenger " + bus.getAssignedPassengerName());

			}

			System.out.println(output);

		}

		//Print the location of all passengers
		for (int j = 0; j < passengersInTheWorld.size(); j++){

			Passenger passenger = passengersInTheWorld.get(j);

			if (!passenger.isPickedUp()){

				System.out.println("Passenger named " + passenger.getName() + " is currently waiting to be picked up from stop " + passenger.getStartingStop());

			}

			if (passenger.isPickedUp() && !passenger.isDroppedOff()){

				System.out.println("Passenger named " + passenger.getName() + " is currently on bus named " + getBusFromPassenger(passenger).getName());

			}

		}
	}
}
