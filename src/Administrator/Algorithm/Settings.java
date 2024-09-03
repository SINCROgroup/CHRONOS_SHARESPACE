package Administrator.Algorithm;

import Administrator.Algorithm.Settings;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;


@SuppressWarnings("unused")
public class Settings implements Serializable {

	//parametri del HKB
	private float alpha[];
	private float beta[];
	private float gamma[];
	private float omega[];
	
	//parametri lineare
	private float a[];
	private float b[];
	
	//parametri del controllo adattativo
	private float epsilon[];
	private float cp[];
	private float uMax = 5f;
	private float delta[];
	private float ch[];
		
	//parametri del PD
	private float KpLeader[];
	private float KdLeader[];
	
	private float KpFollower[];
	private float KdFollower[];
	
	//parametri delle leggi adattative
	private float aee = -10f;
	private float bee = -10f;
	
	//parametri forza elastica
	private float xA = -0.5f;
	private float xB = 0.5f;
	private float kE = 5*15f;
	
	//parametri Kuramoto
	private float coupling[];
	private float frequency[];
	

	private int count = 0;
	private int maxNum = 7;
	private int numVP;
	private int numL2;
	private int numL3_a;
	private int numL3_r;
	private int numPlayers;

	private Boolean ask;

	private int resolution;
	private int time;
	private int delay;	

	private int[] net;
	private int num_trial=0;
	private TypeInput input;	
	private TypeControl controlVP[];
	private TypeDynamic dynamicVP[];
	private TypeVP typeVP[];
	private TypeTest typeTest;
	private float parameter[][];
	
	private ArrayList<ArrayList<Float>> positionsVP;
	private ArrayList<ArrayList<Float>> velocityVP;	
	
	private String IPaddress;
	
	// singleton
	private static Settings settings = null;
	
	private ArrayList<Object> parameterDyadic;
	private int[] valuesFrames;
	
	private String L2_algorithm[];
	private String L2_inner_d[];
	private String L3_a_algorithm[];

	private String L3_a_inner_d[];
	private String L3_s_algorithm[];

	private String L3_s_inner_d[];
	private String L2_role[];
	private String L3_a_role[];
	private String L3_r_role[];
	private String groupName;
	private Settings(){		
	}
	

