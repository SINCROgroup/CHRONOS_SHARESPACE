/*
 * 
 */
package Test.Entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;
import org.junit.After;
import org.junit.*;

import Server.DAO.HumanPlayerDAO;
import Server.DAO.PopulateTestDatabase;
import Server.Entity.HumanPlayer;
import Server.Entity.Signature;

// TODO: Auto-generated Javadoc
/**
 * The Class HumanPlayerTest.
 */
public class HumanPlayerTest {

	/** The oracle. */
	private static HumanPlayer oracle = null;
	
	/** The test. */
	private HumanPlayer test = null;
    
    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PopulateTestDatabase.populate();
		Calendar data  = Calendar.getInstance();
		data.clear();
		data.set(1991, Calendar.OCTOBER, 31);
		oracle = HumanPlayer.getHumanPlayer("Maria", "Lombardi", data);	
	}


	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		Calendar data  = Calendar.getInstance();
		data.clear();
		data.set(1991, Calendar.OCTOBER, 31);
		test = HumanPlayer.getHumanPlayer("Maria", "Lombardi", data);				
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
     * Test human player dao.
     */
    @Test
    public void testHumanPlayerDAO() {
        final String idTest = "Test del costruttore dal DAO";

        HumanPlayerDAO dao = HumanPlayerDAO.findHumanPlayer(3331);
        HumanPlayer nuova = new HumanPlayer(dao);
		
        assertEquals(idTest + " riuscito", oracle, nuova);
    }
      
      
    /**
     * Test prepare dao.
     */
    @Test
    public void testPrepareDAO() {
    	final String idTest = "Test di prepareDAO";
	
    	HumanPlayerDAO dao = test.prepareDAO();
	
    	assertEquals(idTest + "riuscito", HumanPlayerDAO.findHumanPlayer(oracle.getID()), dao);
    }
    
      
    /**
     * Test get name.
     */
    @Test
	public void testGetName() {
		final String idTest = "Test di getName";
		
		assertEquals(idTest + " riuscito", "Maria" , test.getName());
	
	}

	/**
	 * Test get surname.
	 */
	@Test
	public void testGetSurname() {
		final String idTest = "Test di getSurname";
		
		assertEquals(idTest + " riuscito", "Lombardi" , test.getSurname());
	}
    
    /**
     * Test get birth date.
     */
    @Test
	public void testGetBirthDate() {
		final String idTest = "Test di getBirthDate";
		Calendar data  = Calendar.getInstance();
		data.clear();
		data.set(1991, Calendar.OCTOBER, 31);
		assertEquals(idTest + " riuscito", data , test.getDateBirth());
	}
    
    /**
     * Test set name.
     */
    @Test
	public void testSetName() {
		final String idTest = "Test di SetName ";
		test.setName("Francesco");
		assertEquals(idTest + " riuscito", "Francesco" , test.getName());
	}

	/**
	 * Test set surname.
	 */
	@Test
	public void testSetSurname() {
		final String idTest = "Test di SetSurame ";
		test.setSurname("Alderisio");
		assertEquals(idTest + " riuscito", "Alderisio" , test.getSurname());
	}

	/**
	 * Test set birth date.
	 */
	@Test
	public void testSetBirthDate() {
		final String idTest = "Test di SetBirthDate ";
		Calendar data  = Calendar.getInstance();
		data.clear();
		data.set(1980, 11, 12);
		test.setDateBirth(data);
		assertEquals(idTest + " riuscito", data , test.getDateBirth());
	}
    
	/**
	 * Test get owned signatures.
	 */
	@Test
	public void testGetOwnedSignatures(){
	
	    final String idTest = "Test di GetOwnedSignatures()";
	            
		Signature [] ac = new Signature[]{
				Signature.getSignature(1),
				Signature.getSignature(9),
				Signature.getSignature(10)				
		};
	
		ArrayList<Signature> list = test.getOwnedSignatures();
		
		System.out.println("size list " + list.size());
	
		assertEquals(idTest + " riuscito", 3 , list.size());
		
			for (Signature s : ac){
				assertEquals(idTest + " riuscito", true , list.contains(s));
			}
	}

	/**
	 * Test human player.
	 */
	@Test
	public void testHumanPlayer(){
		
		final String idTest = "Test del costruttore";
		
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(1900, Calendar.FEBRUARY, 3);
		Calendar r = Calendar.getInstance();
		r.clear();
		r.set(2013, Calendar.OCTOBER, 29);
		ArrayList<Float> f = new ArrayList<Float>();
		f.add(1f);
		f.add(24f);
		ArrayList<Float> p = new ArrayList<Float>();
		p.add(11f);
		p.add(27f);
		
		Signature sig = new Signature(r, f, p);
		HumanPlayer nuova = new HumanPlayer("Pippo", "Pippozzo", c, sig);

        assertEquals(idTest + " riuscito", HumanPlayer.getHumanPlayer("Pippo", "Pippozzo", c), nuova);
	}
		
	
	/**
	 * Test add signature.
	 */
	@Test 
	public void testAddSignature(){
		final String idTest = "Test di inserimento nuova signature nel Database";
		
		ArrayList<Float> pos = new ArrayList<Float>();
		pos.add(0f);
		pos.add(128f);
		
		ArrayList<Float> vel = new ArrayList<Float>();
		vel.add(0f);
		vel.add(127f);
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2014, Calendar.NOVEMBER, 2);

		Signature s = new Signature (cal, pos, vel);
		oracle.addSignature(s);
		
		assertEquals(idTest + " riuscito", Signature.getSignature(17), s);
	}	
	
	/**
	 * Test retrieve player list.
	 */
	@Test
	public void testRetrievePlayerList(){
		final String idTest = "Test di retrieveList dei giocatori";
		
		Calendar[] cal = new Calendar[6];
		
		for(int i = 0; i < 6; i++){
			cal[i] = Calendar.getInstance();
			cal[i].clear();
		}
		cal[0].set(1991, Calendar.OCTOBER, 31);
		cal[1].set(1990, Calendar.AUGUST, 20);
		cal[2].set(1980, Calendar.FEBRUARY, 10);
		cal[3].set(2000, Calendar.JUNE, 6);
		cal[4].set(1985, Calendar.DECEMBER, 25);
		cal[5].set(1900, Calendar.FEBRUARY, 3);
		
		ArrayList<HumanPlayer> list = new ArrayList<HumanPlayer>(HumanPlayer.retrievePlayerList());
		
		HumanPlayer [] player = new HumanPlayer[]{
				HumanPlayer.getHumanPlayer("Maria", "Lombardi", cal[0]),
				HumanPlayer.getHumanPlayer("Francesco", "Alderisio", cal[1]),
				HumanPlayer.getHumanPlayer("Domenico", "Rossi", cal[2]),
				HumanPlayer.getHumanPlayer("Antonella", "Prisco", cal[3]),
				HumanPlayer.getHumanPlayer("Massimo", "Del Giudice", cal[4]),
				HumanPlayer.getHumanPlayer("Pippo", "Pippozzo", cal[5])
		};
		
		assertEquals(idTest + " riuscito", 6 , list.size());
		
		for (HumanPlayer p : player){
			assertEquals(idTest + " riuscito", true , list.contains(p));
		}
	}
}
