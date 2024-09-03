package Administrator.Algorithm;

import Administrator.Algorithm.Settings;

// TODO: Auto-generated Javadoc
/**
 * @version 
 * <ul>
 * <li> 1.0: March 6, 2016 created by Maria Lombardi </li>
 * </ul>
 * <hr>
 * @author Maria Lombardi
 * <p>
 * <br>
 * The Class AdaptiveController.
 * <p>
 * This class implements an adaptive controller both when the virtual player
 * acts as a follower and when acts as a leader.
 * It has a constructor and only two public methods: one for the leader
 * and one for the follower.
 * <br>
 */
public class AdaptiveController {
	
	/** The settings. */
	private Settings settings;
	
	/** The aee. */
	private float aee;
	
	/** The bee. */
	private float bee;
	
	/**
	 * Instantiates a new adaptive controller.
	 */
	public AdaptiveController(){
		this.settings = Settings.getSettings();
		this.aee = settings.getAee();
		this.bee = settings.getBee();
	}
	
	/**
	 * Controller law when the virtual player acts as a follower.
	 *
	 * @param x  virtual player's position
	 * @param y  virtual player's velocity
	 * @param Rp  human player's sampled position
	 * @param Rv  human player's estimated velocity
	 * @return signal of control u
	 */
	public float controllerLawFollower(int index, float x, float y, float Rp, float Rv, TypeDynamic f){
		float u = 0;			
			
		u = (float) ((this.aee + this.bee*Math.pow(x-Rp, 2))*(y-Rv) - settings.getCp(index)*Math.exp(-settings.getEpsilon(index)*Math.pow(y-Rv, 2))*(x-Rp));
			
		float aeep = adaptiveLawA(this.aee, x, y, Rp, Rv);		
		this.aee = this.aee + aeep*((float)settings.getDelay()/(float)1000);
		this.aee = constrains(this.aee, settings.getuMax());
		float beep = adaptiveLawB(index, this.bee, x, y, Rp, Rv, u, f);
		this.bee = this.bee + beep*((float)settings.getDelay()/(float)1000);
		this.bee = constrains(this.bee, settings.getuMax());
			
		return u;		
	}

	/**
	 * Controller law when the virtual player acts as a leader.
	 *
	 * @param x  virtual player's position
	 * @param y  virtual player's velocity
	 * @param Sp  position of reference signature
	 * @param Sv  velocity of reference signature
	 * @param Rp  human player's sampled position
	 * @param Rv  human player's estimated velocity
	 * @return signal of control u
	 */
	// Sv è il vettore delle velocità registrate
	public float controllerLawLeader(int index, float x, float y, float Sp, float Sv, float Rp, float Rv, TypeDynamic f){
		float u = 0;
		float lambda = calculateLambda(index, x, Rp);
		
		u = lambda*controllerLawFollower(index, x, y, Sp, Sv, f) + (1-lambda)*settings.getCh(index)*(Rp-x);
		
		float aeep = adaptiveLawA(this.aee, x, y, Rp, Rv);		
		this.aee = this.aee + aeep*((float)settings.getDelay()/(float)1000);
		this.aee = constrains(this.aee, settings.getuMax());
		float beep = adaptiveLawB(index, this.bee, x, y, Rp, Rv, u, f);
		this.bee = this.bee + beep*((float)settings.getDelay()/(float)1000);
		this.bee = constrains(this.bee, settings.getuMax());
			
		return u;
	}

	private float adaptiveLawA(float aee, float x, float y, float Rp, float Rv){	

		return (float) ((-1/aee) * ((x-Rp)*(y-Rv) + Math.pow(x-Rp, 2)));
	}
		

	@SuppressWarnings("null")
	private float adaptiveLawB(int index, float bee, float x, float y, float Rp, float Rv, float u, TypeDynamic f){
		
		if(f.equals(TypeDynamic.HKB))
			return (float) ((1/bee) * (y-Rv) * 
				((settings.getAlpha(index)*Math.pow(x, 2) + settings.getBeta(index)*Math.pow(y, 2) 
				- settings.getGamma(index))*y + (Math.pow(settings.getOmega(index), 2)*x - u))); 
		else if(f.equals(TypeDynamic.LIN_OSCILLATOR))
			return (float) ((1/bee) * (y-Rv) * ((settings.getA(index)*y + settings.getB(index)*x) - u));
		else if(f.equals(TypeDynamic.INTEGRATOR))
			return (float) ((1/bee) * (y-Rv) * (2*u));
		else
			System.out.println("Error with adaptive control law");
			return (Float) null;
	}
		
	
	private float constrains(float u, float max){
	    	
	   	if(!(Math.abs(u) <= max) && !Float.isNaN(u))
	   		return Math.signum(u)*max;
	   	else if (Float.isNaN(u))  	
    		return (float) (Math.signum(-0.5 + Math.random())*max);
	   	else
	   		return u;
	}

	
	private float calculateLambda(int index, float x, float Rp){
		
	   	return (float) Math.exp(-settings.getDelta(index)*Math.abs(x-Rp));
	}
}