	public static Settings getSettings(){
		
		if(settings == null){
			settings = new Settings();	
			//initializeParamVP();		
			settings.time = 30;
			settings.num_trial=0;
			settings.numL2=0;
			settings.numL3_a=0;
			settings.numL3_r=0;
			settings.L2_inner_d=new String[settings.maxNum +1];
			settings.L3_a_inner_d=new String[settings.maxNum +1];
			settings.L3_s_inner_d=new String[settings.maxNum +1];
			settings.L2_algorithm=new String[settings.maxNum +1];
			settings.L3_a_algorithm=new String[settings.maxNum +1];
			settings.L3_s_algorithm=new String[settings.maxNum +1];
			settings.L2_role=new String[settings.maxNum +1];
			settings.L3_a_role=new String[settings.maxNum +1];
			settings.L3_r_role=new String[settings.maxNum +1];
			settings.parameter=new float[settings.maxNum +1][settings.maxNum +1];
			settings.ask=false;
			settings.net=new int[settings.maxNum*settings.maxNum];
	        settings.alpha = new float[settings.maxNum];
	        settings.beta = new float[settings.maxNum];
	        settings.gamma = new float[settings.maxNum];
	        settings.omega = new float[settings.maxNum];
	        settings.a = new float[settings.maxNum];
	        settings.b = new float[settings.maxNum];
	        settings.KdFollower = new float[settings.maxNum];
	        settings.KdLeader = new float[settings.maxNum];
	        settings.KpFollower = new float[settings.maxNum];
	        settings.KpLeader = new float[settings.maxNum];
	        settings.delta = new float[settings.maxNum];
	        settings.epsilon = new float[settings.maxNum];
	        settings.ch = new float[settings.maxNum];
	        settings.cp = new float[settings.maxNum];
	        settings.coupling = new float[settings.maxNum];
	        settings.frequency = new float[settings.maxNum];
			settings.valuesFrames=new int[7];
			settings.groupName="";
	        
	        for(int i = 0; i < settings.maxNum; i++){
	        	//settings.alpha[i] = 3;
	        	settings.alpha[i] = 1.25f;
	        	settings.beta[i] = 7;
	        	settings.gamma[i] = 0.2f;
	        	settings.omega[i] = 1.75f;
	        	settings.a[i] = 1.5f;        
		        settings.b[i] = 3;        
		        settings.KdLeader[i] = 10;        
		        settings.KpLeader[i] = 0.2f;        
		        settings.KdFollower[i] = 0.2f;        
		        settings.KpFollower[i] = 15;
		        settings.delta[i] = 0.5f;        
		        settings.epsilon[i] = 0.5f;        
		        settings.ch[i] = 40;        
		        settings.cp[i] = 60; 
		        settings.coupling[i] = 1.25f;
		        settings.frequency[i] = 0f;
	        }                  
			
			settings.resolution = 800; 
			settings.delay = 5; //(delay in ms) frequency 10 Hz
			//settings.delay = 50; //(delay in ms) frequency 10 Hz
			settings.input = TypeInput.MOUSE;
			settings.numVP = 0;
			settings.numPlayers = 4;
			settings.velocityVP = new ArrayList<ArrayList<Float>>();
			settings.positionsVP = new ArrayList<ArrayList<Float>>();
			settings.parameterDyadic = new ArrayList<Object>();
			settings.dynamicVP = new TypeDynamic[settings.maxNum];
			settings.controlVP = new TypeControl[settings.maxNum];
			settings.typeVP = new TypeVP[settings.maxNum];
			settings.typeTest= TypeTest.EMPTY;
			settings.IPaddress = "localhost";
			ArrayList<Float> temp = new ArrayList<Float>();
			temp.add(0f);
			// tutte le posizioni e le velocità sono settate con arraylist di un solo valore pari a zero
			// in questo modo posso fare la set degli arraylist nel seguito
			for(int i = 0; i < settings.maxNum; i++){
				settings.velocityVP.add(temp);
				settings.positionsVP.add(temp);
			}
			for(int i = 0; i < settings.maxNum; i++){
				settings.dynamicVP[i] = TypeDynamic.EMPTY;
				settings.controlVP[i] = TypeControl.EMPTY; 
				settings.typeVP[i] = TypeVP.EMPTY;
			}
			
			//parameter in dyadic interaction
			// 0 position: time
			// 1 position: type_trial
			// 2 position: role player 1
			// 3 position: role player 2
			// 4 position: dynamics
			// 5 position: control
			// 6 position: position vp
			// 7 position: velocity vp
			// 8 position: parameter vp
			settings.parameterDyadic.add(30);
			settings.parameterDyadic.add("");
			settings.parameterDyadic.add("");
			settings.parameterDyadic.add("");
			settings.parameterDyadic.add("");
			settings.parameterDyadic.add("");
			settings.parameterDyadic.add(new ArrayList<Float>());
			settings.parameterDyadic.add(new ArrayList<Float>());
			settings.parameterDyadic.add(new ArrayList<Float>());
		}		
		return settings;
	}
	
