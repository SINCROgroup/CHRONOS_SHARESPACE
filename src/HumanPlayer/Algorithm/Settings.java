
package HumanPlayer.Algorithm;

import Server.Entity.Player;

import java.util.ArrayList;

public class Settings {
	private Player player;
	
	//parametri del leader
	private ArrayList<Float> positionLeader;
	private ArrayList<Float> velocityLeader;
	

	//parametri del HKB
	private float alpha;
	private float beta;
	private float gamma;
	private float omega;

	//parametri lineare
	private float a;
	private float b;
	
	private TypeVP roleVP;
	private TypeControl controlVP;
	private TypeDynamic dynamicVP;
	private TypeInput input;
	
	//parametri del controllo adattativo
	private float epsilon;
	private float cp;
	private float uMax;
	private float delta;
	private float ch;
	
	//parametri del PD
	private float KpLeader;
	private float KdLeader;
	
	private float KpFollower;
	private float KdFollower;
	
	//parametri Campionamento
	private int delay; //indica la frequenza di campionamento in ms
	private int time; //tempo durata del gioco
	
	private int resolution;
	
	//parametro di rete
	private int numPlayers;

	//parametri delle leggi adattative
	private float aee;
	private float bee;

	//parametri forza elastica
	private float xA, xB, kE;
	
	//parametri controllo ottimo
	private float eta;
	private float thetaS, thetaP;
	
	private String IPaddress;
	
	private int[] socialMemory;
	
	// reinf learning Q-table
	private ArrayList<ArrayList<Float>> Q;
	
	// singleton
	private static Settings settings = null;
	private int ID;
	

	private Settings(){
		
	}

	
	public static Settings getSettings(){
		
		if(settings == null){
			settings = new Settings();
			settings.resolution = 800; 
			settings.time = 30;
			settings.delay = 5; // frequency 10 Hz
			settings.uMax = 5f; //era 5
			settings.xA = -0.5f; //era 0.5
			settings.xB = 0.5f;
			settings.kE = 5*15f;
			settings.controlVP = TypeControl.ADAPTIVE;
			settings.dynamicVP = TypeDynamic.HKB;
			settings.roleVP = TypeVP.FOLLOWER;	
			settings.input = TypeInput.MOUSE;
			settings.numPlayers = 0;
			settings.eta = 0.001f;
			//vincolo: thetaS + thetaP = 1
			settings.thetaP = 0.5f;
			settings.thetaS = 0.5f;
			settings.IPaddress = "localhost";
			settings.player=new Player();
			settings.ID=0;
		}
		
		return settings;
	}
	
	
	public String getIPaddress() {
		return IPaddress;
	}

	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}

	public float getKpLeader() {
		return KpLeader;
	}
	
	public float getKpFollower() {
		return KpFollower;
	}
	
	public float getKdFollower() {
		return KdFollower;
	}
	
	public float getKdLeader() {
		return KdLeader;
	}
	
	public void setKpFollower(float kp) {
		KpFollower = kp;
	}
	public void setID(int i){this.ID=i;}
	public int getID(){return this.ID;}
	
	public void setKpLeader(float kp) {
		KpLeader = kp;
	}
	public void setPlayer(Player p){player=p;}
	public Player getPlayer(){return player;}

	public void setKdLeader(float kd) {
		KdLeader = kd;
	}
	
	public void setKdFollower(float kd) {
		KdFollower = kd;
	}

	public float getDelta() {
		return delta;
	}

	public void setDelta(float delta) {
		this.delta = delta;
	}

	public float getCh() {
		return ch;
	}

	public void setCh(float ch) {
		this.ch = ch;
	}

	public ArrayList<Float> getPositionLeader() {
		return positionLeader;
	}

	public void setPositionLeader(ArrayList<Float> positionLeader) {
		this.positionLeader = positionLeader;
	}

	public ArrayList<Float> getVelocityLeader() {
		return velocityLeader;
	}

	public void setVelocityLeader(ArrayList<Float> velocityLeader) {
		this.velocityLeader = velocityLeader;
	}

	public TypeInput getInputChoice() {
		return input;
	}

	public void setInputChoice(TypeInput input) {
		this.input = input;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public TypeVP getRoleVP() {
		return roleVP;
	}

	public void setRoleVP(TypeVP roleVP) {
		this.roleVP = roleVP;
	}

	public TypeControl getControlVP() {
		return controlVP;
	}

	public void setControlVP(TypeControl controlVP) {
		this.controlVP = controlVP;
	}

	public TypeDynamic getDynamicVP() {
		return dynamicVP;
	}

	public void setDynamicVP(TypeDynamic dynamicVP) {
		this.dynamicVP = dynamicVP;
	}

	public float getxA() {
		return xA;
	}

	public float getxB() {
		return xB;
	}

	public float getkE() {
		return kE;
	}

	public float getuMax() {
		return uMax;
	}

	public float getAee() {
		return aee;
	}

	public void setAee(float aee) {
		this.aee = aee;
	}

	public float getBee() {
		return bee;
	}

	public void setBee(float bee) {
		this.bee = bee;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getBeta() {
		return beta;
	}

	public void setBeta(float beta) {
		this.beta = beta;
	}

	public float getGamma() {
		return gamma;
	}

	public void setGamma(float gamma) {
		this.gamma = gamma;
	}

	public float getOmega() {
		return omega;
	}

	public void setOmega(float omega) {
		this.omega = omega;
	}

	public float getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(float eps) {
		this.epsilon = eps;
	}

	public float getCp() {
		return cp;
	}

	public void setCp(float cp) {
		this.cp = cp;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getResolution() {
		return resolution;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public int getNumPlayers(){
		return this.numPlayers;
	}

	public float getEta() {
		return eta;
	}

	public void setEta(float eta) {
		this.eta = eta;
	}
	
	public void setThetaS(float thetaS){
		this.thetaS = thetaS;
	}
	
	public float getThetaS(){
		return this.thetaS;
	}
	
	public void setThetaP(float thetaP){
		this.thetaP = thetaP;
	}
	
	public float getThetaP(){
		return this.thetaP;
	}

	public int[] getSocialMemory() {
		return socialMemory;
	}

	public void setSocialMemory(int[] socialMemory) {
		int numFrames = socialMemory.length;
		this.socialMemory = new int[numFrames];
		for(int i = 0; i < numFrames; i++){
			this.socialMemory[i] = socialMemory[i];
		}
	}
	
	public void setQ(ArrayList<ArrayList<Float>> Q) {
		this.Q = new ArrayList<ArrayList<Float>>();
		for(int i = 0; i < Q.size(); i++) {
			this.Q.add(Q.get(i));
		}
	}
	
	public ArrayList<ArrayList<Float>> getQ() {
		return this.Q;
	}
}
