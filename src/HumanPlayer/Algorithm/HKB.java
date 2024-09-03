/*
 * 
 */
package HumanPlayer.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * The Class HKB.
 */
public class HKB {

	/** The settings. */
	private Settings settings;
	
	/**
	 * Instantiates a new hkb.
	 */
	public HKB(){
		this.settings = Settings.getSettings();
	}
	
	/**
	 * HKB dynamic first.
	 *
	 * @param y the y
	 * @return the float
	 */
	
/*

	// la dinamica è composta da due equazioni
	public float HKBDynamicFirst(float y){
		return y;
	}
	

	public float HKBDynamicSecond(float x, float y, float u){
		
		return (float) (-(settings.getAlpha()*Math.pow(x, 2) + settings.getBeta()*Math.pow(y, 2) - 
				settings.getGamma())*y - Math.pow(settings.getOmega(), 2)*x + u);
	}
*/
	public float Kuramoto(float u){
		
			
		double theta_dot = 0;
		theta_dot = 4 + u;
		theta_dot = (theta_dot + 2 * Math.PI) % (2 * Math.PI);
		return (float) Math.cos(theta_dot) / 10 ; 
					
}
}
