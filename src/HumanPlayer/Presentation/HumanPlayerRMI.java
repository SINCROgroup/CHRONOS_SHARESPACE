/*
 * 
 */
package HumanPlayer.Presentation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;
import HumanPlayer.Algorithm.Settings;
import Server.Control.State;
import Server.RMIInterface.Player;
import Server.RMIInterface.ServicePlayer;
import Server.RMIInterface.SignaturePlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class HumanPlayerRMI.
 */
public class HumanPlayerRMI {
	
	/** The port. */
	private static int PORT = 1024;
	
	/** The registry. */
	private Registry registry;
	
	/** The stub. */
	private ServicePlayer stub;
	
	/**
	 * Instantiates a new human player rmi.
	 *
	 * @param playerNumber the player number
	 * @throws Exception the exception
	 */
	public HumanPlayerRMI (int playerNumber) throws Exception {

		this.registry = LocateRegistry.getRegistry(Settings.getSettings().getIPaddress(), PORT + playerNumber);
		this.stub = (ServicePlayer)registry.lookup("ServicePlayer");
		System.out.println("Server player "+Settings.getSettings().getIPaddress()+" connected");

	}
	public float[] retrivePositions() throws RemoteException{
		return this.stub.retrivePositions();
	}
	public void setPositions(int index,float p) throws  RemoteException{
		this.stub.setPositions(index,p);
	}
	public int getMaxNumPlayers()throws RemoteException{
		return this.stub.getMaxNumPlayers();
	}
	/**
	 * Retrieve player list.
	 *
	 * @return the array list<? extends player>
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<? extends Player> retrievePlayerList() throws RemoteException{
		return this.stub.retrievePlayerList();
	}
	
	/**
	 * Retrieve random signature.
	 *
	 * @param p the p
	 * @return the signature player
	 * @throws RemoteException the remote exception
	 */
	public SignaturePlayer retrieveRandomSignature(Player p) throws RemoteException{
		return this.stub.retrieveRandomSignature(p);
	}
	public void setIsStart(boolean v) throws RemoteException{
		this.stub.setIsStart(v);
	}
	public void setAnswer(int index,String p) throws  RemoteException{
		this.stub.setAnswer(index,p);
	}
	public String[] getAnswer() throws RemoteException{
		return this.stub.getAnswer();
	}
	public boolean getIsStart() throws RemoteException{
		return this.stub.getIsStart();
	}
	public void setIsQuestion(boolean v) throws RemoteException{
		this.stub.setIsQuestion(v);
	}
	public boolean getIsQuestion() throws RemoteException{
		return this.stub.getIsQuestion();
	}
	public boolean getIsGroup() throws RemoteException{
		return this.stub.retriveIsGroup();
	}
	public void setIsSet(boolean v) throws RemoteException{
		this.stub.setIsSet(v);
	}
	public int setNickName(String name) throws RemoteException{
		return this.stub.setNickName(name);
	}
	public void setGroup(String name) throws RemoteException{
		this.stub.setGroup(name);
	}
	public void setPlayer(Server.Entity.Player p, int index) throws RemoteException{
		this.stub.setPlayer(p,index);
	}
	public Server.Entity.Player[] getPlayer()throws RemoteException{
		return this.stub.getPlayer();
	}
	public String retriveGroup() throws RemoteException{
		return this.stub.retriveGroup();
	}
	public boolean getIsSet() throws RemoteException{
		return this.stub.getIsSet();
	}
	public void setInput(TypeInput t) throws RemoteException{
		this.stub.setInput(t);
	}
	public TypeInput retriveInput() throws RemoteException{
		return this.stub.retriveInput();
	}
	public int addplayer(int x, int y) throws RemoteException{
		return this.stub.addplayer(x,y);
	}
	public boolean retriveConnection(int index) throws RemoteException {
		return this.stub.retriveConnection(index);
	}
	public void setConnection(int index,boolean value) throws RemoteException{
		this.stub.setConnection(index,value);
	}
	public void resetConnections()throws RemoteException{
		this.stub.resetConnections();
	}
	public TypeTest retriveTest() throws RemoteException{
		return this.stub.retriveTest();
	}

	public void setTest(TypeTest t) throws RemoteException{
		this.stub.setTest(t);
	}
	/**
	 * Change position2player.
	 *
	 * @param index the index
	 * @param position the position
	 * @return the float
	 * @throws RemoteException the remote exception
	 */
	public float changePosition2player(int index, float position) throws RemoteException{
		return this.stub.changePosition2player(index, position);
	}
	
	/**
	 * Synchronization.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean synchronization(int index)throws RemoteException{
		return this.stub.synchronization(index);
	}
	
	/**
	 * Reset.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean reset(int index) throws RemoteException{
		return this.stub.reset(index);
	}
	
	/**
	 * Retrieve network.
	 *
	 * @param index the index
	 * @return the int[]
	 * @throws RemoteException the remote exception
	 */
	public int[] retrieveNetwork(int index) throws RemoteException{
		return this.stub.retrieveNetwork(index);
	}
	
	public int[] retrieveSocialMemory() throws RemoteException{
		return this.stub.retrieveSocialMemory();
	};
	
	/**
	 * Reset network.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean resetNetwork(int index) throws RemoteException{
		return this.stub.resetNetwork(index);
	}
	
	/**
	 * Retrieve position players.
	 *
	 * @param index the index
	 * @param pos the pos
	 * @return the array list
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<Float> retrievePositionPlayers(int index, float pos) throws RemoteException{
		return this.stub.retrievePositionPlayers(index, pos);
	}
	
	/**
	 * Send positions.
	 *
	 * @param index the index
	 * @param pos the pos
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time, String typeTrial) throws RemoteException{
		return this.stub.sendPositions(index, pos, time, typeTrial);
	}

	/*public boolean sendRandomSignature(int index, ArrayList<Integer> randPos) throws RemoteException {
		return this.stub.sendRandomSignature(index, randPos);
	}*/

	public ArrayList<Object> retrieveDyadic() throws RemoteException{
		return this.stub.retrieveDyadic();
	}
	
	public boolean resetDyadic() throws RemoteException{
		return this.stub.resetDyadic();
	}
}
