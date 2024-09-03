/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Entity;

import Administrator.Algorithm.Settings;
import Server.DAO.SignatureDAO;
import Server.DAO.HumanPlayerDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

// TODO: Auto-generated Javadoc

/**
 * The Class HumanPlayer.
 *
 * @author marialombardi
 */
public class Player implements Serializable {



    private String nick;

    /** The surname. */

    /** The date birth. */
    private Calendar dateBirth;

    /** The signatures. */
    private String nation;
    private String occupation;

    private boolean already;
    private String gender;




    /**
     * Instantiates a new human player.
     */
    public Player(){
    }

    public Player(Calendar c,String nation,String occupation,boolean already,String gender){
        this.nick = "";
        this.dateBirth = c;
        this.nation=nation;
        this.occupation=occupation;
        this.already=already;
        this.gender=gender;
    }
    public Player(String nick,Player p){
        this.nick = nick;
        this.dateBirth = p.getDateBirth();
        this.nation=p.getNation();
        this.occupation=p.getOccupation();
        this.already=p.getAlready();
        this.gender=p.getGender();
    }



    public String getNick(){
        return this.nick;
    }

    public Calendar getDateBirth(){
        return this.dateBirth;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Player getplayer(){
        return this;
    }
    public String getGender(){
        return this.gender;
    }
    public String getOccupation(){
        return this.occupation;
    }
    public String getNation(){
        return this.nation;
    }
    public void setNick(String nick){
        this.nick = nick;
    }
    public boolean getAlready(){
        return this.already;
    }


    /**
     * Sets the date birth.
     *
     * @param date the new date birth
     */
    public void setDateBirth(Calendar date){
        this.dateBirth = date;
    }




    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        Player hp = (Player) obj;

        if ((this.nick.equals(hp.getNick())) &&
                (this.dateBirth.equals(hp.getDateBirth())) &&
                (this.nation.equals(hp.getNation()))&&
                (this.gender.equals(hp.getGender()))&&
                (this.occupation.equals(hp.getOccupation())))
            return true;
        else
            return false;

    }


}
