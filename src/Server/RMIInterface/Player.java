/*
 * 
 */
package Server.RMIInterface;

import java.io.Serializable;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Interface Player.
 */
public interface Player extends Serializable{
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();
	
	/**
	 * Gets the surname.
	 *
	 * @return the surname
	 */
	String getSurname();
	
	/**
	 * Gets the date birth.
	 *
	 * @return the date birth
	 */
	Calendar getDateBirth();
}
