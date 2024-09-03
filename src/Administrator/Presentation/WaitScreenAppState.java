/*
 * 
 */
package Administrator.Presentation;

import Administrator.Algorithm.Setting_general;
import Administrator.Algorithm.Settings;
import Administrator.Algorithm.TypeControl;
import Server.Control.State;
import Server.Entity.Player;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Set the connection with the server and send the settings for the current trial
 * wait for all players are connected
 */
@SuppressWarnings("unused")
public class WaitScreenAppState extends AbstractAppState implements ScreenController{
	
	private SimpleApplication app;
    private Nifty nifty;
    private Screen screen;   
	private ViewPort viewPort;
    private Camera camera;
    private Node rootNode;
    private Node guiNode;
      
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");
    
    private AssetManager assetManager;
    private InputManager inputManager;
    
    private int index[];

	private int index_a[];
	private int type[];
    private AdminRMI admin;

    private boolean result;
	private int numVP;
    

    //costruttore
    public WaitScreenAppState(SimpleApplication app){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
    }
    
    // initialization
    // instantiate AdminRMI and set connection with the server
	@Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication)app;
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        camera = app.getCamera();
        inputManager = app.getInputManager();
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        this.numVP=Settings.getSettings().getNumL2()+Settings.getSettings().getNumL3_a()+Settings.getSettings().getNumL3_r();
        try {
			admin = new AdminRMI();
			result = false;		
			this.index = new int[Settings.getSettings().getMaxNum()];
			this.index_a = new int[Settings.getSettings().getMaxNum()];
			this.type=new int[Settings.getSettings().getMaxNum()];
			if(this.numVP != 0){

				for(int i = 0; i < Settings.getSettings().getNumPlayers(); i++){
					if(Settings.getSettings().getL2_algorithm(i+1)!=null){
						this.index[i] = 1;
						this.type[i]=1;
					}
					if(Settings.getSettings().getL3_a_algorithm(i+1)!=null){
						this.index[i] = admin.retrieveNetwork(i+1);
						this.index_a[i] = admin.retrieveNetwork(i+1);
						this.type[i]=2;
					}
					if(Settings.getSettings().getL3_s_algorithm(i+1)!=null){
						this.index[i] = admin.retrieveNetwork(i+1);
						this.index_a[i] = admin.retrieveNetwork(i+1);
						this.type[i]=3;
					}
				}
			}else if(Settings.getSettings().getNumVP() == 0){
				//admin.resetNetwork();
				System.out.println("There are not Virtual Players in the network.");
			}else{
				System.out.print("Settings are not readable. Error with loading.");
			}
        }catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server connection not established.");
			quit();
		}
		System.out.println(this.screen.toString());
    }
	

    public void quit(){
    	cleanup();   	
    	app.getStateManager().detach(this);
        
        StartScreenAppState startController = new StartScreenAppState(app);
        nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController); 
        app.getStateManager().attach(startController);
        nifty.gotoScreen("startAdmin");
    }
    

    @Override
    public void update(float tpf) {
		de.lessvoid.nifty.elements.Element panel1 = this.screen.findElementByName("panel_1");
		de.lessvoid.nifty.elements.Element panel2 = this.screen.findElementByName("panel_2");
		de.lessvoid.nifty.elements.Element panel3 = this.screen.findElementByName("playButton");
    	if(!result){
			panel2.hide();
			panel3.hide();
			panel1.show();
			try {
				if(this.numVP != 0){
					result = admin.synchronization(this.index);
				}else if(this.numVP ==0){
					result = admin.synchronization(null);
				}else{
		    		System.out.println("Error with synchronization in waitscreen");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
									
    	}else{
			panel2.show();
			panel3.show();
			panel1.hide();
    	}
    }


	public void play(){
		Player[] listPlayer = new Player[Settings.getSettings().getNumPlayers()];
		try {
			if(admin.getIsGroup()==false) {
				for (int i = 0; i < Settings.getSettings().getNumPlayers() - Settings.getSettings().getNumL3_a(); i++) {
					listPlayer[i] = this.admin.getPlayer()[i];
					//System.out.println(this.admin.getPlayer()[i].getNick());
				}
				savePlayers(listPlayer, Paths.get("Groups").toString() + "\\" + Setting_general.getSettings().getGroup_name()+"\\setting_group ");
			}
			this.admin.setIsSet(false);
			this.admin.setIsStart(true);
			this.admin.resetPositions();
		}catch (RemoteException e){
			e.printStackTrace();
		}
    	
    	app.getStateManager().detach(this);    
    	cleanup();
		PlayNetworkScreenAppState playController = new PlayNetworkScreenAppState(app, index, type);
		nifty.fromXml("Interface/playInterface.xml", "hudNetworkAdmin", playController);
		app.getStateManager().attach(playController);
		app.getStateManager().detach(this);
		nifty.gotoScreen("hudNetworkAdmin");

    }
    

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);    
        super.cleanup();
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

	public void savePlayers(Player[] players, String fileName) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
			outputStream.writeObject(players);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
