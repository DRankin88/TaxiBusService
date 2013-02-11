package database;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to collect the output from the simulator and write it to an excel file
 * @author David Rankin
 *
 */
public class Output {

	Map<String, Object[]> data;
	
	public Output(){
		
		data = new HashMap<String, Object[]>();
		
	}
	
	
	
}