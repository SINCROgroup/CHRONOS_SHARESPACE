/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@SuppressWarnings("serial")
@Entity
@Table (name = "Signatures")
public class SignatureDAO implements Serializable {
    
	@Id
	@GeneratedValue
    private int ID;
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateRecord;  
    
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="idSignature") 
	private List<SignatureElementDAO> signaturesElement;
    
    
    public static SignatureDAO findSignature(int cod){
    	SessionFactory sf = HibernateUtil.getSessionFactory();
    	Session session = sf.openSession();
    	session.beginTransaction();  
          
    	Query query = session.createQuery("from SignatureDAO as s where s.ID = :code");
    	query.setParameter("code", cod);
            
    	@SuppressWarnings("unchecked")                      
    	ArrayList<SignatureDAO> result = (ArrayList<SignatureDAO>)query.list();
	
    	session.getTransaction().commit();		
    	session.close();
	
		if (result.isEmpty() == true)
			return null;
		else
			return result.get(0);  
    }
    

    public SignatureDAO(){
    	this.signaturesElement = new ArrayList<SignatureElementDAO>();
    }
    
    public int getID(){
        return this.ID;
    }

    public Calendar getDateRecord(){
        return this.dateRecord;
    }

    public ArrayList<Float> getTimeSeriesPos(){ 
    	ArrayList<Float> p = new ArrayList<Float>();
    	
    	for(int i = 0; i < this.signaturesElement.size(); i++)
    		p.add(this.signaturesElement.get(i).getElementPos());
    	
    	return p;
    }
    
    public ArrayList<Float> getTimeSeriesVel(){ 
    	ArrayList<Float> v = new ArrayList<Float>();
    	
    	for(int i = 0; i < this.signaturesElement.size(); i++)
    		v.add(this.signaturesElement.get(i).getElementVel());
    	
    	return v;
    }
    
    public void setID(int cod){
        this.ID = cod;
    }
    
    public void setDateRecord(Calendar d){
        this.dateRecord = d;
    }

    public void setTimeSeriesPosVel(ArrayList<Float> p, ArrayList<Float> v){
    	
    	for(int i = 0; i < p.size(); i++){
            SignatureElementDAO dao = new SignatureElementDAO(i, p.get(i), v.get(i));
            if(!dao.save())
                throw new NullPointerException();
            this.signaturesElement.add(dao);
        }
    }

    public boolean update() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		try {
			session.update(this);
		
			session.getTransaction().commit();
		}catch (HibernateException ex){
			session.getTransaction().rollback();
			return false;
			
		} finally {
			session.close();
		}
	
		return true;
    }

	public boolean save(){			
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		try{
		session.save(this);
		
			session.getTransaction().commit();
		}catch (HibernateException ex){
			session.getTransaction().rollback();
			return false;
		
		} finally {
			session.close();
		}
	
		return true;
	}


	public boolean delete(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		
		try{				
			session.delete(this);
			session.getTransaction().commit();
		} catch(HibernateException e){
			session.getTransaction().rollback();
			return false;
		
		} finally{
				session.close();
		}
		
		return true;
	}


	@Override
	public boolean equals(Object obj) {
		
		SignatureDAO s = (SignatureDAO) obj;
		
		for (int i = 0; i < this.signaturesElement.size(); i++){
			if (!(s.signaturesElement.get(i).equals(this.signaturesElement.get(i)))){
				return false;	
			}
		}
		
		if (this.dateRecord.equals(s.getDateRecord())
				&& this.signaturesElement.size() == s.signaturesElement.size()) 
				return true;
		else
				return false;
	}
}
