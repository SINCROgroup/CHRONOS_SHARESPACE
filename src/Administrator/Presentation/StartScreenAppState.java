/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrator.Presentation;

import Server.Control.ServerStartup;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import Administrator.Algorithm.Settings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Controller for the Interface to choose the type of game
 * solo, dyadic or network
 * set also the network ip
 *
 * @author marialombardi
 */
@SuppressWarnings("unused")
public class StartScreenAppState extends AbstractAppState implements ScreenController{
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
  

    //costruttore
    public StartScreenAppState(SimpleApplication app){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();

    }
    
    
    //init the screen
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
    }
    
    // no update, it is only a static interface
    @Override
    public void update(float tpf) {
    }
    

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);    
        super.cleanup();
    }


    // function that takes all the user input parameters from the interface
    // and use them
    // this function is connected to the button for Solo trials
    public void SetTest(String nextScreen) {
        cleanup();
        app.getStateManager().detach(this);

        NetworkSetAppState setNetworkController = new NetworkSetAppState(app);
        nifty.fromXml("Interface/NetTestInterface.xml", "network", setNetworkController);
        app.getStateManager().attach(setNetworkController);
        nifty.gotoScreen(nextScreen);


    }

    public void ExecTest(String nextScreen) {
        cleanup();
        app.getStateManager().detach(this);
        StartExeTestAppState chooseController = new StartExeTestAppState(app);
        nifty.fromXml("Interface/SetExeInterface.xml", "setTest", chooseController);
        app.getStateManager().attach(chooseController);
        nifty.gotoScreen(nextScreen);
    }

    /*

    // function that takes all the user input parameters from the interface
    // and use them
    // this function is connected to the button for Group trials
    public void setNetwork(String nextScreen){
    	
    	TextField textFieldIP1 = screen.findNiftyControl("IP1", TextField.class);
        TextField textFieldIP2 = screen.findNiftyControl("IP2", TextField.class);
        TextField textFieldIP3 = screen.findNiftyControl("IP3", TextField.class);
        TextField textFieldIP4 = screen.findNiftyControl("IP4", TextField.class);
        boolean serverLocalhost = false;
        
        if(textFieldIP1.getRealText().length()==0 && textFieldIP2.getRealText().length()==0 &&
      		  textFieldIP3.getRealText().length()==0 && textFieldIP4.getRealText().length()==0){
      	  serverLocalhost = true;
        }
   		  
   	  if((textFieldIP1.getRealText().length()==0 || textFieldIP1.getRealText().equals("????") || 
          		 textFieldIP2.getRealText().length()==0 || textFieldIP2.getRealText().equals("????") ||
          		  textFieldIP3.getRealText().length()==0 || textFieldIP3.getRealText().equals("????") ||
          		  textFieldIP4.getRealText().length()==0 || textFieldIP4.getRealText().equals("????"))
   			  	  && (serverLocalhost == false)){       	 
          	  if(textFieldIP1.getRealText().length()==0 || textFieldIP1.getRealText().equals("????"))
          		  textFieldIP1.setText("????");
          	  if(textFieldIP2.getRealText().length()==0 || textFieldIP2.getRealText().equals("????"))
          		  textFieldIP2.setText("????");
          	  if(textFieldIP3.getRealText().length()==0 || textFieldIP3.getRealText().equals("????"))
          		  textFieldIP3.setText("????");
          	  if(textFieldIP4.getRealText().length()==0 || textFieldIP4.getRealText().equals("????"))
          		  textFieldIP4.setText("????");
         }else{
          	  
                if(serverLocalhost == true){
              	  Settings.getSettings().setIPaddress("localhost");   
              	  //Settings.getSettings().setIPaddress("192.168.0.152");
                } else {
              	  Settings.getSettings().setIPaddress(textFieldIP1.getRealText() + "." + textFieldIP2.getRealText()
              	  	+ "." + textFieldIP3.getRealText() + "." + textFieldIP4.getRealText());
                }
                
                System.out.println("Network set to " + Settings.getSettings().getIPaddress() + ".");
    	
                cleanup();
            	app.getStateManager().detach(this);
            	
                SetNetworkAppState setNetworkController = new SetNetworkAppState(app);
                nifty.fromXml("Interface/setNetworkAdmin.xml", "network", setNetworkController);              
                app.getStateManager().attach(setNetworkController);
                nifty.gotoScreen(nextScreen);
         }   	
    }
    
    
    // function that takes all the user input parameters from the interface
    // and use them
    // this function is connected to the button for Dyadic trials
    public void setDyadic(String nextScreen){
    	    	
    	TextField textFieldIP1 = screen.findNiftyControl("IP1", TextField.class);
        TextField textFieldIP2 = screen.findNiftyControl("IP2", TextField.class);
        TextField textFieldIP3 = screen.findNiftyControl("IP3", TextField.class);
        TextField textFieldIP4 = screen.findNiftyControl("IP4", TextField.class);
        boolean serverLocalhost = false;
        
        if(textFieldIP1.getRealText().length()==0 && textFieldIP2.getRealText().length()==0 &&
      		  textFieldIP3.getRealText().length()==0 && textFieldIP4.getRealText().length()==0){
      	  serverLocalhost = true;
        } 
        if((textFieldIP1.getRealText().length()==0 || textFieldIP1.getRealText().equals("????") || 
         		 textFieldIP2.getRealText().length()==0 || textFieldIP2.getRealText().equals("????") ||
         		  textFieldIP3.getRealText().length()==0 || textFieldIP3.getRealText().equals("????") ||
         		  textFieldIP4.getRealText().length()==0 || textFieldIP4.getRealText().equals("????"))
  			  	  && (serverLocalhost == false)){       	 
         	  if(textFieldIP1.getRealText().length()==0 || textFieldIP1.getRealText().equals("????"))
         		  textFieldIP1.setText("????");
         	  if(textFieldIP2.getRealText().length()==0 || textFieldIP2.getRealText().equals("????"))
         		  textFieldIP2.setText("????");
         	  if(textFieldIP3.getRealText().length()==0 || textFieldIP3.getRealText().equals("????"))
         		  textFieldIP3.setText("????");
         	  if(textFieldIP4.getRealText().length()==0 || textFieldIP4.getRealText().equals("????"))
         		  textFieldIP4.setText("????");
        }else{
        	
        	if(serverLocalhost == true){
            	  Settings.getSettings().setIPaddress("localhost");
        	}else{
            	  Settings.getSettings().setIPaddress(textFieldIP1.getRealText() + "." + textFieldIP2.getRealText()
            	  	+ "." + textFieldIP3.getRealText() + "." + textFieldIP4.getRealText());
        	}
        	
            System.out.println("Network set to " + Settings.getSettings().getIPaddress() + ".");

            cleanup();
        	app.getStateManager().detach(this);
        	
            ChoosePartnerScreenAppState setDyadicController = new ChoosePartnerScreenAppState(app);
            nifty.fromXml("Interface/dyadicAdmin.xml", "dyadic", setDyadicController); 
            app.getStateManager().attach(setDyadicController);
            nifty.gotoScreen(nextScreen);
       }
    }
*/

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
