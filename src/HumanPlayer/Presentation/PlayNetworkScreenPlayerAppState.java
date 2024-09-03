
package HumanPlayer.Presentation;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import Server.Control.State;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import HumanPlayer.Algorithm.LeapMotion;
import HumanPlayer.Algorithm.Settings;
import HumanPlayer.Algorithm.TypeInput;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


@SuppressWarnings("unused")
public class PlayNetworkScreenPlayerAppState extends AbstractAppState implements ScreenController{

	private static final Logger logger = Logger.getLogger(PlayNetworkScreenPlayerAppState.class.getName());
	
	private SimpleApplication app;
    private Nifty nifty;
	private Screen screen;
    private ViewPort viewPort;
	private Camera camera;
    private Node rootNode;
    private Node guiNode;
       
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");
    
    private final ColorRGBA backgroundColor = ColorRGBA.White;
    private InputManager inputManager;
    private AssetManager assetManager;

    private Geometry sphereGeomHuman;
    private AudioNode whiteNoise;
    
    private ArrayList<Geometry> sphereGeomOthers = new ArrayList<Geometry>();
    private ArrayList<Geometry> ropeGeomOthers = new ArrayList<Geometry>();
    
    private int numPlayers, time, indexPlayer;
    private int indexSocialMemory = 0;
    private int[] socialMemory;
    private LeapMotion l;
	private int startT = 0, prevT = 0, currentT= 0, prevT1 = 0, prevT2= 0;
	
	private HumanPlayerRMI human;
	
	private BitmapText countDown, numberPlayer, advice;
	private float posPrecSphere1 = 0;
    private float posSphere1 = 0;

    private ArrayList<Float> arrayPositionsHuman1 = new ArrayList<Float>();
    private ArrayList<Integer> timeSamples = new ArrayList<Integer>();  
    private ArrayList<Integer> randomPosition = new ArrayList<Integer>();
	private float[] bias;
    
    private float offsetPlayerNumber;
    
    private int topY = 550;
    private int botY = 100;
    
    private String letters = "ABCDEFG";
    
    private Settings settings;
	private ArrayList<Float> posSphere = new ArrayList<Float>();
	private ArrayList<Float> posSphere2 = new ArrayList<Float>();
	private float[] CurrentPosSphere;
	private int maxNumPlayers=0;

    //costruttore
    public PlayNetworkScreenPlayerAppState(SimpleApplication app, int index){
    	this.offsetPlayerNumber = 0;
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.inputManager.addMapping("QuitForce",  new KeyTrigger(KeyInput.KEY_Q));
    	this.inputManager.addListener(actionListener,"QuitForce");
        this.indexPlayer = index;
        this.settings = Settings.getSettings();

    }
    

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
        viewPort.setBackgroundColor(backgroundColor);
        this.numPlayers = Settings.getSettings().getNumPlayers();
        this.time = Settings.getSettings().getTime();
        this.socialMemory = Settings.getSettings().getSocialMemory();
        
        // gli indici indexPlayer si trovano
        try {
        	//mettere l'indice del giocatore
			this.human = new HumanPlayerRMI(this.indexPlayer);
			this.maxNumPlayers=human.getMaxNumPlayers();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Server connection not established.");
			quit();
		}
		CurrentPosSphere = new float[this.maxNumPlayers+1];
		bias = new float[this.maxNumPlayers+1];
        for(int i = 0; i < this.numPlayers; i++)
        	this.randomPosition.add(i);
        
        Collections.shuffle(this.randomPosition);

		// SEND RANDOM SIGNATURE
		/*try {
			this.human.sendRandomSignature(this.indexPlayer, this.randomPosition);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.warning("Server connection not established.");
			quit();
		}*/
        

        if(Settings.getSettings().getInputChoice().equals(TypeInput.LEAP_MOTION)) {
			System.out.println("Leap");
			l = new LeapMotion();
		}else{
			System.out.println("mouse");
		}
        
        this.countDown = new BitmapText(assetManager.loadFont("Interface/Fonts/BookmanOldStyle.fnt"), false);
		this.countDown.setSize(50);
		this.countDown.setColor(ColorRGBA.White);
		this.countDown.setLocalTranslation(720, 601, 0);
		this.countDown.setText(Integer.toString(this.time));
		
