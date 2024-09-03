package HumanPlayer.Algorithm;

import java.util.ArrayList;

public class QLearningController {
/*
	private Settings settings;
	private ArrayList<ArrayList<Float>> Q;
	private ArrayList<ArrayList<Float>> states;
	private ArrayList<Float> actions;
	private float dt = 30; // 0.03 s in ms dt used to train the cyberplayer
	
	
	public QLearningController() {
		this.settings = Settings.getSettings();
		this.Q = settings.getQ();
		setActions();
		setStates();
	}
	
	
	private void setActions() {
		//float[] actions = new float[] {-4, -2, -1, -0.5f, 0, 0.5f, 1, 2, 4};
		float[] actions = new float[] {-5, -2.5f, -1.25f, -0.5f, 0, 0.5f, 1.25f, 2.5f, 5};
		this.actions = new ArrayList<Float>();
		for(int i = 0; i < actions.length; i++) {
			this.actions.add(actions[i]);
		}
	}
	
	
	private void setStates() {
		this.states = new ArrayList<ArrayList<Float>>();
		// [-0.5f, 0.5f], stepsize 0.2f
		float[] pos_leader = new float[] {-0.5f, -0.3f, -0.1f, 0.1f, 0.3f, 0.5f};
		// [-1f, 1f], stepsize 0.4f
		float[] vel_leader = new float[] {-1, -0.6f, -0.2f, 0.2f, 0.6f, 1};
		// [-1f, 1f], stepsize 0.2f
		float[] err_pos = new float[] {-1, -0.8f, -0.6f, -0.4f, -0.2f, 0, 0.2f, 0.4f, 0.6f, 0.8f, 1};
		// [-2f, 2f], stepsize 0.4f
		float[] err_vel = new float[] {-2, -1.6f, -1.2f, -0.8f, -0.4f, 0, 0.4f, 0.8f, 1.2f, 1.6f, 2};
		
		for(int m = 0; m < pos_leader.length; m++) {
			for(int n = 0; n < vel_leader.length; n++) {
				for(int j = 0; j < err_pos.length; j++) {
					for(int k = 0; k < err_vel.length; k++) {
						ArrayList<Float> row = new ArrayList<Float>();
						row.add(pos_leader[m]);
						row.add(vel_leader[n]);
						row.add(err_pos[j]);
						row.add(err_vel[k]);
						this.states.add(row);
					}
				}
			}
		}
	}
	
	// x, y pos and vel of virtual player
	// Rp, Sv pos and vel of the human player
	public float controllerLawFollower(float x, float y, float Rp, float Sv) {
		
		// extended state
		ArrayList<Float> currentExtendedState = new ArrayList<Float>();
		currentExtendedState.add(Rp); 	// pos leader
		currentExtendedState.add(Sv); 	// vel leader
		currentExtendedState.add(x-Rp); // err pos
		currentExtendedState.add(y-Sv); // err vel
        
        ArrayList<Float> sumRow = new ArrayList<Float>();
        for(int i = 0; i < this.states.size(); i++) {
        	float sum = 0;
        	for(int j = 0; j < this.states.get(i).size(); j++) {
        		float value = (float) Math.pow((this.states.get(i).get(j) - currentExtendedState.get(j)), 2);
        		sum = sum + value;
        	}
        	sumRow.add(sum);
        }
        
        float min = sumRow.get(0);
        for(int i = 1; i < sumRow.size(); i++) {
        	if(sumRow.get(i) < min) {
        		min = sumRow.get(i);
        	}
        }
        int sIdx = sumRow.indexOf(min);
        
        System.out.println("Q row: " + this.Q.get(sIdx));
        //check if the rows is all zero, if not evaluate the max
        int j = 0;
        while(j < this.Q.get(sIdx).size() && this.Q.get(sIdx).get(j) == 0) {
        	j++;
        }
        
        float u = 0;
        
        if(j == actions.size()) { // sono tutti zero
        	if(x > Rp) {
        		u = this.actions.get(0);
        	} else if(x < Rp) {
        		u = this.actions.get(this.actions.size()-1);
        	}
        	System.out.println("taken action: " + u);
        } else {     
	        float max = this.Q.get(sIdx).get(0);
	        for(int i = 1; i < this.Q.get(sIdx).size(); i++) {
	        	if(this.Q.get(sIdx).get(i) > max) {
	        		max = this.Q.get(sIdx).get(i);
	        	}
	        }
	        int aIdx = this.Q.get(sIdx).indexOf(max);
              
	        u = this.actions.get(aIdx);
        }
		
		return u;
	}
	
	
	float constrains(float u, float max){
    	
    	if(!(Math.abs(u) <= max) && !Float.isNaN(u))
    		return Math.signum(u)*max;
    	else if (Float.isNaN(u))  	
    		return (float) (Math.signum(-0.5 + Math.random())*max);
    	else
    		return u;
    }
	*/

	
	private float dt = 30; // 0.03 s in ms dt used to train the cyberplayer
	
	public float getDt() {
		return dt;
	}

	
	public float coupling(float theta_1, float x_2) {
		
		double theta_2 = Math.atan2(0, x_2);
		double u = 0;
        u = 1.25 / 2 * Math.sin(theta_2 - theta_1);		
		return (float) u;
}

	
}
