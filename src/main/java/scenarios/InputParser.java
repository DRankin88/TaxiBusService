package scenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;

/**
 * Represents a scenario. Reads and input file and then keeps the information in it in memory for being used later by the time stepper.
 * @author David Rankin
 *
 */
public class InputParser {

	private ArrayList<String[]> busesInWaiting = new ArrayList<String[]>();
	private ArrayList<String[]> passengersInWaiting = new ArrayList<String[]>();
	
	
	public InputParser(String input){

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(input));

			while ((sCurrentLine = br.readLine()) != null) {

				
				if (sCurrentLine.startsWith("BUS")){

					String[] splittedInput = sCurrentLine.split(" ");
					
			//		Bus bus = new Bus(name, capacity, startingStop);
					busesInWaiting.add(splittedInput);
				}

				if (sCurrentLine.startsWith("PASSENGER")){
				
					String[] splittedInput = sCurrentLine.split(" ");
					
			//		Passenger passenger = new Passenger(name, startingStop, destinationStop);
					passengersInWaiting.add(splittedInput);
				}



			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)br.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}

	public ArrayList<String[]> getPassengers(int timeStep){
		
		ArrayList<String[]> passengers = new ArrayList<String[]>();
		
		for (int i = 0; i < passengersInWaiting.size(); i++){
			
			if (Integer.parseInt(passengersInWaiting.get(i)[1]) == timeStep){
				
				passengers.add(passengersInWaiting.get(i));
				
			}
			
		}

		return passengers;
		
	}
	
	public ArrayList<String[]> getBuses(int timeStep){
		
		ArrayList<String[]> Buses = new ArrayList<String[]>();
		
		for (int i = 0; i < busesInWaiting.size(); i++){
			
			if (Integer.parseInt(busesInWaiting.get(i)[1]) == timeStep){
				
				Buses.add(busesInWaiting.get(i));
				
			}
			
		}

		return Buses;
		
	}
	
}
