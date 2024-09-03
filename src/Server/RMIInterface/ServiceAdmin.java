
package Server.RMIInterface;

import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;

import java.rmi.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Interface for the Administrator Service.
 */
public interface ServiceAdmin extends Remote{
	void setL2Index (int index,boolean v)throws RemoteException;
	void setL3_aIndex (int index,boolean v)throws RemoteException;
	void setL3_rIndex (int index,boolean v)throws RemoteException;
	boolean[] getL2Index()throws RemoteException;
	boolean[] getL3_aIndex()throws RemoteException;
	boolean[] getL3_rIndex()throws RemoteException;
	void resetLIndex() throws RemoteException;
	float[] retrivePositions() throws RemoteException;
	void setPositions(int index,float p) throws  RemoteException;
	void resetPositions() throws  RemoteException;
	int add(int x, int y) throws RemoteException;
	void setAnswer(int index,String p) throws  RemoteException;
	String[] getAnswer() throws RemoteException;
	TypeTest retriveTest() throws RemoteException;
	void setTest(TypeTest t) throws RemoteException;
	void resetAnswer() throws RemoteException;
	String retriveGroup() throws RemoteException;
	void setGroup(String tg) throws RemoteException;
	void setInput(TypeInput g) throws RemoteException;
	TypeInput retriveInput() throws RemoteException;
	void setNameTest(String name) throws RemoteException;
	String getNameTest() throws RemoteException;
	void setIsStart(boolean v) throws RemoteException;
	boolean getIsStart() throws RemoteException;
	void setIsQuestion(boolean v) throws RemoteException;
	boolean getIsQuestion() throws RemoteException;
	void setIsSet(boolean v) throws RemoteException;
	void setIsGroup(boolean v) throws RemoteException;
	boolean getIsGroup() throws RemoteException;
	void setPlayer(Server.Entity.Player p, int index) throws RemoteException;
	Server.Entity.Player[] getPlayer()throws RemoteException;
	boolean getIsSet() throws RemoteException;

	boolean addNewSignature(String name, String surname, Calendar dateBirth, Calendar dateRecord, ArrayList<Float> position,
			ArrayList<Float> velocity) throws RemoteException;
	
	ArrayList<? extends Player> retrievePlayerList() throws RemoteException;
	
	SignaturePlayer retrieveRandomSignature(Player player) throws RemoteException;

	boolean settingNetwork(int time, int number, int numVPs, int[] net) throws RemoteException;
	
	boolean settingSocialMemory(int[] sequence) throws RemoteException;
	
	ArrayList<Float> retrievePlotPlayer(int index) throws RemoteException;
	
	boolean synchronization(int[] index) throws RemoteException;
	
	boolean reset(int index) throws RemoteException;
	
	boolean resetNetwork(int index) throws RemoteException;

	int retrieveNetwork(int index) throws RemoteException;
	
	ArrayList<Float> retrievePositionPlayers(int index, float position) throws RemoteException;
	
	boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time) throws RemoteException;

	boolean sendPositionsSolo(ArrayList<Float> vel, ArrayList<Float> pos, ArrayList<Integer> time, String nameFile) throws RemoteException;

	boolean storeRandomSignature() throws RemoteException;

	boolean settingDyadic(ArrayList<Object> paramDyadic) throws RemoteException;

	boolean resetDyadic() throws RemoteException;

}
