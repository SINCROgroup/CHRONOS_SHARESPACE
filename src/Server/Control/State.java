
package Server.Control;

import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;
import Server.Entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class State {
	private TypeTest test;
	private String nameTest;
	private String nameGroup;
	private int maxPlayers = 7;
	private float[] positionPlayers;
	private ArrayList<ArrayList<Float>> positionsToPlot;
	private ArrayList<ArrayList<Integer>> randomSignatures;
	private static State state = null;
	private String[] Answer = new String[maxPlayers];
	
	//min num for a trial (dyadic or network)
	public int NumberPlayer = 1;
	private int[] connections;
	private int time=10;
	private int [][] network;	
	private ArrayList<Object> parameterDyadic;
	private int[] socialMemorySequence;
	private TypeInput input;
	private boolean[] IsConnected;
	private boolean isStart;
	private boolean isSet;
	private boolean isGroup;
	private boolean isQuestion;
	private String[] NickName;
	private Player[] players;
	private boolean[] L2_index;
	private boolean[]L3_a_index;
	private boolean[] L3_r_index;
	private ArrayList<Float>[] pos;
	private int currentT= 0;
	
	/**
	 * Instantiates a new state.
	 */
	private State(){
		this.positionPlayers = new float[this.maxPlayers*2+1];
		for(int i = 0; i < this.maxPlayers*2+1; i++)
			this.positionPlayers[i]= 0;
		
		this.connections = new int[this.maxPlayers*2];
		this.IsConnected=new boolean[this.maxPlayers*2];
		this.isQuestion=false;
		for(int y = 0; y < this.maxPlayers; y++) {
			IsConnected[y] = false;
			IsConnected[y+this.maxPlayers] = false;
			connections[y] = 0;
			connections[y+this.maxPlayers] = 0;
		}
		this.pos=new ArrayList[this.maxPlayers*2+1];

		this.nameTest="";
		this.nameGroup="";
		this.positionsToPlot = new ArrayList<ArrayList<Float>>(this.maxPlayers*2);
		for(int i = 0; i < this.maxPlayers*2; i++)
			this.positionsToPlot.add(new ArrayList<Float>());

		this.randomSignatures = new ArrayList<ArrayList<Integer>>(this.maxPlayers);
		for(int i = 0; i < this.maxPlayers; i++)
			this.randomSignatures.add(new ArrayList<Integer>());
		
		this.parameterDyadic = new ArrayList<Object>();
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
		this.parameterDyadic.add(0);
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add("");
		this.parameterDyadic.add(new ArrayList<Float>());
		this.parameterDyadic.add(new ArrayList<Float>());
		this.parameterDyadic.add(new ArrayList<Float>());
		this.input=TypeInput.MOUSE;
		this.test=TypeTest.EMPTY;
		this.isStart=false;
		this.isSet=false;
		this.isGroup=false;
		this.L2_index=new boolean[this.maxPlayers];
		this.L3_a_index=new boolean[this.maxPlayers];
		this.L3_r_index=new boolean[this.maxPlayers];
		this.NickName=new String[this.maxPlayers];
		this.network=new int[this.maxPlayers][this.maxPlayers];
		for(int i = 0; i < this.maxPlayers; i++)
			this.NickName[i]=null;
		this.players=new Player[this.maxPlayers];

	}
	

	public static State getState(){
		
		if(state == null){
			state = new State();		
		}
		return state;
	}
	
	
	public ArrayList<Object> getParameterDyadic() {
		return parameterDyadic;
	}
	public  TypeTest getTest(){
		return this.test;
	}
	public void setTest(TypeTest t){
		this.test=t;
	}
	public  boolean getIsConnected(int index){
		return IsConnected[index];
	}
	public void setIsConnected(int index,boolean value){
		this.IsConnected[index]=value;
	}
    public void setPlayer(Player p,int i){
		this.players[i-1]=p;
		this.setNickName(i-1,p.getNick());
	}
	public Player[] getPlayer(){
		return this.players;
	}
	public void setNickName(int index, String name){
		this.NickName[index]=name;
	}
	public int setNickName(String name){
		for (int i=0; i<this.maxPlayers;i++){
			if (Objects.equals(this.NickName[i], name)){
				if((this.L2_index[i]||this.L3_r_index[i])&&!this.L3_a_index[i]) {
					return i + 1+this.maxPlayers;
				}else{
					return i + 1;
				}
			}
		}
		for (int i=0; i<this.maxPlayers;i++){
			if (Objects.equals(this.NickName[i], null)) {
				this.NickName[i]=name;
				if((this.L2_index[i]||this.L3_r_index[i])&&!this.L3_a_index[i]) {
					return i + 1+this.maxPlayers;
				}else{
					return i + 1;
				}
			}
		}
		return -1;
	}
	public void SetL2Index(int index,boolean v){
		this.L2_index[index-1]=v;
	}
	public void SetL3_aIndex(int index,boolean v){
		this.L3_a_index[index-1]=v;
	}
	public void SetL3_rIndex(int index,boolean v){
		this.L3_r_index[index-1]=v;
	}
	public boolean[] GetL2Index(){
		return this.L2_index;
	}
	public boolean[] GetL3_aIndex(){
		return this.L3_a_index;
	}
	public boolean[] GetL3_rIndex(){
		return this.L3_r_index;
	}
	public void resetLIndex(){
		for(int i=0;i< this.maxPlayers;i++){
			this.L2_index[i]=false;
			this.L3_a_index[i]=false;
			this.L3_r_index[i]=false;
		}
	}
	public  boolean getIsStart(){
		return this.isStart;
	}
	public void setIsStart(boolean value){
		this.isStart=value;
	}
	public  boolean getIsGroup(){
		return this.isGroup;
	}
	public void setIsGroup(boolean value){
		this.isGroup=value;
	}
	public  boolean getIsQuestion(){
		return this.isQuestion;
	}
	public void setIsQuestion(boolean value){
		this.isQuestion=value;
	}

	public  TypeInput getInput(){
		return this.input;
	}

	public void setIsSet(boolean value){
		this.isSet=value;
	}
	public void setAnswer(int index,String value){
		this.Answer[index]=value;
	}
	public String[] getAnswer(){
		return this.Answer;
	}
	public void resetAnswer(){
		this.Answer=new String[maxPlayers];
	}
	public  boolean getIsSet(){
		return this.isSet;
	}

	public void setInput(TypeInput input){
		this.input=input;
	}
	public void resetConnectionPlayer(){
		for(int i=0;i< this.maxPlayers; i++){
			this.setIsConnected(i,false);
		}
	}
	public void setParameterDyadic(ArrayList<Object> parameterDyadic) {
		this.parameterDyadic = parameterDyadic;
	}

	
	public int getMaxPlayer(){
		return this.maxPlayers;
	}

	public String getNameTest(){return this.nameTest;}
	public void setNameGroup(String name){this.nameGroup=name;}
	public String getNameGroup(){return this.nameGroup;}
	public void setNameTest(String name){this.nameTest=name;}
	public void setPositionsToPlot(int index, ArrayList<Float> pos){
		this.positionsToPlot.set(index, pos);
	}

	//SET ORDER HOW PLAYER index VISUALIZE OTHER PLAYERS
	public void setRandomSignaturePlayer(int index, ArrayList<Integer> randPos){
		this.randomSignatures.set(index, new ArrayList<Integer>(randPos));
	}


	public ArrayList<Float> getPositionsToPlot(int index){
		return this.positionsToPlot.get(index);
	}
	

	public float getPositionPlayer(int index){
		return this.positionPlayers[index];
	}
	

	public void setPositionPlayer(int index, float position){
		this.positionPlayers[index]= position;
		//pos[index].add(position);
	}


	//GET HOW EVERYONE SEES EVERYONE
	public ArrayList<Integer> getRandomSignatures(int index){
		return this.randomSignatures.get(index);
	}

	public int getNumberPlayer(){
		return this.NumberPlayer;
	}
	

	public void setNumberPlayer(int num){
		this.NumberPlayer = num;
	}


	public int[] getConnections(){
		return this.connections;
	}
	

	public void setConnections(int i, int value){
		this.connections[i] = value;
	}
	
	
	public int getTime(){
		return this.time;
	}
	

	public void setTime(int t){
		this.time = t;
	}
	

	public void setNetwork(int[][] net){
		this.network = new int[this.NumberPlayer][this.NumberPlayer];
		for(int i = 0; i < this.NumberPlayer; i++)
			for(int y = 0; y < this.NumberPlayer; y++)
				network[i][y] = net[i][y];
	}
	

	public int getNetworkElement(int i, int y){
		return this.network[i%State.getState().getMaxPlayer()][y%State.getState().getMaxPlayer()];
	}
	
	public int[] getSocialMemorySequence() {
		return socialMemorySequence;
	}

	
	public void setSocialMemorySequence(int[] socialMemorySequence) {
		int numFrames = socialMemorySequence.length;
		this.socialMemorySequence = new int[numFrames];
		for(int i = 0; i < numFrames; i++){
			this.socialMemorySequence[i] = socialMemorySequence[i];
		}
	}
}
