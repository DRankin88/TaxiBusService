package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import passengers.Passenger;
import vogella.Vertex;
import buses.Bus;

/**
 * Models a controller back at HQ that holds information about the world as we move through time.
 * @author David Rankin
 *
 */
public class BusCentralDatabase {

	// This is a "static" class and so is non-instantiable
	private BusCentralDatabase(){};

	private static ArrayList<Passenger> passengersInTheWorld = new ArrayList<Passenger>();
	private static ArrayList<Passenger> unallocatedPassengers = new ArrayList<Passenger>();
	private static ArrayList<Passenger> passengersWaiting = new ArrayList<Passenger>();
	private static ArrayList<Bus> busesInTheWorld = new ArrayList<Bus>();
	private static ArrayList<Bus> freeBuses = new ArrayList<Bus>();
	private static boolean allPassengersCreated = false;
	private static int timeToComplete;
	private static HashMap<Passenger, Object[]> passengerStats = new HashMap<Passenger, Object[]>();
	private static HashMap<Bus, Object[]> busStats = new HashMap<Bus, Object[]>();


	public static int getTimeToComplete() {
		return timeToComplete;
	}

	public static void setTimeToComplete(int timeToComplete) {
		BusCentralDatabase.timeToComplete = timeToComplete;
	}

	public static HashMap<Passenger, Object[]> getPassengerStats() {
		return passengerStats;
	}

	public static void setPassengerStats(HashMap<Passenger, Object[]> passengerStats) {
		BusCentralDatabase.passengerStats = passengerStats;
	}

	public static HashMap<Bus, Object[]> getBusStats() {
		return busStats;
	}

	public static void setBusStats(HashMap<Bus, Object[]> busStats) {
		BusCentralDatabase.busStats = busStats;
	}

	public static boolean isAllPassengersCreated() {
		return allPassengersCreated;
	}

	public static void setAllPassengersCreated(boolean allPassengersCreated) {
		BusCentralDatabase.allPassengersCreated = allPassengersCreated;
	}

	public static void addPassengerToWaiting(Passenger passenger){

		passengersWaiting.add(passenger);

	}

	public static void removePassengerFromWaiting(Passenger passenger){

		passengersWaiting.remove(passenger);

	}

	public static void removePassengerFromUnallocated(Passenger passenger){

		unallocatedPassengers.remove(passenger);

	}

	public static void addPassengerToUnallocated(Passenger passenger){

		unallocatedPassengers.add(passenger);

	}

	public static void addBusToFreeBuses(Bus bus){

		freeBuses.add(bus);

	}

	public static ArrayList<Bus> getFreeBuses() {
		return freeBuses;
	}



	public static void setFreeBuses(ArrayList<Bus> freeBuses) {
		BusCentralDatabase.freeBuses = freeBuses;
	}



	public static ArrayList<Passenger> getPassengerInTheWorld() {
		return passengersInTheWorld;
	}



	public static ArrayList<Passenger> getUnallocatedPassengers() {
		return unallocatedPassengers;
	}



	public static void setUnallocatedPassengers(
			ArrayList<Passenger> unallocatedPassengers) {
		BusCentralDatabase.unallocatedPassengers = unallocatedPassengers;
	}



	public static ArrayList<Bus> getBusesInTheWorld() {
		return busesInTheWorld;
	}



	/**
	 * Adds a passenger to the world. Should be called whenever a new passenger is created
	 * @param passenger
	 */
	public static void addPassengerToWorld(Passenger passenger){

		passengersInTheWorld.add(passenger);

	}

	public static void addBusesToWorld(Bus bus){

		busesInTheWorld.add(bus);

	}

	/**
	 * Used by buses to find out the passengers at their stop
	 * @param currentStop Stop the bus is at
	 * @return the ArrayList of passengers that are at the currentStop
	 */
	// THIS MIGHT BE BROKEN!!!!!!
	public static ArrayList<Passenger> getPassengersAtMyStop(Vertex currentStop){

		ArrayList<Passenger> passengersAtYourStop = new ArrayList<Passenger>();

		for (int i = 0; i < passengersInTheWorld.size(); i++){

			if (passengersInTheWorld.get(i).getStartingStop().equals(currentStop))

				passengersAtYourStop.add(passengersInTheWorld.get(i));

		}

		return passengersAtYourStop;

	}

	public static void removeBusFromUnassigned(Bus bus){

		freeBuses.remove(bus);

	}

	/**
	 * If given a passenger this method returns the bus closest to them that is Unallocated
	 * @return
	 */
	public static Bus getClosestFreeBus(Passenger passenger){

		ArrayList<Bus> myFreeBuses = freeBuses;

		Bus firstBus = myFreeBuses.get(0);

		Bus closestBus = firstBus;
		for (int i = 1; i < myFreeBuses.size(); i++){

			Bus bus = myFreeBuses.get(i);
			if (bus.getCurrentStop().equals(passenger.getStartingStop())){

				return bus;

			}

			if (closestBus.distance(passenger.getStartingStop()) > bus.distance(passenger.getStartingStop())){

				closestBus = bus;

			}
		}

		return closestBus;

	}

