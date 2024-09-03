/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrator.Presentation;

import Administrator.Algorithm.*;
import Server.RMIInterface.SignaturePlayer;
import Server.RMIInterface.Player;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import Server.Control.ServerStartup;
/**
 * Controller for the Interface to choose the type of game
 * solo, dyadic or network
 * set also the network ip
 *
 * @author marialombardi
 */
@SuppressWarnings("unused")
public class StartExeTestAppState extends AbstractAppState implements ScreenController{

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
    private DropDown<String> connection_server;
    private DropDown<String> input;
    private DropDown<String> group_name;
    private DropDown<String> input_test;
    private ServerStartup server=new ServerStartup();
    private AdminRMI admin;
    private ArrayList<Player> listPlayer;

    private DropDown<String> signature;
    //costruttore
    public StartExeTestAppState(SimpleApplication app){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();
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
    public void quit(){

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
        this.group_name = this.screen.findNiftyControl("input_group", DropDown.class);
        aggiungiNomiFile(this.group_name,"Groups");
        this.group_name.addItem("set");
        this.input = this.screen.findNiftyControl("input_input", DropDown.class);
        this.input.addItem(TypeInput.LEAP_MOTION.toString());
        this.input.addItem(TypeInput.MOUSE.toString());

        this.input_test = this.screen.findNiftyControl("input_test", DropDown.class);
        aggiungiNomiFile(this.input_test,"Test_settings");
        this.input_test.addItem("Add new signature");

        this.app = (SimpleApplication)app;
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

        String group  = group_name.getSelection();
        de.lessvoid.nifty.elements.Element panel_name = this.screen.findElementByName("name");
        if (Objects.equals(group, "set")){
            panel_name.show();
        }else{
            panel_name.hide();
        }
    }


    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        super.cleanup();
    }

    public void Set(String nextScreen){
        TextField textName = screen.findNiftyControl("name", TextField.class);
        String name=textName.getRealText();
        if(this.group_name.getSelection()=="set"){
            new File(Paths.get("Groups").toString()+"\\"+name).mkdirs();
            Setting_general.getSettings().setGroup_name(name);
        }else {
            Setting_general.getSettings().setGroup_name(this.group_name.getSelection());
        }
        if (input.getSelection() == TypeInput.MOUSE.toString()) {
            Setting_general.getSettings().setInput(TypeInput.MOUSE);
        } else {
            Settings.getSettings().setInput(TypeInput.LEAP_MOTION);
        }
        try {
            this.admin = new AdminRMI();
            admin.setGroup(Setting_general.getSettings().getGroup_name());
            admin.setInput(Settings.getSettings().getInput());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection with Server not established");
            quit();
        }
        cleanup();
        app.getStateManager().detach(this);
        SelectTestInterface startController = new SelectTestInterface(app,this.admin);
        nifty.fromXml("Interface/SetTestInterface2.xml", "setTest", startController);
        app.getStateManager().attach(startController);
        nifty.gotoScreen("setTest");
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


}
