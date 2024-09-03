package Administrator.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * @version 
 * <ul>
 * <li> 1.0: March 8, 2016 created by Maria Lombardi </li>
 * </ul>
 * <hr>
 *  @author Maria Lombardi
 * <p>
 * <br>
 * The Class PDController.
 * <p>
 * This class implements a PD controller both when the virtual player
 * acts as a follower and when acts as a leader.
 * It has a constructor and only one public methods for the mathematical law of the control
 * <br>
 */
public class PDController {
	
	/** The settings. */
	private Settings settings;

	/**
	 * Instantiates a new PD controller.
	 */
	public PDController(){
		this.settings = Settings.getSettings();
	}
	
	/**
	 * Controller law pd.
	 *
	 * @param x  virtual player's position
	 * @param y  virtual player's velocity
	 * @param Rp  human player's sampled position
	 * @param Sv  motor signature
	 * @return signal of control u
	 */
	public float controllerLawLeader(int index, float x, float y, float Rp, float Sv){
		return settings.getKpLeader(index)*(Rp-x) + settings.getKdLeader(index)*(Sv-y);		
	}
	
	public float controllerLawFollower(int index, float x, float y, float Rp, float Sv){
		return settings.getKpFollower(index)*(Rp-x) + settings.getKdFollower(index)*(Sv-y);		
	}
	
	
    float constrains(float u, float max){
    	
    	if(!(Math.abs(u) <= max) && !Float.isNaN(u))
    		return Math.signum(u)*max;
    	else if (Float.isNaN(u))  	
    		return (float) (Math.signum(-0.5 + Math.random())*max);
    	else
    		return u;
    }
}
