package Administrator.Algorithm;
import java.lang.Math;

public class PhaseEstimation {
    
    public static double[] estimatePhaseAHP(double position,
            double positionPrev,
            double velocity,
            double velocityPrev,
            double amplitudePosPPrev,
            double amplitudePosNPrev,
            double amplitudeVelPPrev,
            double amplitudeVelNPrev) {

		double amplitudeVelP, amplitudeVelN, amplitudePosP, amplitudePosN, velocityNormalized, positionNormalized;
		
		if (positionPrev < 0 && position >= 0) {
			amplitudeVelP = Math.abs(velocity);

		} else {
			amplitudeVelP = amplitudeVelPPrev;
		}

		
		if (positionPrev >= 0 && position < 0) {
			amplitudeVelN = Math.abs(velocity);
		} else {
			amplitudeVelN = amplitudeVelNPrev;
		}

		if (velocityPrev >= 0 && velocity < 0) {
			amplitudePosP = Math.abs(position);

		} else {
			amplitudePosP = amplitudePosPPrev;
		}
		
		if (velocityPrev < 0 && velocity >= 0) {
			amplitudePosN = Math.abs(position);
		} else {
			amplitudePosN = amplitudePosNPrev;
		}
		amplitudePosN=Math.max(amplitudePosN,0.2);
		amplitudePosP=Math.max(amplitudePosP,0.2);
		amplitudeVelN=Math.max(amplitudeVelN,0.2);
		amplitudeVelP=Math.max(amplitudeVelP,0.2);
		double[] output = new double[5];
		
		if (position >= 0) {
			positionNormalized = position / amplitudePosP;
		} else {
			positionNormalized = position / amplitudePosN;
		}
		
		if (velocity >= 0) {
			velocityNormalized = velocity / amplitudeVelP;
		} else {
			velocityNormalized = velocity / amplitudeVelN;
		}
		
		
		
		output[0] = Math.atan2(-velocityNormalized, positionNormalized);
		
		output[1] = amplitudePosP;
		output[2] = amplitudePosN;
		output[3] = amplitudeVelP;
		output[4] = amplitudeVelN;
		return output;
	}
}