	public void reset(){
		this.time = 30;
		this.num_trial=0;
		this.numL2=0;
		this.numL3_a=0;
		this.numL3_r=0;
		this.L2_inner_d=new String[settings.maxNum +1];
		this.L3_a_inner_d=new String[settings.maxNum +1];
		this.L3_s_inner_d=new String[settings.maxNum +1];
		this.L2_algorithm=new String[settings.maxNum +1];
		this.L3_a_algorithm=new String[settings.maxNum +1];
		this.L3_s_algorithm=new String[settings.maxNum +1];
		this.L2_role=new String[settings.maxNum +1];
		this.L3_a_role=new String[settings.maxNum +1];
		this.L3_r_role=new String[settings.maxNum +1];
		this.parameter=new float[settings.maxNum +1][settings.maxNum +1];
		this.ask=false;
		this.net=new int[settings.maxNum*settings.maxNum];
		this.alpha = new float[settings.maxNum];
		this.beta = new float[settings.maxNum];
		this.gamma = new float[settings.maxNum];
		this.omega = new float[settings.maxNum];
		this.a = new float[settings.maxNum];
		this.b = new float[settings.maxNum];
		this.KdFollower = new float[settings.maxNum];
		this.KdLeader = new float[settings.maxNum];
		this.KpFollower = new float[settings.maxNum];
		this.KpLeader = new float[settings.maxNum];
		this.delta = new float[settings.maxNum];
		this.epsilon = new float[settings.maxNum];
		this.ch = new float[settings.maxNum];
		this.cp = new float[settings.maxNum];
		this.coupling = new float[settings.maxNum];
		this.frequency = new float[settings.maxNum];
		this.valuesFrames=new int[7];
		this.groupName="";

		for(int i = 0; i < settings.maxNum; i++){
			//settings.alpha[i] = 3;
			this.alpha[i] = 1.25f;
			this.beta[i] = 7;
			this.gamma[i] = 0.2f;
			this.omega[i] = 1.75f;
			this.a[i] = 1.5f;
			this.b[i] = 3;
			this.KdLeader[i] = 10;
			this.KpLeader[i] = 0.2f;
			this.KdFollower[i] = 0.2f;
			this.KpFollower[i] = 15;
			this.delta[i] = 0.5f;
			this.epsilon[i] = 0.5f;
			this.ch[i] = 40;
			this.cp[i] = 60;
			this.coupling[i] = 1.25f;
			this.frequency[i] = 0f;
		}

		this.resolution = 800;
		this.delay = 5; //(delay in ms) frequency 10 Hz
		//settings.delay = 50; //(delay in ms) frequency 10 Hz
		this.input = TypeInput.MOUSE;
		this.numVP = 0;
		this.numPlayers = 4;
		this.velocityVP = new ArrayList<ArrayList<Float>>();
		this.positionsVP = new ArrayList<ArrayList<Float>>();
		this.parameterDyadic = new ArrayList<Object>();
		this.dynamicVP = new TypeDynamic[settings.maxNum];
		this.controlVP = new TypeControl[settings.maxNum];
		this.typeVP = new TypeVP[settings.maxNum];
		this.typeTest= TypeTest.EMPTY;
		this.IPaddress = "localhost";
		ArrayList<Float> temp = new ArrayList<Float>();
		temp.add(0f);
		// tutte le posizioni e le velocità sono settate con arraylist di un solo valore pari a zero
		// in questo modo posso fare la set degli arraylist nel seguito
		for(int i = 0; i < this.maxNum; i++){
			this.velocityVP.add(temp);
			this.positionsVP.add(temp);
		}
		for(int i = 0; i < this.maxNum; i++){
			this.dynamicVP[i] = TypeDynamic.EMPTY;
			this.controlVP[i] = TypeControl.EMPTY;
			this.typeVP[i] = TypeVP.EMPTY;
		}

		//parameter in dyadic interaction
		// 0 position: time
		// 1 position: type_trial
		// 2 position: role player 1
		// 3 position: role player 2
		// 4 position: dynamics
		// 5 position: control
		// 6 position: position vp
		// 7 position: velocity vp
		// 8 position: parameter vp
		this.parameterDyadic.add(30);
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add(new ArrayList<Float>());
		this.parameterDyadic.add(new ArrayList<Float>());
		this.parameterDyadic.add(new ArrayList<Float>());

	}
	public ArrayList<Object> getParameterDyadic() {
		return parameterDyadic;
	}
	public void setSocialMemory(int[] valuesFrames){this.valuesFrames=valuesFrames;}
	public int[] getSocialMemory(){return valuesFrames;}

	public void setParameterDyadic(ArrayList<Object> parameterDyadic) {
		this.parameterDyadic = parameterDyadic;
	}
	public void setL2_role(String role,int index){this.L2_role[index]=role;}
	public String getL2_role(int index){return L2_role[index];}
	public void setL3_a_role(String role,int index){this.L3_a_role[index]=role;}
	public String getL3_a_role(int index){return L3_a_role[index];}
	public void setL3_r_role(String role,int index){this.L3_r_role[index]=role;}
	public String getL3_r_role(int index){return L3_r_role[index];}
	public void setParameterDyadicByIndex(int index, Object obj){
		this.parameterDyadic.set(index, obj);
	}
	
