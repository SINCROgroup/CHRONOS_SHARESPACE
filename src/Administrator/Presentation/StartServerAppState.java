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
public class StartServerAppState extends AbstractAppState implements ScreenController{

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
    public StartServerAppState(SimpleApplication app){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();
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
        this.connection_server = this.screen.findNiftyControl("connection_server", DropDown.class);
        this.connection_server.addItem("local host");
        this.connection_server.addItem("192.168.0.3");
        this.connection_server.addItem("set");
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
        String connection  = connection_server.getSelection();
        de.lessvoid.nifty.elements.Element panel = this.screen.findElementByName("panel_IP");
        if (Objects.equals(connection, "set")){
            panel.show();
        }else{
            panel.hide();
        }
    }


    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        super.cleanup();
    }

    public void Set(){

        switch (connection_server.getSelection()){
            case "local host":
                Setting_general.getSettings().setIPaddress("localhost");
                break;
            case"192.168.0.3":
                Setting_general.getSettings().setIPaddress("192.168.0.3");
                break;
            case "set":
                TextField textFieldIP1 = screen.findNiftyControl("IP1", TextField.class);
                TextField textFieldIP2 = screen.findNiftyControl("IP2", TextField.class);
                TextField textFieldIP3 = screen.findNiftyControl("IP3", TextField.class);
                TextField textFieldIP4 = screen.findNiftyControl("IP4", TextField.class);
                Setting_general.getSettings().setIPaddress(textFieldIP1.getRealText() + "." + textFieldIP2.getRealText()
                        + "." + textFieldIP3.getRealText() + "." + textFieldIP4.getRealText());
                Settings.getSettings().setIPaddress(Setting_general.getSettings().getIPaddress());
                break;
        }
        this.server.startServer(Settings.getSettings().getMaxNum()*2);
        try {
            this.admin = new AdminRMI();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection with Server not established");
            quit();
        }
        cleanup();
        app.getStateManager().detach(this);
        StartScreenAppState startController = new StartScreenAppState(app);
        nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
        app.getStateManager().attach(startController);
        nifty.gotoScreen("startAdmin");
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
