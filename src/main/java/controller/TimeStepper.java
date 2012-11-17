package controller;

import java.util.ArrayList;

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

	public TimeStepper (Graph graph, InputParser scenario) {

		this.graph = graph;
		this.scenario = scenario;

	}

	/**
	 * Steps through time.
	 */
	public static void step(){

		

		ArrayList<String[]> newPassengersToCreate = scenario.getPassengers(time);
		ArrayList<String[]> newBusesToCreate = scenario.getBuses(time);

		for (int i = 0; i < newPassengersToCreate.size(); i++){

			String name = newPassengersToCreate.get(i)[2];
			Vertex startingStop = graph.getVertex(newPassengersToCreate.get(i)[3]);
			Vertex finishingStop = graph.getVertex(newPassengersToCreate.get(i)[4]);

			Passenger passenger = new Passenger(name, startingStop, finishingStop);
			BusCentralDatabase.addPassengerToWorld(passenger);
			BusCentralDatabase.addPassengerToUnallocated(passenger);
			BusCentralDatabase.addPassengerToWaiting(passenger);

		}

		for (int i = 0; i < newBusesToCreate.size(); i++){

			String name = newBusesToCreate.get(i)[2];
			int capacity = Integer.parseInt(newBusesToCreate.get(i)[3]);
			Vertex startingStop = graph.getVertex(newBusesToCreate.get(i)[4]);

			Bus bus = new Bus(name, capacity, startingStop, graph);
			BusCentralDatabase.addBusesToWorld(bus);
			BusCentralDatabase.addBusToFreeBuses(bus);
		}
		
		time++;
		
	}

}
