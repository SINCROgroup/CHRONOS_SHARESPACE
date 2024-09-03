package Administrator.Algorithm;


public class Kuramoto {
	
	private Settings settings;
	
	public Kuramoto(){
		this.settings = Settings.getSettings();
	}
	
	public float KuramotoDyn(int index, double u, double omega){
			
		if (Double.isNaN(u)){
			u = 0;
		}
		double theta_dot = 0;
		settings.setCoupling(index, settings.getAlpha(index));
		//theta_dot = 4;
		theta_dot = omega + (settings.getCoupling(index) / (settings.getNumPlayers())) * u;
		//theta_dot = omega;
		//theta_dot = (theta_dot + 2 * Math.PI) % (2 * Math.PI);
		double print_omega = Math.round(omega * 100.0) / 100.0;
		System.out.println("L3 natural frequency: " + print_omega);
		//System.out.println(settings.getCoupling(index) / (settings.getNumPlayers()));
		return (float) theta_dot; 
					
	}
}