	public Object getParameterDyadicByIndex(int index){
		return this.parameterDyadic.get(index);
	}
	public void setParameter(int indexagent,int indexp,float parameter){this.parameter[indexagent][indexp]=parameter;}
	public float[] getParameter(int indexagent){return parameter[indexagent];}
	public String getIPaddress() {
		return IPaddress;
	}

	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	public int getNumTrial() {return num_trial;}
	public void setNumTrial(int numTrial) {
		this.num_trial = numTrial;
	}

	public int getNumL2() {return numL2;}
	public void setNumL2(int numL2) {
		this.numL2 = numL2;
	}

	public int getNumL3_a() {return numL3_a;}
	public void setNumL3_a(int numL3_a) {
		this.numL3_a = numL3_a;
	}
	public String getL2_algorithm(int index) {return L2_algorithm[index];}
	public void setL2_algorithm(String L2_alg, int index) {this.L2_algorithm[index] = L2_alg;}
	public String getL2_inner_d(int index) {return L2_inner_d[index];}
	public void setL2_inner_d(String L2_inner,int index) {
		this.L2_inner_d[index] = L2_inner;
	}

	public String getL3_a_algorithm(int index) {return L3_a_algorithm[index];}
	public void setL3_a_algorithm(String L3_a_alg,int index) {this.L3_a_algorithm[index] = L3_a_alg;}
	public String getL3_a_inner_d(int index) {return L3_a_inner_d[index];}
	public void setL3_a_inner_d(String L3_a_inner,int index) {
		this.L3_a_inner_d[index] = L3_a_inner;
	}

	public String getL3_s_algorithm(int index) {return L3_s_algorithm[index];}
	public void setL3_s_algorithm(String L3_s_alg,int index) {this.L3_s_algorithm[index] = L3_s_alg;}
	public String getL3_s_inner_d(int index) {return L3_s_inner_d[index];}
	public void setL3_s_inner_d(String L3_s_inner,int index) {
		this.L3_s_inner_d[index] = L3_s_inner;
	}