		this.advice = new BitmapText(assetManager.loadFont("Interface/Fonts/BookmanOldStyle.fnt"), false);
		this.advice.setText("<< Press Q to force quit >>");
		this.advice.setColor(ColorRGBA.Black);
		this.advice.setSize(50);
		this.advice.setLocalTranslation(390, 600, 0);
		
		this.numberPlayer = new BitmapText(assetManager.loadFont("Interface/Fonts/BookmanOldStyle.fnt"), false);
        this.numberPlayer.setSize(55);
        this.numberPlayer.setColor(ColorRGBA.White);
        this.numberPlayer.setLocalTranslation(30, 600, 0);
        this.numberPlayer.setText("You are the player " + this.letters.charAt(this.indexPlayer%7));
        localGuiNode.attachChild(this.numberPlayer);
        
        Sphere sphereMesh = new Sphere(40, 40, 0.38f);
        Box ropeMesh = new Box(6.5f, 0.03f, 0);
        Geometry ropeGeom = new Geometry("Rope", ropeMesh);        
        Material sphereMatBlue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material sphereMatOrange = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material ropeMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        sphereMatBlue.setColor("Color", ColorRGBA.Blue);
        sphereMatOrange.setColor("Color", ColorRGBA.Orange);
        ropeMat.setColor("Color", ColorRGBA.Black);
        
        sphereGeomHuman = new Geometry("SphereHuman", sphereMesh);
        sphereGeomHuman.setLocalTranslation(0, -2.75f, 1); 
        sphereGeomHuman.setMaterial(sphereMatBlue);
        localRootNode.attachChild(sphereGeomHuman);
               
        ropeGeom.setMaterial(ropeMat);
        ropeGeom.setLocalTranslation(0, -3.4f, -1); 
        localRootNode.attachChild(ropeGeom);
		if(this.numPlayers!=0) {
			int deltaY = (this.topY - this.botY) / (this.numPlayers);
			for (int i = 0; i < this.numPlayers; i++) {
				int j=this.numPlayers-1-i;
				sphereGeomOthers.add(i, new Geometry("SphereHuman", sphereMesh));
				sphereGeomOthers.get(i).setLocalTranslation(0, -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
				sphereGeomOthers.get(i).setMaterial(sphereMatOrange);
				// attach if the first slot eyes closed is zero
				// or if the social memory is not set

				// PLOTS NUMBER ON ROPE
				this.numberPlayer = new BitmapText(assetManager.loadFont("Interface/Fonts/BookmanOldStyle.fnt"), false);
				this.numberPlayer.setSize(100);
				this.numberPlayer.setColor(ColorRGBA.Red);
				this.numberPlayer.setLocalTranslation(10, this.botY + (i + 1) * deltaY, 1);
				this.numberPlayer.setText(Integer.toString(j + 1));
				localGuiNode.attachChild(this.numberPlayer);

				if (socialMemory.length == 1 || socialMemory[0] == 0) {
					localRootNode.attachChild(sphereGeomOthers.get(i));
				}

				ropeGeomOthers.add(i, new Geometry("Rope", ropeMesh));
				ropeGeomOthers.get(i).setMaterial(ropeMat);
				ropeGeomOthers.get(i).setLocalTranslation(0, -3.4f + (6.8f / this.numPlayers) + i * (6.8f / this.numPlayers), -1);
				localRootNode.attachChild(ropeGeomOthers.get(i));
			}
		}
		for (int i = 0; i < this.numPlayers+1; i++) {
			CurrentPosSphere[i]=0f;
			bias[i]=0f;
		}
      
         localGuiNode.attachChild(this.countDown);
         localGuiNode.attachChild(this.advice);
	   
         //audio
         whiteNoise = new AudioNode(assetManager, "/Sounds/whiteNoise5s.wav", false);
         whiteNoise.setLooping(true);
         whiteNoise.setPositional(false);
         whiteNoise.setVolume(5);
         rootNode.attachChild(whiteNoise);
         whiteNoise.play();
		startT = (int)System.currentTimeMillis();
		prevT = startT;
		currentT = startT;
		prevT1 = startT;
		prevT2 = startT;

	}
    

