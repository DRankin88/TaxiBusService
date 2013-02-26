package algorithms;

import java.util.ArrayList;

import vogella.Graph;

import buses.Bus;
import controller.BusCentralDatabase;
import controller.TimeStepper;
import database.AllPairsShortestPath;

public abstract class BaseAlgorithmBehaviour {

	protected abstract void doAlgorithm(Graph busGraph, AllPairsShortestPath allPairsShortestPath);
	
	protected void pickupPassengers(){

		ArrayList<Bus> busesInTheWorld = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);

			if (bus.getCostToNextStop() == 0){

				bus.pickupPassengers(TimeStepper.getTime());

			}	
		}		
	}

	protected void dropOffPassengers(){

		ArrayList<Bus> allBuses = BusCentralDatabase.getBusesInTheWorld();

		for (int i = 0; i < allBuses.size(); i++){

			Bus bus = allBuses.get(i);

			if (bus.getCostToNextStop() == 0){

				bus.dropOffPassengers();

			}	
		}
	}

	protected void incrimentBuses(){

		for (int i = 0; i < BusCentralDatabase.getBusesInTheWorld().size(); i++){

			Bus bus = BusCentralDatabase.getBusesInTheWorld().get(i);
			bus.moveAlongPath();

		}
	}	
}

