package controller;

import java.util.ArrayList;

import passengers.Passenger;

/**
 * Models a controller back at HQ that holds information about the world as we move through time.
 * @author David Rankin
 *
 */
public class BusCentralDatabase {

	// This is a "static" class and so is non-instantiable
	private BusCentralDatabase(){};
	
	private static ArrayList<Passenger> passengersAtStops;
	
	public static ArrayList<Passenger> getPassengersAtStops() {
		return passengersAtStops;
	}
	
	/**
	 * Adds a passenger to the world. Should be called whenever a new passenger is created
	 * @param passenger
	 */
	public static void addPassengerToWorld(Passenger passenger){
		
		passengersAtStops.add(passenger);
		
	}


	
	
	
}
