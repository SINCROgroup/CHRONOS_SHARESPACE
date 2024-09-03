/*
 * 
 */
package Server.Entity;

import java.util.ArrayList;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonClass.
 */
public class CommonClass {

	
	/**
	 * Instantiates a new common class.
	 */
	public CommonClass() {
		
	}
	
	/**
	 * Generate id player.
	 *
	 * @param name the name
	 * @param surname the surname
	 * @param date the date
	 * @return the int
	 */
	public int generateIdPlayer(String name, String surname, Calendar date){
	
		int id = 0;	
		int sumName = 0;
		int sumSurname = 0;
		
		for(int i = 0; i < name.length(); i++)
			sumName = sumName + (int)name.charAt(i);
		
		for(int i = 0; i < surname.length(); i++)
			sumSurname = sumSurname + (int)surname.charAt(i);
		
		id = date.get(Calendar.DAY_OF_MONTH) + date.get(Calendar.MONTH) 
					+ date.get(Calendar.YEAR) + sumName + sumSurname;
							
		return id;
	}
	
	/**
	 * Generate id signature.
	 *
	 * @param d the d
	 * @param s the s
	 * @param v the v
	 * @return the int
	 */
	public int generateIdSignature(Calendar d, ArrayList<Float> s, ArrayList<Float> v){
			int id = (int) (d.get(Calendar.DAY_OF_MONTH) + d.get(Calendar.MONTH) + d.get(Calendar.YEAR)
			 + s.get(0) + v.get(0));
		return id;
	}
}
