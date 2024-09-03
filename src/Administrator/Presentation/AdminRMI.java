package Administrator.Presentation;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;

import Administrator.Algorithm.Setting_general;
import Administrator.Algorithm.Settings;
import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;
import Server.Control.State;
import Server.RMIInterface.Player;
import Server.RMIInterface.ServiceAdmin;
import Server.RMIInterface.SignaturePlayer;

/**
 * @version 
 * <ul>
 * <li> 1.0: March 9, 2016 created by Maria Lombardi </li>
 * </ul>
 * <hr>
 * @author Maria Lombardi
 * <p>
 * <br>
 * The Class AdminRMI.
 * <p>
 * The client of administrator can do requests to the Server through the framework Java RMI for the invocation
 * of remote methods, that implements the connector call/return. This class is the stub that the client uses
 * for its requests. The stub has the same interface of the remote object implemented in the Server, and has
 * the task to hide the details of the remote object doing marshalling and unmarshalling of the requests
 * (client side).
 * <br>
 */
public class AdminRMI {
	
	private static int PORT = 1024;
	private Registry registry;
	private ServiceAdmin stub;
	
	/**
	 * Instantiates a new RMI stub for the administrator side.
	 * <p>
	 * This function looks for the administrator services in the Registry. The registry is
	 * a naming service used to publish (Server side) and request (Client side) some services.
	 * @throws Exception the exception
	 */
	public AdminRMI () throws Exception {
		// "localhost"
		this.registry = LocateRegistry.getRegistry(Setting_general.getSettings().getIPaddress(), PORT);
		this.stub = (ServiceAdmin)registry.lookup("ServiceAdmin");
		String ipAddress = Setting_general.getSettings().getIPaddress();
		System.out.println("IP Address: " + ipAddress);
	}
	
	/**
	 * Retrieve list of the players.
	 *
	 * @return the whole list of the players saved in the database and managed by the Server.
	 * The notation "array list<? extends player>" is needed to hide the details of variable type implemented
	 * by the Server.
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<? extends Player> retrievePlayerList() throws RemoteException{
		return this.stub.retrievePlayerList();
	}
	public int add(int x, int y) throws RemoteException {
		return this.stub.add(x,y);
	}
	public void setL2Index (int index,boolean v)throws RemoteException{
		this.stub.setL2Index(index,v);
	}
	public float[] retrivePositions() throws RemoteException{
		return this.stub.retrivePositions();
	}
	public void resetPositions() throws RemoteException{
		this.stub.resetPositions();
	}
	public void setPositions(int index,float p) throws  RemoteException{
		this.stub.setPositions(index,p);
	}
	public void setL3_aIndex (int index,boolean v)throws RemoteException{
		this.stub.setL3_aIndex(index,v);
	}
	public void setL3_rIndex (int index,boolean v)throws RemoteException{
		this.stub.setL3_rIndex(index,v);
	}
	public boolean[] getL2Index()throws RemoteException{
		return this.stub.getL2Index();
	}
	public boolean[] getL3_aIndex()throws RemoteException{
		return this.stub.getL3_aIndex();
	}
	public boolean[] getL3_rIndex()throws RemoteException {
		return this.stub.getL3_rIndex();
	}
	public void resetLIndex() throws RemoteException{
		this.stub.resetLIndex();
	}
	/**
	 * Add the new signature to database.
	 *
	 * @param name name of the player
	 * @param surname surname of the player
	 * @param dateBirth date of birth of the players
	 * @param dateRecord date when the signature is recorded
	 * @param position array of values of saved positions
	 * @param velocity array of values of estimated velocity
	 * @return true, if successful (new signature is saved in database correctly)
	 * @throws RemoteException the remote exception
	 */
	public boolean addNewSignature(String name, String surname, Calendar dateBirth, 
			Calendar dateRecord, ArrayList<Float> position, ArrayList<Float> velocity) throws RemoteException{
		return this.stub.addNewSignature(name, surname, dateBirth, dateRecord, position, velocity);
	}
	
	/**
	 * Retrieve random signature of specific player. A player can have one or more signatures saved in database,
	 * this function retrieves one in random way.
	 *
	 * @param p player of which you want a random signature.
	 * @return the signature of the specified player
	 * @throws RemoteException the remote exception
	 */
	public SignaturePlayer retrieveRandomSignature(Player p) throws RemoteException{
		return this.stub.retrieveRandomSignature(p);
	}
	public TypeTest retriveTest() throws RemoteException{
		return this.stub.retriveTest();
	}

