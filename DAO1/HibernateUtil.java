/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class HibernateUtil.
 *
 * @author marialombardi
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
	
	/** The service registry. */

	static {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    sessionFactory = configuration.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}