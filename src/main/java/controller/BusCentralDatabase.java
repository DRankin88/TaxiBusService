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
	private static ArrayList<Bus> busesInTheWorld = new ArrayList<Bus>();
	public static ArrayList<Passenger> getPassengerInTheWorld() {
		return passengersInTheWorld;
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
	public static ArrayList<Passenger> getPassengersAtMyStop(Vertex currentStop){
		
		ArrayList<Passenger> passengersAtYourStop = new ArrayList<Passenger>();
		
		for (int i = 0; i < passengersInTheWorld.size(); i++){
			
			if (passengersInTheWorld.get(i).getStartingStop().equals(currentStop))
				
				passengersAtYourStop.add(passengersInTheWorld.get(i));
			
		}
		
		return passengersAtYourStop;
		
	}


	
	
	
}
