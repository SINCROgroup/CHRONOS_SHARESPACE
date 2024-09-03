
package HumanPlayer.Algorithm;


public class PDController {
	
	private Settings settings;


	public PDController(){
		this.settings = Settings.getSettings();
	}
	

	public float controllerLawLeader(float x, float y, float Rp, float Sv){
		return settings.getKpLeader()*(Rp-x) + settings.getKdLeader()*(Sv-y);		
	}
	
	public float controllerLawFollower(float x, float y, float Rp, float Sv){
		return settings.getKpFollower()*(Rp-x) + settings.getKdFollower()*(Sv-y);		
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