	public int getNumL3_r() {return numL3_r;}
	public void setNumL3_r(int numL3_r) {
		this.numL3_r = numL3_r;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int[] getNet() {
		return net;
	}

	public void setNet(int[] net) {
		this.net = net;
	}
	public Boolean getAsk() {
		return ask;
	}

	public void setAsk(Boolean ask) {
		this.ask = ask;
	}

	public int getMaxNum(){
		return this.maxNum;
	}
	
	public void setDynamicVP(int index, TypeDynamic inp){
		this.dynamicVP[index] = inp;
	}
	
	public TypeDynamic getDynamicVP(int index){
		return this.dynamicVP[index];
	}
	
	public void setTypeVP(int index, TypeVP inp){
		this.typeVP[index] = inp;
	}
	
	public TypeVP getTypeVP(int index){
		return this.typeVP[index];
	}

	public void setControlVP(int index, TypeControl inp){
		this.controlVP[index] = inp;
	}

	public TypeControl getControlVP(int index){
		return this.controlVP[index];
	}

	public void setPositionVP(int index, ArrayList<Float> pos){
		this.positionsVP.set(index, pos);
	}

	public ArrayList<Float> getPositionVP(int index){
		return this.positionsVP.get(index);
	}

	public void setVelocityVP(int index, ArrayList<Float> vel){
		this.velocityVP.set(index, vel);
	}

	public ArrayList<Float> getVelocityVP(int index){
		return this.velocityVP.get(index);
	}

	public int getNumVP() {
		return numVP;
	}

	public void setNumVP(int numVP) {
		this.numVP = numVP;
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public TypeInput getInput() {
		return input;
	}

	public void setInput(TypeInput input) {
		this.input = input;
	}

	public TypeTest getTest() {
		return typeTest;
	}

	public void setTest(TypeTest test) {this.typeTest = test;}

	public float getAlpha(int index) {
		return alpha[index];
	}
	
	public void setAlpha(int index, float value){
		this.alpha[index] = value;
	}
	

	public float getBeta(int index) {
		return beta[index];
	}
	
	public void setBeta(int index, float value){
		this.beta[index] = value;
	}

	public float getGamma(int index) {
		return gamma[index];
	}
	
	public void setGamma(int index, float value){
		this.gamma[index] = value;
	}

	public float getOmega(int index) {
		return omega[index];
	}
	
	public void setOmega(int index, float value){
		this.omega[index] = value;
	}

	public float getA(int index) {
		return a[index];
	}
	
	public void setA(int index, float value){
		this.a[index] = value;
	}

	public float getB(int index) {
		return b[index];
	}
	
	public void setB(int index, float value){
		this.b[index] = value;
	}

	public float getEpsilon(int index) {
		return epsilon[index];
	}
	
	public void setEpsilon(int index, float value){
		this.epsilon[index] = value;
	}

	public float getCp(int index) {
		return cp[index];
	}
	
	public void setCp(int index, float value){
		this.cp[index] = value;
	}

	public float getuMax() {
		return uMax;
	}

	public float getDelta(int index) {
		return delta[index];
	}
	
	public void setDelta(int index, float value){
		this.delta[index] = value;
	}

	public float getCh(int index) {
		return ch[index];
	}
	
	public void setCh(int index, float value){
		this.ch[index] = value;
	}

	public float getKpLeader(int index) {
		return KpLeader[index];
	}
	
	public void setKpLeader(int index, float value){
		this.KpLeader[index] = value;
	}

	public float getKdLeader(int index) {
		return KdLeader[index];
	}
	
	public void setKdLeader(int index, float value){
		this.KdLeader[index] = value;
	}
	
	public float getKpFollower(int index) {
		return KpFollower[index];
	}
	
	public void setKpFollower(int index, float value){
		this.KpFollower[index] = value;
	}
	
	public float getKdFollower(int index) {
		return KdFollower[index];
	}
	
	public void setKdFollower(int index, float value){
		this.KdFollower[index] = value;
	}

	public float getAee() {
		return aee;
	}
	public String getGroupName(){return this.groupName;}
	public void setGroupName(String n){this.groupName=n;}

	public float getBee() {
		return bee;
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
	
	public float getCoupling(int index) {
		return coupling[index];
	}
	
	public void setCoupling(int index, float value){
		this.coupling[index] = value;
	}
	
	public float getFrequency(int index) {
		return frequency[index];
	}
	
	public void setFrequency(int index, float value){
		this.frequency[index] = value;
	}
	
	public TypeControl[] getControlVP() {
		return controlVP;
	}

	public TypeDynamic[] getDynamicVP() {
		return dynamicVP;
	}

	private void writeToFile (ObjectOutputStream out) throws IOException {
		out.writeObject(this);
		out.close();
	}
	public void save(String percorsoCartella, String nomeFile) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(percorsoCartella + "\\"+nomeFile))) {
			settings.writeToFile(oos);
			System.out.println("Settings salvati con successo.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(String percorsoCartella, String nomeFile) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(percorsoCartella + "\\"+nomeFile))) {
			settings = (Settings) ois.readObject();
			System.out.println("Settings caricati con successo.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
/*	private static void initializeParamVP(){
		Properties properties = new Properties();
        File jarPath = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath = jarPath.getParentFile().getAbsolutePath();
        
        try {
			properties.load(new FileInputStream(propertiesPath+"/configVPnetwork.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

        settings.time = Integer.parseInt(properties.getProperty("time"));	      
        settings.alpha = Float.parseFloat(properties.getProperty("HKB.alpha"));      
        settings.beta = Float.parseFloat(properties.getProperty("HKB.beta"));        
        settings.gamma = Float.parseFloat(properties.getProperty("HKB.gamma"));        
        settings.omega = Float.parseFloat(properties.getProperty("HKB.omega"));        
        settings.a = Float.parseFloat(properties.getProperty("harmonic.a"));        
        settings.b = Float.parseFloat(properties.getProperty("harmonic.b"));        
        settings.KdLeader = Float.parseFloat(properties.getProperty("PD.KdLeader"));        
        settings.KpLeader = Float.parseFloat(properties.getProperty("PD.KpLeader"));        
        settings.KdFollower = Float.parseFloat(properties.getProperty("PD.KdFollower"));        
        settings.KpFollower = Float.parseFloat(properties.getProperty("PD.KpFollower"));        
        settings.delta = Float.parseFloat(properties.getProperty("adaptive.delta"));        
        settings.epsilon = Float.parseFloat(properties.getProperty("adaptive.delta"));        
        settings.ch = Float.parseFloat(properties.getProperty("adaptive.K"));        
        settings.cp = Float.parseFloat(properties.getProperty("adaptive.C"));       
	}*/
}
