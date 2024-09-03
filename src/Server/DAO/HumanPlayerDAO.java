/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import java.util.*;
import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.HibernateException;

import org.hibernate.Query;


@Entity
@Table (name = "HumanPlayer")
public class HumanPlayerDAO{

    @Id
	private int ID;
	
	@Column(name = "username")
	private String name;
	
	@Column(name = "mode")
	private String surname;
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateBirth;
        
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="idHumanPlayer") 
	private List<SignatureDAO> signaturesPlayer;
        

    public static HumanPlayerDAO findHumanPlayer(int cod) {
    	
        SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();  
              
        Query query = session.createQuery("from HumanPlayerDAO as hp where hp.ID = :code");
		query.setParameter("code", cod);
                
        @SuppressWarnings("unchecked")
                        
		ArrayList<HumanPlayerDAO> result = (ArrayList<HumanPlayerDAO>)query.list();
		
		session.getTransaction().commit();		
		session.close();
		
		if (result.isEmpty() == true)
			return null;
		else
			return result.get(0);          
    }
        
        
    public HumanPlayerDAO(){ 
        this.signaturesPlayer = new ArrayList<SignatureDAO>();
    }
        
        
    public String getName(){
        return this.name;
    }
          
    public String getSurname(){
        return this.surname;
    }
        
    public Calendar getDateBirth(){
        return this.dateBirth;
    }

    public int getID(){
        return this.ID;
    }

    public List<SignatureDAO> getOwnedSignatures(){
        return this.signaturesPlayer;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public void setDateBirth(Calendar date){
        this.dateBirth = date;
    }


    public void setId(int cod){
        this.ID = cod;
    }

    public void setOwnedSignatures(SignatureDAO s){
        this.signaturesPlayer.add(s);
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
		
		HumanPlayerDAO hp = (HumanPlayerDAO) obj;  
		
		for (int i = 0; i < this.signaturesPlayer.size(); i++){
			if (!(hp.getOwnedSignatures().get(i).equals(this.signaturesPlayer.get(i))))
				return false;	
		}
		
		if ((this.name.equals(hp.getName())) &&
				(this.ID == hp.getID()) &&
				(this.surname.equals(hp.getSurname())) && 
				(this.dateBirth.equals(hp.getDateBirth())) &&
				(this.signaturesPlayer.size() == hp.getOwnedSignatures().size())) 
			return true;
		else
			return false;
	}


	@SuppressWarnings("unchecked")
	public static ArrayList<HumanPlayerDAO> retrieveHumanPlayerList() {
		ArrayList<HumanPlayerDAO> trovate;
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
				
		Query query = session.createQuery("from HumanPlayerDAO");
		
		trovate = (ArrayList<HumanPlayerDAO>)query.list();
				
		session.getTransaction().commit();		
		session.close();
		
		return trovate ;
	}
	
}
