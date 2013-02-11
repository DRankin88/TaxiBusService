package controller;

import java.util.ArrayList;

import database.AllPairsShortestPath;

import passengers.Passenger;
import scenarios.InputParser;
import vogella.Graph;
import vogella.Vertex;
import buses.Bus;

/**
 * Controls the flow of the simulation. It is called on every time step and generates buses and passengers 
 * on the time steps that the input file decreed.
 * @author David Rankin
 *
 */
public class TimeStepper {


	private static Graph graph;
	private static InputParser scenario;
	private static int time = 1;
	private static AllPairsShortestPath paths;
	
	public static int getTime() {
		return time;
	}

	public TimeStepper (Graph graph, InputParser scenario, AllPairsShortestPath paths) {

		TimeStepper.graph = graph;
		TimeStepper.scenario = scenario;
		TimeStepper.paths = paths;

	}

	/**
	 * Steps through time.
	 */
	public static void step(){

//		System.out.println("Total Number of passengers left to create: " + scenario.getPassengersInWaiting().size());

		ArrayList<String[]> newPassengersToCreate = scenario.getPassengers(time);
		ArrayList<String[]> newBusesToCreate = scenario.getBuses(time);

		while (!newPassengersToCreate.isEmpty()){
			
			String name = newPassengersToCreate.get(0)[2];
			Vertex startingStop = graph.getVertex(newPassengersToCreate.get(0)[3]);
			Vertex finishingStop = graph.getVertex(newPassengersToCreate.get(0)[4]);

			Passenger passenger = new Passenger(name, startingStop, finishingStop);
			passenger.setCreationTime(time);
			BusCentralDatabase.addPassengerToWorld(passenger);
			BusCentralDatabase.addPassengerToUnallocated(passenger);
			BusCentralDatabase.addPassengerToWaiting(passenger);
			scenario.removePassenger(newPassengersToCreate.get(0));
			newPassengersToCreate.remove(newPassengersToCreate.get(0));
			
			if (scenario.getPassengersInWaiting().size() == 0){
				
				BusCentralDatabase.setAllPassengersCreated(true);
				
			}
			
		}
		
		while (!newBusesToCreate.isEmpty()){
			
			String name = newBusesToCreate.get(0)[2];
			int capacity = Integer.parseInt(newBusesToCreate.get(0)[3]);
			Vertex startingStop = graph.getVertex(newBusesToCreate.get(0)[4]);

			Bus bus = new Bus(name, capacity, startingStop, graph, paths);
			BusCentralDatabase.addBusesToWorld(bus);
			BusCentralDatabase.addBusToFreeBuses(bus);
			scenario.removeBus(newBusesToCreate.get(0));
			newBusesToCreate.remove(newBusesToCreate.get(0));
			
		}
		
		for (Passenger passenger : BusCentralDatabase.getPassengerInTheWorld()){
			
			passenger.incrimentTimeInWorld();
			System.out.println("TotalTimeInWorld for passenger " + passenger.getName().toString() + " is " + passenger.getTotalTimeInWorld());
			
		}
		
		time++;
		
	}

}
