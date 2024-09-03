package Administrator.Algorithm.L3_a;

import Administrator.Algorithm.ConstructL3_a;
import Administrator.Algorithm.PhaseEstimation;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeepQNetworkController_MEAN implements ConstructL3_a {

    public enum TypeInput{
        MOUSE,
        LEAP_MOTION
    }
    public enum TypeVP {
    }
    public enum TypeInner {
        KURAMOTO
    }
    public enum parameter{
        c;
    }

    private double[] reward;
    private int k;
    private float theta_a;
    private float[] x_prev;
    private float[] v;
    private float[] v_prev;
    private double[] amplitudePosPPrev;
    private double[] amplitudePosNPrev;
    private double[] amplitudeVelPPrev;
    private double[] amplitudeVelNPrev;
    private float omega;
    private float omega_prec;
    private int currentTime;
    private int prevTime;
    private float dt = 25; // 0.01 s in ms dt used to train the cyberplayer
    private float x_new;
    private float x_a;
    private float amplitude[];
    private double theta_pre[];
    private double theta_w_pre[];
    private float bandwidth_filter = 30;
    private float coeff_filter_velocity = (float) (1.0 - 0.025 * (bandwidth_filter));


    public DeepQNetworkController_MEAN(int NumConnections) {
        this.theta_a=(float)Math.PI / 2;
        this.v=new float[NumConnections];
        this.x_prev=new float[NumConnections];
        this.v_prev=new float[NumConnections];
        this.amplitudePosPPrev = new double[NumConnections];
        this.amplitudePosNPrev = new double[NumConnections];
        this.amplitudeVelPPrev = new double[NumConnections];
        this.amplitudeVelNPrev = new double[NumConnections];
        this.theta_pre=new double[NumConnections];
        this.theta_w_pre=new double[NumConnections];
        this.omega = 0;
        for (int j = 0; j < NumConnections; j ++) {
            this.amplitudePosPPrev[j] = 1;
            this.amplitudePosNPrev[j] = 1;
            this.amplitudeVelPPrev[j] = 1;
            this.amplitudeVelNPrev[j] = 1;
            this.x_prev[j] = 0;
            this.v_prev[j] = 0;
            this.v[j] =0;
        }
        this.currentTime=0;
        this.prevTime=0;
        this.reward = new double[(int) (100*1000/dt)];
        this.k = 0;
        this.x_new = 0;
        this.amplitude = new float[2];
        this.amplitude[0] = 1;
        this.amplitude[1] = 1;
        this.x_a= 0;
        this.omega_prec=0;
    }

    private double[] productRowsColumn(double[][] a, double[] b) {

        double result[] = new double[a.length];

        // loop over rows of a
        for(int i=0; i < a.length; i++) {
            double rowSum = 0;
            for(int j=0; j < b.length; j++) {
                rowSum = rowSum + (a[i][j] * b[j]);
            }
            result[i] = rowSum;
        }

        return result;
    }


    public Complex exponential(double im) {
        //double length = Math.exp(re);
        return new Complex(Math.cos(im), Math.sin(im) );
    }

    public Complex add_complex(Complex a, Complex b) {
        double real_a = a.getReal();
        double real_b = b.getReal();
        double imag_a = a.getImaginary();
        double imag_b = b.getImaginary();

        return new Complex(real_a + real_b, imag_a + imag_b);
    }



    public float PositionComputation(float[] x,float[] c) {

        currentTime = (int)System.currentTimeMillis();
        double[] theta = new double[x.length];
        double u = 0;
        for(int j = 0; j < x.length; j++) {
            v[j] = (x[j] - this.x_prev[j])*((float)1000/(float)(currentTime-prevTime));
        }
        for (int j = 0; j < x.length; j++) {
            double[] result = PhaseEstimation.estimatePhaseAHP((double)x[j], (double)x_prev[j], (double)v[j], (double)v_prev[j], amplitudePosPPrev[j], amplitudePosNPrev[j], amplitudeVelPPrev[j], amplitudeVelNPrev[j]);
            theta[j] = theta_pre[j]+unwrapping(theta_w_pre[j],result[0]);
            theta_w_pre[j]=result[0];
            u += Math.sin(theta[j] - (double)theta_a);
            this.amplitudePosPPrev[j] = (float)result[1];
            this.amplitudePosNPrev[j] = (float)result[2];
            this.amplitudeVelPPrev[j] = (float)result[3];
            this.amplitudeVelNPrev[j] = (float)result[4];
            //System.out.println("amplitude_palyer_"+j+": "+theta[j]+" , "+this.amplitudePosPPrev[j]+" , "+this.amplitudePosNPrev[j]+" , "+this.amplitudeVelPPrev[j]+" , "+this.amplitudeVelNPrev[j]);
        }
        double frequency=0;
        for (int i = 0; i < theta.length; i++) {
            frequency+=((this.theta_pre[i]-theta[i])/((float)(currentTime-prevTime)/(float)1000));
        }
        float mean =  (float)frequency/theta.length;
        this.omega= coeff_filter_velocity * this.omega_prec + (1 - coeff_filter_velocity) * mean;
        this.omega_prec=this.omega;
        double theta_dot = 0;
        theta_dot = this.omega + (c[0] / x.length) * u;

        this.theta_a = this.theta_a + (float)(theta_dot)*((float)(currentTime-prevTime)/(float)1000);
        this.theta_a = (float) ((this.theta_a + 2 * Math.PI) % (2 * Math.PI));
        if (this.x_new > 0) {
            float sum_ampl = 0;
            for (double num : this.amplitudePosPPrev) {
                sum_ampl += num;
            }
            float mean_ampl = sum_ampl / this.amplitudePosPPrev.length;
            this.x_new = (float) ((this.amplitude[0]) * Math.cos(this.theta_a));
            if (this.x_new < 0) {
                this.amplitude[0] = mean_ampl;
            }
        }
        else if (this.x_new <= 0){
            float sum_ampl = 0;
            for (double num : this.amplitudePosNPrev) {
                sum_ampl += num;
            }
            float mean_ampl = sum_ampl / this.amplitudePosNPrev.length;
            this.x_new = (float) ((this.amplitude[1]) * Math.cos(this.theta_a));
            if (this.x_new < 0) {
                this.amplitude[1] = mean_ampl;
            }
        }
        //this.x_new=(float)Math.cos( this.theta_a);
        if(Math.abs(this.x_new) <= 5) {
            this.x_a = this.x_new;
        } else {
            this.x_a = Math.signum(this.x_new)*5f;
        }
        this.v_prev= Arrays.copyOf(this.v, this.v.length);
        this.prevTime=this.currentTime;;
        this.x_prev=Arrays.copyOf(x, x.length);
        for (int i = 0; i < theta.length; i++) {
            this.theta_pre[i] = theta[i];
        }
        //System.out.println(this.theta_a+" , "+this.amplitude[0]+" , "+ this.amplitude[1]+" , "+this.omega+" , "+this.x_a);
        return this.x_a;
    }



    float wrapping(float theta) {
        if (theta > Math.PI) {
            theta -= 2 * Math.PI;
        }
        else if (theta < -Math.PI) {
            theta += 2 * Math.PI;
        }
        return theta;
    }

    float wrapped_difference(float angle1,float angle2) {
        float diff=angle1-angle2;
        if (diff > Math.PI) {
            diff -= 2 * Math.PI;
        }
        else if (diff < -Math.PI) {
            diff += 2 * Math.PI;
        }
        return diff;
    }
    double unwrapping(double angle1,double angle2) {
        double diff=angle1-angle2;
        if (diff > Math.PI) {
            diff -= 2 * Math.PI;
        }
        else if (diff < -Math.PI) {
            diff += 2 * Math.PI;
        }
        return diff;
    }

}

