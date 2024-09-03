
package Server.Control;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class SignatureC implements Server.RMIInterface.SignaturePlayer{
	
	private ArrayList<Float> positions;
	private ArrayList<Float> velocity;

	
	public SignatureC(){
		this.positions = new ArrayList<Float>();
		this.velocity = new ArrayList<Float>();
	}
	
	public SignatureC(ArrayList<Float> p, ArrayList<Float> v){
		this.positions = new ArrayList<Float>(p);
		this.velocity = new ArrayList<Float>(v);
	}

	public ArrayList<Float> getPosition() {
		return this.positions;
	}

	public void setPositions(ArrayList<Float> positions) {
		this.positions = positions;
	}

	public ArrayList<Float> getVelocity() {
		return this.velocity;
	}

	public void setVelocity(ArrayList<Float> velocity) {
		this.velocity = velocity;
	}
	
}
