
package HumanPlayer.Algorithm;

import HumanPlayer.Algorithm.TypeDynamic;


public class AdaptiveController {

	private Settings settings;
	private float aee;
	private float bee;
	

	public AdaptiveController(){
		this.settings = Settings.getSettings();
		this.aee = settings.getAee();
		this.bee = settings.getBee();
	}
	
	// Rp è il vettore delle posizioni rilevate
	// Rv è il vettore delle velocità stimate
	// x è la posizione del virtual player
	// y è la velocità
	public float controllerLawFollower(float x, float y, float Rp, float Rv, TypeDynamic f){
		float u = 0;			
		// constrains su aee e bee
		
		u = (float) ((this.aee + this.bee*Math.pow(x-Rp, 2))*(y-Rv) - settings.getCp()*Math.exp(-settings.getEpsilon()*Math.pow(y-Rv, 2))*(x-Rp));
		
		float aeep = adaptiveLawA(this.aee, x, y, Rp, Rv);		
		this.aee = this.aee + aeep*((float)settings.getDelay()/(float)1000);
		this.aee = constrains(this.aee, settings.getuMax());
		float beep = adaptiveLawB(this.bee, x, y, Rp, Rv, u, f);
		this.bee = this.bee + beep*((float)settings.getDelay()/(float)1000);
		this.bee = constrains(this.bee, settings.getuMax());
		
		return u;		
	}
	
	// Sp è il vettore delle posizioni registrate
	// Sv è il vettore delle velocità registrate
	public float controllerLawLeader(float x, float y, float Sp, float Sv, float Rp, float Rv, TypeDynamic f){
		float u = 0;
		float lambda = calculateLambda(x, Rp);
		
		u = lambda*controllerLawFollower(x, y, Sp, Sv, f) + (1-lambda)*settings.getCh()*(Rp-x);
		
		float aeep = adaptiveLawA(this.aee, x, y, Rp, Rv);		
		this.aee = this.aee + aeep*((float)settings.getDelay()/(float)1000);
		this.aee = constrains(this.aee, settings.getuMax());
		float beep = adaptiveLawB(this.bee, x, y, Rp, Rv, u, f);
		this.bee = this.bee + beep*((float)settings.getDelay()/(float)1000);
		this.bee = constrains(this.bee, settings.getuMax());
		
		return u;
	}
	

	private float adaptiveLawA(float aee, float x, float y, float Rp, float Rv){	

		return (float) ((-1/aee) * ((x-Rp)*(y-Rv) + Math.pow(x-Rp, 2)));
	}
	

	@SuppressWarnings("null")
	private float adaptiveLawB(float bee, float x, float y, float Rp, float Rv, float u, TypeDynamic f){
		
		if(f.equals(TypeDynamic.HKB))
			return (float) ((1/bee) * (y-Rv) * 
				((settings.getAlpha()*Math.pow(x, 2) + settings.getBeta()*Math.pow(y, 2) 
				- settings.getGamma())*y + (Math.pow(settings.getOmega(), 2)*x - u))); 
		else if(f.equals(TypeDynamic.LIN_OSCILLATOR))
			return (float) ((1/bee) * (y-Rv) * ((settings.getA()*y + settings.getB()*x) - u));
		else if(f.equals(TypeDynamic.INTEGRATOR))
			return (float) ((1/bee) * (y-Rv) * (2*u));
		else
			System.out.println("Error with adaptive control law");
			return (Float) null;
	}
	

    float constrains(float u, float max){
    	
    	if(!(Math.abs(u) <= max) && !Float.isNaN(u))
    		return Math.signum(u)*max;
    	else if (Float.isNaN(u))  	
    		return (float) (Math.signum(-0.5 + Math.random())*max);
    	else
    		return u;
    }
    
    
    float calculateLambda(float x, float Rp){

    	return (float) Math.exp(-settings.getDelta()*Math.abs(x-Rp));
    }
		
}
