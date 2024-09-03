
package Server.DAO;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@SuppressWarnings("serial")
@Entity
@Table (name = "SignatureElement")
public class SignatureElementDAO implements Serializable{

	@Id
	@GeneratedValue
	private int ID;
	
	@Column
	private int indexSamples;
	
	@Column
	private float elementPos;
	
	@Column
	private float elementVel;
	
	
	@SuppressWarnings("unused")
	private SignatureElementDAO(){
		
	}

	public SignatureElementDAO(int index, float p, float v){
		this.indexSamples = index;
		this.elementPos = p;
		this.elementVel = v;
	}
	

	//cod è l'id della firma della tabella Signature
    public static ArrayList<SignatureElementDAO> rebuildSignature(int cod) {
    	
        SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();  
              
        Query query = session.createQuery("from SignatureElementDAO as es where es.ID = :code");
		query.setParameter("code", cod);
                
        @SuppressWarnings("unchecked")
                        
		ArrayList<SignatureElementDAO> result = (ArrayList<SignatureElementDAO>)query.list();
		
		session.getTransaction().commit();		
		session.close();
		
		if (result.isEmpty() == true)
			return null;
		else
			return result;          
    }


	public int getId() {
		return ID;
	}

	public void setId(int id) {
		this.ID = id;
	}

	public int getIndexSamples() {
		return indexSamples;
	}

	public void setIndexSamples(int indexSamples) {
		this.indexSamples = indexSamples;
	}

	public float getElementPos() {
		return elementPos;
	}

	public void setElementPos(float elementPos) {
		this.elementPos = elementPos;
	}

	public float getElementVel() {
		return elementVel;
	}

	public void setElementVel(float elementVel) {
		this.elementVel = elementVel;
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
		
		SignatureElementDAO se = (SignatureElementDAO) obj;  
		
		if (this.indexSamples == se.getIndexSamples() &&
				Float.compare(this.elementPos, se.getElementPos()) == 0 && 
				Float.compare(this.elementVel, se.getElementVel()) == 0)
			return true;
		else
			return false;
	}
}
