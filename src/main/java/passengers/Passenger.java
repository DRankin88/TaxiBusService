package passengers;

import vogella.Vertex;

/**
 * Object representing a passenger on the bus
 * @author David Rankin
 *
 */
public class Passenger {

	private String name;
	private Vertex startingStop;
	private Vertex destinationStop;
	private int totalTimeInWorld = -1;
	private boolean droppedOff;
	private boolean pickedUp;
	private int whenPickedUp;
	private int creationTime;
	
	public int getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(int creationTime) {
		this.creationTime = creationTime;
	}
	public int getWhenPickedUp() {
		return whenPickedUp;
	}
	public void setWhenPickedUp(int whenPickedUp) {
		this.whenPickedUp = whenPickedUp;
	}
	public void incrimentTimeInWorld(){
		totalTimeInWorld++;	
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public boolean isDroppedOff() {
		return droppedOff;
	}

	public String getName() {
		return name;
	}

	public Vertex getStartingStop() {
		return startingStop;
	}

	public Vertex getDestinationStop() {
		return destinationStop;
	}

	public int getTotalTimeInWorld() {
		return totalTimeInWorld;
	}

	public void setDroppedOff(boolean droppedOff) {
		this.droppedOff = droppedOff;
	}

	public Passenger (String name, Vertex startingStop, Vertex destinationStop){
		
		this.name = name;
		this.destinationStop = destinationStop;
		this.startingStop = startingStop;
		this.droppedOff = false;
		this.pickedUp = false;
		
	}
	
	public String toString(){
		
		return name;
		
	}	
}
