/*
 * 
 */
package HumanPlayer.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * The Class Linear.
 */
public class Linear {
	
	/** The settings. */
	private Settings settings;
	
	/**
	 * Instantiates a new linear.
	 */
	public Linear(){
		this.settings = Settings.getSettings();
	}

	/**
	 * Dynamic linear first.
	 *
	 * @param y the y
	 * @return the float
	 */
	public float dynamicLinearFirst(float y){
		return y;
	}
	
	/**
	 * Dynamic linear second.
	 *
	 * @param x the x
	 * @param y the y
	 * @param u the u
	 * @return the float
	 */
	public float dynamicLinearSecond(float x, float y, float u){
		return (float)(-settings.getA()*y - settings.getB()*x + u);
	}
}
