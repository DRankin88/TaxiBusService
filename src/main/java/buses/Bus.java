package buses;

import java.util.ArrayList;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

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
	private LinkedList<Vertex> path;
	private int costToNextStop;

	public Bus(String name, int capacity, Vertex currentStop, Graph graph) {

		this.name = name;
		this.capacity = capacity;
		this.currentStop = currentStop;
		this.graph = graph;
		this.path= new LinkedList<Vertex>();

	}

	public Vertex getTargetStop() {
		return targetStop;
	}

	public void setTargetStop(Vertex targetStop) {
		this.targetStop = targetStop;

		this.path = AllPairsShortestPath.getPath(currentStop.getName(), targetStop.getName());

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

	
	public void pickupPassenger(Passenger passenger){

		passengersOnBus.add(passenger);

	}

	/**
	 * Works out the distance in terms of cost between this bus and a targeted Vertex.
	 * @param targetStop
	 * @return
	 */
	public int distance(Vertex targetStop){


		int cost = 0;

		LinkedList<Vertex> pathToTarget = AllPairsShortestPath.getPath(currentStop.getName(), targetStop.getName());

		for (int i = 0; i < pathToTarget.size(); i++){

			Vertex A = pathToTarget.get(i);
			Vertex B = pathToTarget.get(i+1);

			cost += graph.getEdgeBetweenVertices(A, B).getWeight();

		}

		return cost;

	}

	/**
	 * Moves the bus along its path the equivalent of one time step
	 */
	public void moveAlongPath(){

		if (costToNextStop != 0){
			// We must be traversing an edge between stops

			costToNextStop = costToNextStop - 1;

			if (costToNextStop == 0){
				//We have reached next stop	
				currentStop = path.get(1);
				path.remove(0);	

			}
		}

		// We are at a stop
		else if (graph.getVertexes().contains(currentStop)) {

			
			if(!path.isEmpty()){


				currentStop = path.get(0);
				Vertex nextStop = path.get(1);

				costToNextStop = graph.getEdgeBetweenVertices(currentStop, nextStop).getWeight();
				costToNextStop = costToNextStop - 1;


			}
		}

		// We reached the final stop in our path
		if (path.size() == 1){

			path.clear();

		}

	}



}