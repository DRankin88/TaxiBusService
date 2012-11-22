package algorithms;

import java.util.ArrayList;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;

public class AssignClosestBus {

	private static int count = -1;

	public static void doAlgorithm(){

		count++;

		//Only printing the first 60 iterations of the time step

		if(count < 120) {

			BusCentralDatabase.printStateOfWorld();
			
		}

		// Check if any buses can drop their passengers off right now and do it
		dropOffPassengers();

		// Check if there are outstanding passengers that need to be assigned a free bus and do it
		allocatePassengersToBuses();

		// TODO Pick up passenger if bus is at destination
		pickupPassengers();

		// Tell all the buses to move along their routes to the next location
		incrimentBuses();

		// Step through time
		TimeStepper.step();

	}


	private static void dropOffPassengers(){

		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < allBuses.size(); i++) {

			Bus bus = allBuses.get(i);
			ArrayList<Passenger> busesDropOffs = bus.getPassengersWantThisStop();

			for (int l = 0; l < busesDropOffs.size(); l++){

				busesDropOffs.get(l).setDroppedOff(true);
				int remove = BusCentralDatabase.getPassengerInTheWorld().indexOf(busesDropOffs.get(l));
				BusCentralDatabase.getPassengerInTheWorld().remove(remove);
				bus.removePassenger(busesDropOffs.get(l));
				bus.setAssignedPassengerName("No One");
				BusCentralDatabase.addBusToFreeBuses(bus);

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

				bus.setAssignedPassengerName(unallocatedPassengers.get(i).getName());

				BusCentralDatabase.removePassengerFromUnallocated(unallocatedPassengers.get(i));

			}	
		}
	}

	private static void incrimentBuses(){

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			bus.moveAlongPath();

		}

	}

	//TODO Right now algorithm does not support on route pickups
	private static void pickupPassengers(){

		if (BusCentralDatabase.getPassengerInTheWorld().isEmpty()){

			return;

		}

		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();

		for (int e = 0; e < allBuses.size(); e++){

			Bus currentBus = allBuses.get(e);


			if ((currentBus.getPassengersOnBus().size() != 1) && currentBus.getCurrentStop().equals(currentBus.getTargetStop())){


				ArrayList<Passenger> passengers = BusCentralDatabase.getPassengersAtMyStop(currentBus.getCurrentStop());

				for (int y = 0; y < passengers.size(); y++){

					if(currentBus.getAssignedPassengerName().equals(passengers.get(y).getName())){

						currentBus.pickupPassenger(passengers.get(y));
						passengers.get(y).setPickedUp(true);
						BusCentralDatabase.removePassengerFromWaiting(passengers.get(y));
						BusCentralDatabase.removePassengerFromUnallocated(passengers.get(y));
						currentBus.setAssignedPassengerName("No One");
					}
				}


				// Now the bus must be routed to drop the passenger off
				if (!currentBus.getPassengersOnBus().isEmpty()){

					ArrayList<Passenger> busPassengers = currentBus.getPassengersOnBus();
					currentBus.setTargetStop(busPassengers.get(0).getDestinationStop());

				}
			}

		}

	}

}
