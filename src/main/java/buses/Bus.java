package buses;

import java.util.ArrayList;
import java.util.LinkedList;

import controller.BusCentralDatabase;

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
	private String assignedPassengerName;
	private AllPairsShortestPath myPath;
	private ArrayList<Passenger> assignedPassengers = new ArrayList<Passenger>();

	public Bus(String name, int capacity, Vertex currentStop, Graph graph, AllPairsShortestPath myPath) {

		this.name = name;
		this.capacity = capacity;
		this.currentStop = currentStop;
		this.graph = graph;
		this.path= new LinkedList<Vertex>();
		this.assignedPassengerName = "No One";

	}

	// DEPRACATED
	public void setAssignedPassengerName(String assignedPassengerName) {
		this.assignedPassengerName = assignedPassengerName;
	}

	public ArrayList<Passenger> getAssignedPassengers() {
		return assignedPassengers;
	}

	public String getAssignedPassengerName() {
		return assignedPassengerName;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public Graph getGraph() {
		return graph;
	}

	public LinkedList<Vertex> getPath() {
		return path;
	}

	public int getCostToNextStop() {
		return costToNextStop;
	}

	public ArrayList<Passenger> getPassengersOnBus() {
		return passengersOnBus;
	}

	public void setPath(LinkedList<Vertex> path) {
		this.path = path;
	}

	public Vertex getTargetStop() {
		return targetStop;
	}

	public void setTargetStop(Vertex targetStop) {
		this.targetStop = targetStop;

		AllPairsShortestPath thisPath = new AllPairsShortestPath(graph);

		this.path = thisPath.getPath(currentStop.getName(), targetStop.getName());

	}

	public Vertex getCurrentStop() {
		return currentStop;
	}

	public void setCurrentStop(Vertex currentStop) {
		this.currentStop = currentStop;
	}

	public void removePassenger(Passenger passenger){

		passengersOnBus.remove(passenger);

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

	// DEPRACATED
	public void pickupPassenger(Passenger passenger){

		passengersOnBus.add(passenger);

	}

	public void dropOffPassengers(){

		int passengercount = passengersOnBus.size();
		ArrayList<Passenger> dropOffs = new ArrayList<Passenger>();

		for (int i = 0; i < passengercount; i++){

			Passenger passenger = passengersOnBus.get(i);

			if (passenger.getDestinationStop().equals(currentStop)){

				dropOffs.add(passenger);

			}
		}

		passengersOnBus.removeAll(dropOffs);
		BusCentralDatabase.removePassengersFromTheWorld(dropOffs);
		
	}

	public void pickupPassengers(){

		ArrayList<Passenger> pickups = BusCentralDatabase.getPassengersAtMyStop(currentStop);
		int numberOfPickups = pickups.size();

		for (int i = 0; i < numberOfPickups; i++){

			Passenger passenger = pickups.get(i);

			// Pickup the passenger if true
			if (assignedPassengers.contains(passenger)){

				passengersOnBus.add(passenger);
				passenger.setPickedUp(true);

			}
		}
	}

	/**
	 * Works out the distance in terms of cost between this bus and a targeted Vertex.
	 * @param targetStop
	 * @return
	 */
	public int distance(Vertex targetStop){


		int cost = 0;

		LinkedList<Vertex> pathToTarget = myPath.getPath(currentStop.getName(), targetStop.getName());

		if (pathToTarget.size() == 1){

			return cost;

		}

		if (pathToTarget.size() == 2){

			Vertex A = pathToTarget.get(0);
			Vertex B = pathToTarget.get(1);

			cost += graph.getEdgeBetweenVertices(A, B).getWeight();

			return cost;

		}

		for (int i = 0; i < pathToTarget.size() - 1; i++){

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

	/**
	 * This assigns a passenger to this bus for pickup
	 * @param pickups
	 */
	public void assignPassenger(Passenger pickup){

		assignedPassengers.add(pickup);
		BusCentralDatabase.removePassengerFromUnallocated(pickup);

	}

}