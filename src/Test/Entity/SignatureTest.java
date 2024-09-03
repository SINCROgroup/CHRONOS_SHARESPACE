/*
 * 
 */
package Test.Entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.DAO.PopulateTestDatabase;
import Server.DAO.SignatureDAO;
import Server.Entity.Signature;

// TODO: Auto-generated Javadoc
/**
 * The Class SignatureTest.
 */
public class SignatureTest {

	/** The oracle. */
	private static Signature oracle = null;
	
	/** The test. */
	private Signature test = null;
	
    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PopulateTestDatabase.populate();

		oracle = Signature.getSignature(1);
	}
    
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

		test = Signature.getSignature(1);				
	}
	
	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test signature dao.
	 */
	@Test
	public void testSignatureDAO() {
	       final String idTest = "Test del costruttore dal DAO";

	       	SignatureDAO dao = SignatureDAO.findSignature(1);
	        Signature nuova = new Signature(dao);
			
	        assertEquals(idTest + " riuscito", oracle, nuova);
	}
	
    /**
     * Test prepare dao.
     */
    @Test
    public void testPrepareDAO() {
    	final String idTest = "Test di prepareDAO";
	
    	SignatureDAO dao = test.prepareDAO();
	
    	assertEquals(idTest + "riuscito", SignatureDAO.findSignature(1), dao);
    }
    
	/**
	 * Test signature.
	 */
	@Test
	public void testSignature(){
		
		final String idTest = "Test del costruttore";
		
		Calendar r = Calendar.getInstance();
		r.clear();
		r.set(2013, Calendar.OCTOBER, 29);
		ArrayList<Float> f = new ArrayList<Float>();
		f.add(3f);
		f.add(4f);		
		ArrayList<Float> p = new ArrayList<Float>();
		p.add(13f);
		p.add(42f);
		
		
		Signature sig = new Signature(r, f, p);
		System.out.println("elementi f " + sig.getTimeSeriesPos().get(0));
		System.out.println("elementi f " + sig.getTimeSeriesPos().get(1));
		System.out.println("elementi p " + sig.getTimeSeriesVel().get(0));
		System.out.println("elementi p " + sig.getTimeSeriesVel().get(1));
		
		Signature s = Signature.getSignature(11);
		System.out.println("elementi s pos " + s.getTimeSeriesPos().get(0));
		System.out.println("elementi s pos " + s.getTimeSeriesPos().get(1));
		System.out.println("elementi s vel " + s.getTimeSeriesVel().get(0));
		System.out.println("elementi s vel " + s.getTimeSeriesVel().get(1));

        assertEquals(idTest + " riuscito", s, sig);
	}
	
    /**
     * Test get record date.
     */
    @Test
	public void testGetRecordDate() {
		final String idTest = "Test di getRecordDate";
		Calendar data  = Calendar.getInstance();
		data.clear();
		data.set(2014, Calendar.NOVEMBER, 15);
		assertEquals(idTest + " riuscito", data , test.getDateRecord());
	}
    
    /**
     * Test get time series pos.
     */
    @Test
    public void testGetTimeSeriesPos(){
    	final String idTest = "Test di getTimeSeriesPos";
    	ArrayList<Float> serie = new ArrayList<Float>();
    	serie.add(3f);
    	serie.add(15f);
    	serie.add(98f);
    	
    	assertEquals(idTest + " riuscito", serie.size() , test.getTimeSeriesPos().size());
    	assertEquals(idTest + " riuscito", serie.get(0) , test.getTimeSeriesPos().get(0));
    	assertEquals(idTest + " riuscito", serie.get(1) , test.getTimeSeriesPos().get(1));
    	assertEquals(idTest + " riuscito", serie.get(2) , test.getTimeSeriesPos().get(2));
    }
    
    /**
     * Test get time series vel.
     */
    @Test
    public void testGetTimeSeriesVel(){
    	final String idTest = "Test di getTimeSeriesVel";
    	ArrayList<Float> serie = new ArrayList<Float>();
    	serie.add(12f);
    	serie.add(2f);
    	serie.add(89f);
    	
    	assertEquals(idTest + " riuscito", serie.size() , test.getTimeSeriesVel().size());
    	assertEquals(idTest + " riuscito", serie.get(0) , test.getTimeSeriesVel().get(0));
    	assertEquals(idTest + " riuscito", serie.get(1) , test.getTimeSeriesVel().get(1));
    	assertEquals(idTest + " riuscito", serie.get(2) , test.getTimeSeriesVel().get(2));
    }
    
    /**
     * Test set record date.
     */
    @Test
    public void testSetRecordDate(){
		final String idTest = "Test di SetRecordDate ";
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2015, Calendar.OCTOBER, 21);
		test.setDateRecord(date);
		assertEquals(idTest + " riuscito", date , test.getDateRecord());
    }

    /**
     * Test set time series pos.
     */
    @Test
    public void testSetTimeSeriesPos(){
		final String idTest = "Test di SetTimeSeriesPos ";
    	ArrayList<Float> serie = new ArrayList<Float>();
    	serie.add(10f);
    	serie.add(9f);
    	serie.add(8f);
    	
		test.setTimeSeriesPos(serie);
		
    	assertEquals(idTest + " riuscito", serie.size() , test.getTimeSeriesPos().size());
    	assertEquals(idTest + " riuscito", serie.get(0) , test.getTimeSeriesPos().get(0));
    	assertEquals(idTest + " riuscito", serie.get(1) , test.getTimeSeriesPos().get(1));
    	assertEquals(idTest + " riuscito", serie.get(2) , test.getTimeSeriesPos().get(2));
    }
    
    /**
     * Test set time series vel.
     */
    @Test
    public void testSetTimeSeriesVel(){
		final String idTest = "Test di SetTimeSeriesVel ";
    	ArrayList<Float> serie = new ArrayList<Float>();
    	serie.add(13f);
    	serie.add(1f);
    	serie.add(78f);
    	
		test.setTimeSeriesVel(serie);
		
    	assertEquals(idTest + " riuscito", serie.size() , test.getTimeSeriesVel().size());
    	assertEquals(idTest + " riuscito", serie.get(0) , test.getTimeSeriesVel().get(0));
    	assertEquals(idTest + " riuscito", serie.get(1) , test.getTimeSeriesVel().get(1));
    	assertEquals(idTest + " riuscito", serie.get(2) , test.getTimeSeriesVel().get(2));
    }
}