    @Override
    public void update(float tpf) {
		prevT2=(int) System.currentTimeMillis();
       	if(currentT - startT > 5*1000){
    		this.countDown.setText(Integer.toString(this.time-(currentT-startT)/1000+5));
       	}
    	if((currentT - startT) <= (this.time+5)*1000){
				if (Settings.getSettings().getInputChoice() == TypeInput.MOUSE) {
					//prendo la posizione del mouse
					Vector2f vettMousePos = inputManager.getCursorPosition();
					this.posSphere1 = vettMousePos.x;
				} else if (Settings.getSettings().getInputChoice() == TypeInput.LEAP_MOTION) {
					this.posSphere1 = l.getLeapX();
				} else {
					logger.warning("Error with position input from the leap motion/mouse");
				}

				this.posSphere1 = (this.posSphere1 / 80) - 5f;
				if (Math.abs(this.posSphere1) > 5) {
					this.posSphere1 = Math.signum(this.posSphere1) * 5f;
				}

				if (currentT - startT > 5 * 1000) {
					timeSamples.add(currentT - startT - 5000);
				}

				try {
					this.posSphere = human.retrievePositionPlayers(this.indexPlayer, this.posSphere1);
					if (posSphere.size() != 0) {
							for (int i = 0; i < this.numPlayers; i++) {
								int j=this.numPlayers-1-i;
								sphereGeomOthers.get(this.randomPosition.get(i)).setLocalTranslation(posSphere.get(this.randomPosition.get(i)), -2.75f + (5.5f / this.numPlayers) + j * (5.5f / this.numPlayers), 1);
							}
					}
					sphereGeomHuman.setLocalTranslation(posSphere.get(this.numPlayers), -2.75f, 1);
				} catch (RemoteException e) {
					e.printStackTrace();
					logger.warning("Server connection not established.");
					quit();
				}
			while (currentT - prevT < 25)
				currentT = (int) System.currentTimeMillis();
			prevT = currentT;
    	}else{
				Element image = this.screen.findElementByName("gameGo");
	    		image.hideWithoutEffect();
				clean();
    	}	
    }
    

    void clean(){
    	whiteNoise.stop();
    	cleanup(); 	
    	app.getStateManager().detach(this);

    	try {
			if(human.reset(this.indexPlayer) == false){
				logger.warning("Error with reset of the connection.");
			}
			if(human.getIsQuestion()) {
				cleanup();
				app.getStateManager().detach(this);
				IsThereVP chooseInputIndex = new IsThereVP(app, this.indexPlayer,this.randomPosition);
				nifty.fromXml("Interface/IsThereVPInterface.xml", "IsThere", chooseInputIndex);
				app.getStateManager().attach(chooseInputIndex);
				nifty.gotoScreen("IsThere");
			}else{
				cleanup();
				app.getStateManager().detach(this);
				int index = human.setNickName(Settings.getSettings().getPlayer().getNick());
				human.setConnection(index, true);
				AcquireNetworkSet chooseInputIndex = new AcquireNetworkSet(app);
				nifty.fromXml("Interface/setting.xml", "setting", chooseInputIndex);
				app.getStateManager().attach(chooseInputIndex);
				nifty.gotoScreen("setting");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.warning("Server connection not established");
			quit();
		}

        //EndNetworkScreenPlayerAppState endController = new EndNetworkScreenPlayerAppState(this.app, this.indexPlayer);
        //nifty.fromXml("Interface/endScreenNetworkPlayer.xml", "endNetworkScreen", endController);
        //app.getStateManager().attach(endController);
    }


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
	
    /** The action listener. */
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("QuitForce") && !keyPressed) {
        	 whiteNoise.stop();
        	 quit();
          }
        }
      };


	public void quit(){
		cleanup();

		app.getStateManager().detach(this);

		StartScreenNamePlayerAppState startPlayerController = new StartScreenNamePlayerAppState(this.app);
		nifty.fromXml("Interface/startNameInterfacePlayer.xml", "startPlayer", startPlayerController);
		app.getStateManager().attach(startPlayerController);
	}
}
