package buses;

import java.util.ArrayList;
import java.util.LinkedList;

import passengers.Passenger;
import vogella.Graph;
import vogella.Vertex;
import database.AllPairsShortestPath;

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
	private Vertex targetStop;
	private Graph graph;

	public Bus(String name, int capacity, Vertex currentStop, Graph graph) {

		this.name = name;
		this.capacity = capacity;
		this.currentStop = currentStop;
		this.graph = graph;

	}
	
	public Vertex getTargetStop() {
		return targetStop;
	}

	public void setTargetStop(Vertex targetStop) {
		this.targetStop = targetStop;
	}
	
	public Vertex getCurrentStop() {
		return currentStop;
	}

	public void setCurrentStop(Vertex currentStop) {
		this.currentStop = currentStop;
	}

	/**
	 * Gets the passengers you have on board that want to be dropped off at this stop
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
	
	/**
	 * Works out the distance in terms of cost between this bus and a targeted Vertex.
	 * @param targetStop
	 * @return
	 */
	public int distance(Vertex targetStop){
		//TODO
		
		int cost = 0;
		
		LinkedList<Vertex> pathToTarget = AllPairsShortestPath.getPath(currentStop.getName(), targetStop.getName());
		
		for (int i = 0; i < pathToTarget.size(); i++){
			
			Vertex A = pathToTarget.get(i);
			Vertex B = pathToTarget.get(i+1);
			
			cost += graph.getEdgeBetweenVertices(A, B).getWeight();
			
		}
		
		return cost;
		
	}
}