/*
 * 
 */
package Test.Control;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import Server.Control.AdminCoordinator;
import Server.DAO.PopulateTestDatabase;

// TODO: Auto-generated Javadoc
/**
 * The Class AdminCoordinatorTest.
 */
public class AdminCoordinatorTest {
	
	/** The test. */
	private static AdminCoordinator test = null;
    
    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PopulateTestDatabase.populate();	
		test = new AdminCoordinator();
	}

	/**
	 * Adds the new signature test.
	 *
	 * @throws RemoteException the remote exception
	 */
	@Test
	public void addNewSignatureTest() throws RemoteException{
		final String idTest = "Test di inserimento nuova firma nel database";
		
		ArrayList<Float> pos = new ArrayList<Float>();
		pos.add(-500f);
		pos.add(500f);
		
		ArrayList<Float> vel = new ArrayList<Float>();
		vel.add(43f);
		vel.add(29f);
		
		Calendar cal = Calendar.getInstance();
		Calendar rec = Calendar.getInstance();
		cal.clear();
		rec.clear();
		cal.set(1965, Calendar.FEBRUARY, 2);
		rec.set(2014, Calendar.NOVEMBER, 2);

		Boolean result = test.addNewSignature("Franco", "Benedetto", cal, rec, pos, vel);
		
		assertTrue(idTest + " riuscito", result);
	}

}
