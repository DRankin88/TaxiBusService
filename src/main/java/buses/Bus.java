package buses;

import java.util.HashMap;

import passangers.Passanger;

/**
 * Object representing a bus that travels around a graph along edges and between vertices
 * @author David Rankin
 *
 */
public class Bus {

	private String name;
	private int capacity;
	private HashMap passangers = new HashMap<String, Passanger>();
	
	public Bus(String name) {
	
		this.name = name;
		
	}
	
	
	
}
