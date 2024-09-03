/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HumanPlayer.Presentation;

import Administrator.Algorithm.TypeTest;

import HumanPlayer.Algorithm.Settings;
import HumanPlayer.Algorithm.TypeInput;
import Server.Control.State;
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
import de.lessvoid.nifty.elements.Element;
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
public class AcquireNetworkSet extends AbstractAppState implements ScreenController {

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
    private int index;
    private boolean set=false;
    private boolean isGroup=false;
    private de.lessvoid.nifty.elements.Element panel1;
    private de.lessvoid.nifty.elements.Element panel2;
    private de.lessvoid.nifty.elements.Element panel3;
    private de.lessvoid.nifty.elements.Element panel4;
    private de.lessvoid.nifty.elements.Element panel5;
    private DropDown<String> input_group;
    private HumanPlayerRMI human;
    private boolean setted=false;
    /**
     * Instantiates a new start screen player app state.
     *
     * @param app the app
     */
    //costruttore
    public AcquireNetworkSet(SimpleApplication app) {
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
        this.panel1 = this.screen.findElementByName("panel_setting");
        this.panel3 = this.screen.findElementByName("panel_1");
        this.panel4 = this.screen.findElementByName("panel_2");
        this.panel2 = this.screen.findElementByName("panel_loading");
        this.panel5 = this.screen.findElementByName("panel_bottom");
        this.panel1.show();
        this.panel2.hide();
        this.panel3.hide();
        this.panel4.hide();
        this.panel5.hide();
        boolean net=true;
        while (net){
            try {
                this.human = new HumanPlayerRMI(1);
                net=false;
            } catch (Exception e) {
                net=true;
            }
        }


        System.out.println(this.index);
    }

    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#update(float)
     */
    @Override
    public void update(float tpf) {
        try {
            if (human.getIsSet()) {
                isGroup=human.getIsGroup();
                if(!isGroup) {
                    if (Settings.getSettings().getPlayer().getNick() == "") {
                        this.panel1.hide();
                        this.panel4.hide();
                        this.panel2.show();
                        this.panel3.show();
                        this.panel5.show();
                    }else{

                        this.panel1.show();
                        this.panel2.hide();
                        this.panel3.hide();
                        this.panel4.hide();
                        this.panel5.hide();
                        Set();
                    }
                }else{
                    if(!setted){
                        this.input_group = this.screen.findNiftyControl("input_nickname_1", DropDown.class);
                        Player[] list=this.human.getPlayer();
                        for(int i=0;i<7;i++){
                            if(list[i]!=null){
                                this.input_group.addItem(list[i].getNick());
                            }
                        }
                        setted=true;
                    }
                    if (Settings.getSettings().getPlayer().getNick() == "") {
                        this.panel1.hide();
                        this.panel2.show();
                        this.panel4.show();
                        this.panel5.show();
                        this.panel3.hide();
                    }else{
                        this.panel1.show();
                        this.panel2.hide();
                        this.panel3.hide();
                        this.panel4.hide();
                        this.panel5.hide();
                        Set();
                    }
                }
            }
            if (human.getIsSet()&&set) {
                this.panel1.hide();
                this.panel4.hide();
                this.panel2.show();
                this.panel3.show();
                this.panel5.show();
                Nex();
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public void Set(){
        TextField textNick = screen.findNiftyControl("input_nickname", TextField.class);
        try {
            if (!isGroup) {
                if (Settings.getSettings().getPlayer().getNick() == "") {
                    Player player=new Player(textNick.getRealText(),Settings.getSettings().getPlayer());
                    Settings.getSettings().setPlayer(player);
                    System.out.println(player.getNick());
                    this.index= human.setNickName(player.getNick());
                }else{
                    this.index= human.setNickName(Settings.getSettings().getPlayer().getNick());
                }
                this.human = new HumanPlayerRMI(this.index);
                this.human.setPlayer(Settings.getSettings().getPlayer(),this.index);
                this.human.setConnection(this.index, true);

                /*if (Settings.getSettings().getID() == 0) {
                    for (int i = 1; i < 8; i++) {
                        this.human = new HumanPlayerRMI(i);
                        boolean isConnected = this.human.retriveConnection(i);
                        if (!isConnected) {
                            this.index = i;
                            human.setConnection(this.index, true);
                            i = 8;
                        }
                    }
                } else {
                    this.human = new HumanPlayerRMI(Settings.getSettings().getID());
                    this.index = Settings.getSettings().getID();
                }*/
            }else{
                if (Settings.getSettings().getPlayer().getNick() == "") {
                    String name=this.input_group.getSelection();
                    Player[] list=this.human.getPlayer();
                    for(int i=0;i<7;i++){
                        if(list[i]!=null){
                            if(Objects.equals(list[i].getNick(), name)){
                                Settings.getSettings().setPlayer(list[i]);
                                System.out.println(list[i].getNick());
                            }
                        }
                    }
                    this.index= human.setNickName(name);
                }else{
                    this.index= human.setNickName(Settings.getSettings().getPlayer().getNick());
                }
                    this.human = new HumanPlayerRMI(this.index);
                    this.human.setPlayer(Settings.getSettings().getPlayer(), this.index);
                    this.human.setConnection(this.index, true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.set=true;
    }
    /* (non-Javadoc)
     * @see com.jme3.app.state.AbstractAppState#cleanup()
     */
    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        try {
            human.setConnection(this.index, false);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        super.cleanup();
    }
    public void Nex() {
        try {
            Settings.getSettings().setID(this.index);
            System.out.println("you are the player" + this.index);
            TypeTest test = human.retriveTest();
            System.out.println(test);
            if (test == TypeTest.GROUP_HUMANS || test == TypeTest.GROUP_WITH_VA) {
                System.out.println(human.retriveInput().toString());
                if (human.retriveInput().toString() == TypeInput.MOUSE.toString()) {
                    Settings.getSettings().setInputChoice(TypeInput.MOUSE);
                } else {
                    Settings.getSettings().setInputChoice(TypeInput.LEAP_MOTION);
                }
                System.out.println("You are player number " + Integer.toString(this.index) + " Wait for all players...");
                cleanup();
                app.getStateManager().detach(this);
                WaitScreenPlayerAppState playController = new WaitScreenPlayerAppState(app, this.index, "network", human);
                nifty.fromXml("Interface/loading.xml", "loading", playController);
                app.getStateManager().attach(playController);
                nifty.gotoScreen("loading");
                // NetworkScreenPlayerAppState chooseNetworkController = new NetworkScreenPlayerAppState(app);
                // nifty.fromXml("Interface/network.xml", "network", chooseNetworkController);
                // app.getStateManager().attach(chooseNetworkController);
                // nifty.gotoScreen(nextScreen);
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server connection not established. I cannot get the settings for the trial.");
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