	public void setIsStart(boolean v) throws RemoteException{
		this.stub.setIsStart(v);
	}
	public boolean getIsStart() throws RemoteException{
		return this.stub.getIsStart();
	}
	public void setAnswer(int index,String p) throws  RemoteException{
		this.stub.setAnswer(index,p);
	}
	public void resetAnswer() throws RemoteException{
		this.stub.resetAnswer();
	}
	public String[] getAnswer() throws RemoteException{
		return this.stub.getAnswer();
	}
	public void setIsQuestion(boolean v) throws RemoteException{
		this.stub.setIsQuestion(v);
	}
	public boolean getIsQuestion() throws RemoteException{
		return this.stub.getIsQuestion();
	}
	public void setGroup(String name) throws RemoteException{
		this.stub.setGroup(name);
	}
	public String retriveGroup() throws RemoteException{
		return this.stub.retriveGroup();
	}
	public void setIsGroup(boolean v) throws RemoteException{
		this.stub.setIsGroup(v);
	}
	public void setIsSet(boolean v) throws RemoteException{
		this.stub.setIsSet(v);
	}
	public boolean getIsSet() throws RemoteException{
		return this.stub.getIsSet();
	}
	public boolean getIsGroup() throws RemoteException{
		return this.stub.getIsGroup();
	}
	public void setNameTest(String name) throws RemoteException{
		this.stub.setNameTest(name);
	}
	public String getNameTest() throws RemoteException{
		return this.stub.getNameTest();
	}
	public void setPlayer(Server.Entity.Player p, int index) throws RemoteException{
		this.stub.setPlayer(p,index);
	}
	public Server.Entity.Player[] getPlayer()throws RemoteException{
		return this.stub.getPlayer();
	}
	public void setTest(TypeTest t) throws RemoteException{
		this.stub.setTest(t);
	}
	public void setInput(TypeInput t) throws RemoteException{
		this.stub.setInput(t);
	}
	public TypeInput retriveInput() throws RemoteException{
		return this.stub.retriveInput();
	}
	
	/**
	 * Setting network.
	 *
	 * @param time duration of trial (time in seconds)
	 * @param number number of players
	 * @param net matrix of adjacent, the location (i,j) = 1 if the link between player i and player j exists
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean settingNetwork(int time, int number, int numVPs, int[] net) throws RemoteException{
		return this.stub.settingNetwork(time, number, numVPs, net);
	}
	
	public boolean settingDyadic(ArrayList<Object> paramDyadic) throws RemoteException{
		return this.stub.settingDyadic(paramDyadic);
	}
	
	/**
	 * Retrieve plot of player.
	 *
	 * @param index identifier of the player (number)
	 * @return the array of the saved positions of the specified player in input
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<Float> retrievePlotPlayer(int index) throws RemoteException{
		return this.stub.retrievePlotPlayer(index);
	}
	
	/**
	 * Retrieve network.
	 *
	 * @param index identifier of the player (number)
	 * @return the number of players by which the player in input is linked
	 * @throws RemoteException the remote exception
	 */
	public int retrieveNetwork(int index) throws RemoteException{
		return this.stub.retrieveNetwork(index);
	}

	/**
	 * Synchronization.
	 *
	 * @param index array of the identifier of the virtual players (number)
	 * @return true, if successful; all players are connected
	 * @throws RemoteException the remote exception
	 */
	public boolean synchronization(int[] index) throws RemoteException{
		return this.stub.synchronization(index);
	}
	
	/**
	 * Retrieve position players. Each player send own position and retrieve the positions of the others 
	 * players to which they are linked.
	 *
	 * @param index identifier of the virtual players (number)
	 * @param pos position of the player with identifier equal to index
	 * @return the array list
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<Float> retrievePositionPlayers(int index, float pos) throws RemoteException{
		return this.stub.retrievePositionPlayers(index, pos);
	}
	
	/**
	 * Send positions. At the end of the trial, each player send the array of own positions to the server
	 *
	 * @param index identifier of the virtual players (number)
	 * @param pos array of positions saved during the trial
	 * @return true, if successful
	 */
	public boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time) throws RemoteException{
		return this.stub.sendPositions(index, pos, time);
	}
	
	public boolean sendPositionsSolo(ArrayList<Float> vel, ArrayList<Float> pos, ArrayList<Integer> time, String nameFile) throws RemoteException{
		return this.stub.sendPositionsSolo(vel, pos, time, nameFile);
	}

	/*public boolean storeRandomSignature() throws RemoteException{
		return this.stub.storeRandomSignature();
	}*/
	
	/**
	 * Reset the network. At the end of the trial, the game must leave the server in a consistent state.
	 * All connections are set to zero.
	 *
	 * @param index identifier of the virtual players (number)
	 * @return true, if successful
	 */
	public boolean reset(int index) throws RemoteException{
		return this.stub.reset(index);
	}

	/**
	 * Set the social memory option on the server.
	 * 
	 * @param sequence of all the time frames chosen by the user, each element of the sequence is the
	 * number of seconds for the slot (example of sequence: EC - EO- EC - EO - etc.)
	 * @return true, if successful
	 */
	public boolean settingSocialMemory(int[] sequence) throws RemoteException{
		return this.stub.settingSocialMemory(sequence);
	}
}
