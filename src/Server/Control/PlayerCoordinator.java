
package Server.Control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;
import HumanPlayer.Algorithm.Settings;
import Server.Entity.HumanPlayer;
import Server.Entity.Signature;
import Server.RMIInterface.Player;
import Server.RMIInterface.ServicePlayer;
import Server.RMIInterface.SignaturePlayer;
import de.lessvoid.nifty.effects.impl.Remote;


@SuppressWarnings("serial")
public class PlayerCoordinator extends UnicastRemoteObject implements ServicePlayer{


	public PlayerCoordinator() throws RemoteException{
		super();
	}
	public int addplayer(int x, int y) throws RemoteException {
		return x + y;
	}
	public float[] retrivePositions() throws RemoteException {
		float[] positions =new float[State.getState().getMaxPlayer()*2+1];
		for (int i = 0; i < State.getState().getMaxPlayer()*2+1; i++){
			positions[i]=State.getState().getPositionPlayer(i+1);
		}
		return positions;
	}

	public void setPositions(int index,float p) throws  RemoteException{
		State.getState().setPositionPlayer(index,p);
	}

	@Override
	public ArrayList<? extends Player> retrievePlayerList() throws RemoteException{
		
		ArrayList<? extends HumanPlayer> listPlayer = HumanPlayer.retrievePlayerList();
		
		ArrayList<Player> list = new ArrayList<Player>(listPlayer.size());

		for(HumanPlayer p: listPlayer){
			list.add(new HumanPlayerC(p.getName(), p.getSurname(), p.getDateBirth()));
		}	
		return list;
	}
	

	@Override
	public SignaturePlayer retrieveRandomSignature(Player player) throws RemoteException{
		
		HumanPlayer contrPlayer = HumanPlayer.getHumanPlayer(player.getName(), player.getSurname(), player.getDateBirth());
		
		ArrayList<? extends Signature> listSig = contrPlayer.getOwnedSignatures();
		
		ArrayList<SignaturePlayer> s = new ArrayList<SignaturePlayer>(listSig.size());
		
		for(Signature sig : listSig)
			s.add(new SignatureC(sig.getTimeSeriesPos(), sig.getTimeSeriesVel()));
				
		return s.get((int) (Math.random()*s.size()));
	}
	

	@Override
	// gli index dei player vanno da 1 a N
	// gli index del vettore vanno da 0 a N-1
	public float changePosition2player(int index, float position){
		
		State.getState().setPositionPlayer(index-1, position);
		// leader è 1, follower è 2
		if(index == 1){
			// la posizione del follower sta in 1
			return State.getState().getPositionPlayer(index);
		}else if(index == 2){
			return State.getState().getPositionPlayer(index-2);
		}else
			return -10; //errore (le posizioni vanno da -5 a 5)
	}
	

	//ad ogni chiamata aggiungo +1
	@Override
	public boolean synchronization(int index) throws RemoteException {
		
		
		int[] alreadyConnected = State.getState().getConnections();
		int allConnected = 0;
		
		if( alreadyConnected[index-1] == 0){
			 System.out.println("Connected player number " + index);
			 State.getState().setConnections(index-1, 1);
		}
		
		for(int i = 0; i < State.getState().getNumberPlayer(); i++){
			if(alreadyConnected[i] == 1)
				allConnected++;
		}
		
		if(allConnected == State.getState().getNumberPlayer()){				
			return true;
		}else
			return false;
	}


	@Override
	public boolean reset(int index) throws RemoteException {		
		State.getState().setConnections(index-1, 0);
		
		if(State.getState().getConnections()[index-1]==0)
			return true;
		else 
			return false;
	}
	

	@Override
	public boolean resetNetwork(int index) throws RemoteException{
		State.getState().setConnections(index-1, 0);
		State.getState().setNumberPlayer(2);
		State.getState().setSocialMemorySequence(new int[]{0});
		
		if(State.getState().getConnections()[index-1]==0){
			return true;
		}else{
			return false;
		}
	}
	public int getMaxNumPlayers()throws RemoteException{
		return State.getState().getMaxPlayer();
	}

	@Override
	//l'indice zero è il tempo
	//l'indice uno è il numero di giocatori
	// se non vengono impostati dall'amministratore rimangono quelli di default
	// numero di giocatori = 2
	// tempo 30 secondi
	public int[] retrieveNetwork(int index) throws RemoteException {
		int[] s = new int[2];
		int count = 0;
		s[0] = State.getState().getTime();
		
		for(int i = 0; i < State.getState().getNumberPlayer(); i++)
			if(State.getState().getNetworkElement(index-1, i)==1)
				count++;
					
		s[1] = count;
		
		return s;
	}
	
