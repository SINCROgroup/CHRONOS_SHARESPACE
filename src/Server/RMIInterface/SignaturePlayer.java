/*
 * 
 */
package Server.RMIInterface;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Interface SignaturePlayer.
 */
public interface SignaturePlayer extends Serializable{

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	ArrayList<Float> getPosition();
	
	/**
	 * Gets the velocity.
	 *
	 * @return the velocity
	 */
	ArrayList<Float> getVelocity();
}
