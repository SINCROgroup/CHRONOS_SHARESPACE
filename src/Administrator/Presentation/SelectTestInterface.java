/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrator.Presentation;

import Administrator.Algorithm.*;
import Server.Control.State;
import Server.Entity.Player;
import Server.RMIInterface.SignaturePlayer;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import Server.Control.ServerStartup;
import org.lwjgl.Sys;

/**
 * Controller for the Interface to choose the type of game
 * solo, dyadic or network
 * set also the network ip
 *
 * @author marialombardi
 */
@SuppressWarnings("unused")
public class SelectTestInterface extends AbstractAppState implements ScreenController {
    private boolean isFolder=false;
    private SimpleApplication app;
    private Nifty nifty;
    private Screen screen;
    private ViewPort viewPort;
    private Camera camera;
    private Node rootNode;
    private Node guiNode;
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");

    private InputManager inputManager;
    private AssetManager assetManager;
    private DropDown<String> input_test;
    private AdminRMI admin;
    private Player[] listPlayer;


    //costruttore
    public SelectTestInterface(SimpleApplication app,AdminRMI admin) {
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.admin=admin;
    }

    private void aggiungiNomiFile(DropDown<String> list, String folder) {
        // Percorso della directory
        String directoryPath = (Paths.get(folder)).toString();

        // Ottieni l'elenco dei file nella directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        // Verifica se la directory esiste e contiene file
        if (files != null) {
            // Lista per memorizzare i nomi dei file
            List<String> nomiFile = new ArrayList<>();

            // Itera attraverso i file e aggiungi i nomi alla lista
            for (File file : files) {
                nomiFile.add(file.getName());
            }

            // Aggiungi i nomi dei file a this.input_test
            for (String nomeFile : nomiFile) {
                list.addItem(nomeFile);
            }
        } else {
            System.out.println("La directory non esiste o non contiene file.");
        }

    }

    public void quit() {

        cleanup();
        app.getStateManager().detach(this);
        StartScreenAppState startController = new StartScreenAppState(app);
        nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
        app.getStateManager().attach(startController);
        nifty.gotoScreen("startAdmin");
    }

    //init the screen
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        this.input_test = this.screen.findNiftyControl("test", DropDown.class);
        aggiungiNomiFile(this.input_test, "Test_settings");

