/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Entity;

import Server.DAO.SignatureDAO;
import java.util.ArrayList;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Class Signature.
 *
 * @author marialombardi
 */
public class Signature {
    
 /** The date record. */
 //   private int ID;
    Calendar dateRecord;
    
    /** The time series pos. */
    ArrayList<Float> timeSeriesPos;
    
    /** The time series vel. */
    ArrayList<Float> timeSeriesVel;
    
    /**
     * Instantiates a new signature.
     */
    public Signature(){
        
    }
    
    /**
     * Instantiates a new signature.
     *
     * @param dao the dao
     */
    public Signature(SignatureDAO dao) {
        this.dateRecord = dao.getDateRecord();
        this.timeSeriesVel = new ArrayList<Float>(dao.getTimeSeriesVel());
        this.timeSeriesPos = new ArrayList<Float>(dao.getTimeSeriesPos());
    }
    
    /**
     * Instantiates a new signature.
     *
     * @param date the date
     * @param pos the pos
     * @param vel the vel
     */
    public Signature(Calendar date, ArrayList<Float> pos, ArrayList<Float> vel){
        
        this.dateRecord = date;
        this.timeSeriesPos = new ArrayList<Float>(pos);
        this.timeSeriesVel = new ArrayList<Float>(vel);
        
        SignatureDAO dao = this.prepareDAO();
        if(!dao.save())
            throw new NullPointerException();
    }
    
    /**
     * Prepare dao.
     *
     * @return the signature dao
     */
    public SignatureDAO prepareDAO(){
        SignatureDAO dao = new SignatureDAO();

        dao.setDateRecord(this.dateRecord);
        dao.setTimeSeriesPosVel(this.timeSeriesPos, this.timeSeriesVel);

        return dao;
    }
    
    /**
     * Update.
     *
     * @return true, if successful
     */
    public boolean update(){
        SignatureDAO dao = this.prepareDAO();
        return dao.update();
    }
    
    /**
     * Delete.
     *
     * @return true, if successful
     */
    public boolean delete(){
        SignatureDAO dao = this.prepareDAO();
        return dao.delete();
    }
    
    
    /**
     * Sets the date record.
     *
     * @param d the new date record
     */
    public void setDateRecord(Calendar d){
        this.dateRecord = d;
    }
    
    /**
     * Sets the time series pos.
     *
     * @param t the new time series pos
     */
    public void setTimeSeriesPos(ArrayList<Float> t){
        this.timeSeriesPos = t;
    }
    
    /**
     * Sets the time series vel.
     *
     * @param t the new time series vel
     */
    public void setTimeSeriesVel(ArrayList<Float> t){
        this.timeSeriesVel = t;
    }
    
    /**
     * Gets the date record.
     *
     * @return the date record
     */
    public Calendar getDateRecord(){
        return this.dateRecord;
    }
    
    /**
     * Gets the time series pos.
     *
     * @return the time series pos
     */
    public ArrayList<Float> getTimeSeriesPos(){
        return this.timeSeriesPos;
    }
    
    /**
     * Gets the time series vel.
     *
     * @return the time series vel
     */
    public ArrayList<Float> getTimeSeriesVel(){
        return this.timeSeriesVel;
    }
    
    /**
     * Gets the signature.
     *
     * @param id the id
     * @return the signature
     */
    public static Signature getSignature(int id){
        SignatureDAO dao = SignatureDAO.findSignature(id);
        return new Signature(dao);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		Signature s = (Signature) obj;
		
		for(int i = 0; i < this.timeSeriesPos.size(); i++){
			if(Float.compare(this.timeSeriesPos.get(i), s.getTimeSeriesPos().get(i)) != 0){			
				return false;				
			}
		}
		
		for(int i = 0; i < this.timeSeriesVel.size(); i++){
			if(Float.compare(this.timeSeriesVel.get(i), s.getTimeSeriesVel().get(i)) != 0){
				System.out.println("equals velocità");
				return false;
			}
		}
		
		if ((this.dateRecord.equals(s.getDateRecord())) &&
			(this.timeSeriesPos.size() == s.getTimeSeriesPos().size()) &&
			(this.timeSeriesVel.size() == s.getTimeSeriesVel().size()))
			return true;
		else
			return false;
	}

}