	@Override
	public int[] retrieveSocialMemory() throws RemoteException{
		return State.getState().getSocialMemorySequence();
	}
	public void setTest(TypeTest t) throws RemoteException{
		State.getState().setTest(t);
	}
	public void setIsStart(boolean v) throws RemoteException{
		State.getState().setIsStart(v);
	}
	public void setPlayer(Server.Entity.Player p, int index) throws RemoteException{
		State.getState().setPlayer(p,index);
	}
	public Server.Entity.Player[] getPlayer()throws RemoteException{
		return State.getState().getPlayer();
	}
	public boolean getIsStart() throws RemoteException{
		return State.getState().getIsStart();
	}
	public void setAnswer(int index,String p) throws  RemoteException{
		State.getState().setAnswer(index,p);
	}
	public String[] getAnswer() throws RemoteException{
		return State.getState().getAnswer();
	}
	public void setIsQuestion(boolean v) throws RemoteException{
		State.getState().setIsQuestion(v);
	}
	public boolean getIsQuestion() throws RemoteException{
		return State.getState().getIsQuestion();
	}
	public void setInput(TypeInput t) throws RemoteException{
		State.getState().setInput(t);
	}
	public TypeInput retriveInput() throws RemoteException{
		return State.getState().getInput();
	}
	public void setIsSet(boolean v) throws RemoteException{
		State.getState().setIsSet(v);
	}
	public void setGroup(String name) throws RemoteException{
		State.getState().setNameGroup(name);
	}
	public String retriveGroup() throws RemoteException{
		return State.getState().getNameGroup();
	}
	public boolean getIsSet() throws RemoteException{
		return State.getState().getIsSet();
	}
	public boolean retriveIsGroup() throws RemoteException{
		return State.getState().getIsGroup();
	}
	public int setNickName(String name) throws RemoteException{
		return State.getState().setNickName(name);
	}
	public TypeTest retriveTest() throws RemoteException{
		return State.getState().getTest();
	}
	public boolean retriveConnection(int index) throws RemoteException {
		return State.getState().getIsConnected(index);
	}
	public void setConnection(int index,boolean value) throws RemoteException{
		State.getState().setIsConnected(index,value);
	}
	public void resetConnections()throws RemoteException{
		State.getState().resetConnectionPlayer();
	}

	@Override
	public ArrayList<Float> retrievePositionPlayers(int index, float position) throws RemoteException {
		int index_or=index;
		State.getState().setPositionPlayer(index, position);
		ArrayList<Float> positions = new ArrayList<Float>();
		index=index%State.getState().getMaxPlayer();
		for(int y = 0; y < State.getState().getNumberPlayer(); y++) {
			if (State.getState().getNetworkElement(index - 1, y) == 1) {
				positions.add(State.getState().getPositionPlayer(y + 1));

			}
		}
		if(State.getState().GetL3_rIndex()[index-1]){
			positions.add(State.getState().getPositionPlayer(index_or));
		}else{
			positions.add(State.getState().getPositionPlayer(index));
		}

		return positions;
	}


	@Override
	public boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time, String typeTrial) throws RemoteException {
		PrintWriter saveSamples = null;
		//PrintWriter saveNetwork = null;
		/*
		try {
			saveNetwork = new PrintWriter(new BufferedWriter(new FileWriter("settings.txt", true)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		
		if(typeTrial.equals("group")){
			/*
			State.getState().setPositionsToPlot(index-1, pos);
			// here I have position and time for saving data	
			try {
				saveSamples = new PrintWriter(new FileWriter("P"+State.getState().getNumberPlayer()+"_player"+index+"_1d.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
			
			State.getState().setPositionsToPlot(index-1, pos);
			// here I have position and time for saving data	
			try {
				saveSamples = new PrintWriter(new BufferedWriter(new FileWriter(".//Groups//"+State.getState().getNameGroup()+"//"+State.getState().getNameTest()+"//P"+State.getState().getNumberPlayer()+"_player"+index+"_1d.txt")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(typeTrial.equals("jointImprov")){
			State.getState().setPositionsToPlot(index-1, pos);
			try {
				saveSamples = new PrintWriter(new BufferedWriter
						(new FileWriter("P2_JI"+index+"_1d.txt", true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(typeTrial.equals("leader")){
			State.getState().setPositionsToPlot(index-1, pos);
			try {
				saveSamples = new PrintWriter(new BufferedWriter
						(new FileWriter("P2_L_1d.txt", true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(typeTrial.equals("follower")){
			State.getState().setPositionsToPlot(index-1, pos);
			try {
				saveSamples = new PrintWriter(new BufferedWriter
						(new FileWriter("P2_F_1d.txt", true)));	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*
		saveSamples.println("\n-------------------------------------------\n");
		saveSamples.println(formatter.format(date));
		saveSamples.println("\n-------------------------------------------\n");
		*/
		
		for(int i = 0; i < pos.size(); i++)
			saveSamples.print(Integer.toString(time.get(i)) + " "
					+ Float.toString(pos.get(i)) + "\n");  
		
		//saveNetwork.print("\nPlayer number " + index + ": Human Player");
		
		//saveNetwork.close();
		saveSamples.close();
		
		return true;
	}

	@Override
	public boolean sendRandomSignature(int index, ArrayList<Integer> randPos) throws RemoteException{
		State.getState().setRandomSignaturePlayer(index-1, randPos);

		PrintWriter saveSamples = null;

		try {
			saveSamples = new PrintWriter(new BufferedWriter(new FileWriter("./data/RandomSignatureP"+State.getState().getNumberPlayer()+"_player"+index+"_1d.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Integer> indices = new ArrayList<Integer>(randPos.size());

		for(int i = 0; i < State.getState().getNumberPlayer(); i++) {
			if (State.getState().getNetworkElement(index-1, i) == 1) {
				indices.add(i);
			} 
		}

		for(int i = 0; i < randPos.size(); i++)
			saveSamples.print(Integer.toString(indices.get(randPos.get(i)) + 1) + "\n");
			

		saveSamples.close();
		
		return true;
	}


	@Override
	public ArrayList<Object> retrieveDyadic() throws RemoteException {
		
		return State.getState().getParameterDyadic();
	}
	
	@Override
	public boolean resetDyadic() throws RemoteException{
		ArrayList<Object> reset = new ArrayList<Object>();
		reset.add(30);
		reset.add("");
		reset.add("");
		reset.add("");
		reset.add("");
		reset.add("");
		reset.add(new ArrayList<Float>());
		reset.add(new ArrayList<Float>());
		reset.add(new ArrayList<Float>());
		
		State.getState().setParameterDyadic(reset);
		System.out.println("Dyadic trial is reset");
		return true;
	}

}
