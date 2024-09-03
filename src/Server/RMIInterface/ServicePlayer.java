/*
 * 
 */
package Server.RMIInterface;

import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Interface ServicePlayer.
 */
public interface ServicePlayer extends Remote{
	int addplayer(int x, int y) throws RemoteException;
	int getMaxNumPlayers()throws RemoteException;
	/**
	 * Retrieve player list.
	 *
	 * @return the array list<? extends player>
	 * @throws RemoteException the remote exception
	 */
	ArrayList<? extends Player> retrievePlayerList() throws RemoteException;
	
	/**
	 * Retrieve random signature.
	 *
	 * @param player the player
	 * @return the signature player
	 * @throws RemoteException the remote exception
	 */
	SignaturePlayer retrieveRandomSignature(Player player) throws RemoteException;
	
	/**
	 * Change position2player.
	 *
	 * @param index the index
	 * @param position the position
	 * @return the float
	 * @throws RemoteException the remote exception
	 */
	float changePosition2player(int index, float position) throws RemoteException;
	
	/**
	 * Synchronization.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	boolean synchronization(int index) throws RemoteException;
	
	/**
	 * Reset.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	boolean reset(int index) throws RemoteException;
	
	/**
	 * Reset network.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	boolean resetNetwork(int index) throws RemoteException;
	float[] retrivePositions() throws RemoteException;
	void setPositions(int index,float p) throws  RemoteException;
	
	/**
	 * Retrieve network.
	 *
	 * @param index the index
	 * @return the int[]
	 * @throws RemoteException the remote exception
	 */
	int[] retrieveNetwork(int index) throws RemoteException;
	
	int[] retrieveSocialMemory() throws RemoteException;
	
	/**
	 * Retrieve position players.
	 *
	 * @param index the index
	 * @param position the position
	 * @return the array list
	 * @throws RemoteException the remote exception
	 */
	ArrayList<Float> retrievePositionPlayers(int index, float position) throws RemoteException;
	
	/**
	 * Send positions.
	 *
	 * @param index the index
	 * @param pos the pos
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time, String typeTrial) throws RemoteException;
	TypeTest retriveTest() throws RemoteException;
	void setTest(TypeTest t) throws RemoteException;
	String retriveGroup() throws RemoteException;
	void setGroup(String tg) throws RemoteException;
	boolean retriveConnection(int index) throws RemoteException;
	void setConnection(int index,boolean value) throws RemoteException;
	int  setNickName(String name) throws RemoteException;
	boolean retriveIsGroup() throws RemoteException;
	void resetConnections()throws RemoteException;
	void setIsStart(boolean v) throws RemoteException;
	boolean getIsStart() throws RemoteException;
	void setAnswer(int index,String p) throws  RemoteException;
	String[] getAnswer() throws RemoteException;
	void setIsQuestion(boolean v) throws RemoteException;
	boolean getIsQuestion() throws RemoteException;
	void setIsSet(boolean v) throws RemoteException;
	boolean getIsSet() throws RemoteException;
	void setInput(TypeInput t) throws RemoteException;
	void setPlayer(Server.Entity.Player p, int index) throws RemoteException;
	Server.Entity.Player[] getPlayer()throws RemoteException;
	TypeInput retriveInput() throws RemoteException;
	/**
	 * Send positions.
	 *
	 * @param index the index
	 * @param randPos the random signature
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	boolean sendRandomSignature(int index, ArrayList<Integer> randPos) throws RemoteException;

	ArrayList<Object> retrieveDyadic() throws RemoteException;

	boolean resetDyadic() throws RemoteException;
}
