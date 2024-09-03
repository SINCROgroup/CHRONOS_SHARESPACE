/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HumanPlayer.Presentation;

import Administrator.Algorithm.TypeTest;

import HumanPlayer.Algorithm.Settings;
import HumanPlayer.Algorithm.TypeInput;
import Server.Entity.Player;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

// TODO: Auto-generated Javadoc
/**
 * The Class StartScreenPlayerAppState.
 *
 * @author marialombardi
 */
@SuppressWarnings("unused")
public class StartScreenNamePlayerAppState extends AbstractAppState implements ScreenController {

    /**
     * The app.
     */
    private MainPlayer app;

    /**
     * The nifty.
     */
    private Nifty nifty;

    /**
     * The screen.
     */
    private Screen screen;

    /**
     * The view port.
     */
    private ViewPort viewPort;

    /**
     * The camera.
     */
    private Camera camera;

    /**
     * The root node.
     */
    private Node rootNode;

    /**
     * The gui node.
     */
    private Node guiNode;

    /**
     * The asset manager.
     */
    private AssetManager assetManager;

    /**
     * The local root node.
     */
    private Node localRootNode = new Node("Start Screen RootNode");

    /**
     * The local gui node.
     */
    private Node localGuiNode = new Node("Start Screen GuiNode");

    /**
     * The input manager.
     */
    private InputManager inputManager;

    private Label l;

    private DropDown<String> connection_server;
    private DropDown<String> gender;
    private DropDown<String> already;
    private Player player;

    /**
     * Instantiates a new start screen player app state.
     *
     * @param app the app
     */
    //costruttore
    public StartScreenNamePlayerAppState(SimpleApplication app) {
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
    }

    /**
     * jME3 AppState methods.
     *
     * @param stateManager the state manager
     * @param app          the app
     */
    //init the screen
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (MainPlayer) app;
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        camera = app.getCamera();
        inputManager = app.getInputManager();
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        this.connection_server = this.screen.findNiftyControl("connection_server", DropDown.class);
        this.connection_server.addItem("192.168.0.3");
        this.connection_server.addItem("local host");
        this.connection_server.addItem("set");
        this.gender = this.screen.findNiftyControl("input_gender", DropDown.class);
        this.gender.addItem("Female");
        this.gender.addItem("Male");
        this.gender.addItem("None of the previous ones");
        this.already = this.screen.findNiftyControl("input_already", DropDown.class);
        this.already.addItem("No");
        this.already.addItem("Si");
    }

    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#update(float)
     */
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

    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#cleanup()
     */
    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        super.cleanup();
    }
    public void Setting() {
        switch (connection_server.getSelection()){
            case "local host":
                Settings.getSettings().setIPaddress("localhost");
                break;
            case"192.168.0.3":
                Settings.getSettings().setIPaddress("192.168.0.3");
                break;
            case "set":
                TextField textFieldIP1 = screen.findNiftyControl("IP1", TextField.class);
                TextField textFieldIP2 = screen.findNiftyControl("IP2", TextField.class);
                TextField textFieldIP3 = screen.findNiftyControl("IP3", TextField.class);
                TextField textFieldIP4 = screen.findNiftyControl("IP4", TextField.class);
                Settings.getSettings().setIPaddress(textFieldIP1.getRealText() + "." + textFieldIP2.getRealText()
                        + "." + textFieldIP3.getRealText() + "." + textFieldIP4.getRealText());
                break;
        }
        TextField textNation = screen.findNiftyControl("input_nationality", TextField.class);
        TextField textDay = screen.findNiftyControl("input_day", TextField.class);
        TextField textMonth = screen.findNiftyControl("input_month", TextField.class);
        TextField textYear = screen.findNiftyControl("input_year", TextField.class);
        TextField textOccupation = screen.findNiftyControl("input_occupation", TextField.class);
        Calendar dataBirth = Calendar.getInstance();
        dataBirth.set(Integer.parseInt(textYear.getRealText()),
                Integer.parseInt(textMonth.getRealText())-1,
                Integer.parseInt(textDay.getRealText()));
        boolean alr=false;
        if(already.getSelection()=="Si"){
            alr=true;
        }
        player=new Player(dataBirth,textNation.getRealText(),textOccupation.getRealText(),alr,gender.getSelection());
        Settings.getSettings().setPlayer(player);
        cleanup();
        app.getStateManager().detach(this);
        AcquireNetworkSet chooseInputIndex = new AcquireNetworkSet(app);
        nifty.fromXml("Interface/setting.xml", "setting", chooseInputIndex);
        app.getStateManager().attach(chooseInputIndex);
        nifty.gotoScreen("setting");
    }


    /**
     * Nifty GUI ScreenControl methods.
     *
     * @param nifty  the nifty
     * @param screen the screen
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /* (non-Javadoc)
     * @see de.lessvoid.nifty.screen.ScreenController#onEndScreen()
     */
    @Override
    public void onEndScreen() {
    }


    /* (non-Javadoc)
     * @see de.lessvoid.nifty.screen.ScreenController#onStartScreen()
     */
    @Override
    public void onStartScreen() {
    }
}
