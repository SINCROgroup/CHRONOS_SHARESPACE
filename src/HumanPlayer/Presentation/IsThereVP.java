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
import java.util.ArrayList;
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
public class IsThereVP extends AbstractAppState implements ScreenController {

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
    private HumanPlayerRMI human;
    private int index;
    private ArrayList<Integer> randomPosition;

    /**
     * Instantiates a new start screen player app state.
     *
     * @param app the app
     */
    //costruttore
    public IsThereVP(SimpleApplication app,int indexPlayer,ArrayList<Integer> randomPosition1) {
        this.randomPosition=randomPosition1;
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.index=indexPlayer;
        try {
            //mettere l'indice del giocatore
            this.human = new HumanPlayerRMI(indexPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#update(float)
     */
    @Override
    public void update(float tpf) {
    }

    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#cleanup()
     */
    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        try {
            human.setConnection(Settings.getSettings().getID(), false);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        super.cleanup();
    }
    public void set() throws RemoteException {
        TextField textVP = screen.findNiftyControl("input_VP", TextField.class);
        textVP.getRealText();
        this.index=(this.index-1)%7;
        if(Integer.parseInt(textVP.getRealText())==8) {
            human.setAnswer(this.index, String.valueOf(8));
        } else if(Integer.parseInt(textVP.getRealText())!=0 && Integer.parseInt(textVP.getRealText())<=this.randomPosition.size()) {
            human.setAnswer(this.index, String.valueOf(this.randomPosition.get(Integer.parseInt(textVP.getRealText())-1)+1));

        }else{
            human.setAnswer(this.index, "0");
        }
        try {
            cleanup();
            app.getStateManager().detach(this);
            int index = human.setNickName(Settings.getSettings().getPlayer().getNick());
            human.setConnection(index, true);
            AcquireNetworkSet chooseInputIndex = new AcquireNetworkSet(app);
            nifty.fromXml("Interface/setting.xml", "setting", chooseInputIndex);
            app.getStateManager().attach(chooseInputIndex);
            nifty.gotoScreen("setting");
        }catch (RemoteException e) {
            e.printStackTrace();
        }
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