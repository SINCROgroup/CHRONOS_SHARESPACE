/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import java.util.ArrayList;
import java.util.Calendar;

import Server.Entity.CommonClass;


public class PopulateTestDatabase {


    public static void populate() throws Exception{
    	// insert some human players
    	CommonClass g = new CommonClass();
    	
        HumanPlayerDAO[] hp = new HumanPlayerDAO[5];
        SignatureDAO[] s = new SignatureDAO[10];
        Calendar[] cal = new Calendar[5];
        Calendar[] rec = new Calendar[10];
    
        hp[0] = new HumanPlayerDAO();
        s[0] = new SignatureDAO();
        hp[0].setName("Maria");
        hp[0].setSurname("Lombardi");
        cal[0] = Calendar.getInstance();
        rec[0] = Calendar.getInstance();
        cal[0].clear();
        rec[0].clear();
        cal[0].set(1991, Calendar.OCTOBER, 31);
        rec[0].set(2014, Calendar.NOVEMBER, 15);
        hp[0].setDateBirth(cal[0]);
        hp[0].setId(g.generateIdPlayer("Maria", "Lombardi", cal[0]));
        ArrayList<Float> arrayPos = new ArrayList<Float>();
        arrayPos.add(3f);
        arrayPos.add(15f);
        arrayPos.add(98f);
        ArrayList<Float> arrayVel = new ArrayList<Float>();
        arrayVel.add(12f);
        arrayVel.add(2f);
        arrayVel.add(89f);
        s[0].setDateRecord(rec[0]);
        s[0].setTimeSeriesPosVel(arrayPos, arrayVel);
        hp[0].setOwnedSignatures(s[0]);    
        
        hp[0].save();
           
        hp[1] = new HumanPlayerDAO();
        s[1] = new SignatureDAO();
        hp[1].setName("Francesco");
        hp[1].setSurname("Alderisio");
        cal[1] = Calendar.getInstance();
        rec[1] = Calendar.getInstance();
        cal[1].clear();
        rec[1].clear();
        cal[1].set(1990, Calendar.AUGUST, 20);
        rec[1].set(2015, Calendar.OCTOBER, 10);
        hp[1].setDateBirth(cal[1]);
        hp[1].setId(g.generateIdPlayer("Francesco", "Alderisio", cal[1]));
        arrayPos.set(0, 1f);
        arrayPos.set(1, 2f);
        arrayPos.set(2, 3f);
        arrayVel.set(0, 34f);
        arrayVel.add(1, 2f);
        arrayVel.add(2, 67f);
        s[1].setDateRecord(rec[1]);
        s[1].setTimeSeriesPosVel(arrayPos, arrayVel);
        hp[1].setOwnedSignatures(s[1]);
        
        hp[1].save();
        
        hp[2] = new HumanPlayerDAO();
        s[2] = new SignatureDAO();
        hp[2].setName("Domenico");
        hp[2].setSurname("Rossi");
        cal[2] = Calendar.getInstance();
        rec[2] = Calendar.getInstance();
        cal[2].clear();
        rec[2].clear();
        cal[2].set(1980, Calendar.FEBRUARY, 10);
        rec[2].set(2015, Calendar.OCTOBER, 29);
        hp[2].setDateBirth(cal[2]);
        hp[2].setId(g.generateIdPlayer("Domenico", "Rossi", cal[2]));
        arrayPos.set(0, 18f);
        arrayPos.set(1, 3f);
        arrayPos.set(2, 77f);
        arrayVel.set(0, 3f);
        arrayVel.add(1, 22f);
        arrayVel.add(2, 27f);
        s[2].setDateRecord(rec[2]);
        s[2].setTimeSeriesPosVel(arrayPos, arrayVel);
        hp[2].setOwnedSignatures(s[2]);

        hp[2].save();
        
        hp[3] = new HumanPlayerDAO();
        s[3] = new SignatureDAO();
        hp[3].setName("Antonella");
        hp[3].setSurname("Prisco");
        cal[3] = Calendar.getInstance();
        rec[3] = Calendar.getInstance();
        cal[3].clear();
        rec[3].clear();
        cal[3].set(2000, Calendar.JUNE, 6);
        rec[3].set(2014, Calendar.DECEMBER, 7);
        hp[3].setDateBirth(cal[3]);
        hp[3].setId(g.generateIdPlayer("Antonella", "Prisco", cal[3]));
        arrayPos.set(0, 10f);
        arrayPos.set(1, 25f);
        arrayPos.set(2, 80f);
        arrayVel.set(0, 1f);
        arrayVel.add(1, 7f);
        arrayVel.add(2, 78f);
        s[3].setDateRecord(rec[3]);
        s[3].setTimeSeriesPosVel(arrayPos, arrayVel);
        hp[3].setOwnedSignatures(s[3]);
        
        hp[3].save();
        
        hp[4] = new HumanPlayerDAO();
        s[4] = new SignatureDAO();
        hp[4].setName("Massimo");
        hp[4].setSurname("Del Giudice");
        cal[4] = Calendar.getInstance();
        rec[4] = Calendar.getInstance();
        cal[4].clear();
        rec[4].clear();
        cal[4].set(1985, Calendar.DECEMBER, 25);
        rec[4].set(2015, Calendar.SEPTEMBER, 11);
        hp[4].setDateBirth(cal[4]);
        hp[4].setId(g.generateIdPlayer("Massimo", "Del Giudice", cal[4]));
        arrayPos.set(0, 1f);
        arrayPos.set(1, 60f);
        arrayPos.set(2, 58f);
        arrayVel.set(0, 28f);
        arrayVel.add(1, 67f);
        arrayVel.add(2, 36f);
        s[4].setDateRecord(rec[4]);
        s[4].setTimeSeriesPosVel(arrayPos, arrayVel);
        hp[4].setOwnedSignatures(s[4]);
        
        hp[4].save();
        
    
    //insert some signatures for each human players
        
        s[5] = new SignatureDAO();
        rec[5] = Calendar.getInstance();
        rec[5].clear();
        rec[5].set(2015, Calendar.AUGUST, 2);
        s[5].setDateRecord(rec[5]);
        arrayPos.set(0, 23f);
        arrayPos.set(1, 48f);
        arrayPos.set(2, 26f);
        arrayVel.set(0, 28f);
        arrayVel.add(1, 54f);
        arrayVel.add(2, 89f);
        s[5].setTimeSeriesPosVel(arrayPos, arrayVel);
        
        s[5].save();
        
        s[6] = new SignatureDAO();
        rec[6] = Calendar.getInstance();
        rec[6].clear();
        rec[6].set(2015, Calendar.AUGUST, 18);
        s[6].setDateRecord(rec[6]);
        arrayPos.set(0, 37f);
        arrayPos.set(1, 72f);
        arrayPos.set(2, 26f);
        arrayVel.set(0, 28f);
        arrayVel.add(1, 54f);
        arrayVel.add(2, 89f);
        s[6].setTimeSeriesPosVel(arrayPos, arrayVel);
        
        s[6].save();
        
        s[7] = new SignatureDAO();
        rec[7] = Calendar.getInstance();
        rec[7].clear();
        rec[7].set(2015, Calendar.SEPTEMBER, 9);
        s[7].setDateRecord(rec[7]);
        arrayPos.set(0, 82f);
        arrayPos.set(1, 99f);
        arrayPos.set(2, 46f);
        arrayVel.set(0, 3f);
        arrayVel.add(1, 22f);
        arrayVel.add(2, 27f);
        s[7].setTimeSeriesPosVel(arrayPos, arrayVel);
        
        s[7].save();
        
        s[8] = new SignatureDAO();
        rec[8] = Calendar.getInstance();
        rec[8].clear();
        rec[8].set(2015, Calendar.OCTOBER, 14);
        s[8].setDateRecord(rec[8]);
        arrayPos.set(0, 38f);
        arrayPos.set(1, 56f);
        arrayPos.set(2, 22f);
        arrayVel.set(0, 34f);
        arrayVel.add(1, 2f);
        arrayVel.add(2, 67f);
        s[8].setTimeSeriesPosVel(arrayPos, arrayVel);
        
        s[8].save();
        
        s[9] = new SignatureDAO();
        rec[9] = Calendar.getInstance();
        rec[9].clear();
        rec[9].set(2015, Calendar.NOVEMBER, 18);
        s[9].setDateRecord(rec[9]);
        arrayPos.set(0, 39f);
        arrayPos.set(1, 65f);
        arrayPos.set(2, 78f);
        arrayVel.set(0, 7f);
        arrayVel.add(1, 56f);
        arrayVel.add(2, 77f);
        s[9].setTimeSeriesPosVel(arrayPos, arrayVel);
        
        s[9].save();
      
        hp[1].setOwnedSignatures(s[5]);
        hp[3].setOwnedSignatures(s[6]);       
        hp[4].setOwnedSignatures(s[7]);
        hp[0].setOwnedSignatures(s[8]);
        hp[0].setOwnedSignatures(s[9]);

        for (HumanPlayerDAO p : hp) p.update();  
        
    }
}
