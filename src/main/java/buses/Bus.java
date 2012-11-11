package buses;

import java.util.ArrayList;
import java.util.HashMap;

import controller.BusCentralDatabase;

import passengers.Passenger;
import vogella.Vertex;

/**
 * Object representing a bus that travels around a graph along edges and between vertices
 * @author David Rankin
 *
 */
public class Bus {

	private String name;
	private int capacity;
	private HashMap<String, Passenger> passengers = new HashMap<String, Passenger>();
	private Vertex currentStop;

	public Bus(String name, int capacity, Vertex currentStop) {

		this.name = name;
		this.capacity = capacity;
		this.currentStop = currentStop;

	}

	/**
	 * Instructs this bus to pickup any passengers at the current stop. Who and what is picked up will depend on the 
	 * algorithm that we are currently using.
	 */
	public void pickupPassengers(){

//		ArrayList<Passenger> passengersAtMyStop = BusCentralDatabase.getPassengersAtMyStop(currentStop);

	}






}
