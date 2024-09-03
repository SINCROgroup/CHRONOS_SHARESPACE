package Administrator.Algorithm;


public class NonAdaptiveController {
	
	private Settings settings;
	
	private float dt = 100; // 0.01 s in ms dt used to train the cyberplayer
	
	
	public NonAdaptiveController() {
		this.settings = Settings.getSettings();
	}
	
	
	public float controllerLawImproviser(int index) {	
		settings.setFrequency(index, settings.getAlpha(index));
		return settings.getFrequency(index);
	}
	
	public float[] coupling(float theta_a, double[] x, double[] x_prev, double[] v, double[] v_prev, double[] amplitudePosPPrev, double[] amplitudePosNPrev, double[] amplitudeVelPPrev, double[] amplitudeVelNPrev) {
		
		float[] output = new float[x.length * 5 + 2];
		double[] theta = new double[x.length];
		double u = 0;
		int i = x.length;
		
		for (int j = 0; j < x.length; j++) {
			double[] result = PhaseEstimation.estimatePhaseAHP(x[j], x_prev[j],
	                v[j], v_prev[j], amplitudePosPPrev[j], amplitudePosNPrev[j], amplitudeVelPPrev[j], amplitudeVelNPrev[j]);
			theta[j] = result[0];
			u += Math.sin(theta[j] - (double)theta_a);
			output[i + 1] = (float)result[1];
			output[i + 2] = (float)result[2];
			output[i + 3] = (float)result[3];
			output[i + 4] = (float)result[4];
			i = i + 4;
  
		}
		
		output[0] = (float)u;
		for (int k = 0; k < x.length; k ++) {
			output[k + 1] = (float)theta[k];
		}
		return output;
	}
	
	
	public float getDt() {
		return dt;
	}	
}