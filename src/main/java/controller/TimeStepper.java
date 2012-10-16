package controller;

import java.util.ArrayList;
import java.util.HashMap;

import buses.Bus;

import vogella.Graph;

/**
 * Controls the flow of the simulation. It is called on every time step and controls the picking up and dropping off of passengers.
 * @author David Rankin
 *
 */
public class TimeStepper {

	private Graph graph;
	private ArrayList<Bus> buses;
	
	public TimeStepper (Graph graph, ArrayList<Bus> buses) {
		
		this.graph = graph;
		this.buses = buses;
		
	}
	
	/**
	 * Steps through time.
	 */
	public void step(){
		
		busesDealWithPassengers();
	}
	
	/**
	 * Loops over all buses in the graph and tells them to deal with their passengers.
	 */
	private void busesDealWithPassengers(){
		
		for (int i = 0; i < buses.size(); i++){
			
			//TODO Call each bus objects pickupPassengers method
			buses.get(i);
			
		}
		
	}
	
}
