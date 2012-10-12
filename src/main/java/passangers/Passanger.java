package passangers;

/**
 * Object representing a passanger on the bus
 * @author David Rankin
 *
 */
public class Passanger {

	private String name;
	private String destinationStop;
	private int totalTimeOnBus;
	
	public Passanger (String name, String destinationStop){
		
		this.name = name;
		this.destinationStop = destinationStop;
		
	}
}
