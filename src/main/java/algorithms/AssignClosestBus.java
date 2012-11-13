package algorithms;

import java.util.ArrayList;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;

public class AssignClosestBus {

	public static void doAlgorithm(){
		
		// Check if any buses can drop their passengers off right now and do it
		dropOffPassengers();
		
		// Check if there are outstanding passengers that need to be assigned a free bus and do it
		allocatePassengersToBuses();
		
		// TODO Pick up passenger if bus is at destination
		
		
		
		
		// Tell all the buses to move along their routes to the next location
		incrimentBuses();
		
		
		
		
		
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
	
	private static void allocatePassengersToBuses(){
		
		// Get the unallocated passengers
		ArrayList<Passenger> unallocatedPassengers = BusCentralDatabase.getUnallocatedPassengers();

		// Loop over the unallocated passengers and have the closest free bus be sent to them provided one exists.
		for (int i = 0; i < unallocatedPassengers.size(); i++){
			
			if (!BusCentralDatabase.getFreeBuses().isEmpty()){
				
				Bus bus = BusCentralDatabase.getClosestFreeBus(unallocatedPassengers.get(i));
				bus.setTargetStop(unallocatedPassengers.get(i).getStartingStop());
				BusCentralDatabase.removeBusFromUnassigned(bus);
				
			}	
		}
	}
	
	private static void incrimentBuses(){
		
		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){
			
			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			bus.moveAlongPath();
			
		}
		
	}
	
	
}
