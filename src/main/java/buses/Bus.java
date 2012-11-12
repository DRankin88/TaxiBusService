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
	private ArrayList<Passenger> passengersOnBus = new ArrayList<Passenger>();
	private Vertex currentStop;

	public Bus(String name, int capacity, Vertex currentStop) {

		this.name = name;
		this.capacity = capacity;
		this.currentStop = currentStop;

	}
	
	

	public Vertex getCurrentStop() {
		return currentStop;
	}



	public void setCurrentStop(Vertex currentStop) {
		this.currentStop = currentStop;
	}

	/**
	 * Gets the passengers you have on board that want to be dropped off at a particular stop
	 * @return
	 */
	public ArrayList<Passenger> getPassengersWantThisStop(){
		
		ArrayList<Passenger> passengers = new ArrayList<Passenger>();
		
		for (int i = 0; i < passengersOnBus.size(); i++){
			
			Passenger passenger = passengersOnBus.get(i);
			if (passenger.getDestinationStop().equals(currentStop)){
				
				passengers.add(passenger);
				
			}
			
		}
		
		return passengers;
		
	}


	/**
	 * Instructs this bus to pickup any passengers at the current stop. Who and what is picked up will depend on the 
	 * algorithm that we are currently using.
	 */
	public void pickupPassengers(){

//		ArrayList<Passenger> passengersAtMyStop = BusCentralDatabase.getPassengersAtMyStop(currentStop);

	}






}
