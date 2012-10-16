package passengers;

/**
 * Object representing a passenger on the bus
 * @author David Rankin
 *
 */
public class Passenger {

	private String name;
	private String destinationStop;
	private int totalTimeOnBus;
	
	public Passenger (String name, String destinationStop){
		
		this.name = name;
		this.destinationStop = destinationStop;
		
	}
}