        this.app = (SimpleApplication) app;
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        camera = app.getCamera();
        inputManager = app.getInputManager();
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);

            }

    // no update, it is only a static interface
    @Override
    public void update(float tpf) {
        Label l = this.screen.findNiftyControl("trial_title2", Label.class);
        Label l1 = this.screen.findNiftyControl("trial_title3", Label.class);
        Label l2 = this.screen.findNiftyControl("trial_title4", Label.class);
        Element panel3 = this.screen.findElementByName("playButton2");
        Element panel = this.screen.findElementByName("panel_trial");
        Element panel2 = this.screen.findElementByName("panel_test2");
        String folderPath = ".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + input_test.getSelection();
        int trial;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            trial=0;
        } else {
            // Se la cartella esiste, conta il numero di cartelle contenute
            File[] subfolders = folder.listFiles(File::isDirectory);
            if (subfolders != null) {
                trial = subfolders.length;
            } else {
                trial = 0;
            }
        }
        l.setText(Integer.toString(trial));
        if(trial<1){
            l1.setText("trial of the test ");
        }
        String[] part = input_test.getSelection().split("_");
        l2.setText(part[0]);
    }


    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        super.cleanup();
    }
    public void Continue() throws RemoteException {
        this.admin.resetAnswer();
                Path outputPath = Paths.get("Test_settings").toAbsolutePath();
                        try {
                            if (admin.settingNetwork(Settings.getSettings().getTime(), Settings.getSettings().getNumPlayers(), Settings.getSettings().getNumVP(), Settings.getSettings().getNet())) {
                                System.out.println("Network was set.");

                            } else {
                                System.out.println("Error with settings.");
                            }
                            boolean result = admin.settingSocialMemory(Settings.getSettings().getSocialMemory());
                            if (result) {
                                cleanup();
                                app.getStateManager().detach(this);
                                WaitScreenAppState waitController = new WaitScreenAppState(app);
                                nifty.fromXml("Interface/loadingAdmin.xml", "loading", waitController);
                                app.getStateManager().attach(waitController);
                                nifty.gotoScreen("loading");
                            } else {
                                System.out.println("Error to set social memory option on the Server.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Connection with Server not established.");
                            quit();
                        }
        try {
            String folderPath = Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name();
            File file = new File(folderPath, "setting_group");
            if (file.exists()) {
                int ind=0;
                listPlayer=loadPlayers(Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name()+"\\setting_group");
                Setting_general.getSettings().setPlayers(listPlayer);

                for(int i=0;i<listPlayer.length;i++){

                    if(listPlayer[i]!=null) {
                        System.out.println(listPlayer[i].getNick()+" "+listPlayer[i].getGender()+" "+listPlayer[i].getNation()+" "+listPlayer[i].getOccupation()+" "+listPlayer[i].getDateBirth().getWeekYear()+" "+listPlayer[i].getAlready());
                        admin.setPlayer(listPlayer[i], i + 1);
                        ind=i;
                    }else{
                        i=listPlayer.length;
                    }
                }

                if(ind+1>=Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a()) {
                    admin.setIsGroup(true);
                }else{
                    admin.setIsGroup(false);
                }
            }else{
                admin.setIsGroup(false);
            }
            admin.setIsSet(true);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection with Server not established.");
            quit();
        }
    }
    public void Set() throws RemoteException {
        Setting_general.getSettings().setName_Test(input_test.getSelection());
        File directory = new File(Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name()+ "\\" + Setting_general.getSettings().getName_Test());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if(!Objects.equals(input_test.getSelection(), "Add new signature")) {
            Settings.load(Paths.get("Test_settings").toString(), input_test.getSelection());
        }
        Settings.getSettings().setIPaddress(Setting_general.getSettings().getIPaddress());
        int num=Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a();
        String searchString = "solo_L0"+num;
        String directoryPath = ".//Groups//" + Setting_general.getSettings().getGroup_name();
        this.isFolder = checkFolderStartingWithString(directoryPath, searchString);
        try {
            this.admin.resetLIndex();
            for(int i=0;i<Settings.getSettings().getNumPlayers();i++){
                if(Settings.getSettings().getL2_algorithm(i+1)!=null)
                    this.admin.setL2Index(i+1,true);
                if(Settings.getSettings().getL3_a_algorithm(i+1)!=null)
                    this.admin.setL3_aIndex(i+1,true);
                if(Settings.getSettings().getL3_s_algorithm(i+1)!=null) {
                    this.admin.setL3_rIndex(i+1, true);
                    System.out.println(i+1);
                    if(Settings.getSettings().getL3_r_role(i+1)!="ID"){
                        File directory_solo = new File(Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name());
                        String nameFile=new String();
                        boolean found = false;

                        if (directory_solo.exists() && directory_solo.isDirectory()) {
                            File[] files = directory_solo.listFiles();
                            for (File file : files) {
                                if (file.isDirectory() && (file.getName().startsWith("solo")||file.getName().startsWith("Solo"))) {
                                    nameFile=file.getName();
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if(found){
                            try {
                                String directory1 = Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name()+"\\"+nameFile+ "\\";
                                String fileName1 = "Mean_phase.txt";
                                String filePath = directory1+fileName1;
                                BufferedReader br = new BufferedReader(new FileReader(filePath));
                                String line = null;
                                List<List<Double>> columns = new ArrayList<>();
                                String firstLine = br.readLine();
                                if (firstLine != null) {
                                    String[] values = firstLine.split(";");

                                    for (int n = 0; n < values.length; n++) {
                                        columns.add(new ArrayList<>());
                                    }
                                    for (int l = 0; l < values.length; l++) {
                                        columns.get(l).add(Double.parseDouble(values[l]));
                                    }
                                    while ((line = br.readLine()) != null) {
                                        values = line.split(";");
                                        for (int l = 0; l < values.length; l++) {
                                            columns.get(l).add(Double.parseDouble(values[l]));
                                        }
                                    }
                                }
                                br.close();
                                List<Double> w_n=new ArrayList<>();
                                double mean= 0;
                                // Calcoliamo le medie delle colonne
                                for (int a = 0; a < columns.size(); a++) {
                                    List<Double> column = columns.get(a);
                                    double sum = 0;
                                    for (Double value : column) {
                                        sum += value;
                                    }
                                    double average = sum / column.size();
                                    w_n.add(average);
                                }
                                for(int a = 0; a < w_n.size(); a++) {
                                    double sum = 0;
                                    for (Double value : w_n) {
                                        sum += value;
                                    }
                                    mean = sum / w_n.size();
                                }
                                int index = 1;
                                if(Settings.getSettings().getL3_r_role(i + 1).toString().equals("NEAR_MEAN")){
                                    double differenzaMinima = Math.abs(w_n.get(0) - mean);
                                    for (int v = 1; v < w_n.size(); v++) {
                                        double differenzaAttuale = Math.abs(w_n.get(v) - mean);
                                        if (differenzaAttuale < differenzaMinima) {
                                            differenzaMinima = differenzaAttuale;
                                            index = v+1;
                                        }
                                    }
                                }
                                if(Settings.getSettings().getL3_r_role(i + 1).toString().equals("FAR_MEAN")){
                                    double differenzaMax = Math.abs(w_n.get(0) - mean);
                                    for (int v = 1; v < w_n.size(); v++) {
                                        double differenzaAttuale = Math.abs(w_n.get(v) - mean);
                                        if (differenzaAttuale > differenzaMax) {
                                            differenzaMax = differenzaAttuale;
                                            index = v+1;
                                        }
                                    }
                                }
                                if(index!=i+1) {
                                    Settings.getSettings().setL3_s_inner_d(Settings.getSettings().getL3_s_inner_d(i + 1), index);
                                    Settings.getSettings().setL3_r_role(Settings.getSettings().getL3_r_role(i + 1), index);
                                    Settings.getSettings().setL3_s_algorithm(Settings.getSettings().getL3_s_algorithm(i + 1), index);
                                    for(int k=0;k<1;k++){
                                        Settings.getSettings().setParameter(index,k,Settings.getSettings().getParameter(i+1)[k]);
                                    }
                                    Settings.getSettings().setL3_s_inner_d(null, i + 1);
                                    Settings.getSettings().setL3_r_role(null, i + 1);
                                    Settings.getSettings().setL3_s_algorithm(null, i + 1);
                                    for(int k=0;k<1;k++){
                                        Settings.getSettings().setParameter(i+1,k,0);
                                    }
                                    this.admin.setL3_rIndex(index, true);
                                    this.admin.setL3_rIndex(i+1, false);
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            this.admin.setIsQuestion(Settings.getSettings().getAsk());
            this.admin.setTest(Settings.getSettings().getTest());

        } catch (Exception e){
            quit();
        }
        Continue();

    }


    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }


    @Override
    public void onEndScreen() {
    }


    @Override
    public void onStartScreen() {
    }


    // Metodo per caricare il vettore di giocatori da un file
    public static Player[] loadPlayers(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Player[]) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean checkFolderStartingWithString(String directoryPath, String searchString) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Directory not valid: " + directoryPath);
            return false;
        }
        String[] filesAndFolders = directory.list();

        for (String fileName : filesAndFolders) {
            File file = new File(directoryPath + File.separator + fileName);
            if (file.isDirectory() && fileName.startsWith(searchString)) {
                return true;
            }
        }
        return false;
    }
}