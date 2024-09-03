package Server.Control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;

import Administrator.Algorithm.Settings;
import Administrator.Algorithm.TypeInput;
import Administrator.Algorithm.TypeTest;

import Administrator.Algorithm.TypeTest;
import Server.DAO.HumanPlayerDAO;
import Server.Entity.CommonClass;
import Server.Entity.HumanPlayer;
import Server.Entity.Signature;
import Server.RMIInterface.*;


/**
 * Implement the interface of the server used by the administrator
 */
@SuppressWarnings("serial")
public class AdminCoordinator extends UnicastRemoteObject implements ServiceAdmin{
	
	private static final Logger logger = Logger.getLogger(AdminCoordinator.class.getName());
	private int sessioneCounter = 0;


	public AdminCoordinator() throws RemoteException{
		super();
	}

	public int add(int x, int y) throws RemoteException {
		return x + y;
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
	public void setNameTest(String name) throws RemoteException{
		State.getState().setNameTest(name);
	}
	public String getNameTest() throws RemoteException{
		return State.getState().getNameTest();
	}
	public void setGroup(String name) throws RemoteException{
		State.getState().setNameGroup(name);
	}
	public String retriveGroup() throws RemoteException{
		return State.getState().getNameGroup();
	}
	public void setIsGroup(boolean v) throws RemoteException{
		State.getState().setIsGroup(v);
	}

	public void setIsStart(boolean v) throws RemoteException{
		State.getState().setIsStart(v);
	}
	public boolean getIsStart() throws RemoteException{
		return State.getState().getIsStart();
	}
	public void resetAnswer() throws RemoteException{
		State.getState().resetAnswer();
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
	public void setIsSet(boolean v) throws RemoteException{
		State.getState().setIsSet(v);
	}
	public boolean getIsSet() throws RemoteException{
		return State.getState().getIsSet();
	}
	public void setL2Index (int index,boolean v)throws RemoteException{
		State.getState().SetL2Index(index,v);
	}
	public void setL3_aIndex (int index,boolean v)throws RemoteException{
		State.getState().SetL3_aIndex(index,v);
	}
	public void setL3_rIndex (int index,boolean v)throws RemoteException{
		State.getState().SetL3_rIndex(index,v);
	}
	public boolean[] getL2Index()throws RemoteException{
		return State.getState().GetL2Index();
	}
	public boolean[] getL3_aIndex()throws RemoteException{
		return State.getState().GetL3_aIndex();
	}
	public boolean[] getL3_rIndex()throws RemoteException {
		return State.getState().GetL3_rIndex();
	}
	public void resetLIndex() throws RemoteException{
		State.getState().resetLIndex();
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

	public void setTest(TypeTest t) throws RemoteException{
		State.getState().setTest(t);
	}
	public TypeTest retriveTest() throws RemoteException{
		return State.getState().getTest();
	}
	public boolean getIsGroup() throws RemoteException{
		return State.getState().getIsGroup();
	}
	public void setPlayer(Server.Entity.Player p, int index) throws RemoteException{
		State.getState().setPlayer(p,index);
	}
	public Server.Entity.Player[] getPlayer()throws RemoteException{
		return State.getState().getPlayer();
	}
	public void setInput(TypeInput t) throws RemoteException{
		State.getState().setInput(t);
	}
	public TypeInput retriveInput() throws RemoteException{
		return State.getState().getInput();
	}
	@Override
	public boolean addNewSignature(String name, String surname, Calendar data, Calendar rec,
			ArrayList<Float> p, ArrayList<Float> v) throws RemoteException{

		//creazione dell'oggetto firma e memorizzazione
		try{
			HumanPlayer hp = HumanPlayer.getHumanPlayer(name, surname, data);
			if( hp == null)	{
				Signature sig = new Signature();
				sig.setDateRecord(rec);
				sig.setTimeSeriesPos(p);
				sig.setTimeSeriesVel(v);
				new HumanPlayer(name, surname, data, sig);
			}
			else{
				Signature sig = new Signature();
				sig.setDateRecord(rec);
				sig.setTimeSeriesPos(p);
				sig.setTimeSeriesVel(v);
				hp.addSignature(sig);				
				
				CommonClass g = new CommonClass();
		    	int tempid = g.generateIdPlayer(name, surname, data);
		        HumanPlayerDAO dao = HumanPlayerDAO.findHumanPlayer(tempid);	
		        dao.setOwnedSignatures(sig.prepareDAO());
		        
		        if(!dao.update())
		            throw new NullPointerException();				
			}
			return true;
		}catch(NullPointerException ex){
			return false;
		}
	}

	@Override
	public boolean settingSocialMemory(int[] sequence) throws RemoteException {

		State.getState().setSocialMemorySequence(sequence);
		
		//add social memory option to the settings file
		PrintWriter saveSocialMemory = null;
		try{
			saveSocialMemory = new PrintWriter(new BufferedWriter(new FileWriter("settings.txt", true)));
		}catch(IOException e){
			logger.warning("Error with opening the file settings.txt. I can't add social memory settings.");
			e.printStackTrace();
		}
		
		if(sequence.length == 1){
			saveSocialMemory.print("\n\nSocial memory: No");
		}else{
			saveSocialMemory.print("\n\nSocial memory: Yes. \n Sequence of time frames in seconds [EC - EO - EC - etc.]: " 
					+ Arrays.toString(sequence));
		}
		saveSocialMemory.println("\n-------------------------------------------\n");
		saveSocialMemory.close();
		
		logger.info("Social memory sequence: " + Arrays.toString(State.getState().getSocialMemorySequence()));
		if(Arrays.equals(sequence, State.getState().getSocialMemorySequence())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean settingNetwork(int time, int number, int numVPs, int[] net) {
		State.getState().setNumberPlayer(number);
		State.getState().setTime(time);
		int [][] matrixNet = new int[number][number];
		for(int i = 0; i < number; i++){
			for(int y = 0; y < number; y++){
				matrixNet[i][y] = net[y + number*i];				
			}
		}
		
		State.getState().setNetwork(matrixNet);	
		
		//save network in a file
		PrintWriter saveNetwork = null;
		try{
			saveNetwork = new PrintWriter(new BufferedWriter(new FileWriter("settings.txt", true)));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		saveNetwork.println("\n-------------------------------------------\n");
		saveNetwork.print("Adjacency matrix:\n");
		
		for(int i = 0; i < number; i++){
			for(int j = 0; j < number; j++){
				saveNetwork.print(Integer.toString(matrixNet[i][j]) + " ");
			}
			saveNetwork.print("\n");
		}
		
		saveNetwork.print("\nTime: " + time + " seconds");
		saveNetwork.print("\nNumber Virtual Player: " + numVPs);
		saveNetwork.print("\nNumber Human Player: " + (number - numVPs));
		
		// print to output video
		if(State.getState().getTime() == time && 
				State.getState().getNumberPlayer() == number){
			logger.info("Time: " + State.getState().getTime());
			logger.info("Number of player: " + State.getState().getNumberPlayer());
			logger.info("Adjacency matrix: ");
			for(int i = 0; i < State.getState().getNumberPlayer(); i++){
				for(int y = 0; y < State.getState().getNumberPlayer(); y++)
					logger.info("element [" + i + " " + y + "]: " + State.getState().getNetworkElement(i, y));
			}
			saveNetwork.close();
			return true;
		}else{
			saveNetwork.close();
			logger.warning("Error with settings");
			return false;
		}
	}
	

	@Override
	public ArrayList<Float> retrievePlotPlayer(int index){
		return State.getState().getPositionsToPlot(index-1);		
	}
	public float[] retrivePositions() throws RemoteException {
		float[] positions =new float[State.getState().getMaxPlayer()*2];
		for (int i = 0; i < State.getState().getMaxPlayer()*2; i++){
			positions[i]=State.getState().getPositionPlayer(i+1);
		}
		return positions;
	}

	public void setPositions(int index,float p) throws  RemoteException{
		State.getState().setPositionPlayer(index,p);
	}
	public void resetPositions() throws  RemoteException{
		for (int i = 0; i < State.getState().getMaxPlayer()*2; i++) {
			State.getState().setPositionPlayer(i+1, 0);
		}
	}

	@Override
	public boolean synchronization(int[] index) throws RemoteException {
		//vettore dei numeri dei virtual player
		
		int[] alreadyConnected = State.getState().getConnections();
		int allConnected = 0;
		
		if(index != null){
			//il virtual player deve essere connesso per forza con un giocatore
			for(int i = 0; i < index.length; i++){
				if( index[i] != 0 && alreadyConnected[i] == 0){
					 logger.info("Connected player number " + (i+1));
					 State.getState().setConnections(i, 1);
				}
			}
		}
		int count =0;
		for(int i = 0; i < State.getState().getNumberPlayer(); i++){
			if(State.getState().GetL2Index()[i]) {
				count++;
				if (alreadyConnected[i + State.getState().getMaxPlayer()] == 1)
					allConnected++;
			}else if(State.getState().GetL3_rIndex()[i]) {
				count++;
				if (alreadyConnected[i + State.getState().getMaxPlayer()] == 1)
					allConnected++;
			}
				if (alreadyConnected[i] == 1)
					allConnected++;
		}
		if(allConnected == State.getState().getNumberPlayer()+count){
			return true;
		}else
			return false;
	}


	@Override
	public boolean reset(int index) throws RemoteException {
		State.getState().setConnections(index-1, 0);
		
		if(State.getState().getConnections()[index-1]==0)
			return true;
		else return false;
	}


	@Override
	public boolean resetNetwork(int index) throws RemoteException {
		State.getState().setConnections(index-1, 0);
		State.getState().setNumberPlayer(2);
		State.getState().setSocialMemorySequence(new int[]{0});
		
		if(State.getState().getConnections()[index-1]==0){
			return true;
		}
		
		else return false;
	}


	@Override
	public int retrieveNetwork(int index) throws RemoteException {
		int count = 0;
		
		for(int i = 0; i < State.getState().getNumberPlayer(); i++)
			if(State.getState().getNetworkElement(index-1, i)==1)
				count++;
		
		return count;
	}


	@Override
	public ArrayList<Float> retrievePositionPlayers(int index, float position) throws RemoteException {
		
		State.getState().setPositionPlayer(index, position);
		ArrayList<Float> positions = new ArrayList<Float>();
		
		// mi prendo la riga della matrice di adiacenza che mi serve
		// e valuto se il valore è zero o no
		// la riga è index-1
		for(int y = 0; y < State.getState().getNumberPlayer(); y++)
			if(State.getState().getNetworkElement(index-1, y) == 1)
				positions.add(State.getState().getPositionPlayer(y));
		
		return positions;
	}


	@Override
	public boolean sendPositions(int index, ArrayList<Float> pos, ArrayList<Integer> time) throws RemoteException {
		/*
		// saveSamples = null;
		//PrintWriter saveNetwork = null;
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		
		State.getState().setPositionsToPlot(index-1, pos);

		try {
			String fileName = "P"+State.getState().getNumberPlayer()+"_player"+index+"_1d_"+formatter.format(date)+".txt";
			PrintWriter saveSamples = new PrintWriter(new FileWriter(fileName));
			//saveNetwork = new PrintWriter(new BufferedWriter
			//		(new FileWriter("settings.txt", true)));

			saveSamples.println("\n-------------------------------------------\n");
			saveSamples.println(formatter.format(date));
			saveSamples.println("\n-------------------------------------------\n");
			
			for(int i = 0; i < pos.size(); i++)
				saveSamples.print(Integer.toString(time.get(i)) + " "
						+ Float.toString(pos.get(i)) + "\n");
		
			//saveNetwork.print("\nPlayer number " + index + ": Virtual Player");
			
			//saveNetwork.close();
			saveSamples.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		PrintWriter saveSamples = null;
		//PrintWriter saveNetwork = null;
		
		State.getState().setPositionsToPlot(index-1, pos);

		try {
			String fileName = "./data/P"+State.getState().getNumberPlayer()+"_player"+index+"_1d.txt";
			saveSamples = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			//saveNetwork = new PrintWriter(new BufferedWriter
			//		(new FileWriter("settings.txt", true)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i = 0; i < pos.size(); i++)
			saveSamples.print(Integer.toString(time.get(i)) + " "
					+ Float.toString(pos.get(i)) + "\n");
	
		//saveNetwork.print("\nPlayer number " + index + ": Virtual Player");
		
		//saveNetwork.close();
		saveSamples.close();
			
		return true;
	}
	
	
	@Override
	public boolean sendPositionsSolo(ArrayList<Float> vel, ArrayList<Float> pos, ArrayList<Integer> time, String nameFile) throws RemoteException {
		PrintWriter saveSamples = null;
		try {
			saveSamples = new PrintWriter(new BufferedWriter(new FileWriter(nameFile, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < Math.min(Math.min(time.size(),pos.size()), vel.size()); i++){
			saveSamples.print(Integer.toString(time.get(i)) + " "
					+ Float.toString(pos.get(i)) + " "
					+ Float.toString(vel.get(i)) + "\n");  			
		}
		saveSamples.close();
		return true;

	}


	@Override
	public boolean storeRandomSignature() throws RemoteException {
		logger.info("entrato");
		PrintWriter saveSamples = null;

		try {
			String fileName = "./data/RandomSignatures.txt";
			saveSamples = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//ArrayList<ArrayList<Integer>> randomSignatures = new ArrayList<ArrayList<Integer>>(State.getState().getNumberPlayer());
		for(int i = 0; i < State.getState().getNumberPlayer(); i++)
			for(int j = 0; j < State.getState().getNumberPlayer() - 1; j++){
				saveSamples.print(Integer.toString(State.getState().getRandomSignatures(i).get(j) + 1) + " ");
			}
			saveSamples.print("\n");

		saveSamples.close();

		return true;
	}
	
	
	@Override
	public boolean settingDyadic(ArrayList<Object> paramDyadic) throws RemoteException {
		
		State.getState().setParameterDyadic(paramDyadic);
		logger.info("Parameters of dyadic trial: " + State.getState().getParameterDyadic());
		
		return true;
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
		logger.info("Dyadic trial is reset");
		return true;
	}
}
