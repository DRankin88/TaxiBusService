package algorithms;

import java.util.ArrayList;

import passengers.Passenger;

import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;

public class FirstComeFirstServed {

	public static void doAlgorithm(){
		
		// Check if any buses can drop their passengers off right now and do it
		dropOffPassengers();
		
		// Check if there are outstanding passengers that need to be assigned a free bus and do it
		
		// Tell all the buses to move along their routes to the next location
		
		
		
		
		
		
		// Step through time
		TimeStepper.step();
		
	}
	
	private static void dropOffPassengers(){
		
		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();
		
		for (int i = 0; i < allBuses.size(); i++) {
			
			Bus bus = allBuses.get(i);
			Vertex currentStop = bus.getCurrentStop();
			ArrayList<Passenger> busesDropOffs = bus.getPassengersWantThisStop();
			
			for (int l = 0; l < busesDropOffs.size(); l++){
				
				busesDropOffs.get(l).setDroppedOff(true);
				int remove = BusCentralDatabase.getPassengerInTheWorld().indexOf(busesDropOffs.get(l));
				BusCentralDatabase.getPassengerInTheWorld().remove(remove);
				
			}
			
			
		}
		
	}
	
}
