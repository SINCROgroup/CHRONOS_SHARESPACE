/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Entity;

import Server.DAO.SignatureDAO;
import Server.DAO.HumanPlayerDAO;

import java.util.ArrayList;
import java.util.Calendar;
// TODO: Auto-generated Javadoc

/**
 * The Class HumanPlayer.
 *
 * @author marialombardi
 */
public class HumanPlayer {

	/** The id. */
	private int ID;
    
    /** The name. */
    private String name;
    
    /** The surname. */
    private String surname;
    
    /** The date birth. */
    private Calendar dateBirth;
    
    /** The signatures. */
    private ArrayList<Signature> signatures;
    
    /**
     * Gets the human player.
     *
     * @param n the n
     * @param s the s
     * @param d the d
     * @return the human player
     */
    public static HumanPlayer getHumanPlayer(String n, String s, Calendar d){

    	CommonClass g = new CommonClass();
    	int tempid = g.generateIdPlayer(n, s, d);

        HumanPlayerDAO dao = HumanPlayerDAO.findHumanPlayer(tempid);

        if (dao != null){
            //return null;
        	return new HumanPlayer(dao);
        }
        else
        	return null;
    }
    
    /**
     * Retrieve player list.
     *
     * @return the array list
     */
    public static ArrayList<HumanPlayer> retrievePlayerList(){
    	ArrayList<HumanPlayer> list = new ArrayList<HumanPlayer>();
    	ArrayList<HumanPlayerDAO> trovati = HumanPlayerDAO.retrieveHumanPlayerList();
    	if(trovati != null){
    		for(int i = 0; i < trovati.size(); i++)
    			list.add(new HumanPlayer(trovati.get(i)));
    		return list;
    	}else
    		return null;
    }
    
    /**
     * Instantiates a new human player.
     */
    public HumanPlayer(){
        
    }
    
    /**
     * Instantiates a new human player.
     *
     * @param dao the dao
     */
    public HumanPlayer(HumanPlayerDAO dao){
    	this.ID = dao.getID();
        this.name = dao.getName();
        this.surname = dao.getSurname();
        this.dateBirth = dao.getDateBirth();
        this.signatures = new ArrayList<Signature>();
        for(SignatureDAO s: dao.getOwnedSignatures()){
            this.signatures.add(new Signature(s));
        }
    }
    
    /**
     * Instantiates a new human player.
     *
     * @param name the name
     * @param surname the surname
     * @param dateBirth the date
     * @param sig the sig
     */
    public HumanPlayer(String name, String surname, Calendar dateBirth, Signature sig){
        this.name = name;
        this.surname = surname;
        this.dateBirth = dateBirth;
        this.signatures = new ArrayList<Signature>();
        this.signatures.add(sig);
        System.out.println(dateBirth);
        CommonClass g = new CommonClass();
        this.ID = g.generateIdPlayer(name, surname, dateBirth);

        HumanPlayerDAO dao = this.prepareDAO();
        if(!dao.save())
            throw new NullPointerException();
    }
    
    /**
     * Prepare dao.
     *
     * @return the human player dao
     */
    public HumanPlayerDAO prepareDAO(){
        HumanPlayerDAO dao = new HumanPlayerDAO();
        dao.setId(this.ID);
        //Calendar data=Calendar.getInstance() ;
        //data= ;
        dao.setDateBirth(this.dateBirth);
        dao.setName(this.name);
        //dao.setSurname(this.surname);
        
        for(Signature s: this.signatures){
            dao.setOwnedSignatures(s.prepareDAO());
        }
        return dao;
    }
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Gets the surname.
     *
     * @return the surname
     */
    public String getSurname(){
        return this.surname;
    }
    
    /**
     * Gets the date birth.
     *
     * @return the date birth
     */
    public Calendar getDateBirth(){
        return this.dateBirth;
    }
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getID(){
        return this.ID;
    }
    
    /**
     * Gets the owned signatures.
     *
     * @return the owned signatures
     */
    public ArrayList<Signature> getOwnedSignatures(){
        return this.signatures;
    }
    
    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Sets the surname.
     *
     * @param surname the new surname
     */
    public void setSurname(String surname){
        this.surname = surname;
    }
    
    /**
     * Sets the date birth.
     *
     * @param date the new date birth
     */
    public void setDateBirth(Calendar date){
        this.dateBirth = date;
    }
    
    /**
     * Adds the signature.
     *
     * @param s the s
     */
    public void addSignature(Signature s){
        this.signatures.add(s);
    }
    
    /**
     * Update.
     *
     * @return true, if successful
     */
    public boolean update(){
        HumanPlayerDAO dao = this.prepareDAO();
        return dao.update();
    }
    
    /**
     * Delete.
     *
     * @return true, if successful
     */
    public boolean delete(){
        HumanPlayerDAO dao = this.prepareDAO();
        return dao.delete();
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		HumanPlayer hp = (HumanPlayer) obj;  
		
		for (int i = 0; i < this.signatures.size(); i++){
			if (!(hp.getOwnedSignatures().get(i).equals(this.signatures.get(i))))
				return false;	
		}
		
		if ((this.name.equals(hp.getName())) &&				
				(this.surname.equals(hp.getSurname())) && 
				(this.dateBirth.equals(hp.getDateBirth())) &&
				(this.signatures.size() == hp.getOwnedSignatures().size())) 
			return true;
		else
			return false;
		
/*		if ((this.name.equals(hp.getName())) &&
				(this.surname.equals(hp.getSurname())) &&
				(this.ID == hp.getID()) &&
				(this.dateBirth.equals(hp.getDateBirth()))) 
			return true;
		else
			return false;*/
	}    
}
