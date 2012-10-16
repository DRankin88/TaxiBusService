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
	private HashMap passangers = new HashMap<String, Passenger>();
	private Vertex currentStop;
	
	public Bus(String name) {
	
		this.name = name;
		
	}
	
	/**
	 * Instructs this bus to pickup any passengers at the current stop. Who and what is picked up will depend on the 
	 * algorithm that we are currently using.
	 */
	public void pickupPassengers(){
		
		//Ask the bus central database for an arraylist of the passengers at this stop this bus is sitting at
		BusCentralDatabase.getPassengersAtStops()
		
		
	}
	
	private ArrayList<Passenger> getPassengersAtMyCurrentStop(){
		
		for (int i = 0; i < BusCentralDatabase.getPassengersAtStops().size(); i++){
			
			// passengers must know their stop
			if (BusCentralDatabase.getPassengersAtStops().get(i).getCurrentStop.equals)
			
		}
		
	}
	
	
}