	/**
	 * Finds the bus that the passenger is riding in
	 * @param passenger
	 * @return
	 */
	public static Bus getBusFromPassenger(Passenger passenger){

		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);

			if (bus.getPassengersOnBus().contains(passenger)){

				return bus;

			}

		}

		return null;

	}

	public static void removePassengersFromTheWorld (ArrayList<Passenger> passengers){



		for (Passenger passenger : passengers){

			int time = passenger.getTotalTimeInWorld();
			int timePickedUp = passenger.getWhenPickedUp() - passenger.getCreationTime() - 1;
			int timeOnBus = time - timePickedUp;
			String name = passenger.getName();

			Object[] information = new Object[] {name, time, timePickedUp, timeOnBus};

			passengerStats.put(passenger, information);

		}

		passengersInTheWorld.removeAll(passengers);

		// If this was the last ever dropOff then record the current timestep
		if (allPassengersCreated == true && passengersInTheWorld.size() == 0){

			System.out.println("Algorithm Terminated on timeStep " + TimeStepper.getTime());
			writeOutputToExcel();
			System.exit(0);

		}
	}

	public static void collectBusData(){

		for (Bus bus : busesInTheWorld){

			int distanceTravelled = bus.getDistanceTravelled();
			String busName = bus.getName();
			Object[] stats = {busName, distanceTravelled};

			busStats.put(bus, stats);

		}
	}

	public static void writeOutputToExcel(){

		collectBusData();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sample");

		Set<Passenger> keyset = passengerStats.keySet();
		Row row = sheet.createRow(0);
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Time in System");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("Time waiting for pickup");
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Time on Bus");
		int rownum = 1;
		for (Passenger passenger : keyset){

			row = sheet.createRow(rownum++);
			Object[] objArr = passengerStats.get(passenger);
			int cellnum = 0;
			for (Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if(obj instanceof Integer){
					cell.setCellValue((Integer)obj);
				}
				if(obj instanceof String){
					cell.setCellValue((String)obj);
				}
			}
		}
		
		row = sheet.createRow(rownum++);
		Cell cell4 = row.createCell(1);
		cell4.setCellValue("Distance Travelled");
		
		Set<Bus> busset = busStats.keySet();
		
		for (Bus bus : busset){

			row = sheet.createRow(rownum++);
			Object[] objArr = busStats.get(bus);
			int cellnum = 0;
			for (Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if(obj instanceof Integer){
					cell.setCellValue((Integer)obj);
				}
				if(obj instanceof String){
					cell.setCellValue((String)obj);
				}
			}
		}
			
		try {
			FileOutputStream out = new FileOutputStream(new File("C:\\Users\\David Rankin\\Dropbox\\University\\Honours Project\\DATA\\new.xls"));
			workbook.write(out);
			out.close();
			System.out.println("Output Written to Excel");
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void printStateOfWorld(){

		//Print the location of all buses and who they are assigned to and where they are going
		for (int i = 0; i < busesInTheWorld.size(); i++){

			Bus bus = busesInTheWorld.get(i);
			StringBuffer output = new StringBuffer();


			if (bus.getCostToNextStop() == 0){

				output.append("Bus named " + bus.getName() + " is currently at stop " + bus.getCurrentStop());

			}

			else {

				output.append("Bus named " + bus.getName() + " is between stops " + bus.getCurrentStop() + " " + bus.getPath().get(1) + " and will get there in " + bus.getCostToNextStop() + " steps");

			}

			if (!bus.getPath().isEmpty()){

				output.append(", is moving along the path " + bus.getPath());

			}

			if (bus.getAssignedPassengers().size() > 0){

				output.append(" and is assigned to pick up passengers " + bus.getAssignedPassengers().toString());

			}

			if (bus.getPassengersOnBus().size() > 0){

				output.append(" and has passengers on bus " + bus.getPassengersOnBus().toString());

			}

			System.out.println(output);

		}

		//Print the location of all passengers
		for (int j = 0; j < passengersInTheWorld.size(); j++){

			Passenger passenger = passengersInTheWorld.get(j);

			if (!passenger.isPickedUp()){

				System.out.println("Passenger named " + passenger.getName() + " is currently waiting to be picked up from stop " + passenger.getStartingStop());

			}

			if (passenger.isPickedUp() && !passenger.isDroppedOff()){

				System.out.println("Passenger named " + passenger.getName() + " is currently on bus named " + getBusFromPassenger(passenger).getName());

			}

		}
	}
}
