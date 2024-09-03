package Administrator.Algorithm;

import Administrator.Algorithm.Setting_general;
import Server.Entity.Player;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;


@SuppressWarnings("unused")
public class Setting_general implements Serializable {

    //parametri del HKB
    private String IPaddress;
    private TypeInput input;
    private String Group_name;
    private String Name_Test;
    private int trial_did;
    private static Setting_general settings = null;
    private Player[] player;
    private Setting_general(){
    }


    public static Setting_general getSettings(){

        if(settings == null){
            settings = new Setting_general();
            settings.input = TypeInput.MOUSE;
            settings.Group_name="";
            settings.IPaddress="localhost";
            settings.Name_Test="";
            settings.trial_did=0;
            settings.player=new Player[7];
        }
        return settings;
    }



    public void setIPaddress(String IP){this.IPaddress=IP;}
    public String getIPaddress(){return this.IPaddress;}

    public void setName_Test(String name){this.Name_Test=name;}
    public String getName_Test(){return this.Name_Test;}
    public void setTrial_did(int index){this.trial_did=index;}
    public int getTrial_did(){return this.trial_did;}
    public void setGroup_name(String Name) {
        this.Group_name=Name;
    }

    public String getGroup_name() {return Group_name;}

    public void setInput(TypeInput t){this.input=t;}

    public TypeInput getInput(){return this.input;}

    public Player[] getPlayers(){return this.player;}

    public void setPlayers(Player[] p){this.player=p;}

}

