
package HumanPlayer.Presentation;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import HumanPlayer.Algorithm.Settings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

@SuppressWarnings("unused")
public class WaitScreenPlayerAppState extends AbstractAppState implements ScreenController{
	
	private static final Logger logger = Logger.getLogger(WaitScreenPlayerAppState.class.getName());
	
	private MainPlayer app;
    private Nifty nifty;
    private Screen screen;   
	private ViewPort viewPort;
    private Camera camera;
    private Node rootNode;
    private Node guiNode;
    
    private AssetManager assetManager;
    private InputManager inputManager;
    
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");
    
    private int index;
    private HumanPlayerRMI human;
    
    private boolean result;
    private String whichGame;
    
    /**
     * Instantiates a new wait screen player app state.
     */
    //costruttore
    public WaitScreenPlayerAppState(SimpleApplication app, int index, String whichGame){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
        this.index = index;
        this.whichGame = whichGame;
        System.out.println("Type of the game: " + this.whichGame);
    }
	public WaitScreenPlayerAppState(SimpleApplication app, int index, String whichGame,HumanPlayerRMI human){
		this.rootNode     = app.getRootNode();
		this.viewPort     = app.getViewPort();
		this.guiNode      = app.getGuiNode();
		this.assetManager = app.getAssetManager();
		this.index = index;
		this.whichGame = whichGame;
		this.human=human;
		System.out.println("Type of the game: " + this.whichGame);
	}
    

	@Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (MainPlayer)app;
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        camera = app.getCamera();
        inputManager = app.getInputManager();
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
		

        try {
			result = false;		
			if(this.whichGame.equals("network")){
				int[] s = this.human.retrieveNetwork(this.index%7);
				Settings.getSettings().setTime(s[0]);
				Settings.getSettings().setNumPlayers(s[1]);
				System.out.println("Le conness = "+ Integer.toString(this.index));
				int[] socialMemory = this.human.retrieveSocialMemory();
				Settings.getSettings().setSocialMemory(socialMemory);
			}else if(this.whichGame.equals("two")){
				this.human.resetNetwork(index);
			}else{
				logger.warning("Error with the option of the trial. Neither dyadic nor group.");
			}
        }catch (Exception e) {
			e.printStackTrace();
			logger.warning("Server connection not established. I cannot get the settings for the trial.");
			quit();
		}
    }
	

    public void quit(){
    	cleanup();
    	app.getStateManager().detach(this);
        StartScreenNamePlayerAppState startPlayerController = new StartScreenNamePlayerAppState(this.app);
        nifty.fromXml("Interface/startNameInterfacePlayer.xml", "startPlayer", startPlayerController);
        app.getStateManager().attach(startPlayerController);  
    }
    

    @Override
    public void update(float tpf) {
    	
    	if(result == false){
			try {
				this.human.synchronization(this.index);
				result=this.human.getIsStart();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
									
    	}else{
    		System.out.println("All players are connected...");
    		try {
				clean();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    	}
    }
    

    void clean() throws RemoteException{
    	
    	for(int i = 0; i < 500000; i++){}
    	
    	app.getStateManager().detach(this);    
    	cleanup();    			
		if(this.whichGame.equals("network")){
			PlayNetworkScreenPlayerAppState netController = new PlayNetworkScreenPlayerAppState(app, this.index);
			nifty.fromXml("Interface/playInterfacePlayer.xml", "hudNetworkPlayer", netController);
			app.getStateManager().attach(netController);
			nifty.gotoScreen("hudNetworkPlayer");
		}else
			logger.warning("Error with loading the next screen.");		
    }
    

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
