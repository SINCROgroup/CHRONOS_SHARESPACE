/*
 * 
 */
package Administrator.Presentation;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import Administrator.Algorithm.*;
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
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.apache.commons.math3.complex.Complex;
import org.lwjgl.Sys;

@SuppressWarnings("unused")
public class PlayNetworkScreenAppState extends AbstractAppState implements ScreenController{

	private static final Logger logger = Logger.getLogger(HumanPlayer.Presentation.PlayNetworkScreenPlayerAppState.class.getName());

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

	private AudioNode whiteNoise;

	private ArrayList<Geometry> sphereGeomHumans = new ArrayList<Geometry>();
	private ArrayList<Geometry> ropeGeomHumans = new ArrayList<Geometry>();
	private ArrayList<Geometry> sphereGeomVPs = new ArrayList<Geometry>();
	private ArrayList<Geometry> ropeGeomVPs= new ArrayList<Geometry>();

	private int numPlayers, time, indexPlayer,numVP;
	private int indexSocialMemory = 0;
	private int[] index, type;
	private int[] socialMemory;
	private int startT = 0, prevT = 0, currentT= 0;
    private float[] x_VP;

	private AdminRMI admin;

	private BitmapText countDown, numberPlayer, advice;
	private float[] posPrecSphereVP ;
	private float[] posSphereVP ;

	private ArrayList<Float>[] arrayPositionsVP ;
	private ArrayList<Integer> timeSamples = new ArrayList<Integer>();
	private ArrayList<Integer> randomPosition = new ArrayList<Integer>();
	private ArrayList<float[]> pos;
	private ArrayList<double[]> theta;
	private float offsetPlayerNumber;

	private int topY = 550;
	private int botY = 100;
	private float[] x = new float[Settings.getSettings().getMaxNum()*2+1];
	private String letters = "ABCDEFG";
	private ConstructL3_a[] VPs_L3_a=new ConstructL3_a[7];
	private ConstructL3_r[] VPs_L3_r=new ConstructL3_r[7];
	private float[][] parameter=new float[8][8];
	private double[] positionPrev=new double[Settings.getSettings().getMaxNum()];
	private double[] velocity=new double[Settings.getSettings().getMaxNum()];
	private double[] velocityPrev=new double[Settings.getSettings().getMaxNum()];
	private double[] amplitudePosPPrev=new double[Settings.getSettings().getMaxNum()];
	private double[] amplitudePosNPrev=new double[Settings.getSettings().getMaxNum()];
	private double[] amplitudeVelPPrev=new double[Settings.getSettings().getMaxNum()];
	private double[] amplitudeVelNPrev=new double[Settings.getSettings().getMaxNum()];



	private Settings settings;

	//costruttore
	public PlayNetworkScreenAppState(SimpleApplication app, int[] index,int[] type){
		this.offsetPlayerNumber = 0;
		this.rootNode     = app.getRootNode();
		this.viewPort     = app.getViewPort();
		this.guiNode      = app.getGuiNode();
		this.assetManager = app.getAssetManager();
		this.inputManager = app.getInputManager();
		this.inputManager.addMapping("QuitForce",  new KeyTrigger(KeyInput.KEY_Q));
		this.inputManager.addListener(actionListener,"QuitForce");
		this.settings = Settings.getSettings();
		this.index=index;
		this.type=type;

	}


	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = (SimpleApplication)app;
		super.initialize(stateManager, app);
		assetManager = app.getAssetManager();
		stateManager = app.getStateManager();
		camera = app.getCamera();
		this.numVP=Settings.getSettings().getNumL2()+Settings.getSettings().getNumL3_a()+Settings.getSettings().getNumL3_r();
		inputManager = app.getInputManager();
		rootNode.attachChild(localRootNode);
		guiNode.attachChild(localGuiNode);
		viewPort.setBackgroundColor(backgroundColor);
		this.numPlayers = Settings.getSettings().getNumPlayers();
		this.time = Settings.getSettings().getTime();
		this.socialMemory = Settings.getSettings().getSocialMemory();
		this.x_VP=new float[this.numPlayers];
		posPrecSphereVP=new float[this.numVP];
		posSphereVP=new float[this.numVP];
		arrayPositionsVP =new ArrayList[this.numVP];
		this.pos=new ArrayList<>();
		this.theta=new ArrayList<double[]>();
        Arrays.fill(this.positionPrev, 0.0);
		Arrays.fill(this.velocity, 0.0);
		Arrays.fill(this.velocityPrev, 0.0);
		Arrays.fill(this.amplitudePosPPrev, 1.0);
		Arrays.fill(this.amplitudePosNPrev, 1.0);
		Arrays.fill(this.amplitudeVelPPrev, 1.0);
		Arrays.fill(this.amplitudeVelNPrev, 1.0);

		// gli indici indexPlayer si trovano
		try {
			//mettere l'indice del giocatore
			this.admin = new AdminRMI();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Server connection not established.");
			quit();
		}

		for(int i = 0; i < this.numPlayers; i++)
			this.randomPosition.add(i);




		startT = (int)System.currentTimeMillis();
		prevT = startT;
		currentT = startT;


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
		this.numberPlayer.setText("You are the admin player ");
		localGuiNode.attachChild(this.numberPlayer);

		Sphere sphereMesh = new Sphere(40, 40, 0.38f);
		Box ropeMesh = new Box(6.5f, 0.03f, 0);

		Material sphereMatBlue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Material sphereMatOrange = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Material ropeMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

		sphereMatBlue.setColor("Color", ColorRGBA.Blue);
		sphereMatOrange.setColor("Color", ColorRGBA.Orange);
		ropeMat.setColor("Color", ColorRGBA.Black);


		if(this.numPlayers!=0) {
			int deltaY = (this.topY - this.botY) / (this.numPlayers);
			int n=0;
			int m=0;
			for (int i = 0; i < this.numPlayers; i++) {
				if(this.type[i]==0||this.type[i]==1||this.type[i]==3){
					sphereGeomHumans.add(m,new Geometry("SphereHuman", sphereMesh));
					sphereGeomHumans.get(m).setLocalTranslation(0, -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
					sphereGeomHumans.get(m).setMaterial(sphereMatBlue);
					localRootNode.attachChild(sphereGeomHumans.get(m));
					ropeGeomHumans.add(m, new Geometry("Rope", ropeMesh));
					ropeGeomHumans.get(m).setMaterial(ropeMat);
					ropeGeomHumans.get(m).setLocalTranslation(0, -3.4f + (6.8f / this.numPlayers) + i * (6.8f / this.numPlayers), -1);
					localRootNode.attachChild(ropeGeomHumans.get(m));
					m++;
				}
				if(this.type[i]==1||this.type[i]==2||this.type[i]==3){
					sphereGeomVPs.add(n,new Geometry("SphereHuman", sphereMesh));
					sphereGeomVPs.get(n).setLocalTranslation(0, -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
					sphereGeomVPs.get(n).setMaterial(sphereMatOrange);
					localRootNode.attachChild(sphereGeomVPs.get(n));
					ropeGeomVPs.add(n, new Geometry("Rope", ropeMesh));
					ropeGeomVPs.get(n).setMaterial(ropeMat);
					ropeGeomVPs.get(n).setLocalTranslation(0, -3.4f + (6.8f / this.numPlayers) + i * (6.8f / this.numPlayers), -1);
					localRootNode.attachChild(ropeGeomVPs.get(n));
					n++;
				}
				// PLOTS NUMBER ON ROPE
				this.numberPlayer = new BitmapText(assetManager.loadFont("Interface/Fonts/BookmanOldStyle.fnt"), false);
				this.numberPlayer.setSize(80);
				this.numberPlayer.setColor(ColorRGBA.White);
				this.numberPlayer.setLocalTranslation(10, this.botY + (i + 1) * deltaY, 1);
				this.numberPlayer.setText(Integer.toString(i + 1));
				localGuiNode.attachChild(this.numberPlayer);
			}
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
		try{
			if(this.numVP!=0){
				for(int i = 0; i < this.numPlayers; i++){
					switch (this.type[i]){
						case(1):
							break;
						case(2):
							Class<?> classea = Class.forName("Administrator.Algorithm.L3_a."+Settings.getSettings().getL3_a_algorithm(i+1));
							Constructor<?> costruttorea = classea.getConstructor(Integer.TYPE);
							//System.out.println(costruttore.getName());
							VPs_L3_a[i] = (ConstructL3_a) costruttorea.newInstance(Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a());
							parameter[i]=Settings.getSettings().getParameter(i+1);
							break;
						case(3):
							Class<?> classer = Class.forName("Administrator.Algorithm.L3_r."+Settings.getSettings().getL3_s_algorithm(i+1));
							Constructor<?> costruttorer = classer.getConstructor(Integer.TYPE);
							//System.out.println(costruttore.getName());
							VPs_L3_r[i] = (ConstructL3_r) costruttorer.newInstance(Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_r());
							parameter[i]=Settings.getSettings().getParameter(i+1);
							break;
					}
				}
			}
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


	@Override
	public void update(float tpf) {

		if (currentT - startT > 5 * 1000) {
			this.countDown.setText(Integer.toString(this.time - (currentT - startT) / 1000 + 5));
		}

		// scan the sequence only if social memory is set
		if (socialMemory.length != 1) {
			// sum of the element until the indexSocialMemory
			int elapsedTime = 5;
			for (int i = 0; i <= indexSocialMemory; i++) {
				elapsedTime = elapsedTime + socialMemory[i];
			}
			elapsedTime = elapsedTime * 1000;
			if ((currentT - startT) > elapsedTime && (currentT - startT) <= (this.time + 5) * 1000) {
				indexSocialMemory++;
				if ((indexSocialMemory % 2) == 0) {
					//index is even -> closed
					for (int i = 0; i < this.numPlayers; i++) {
						localRootNode.detachChild(sphereGeomHumans.get(i));
					}
				} else {
					//index is odd -> open
					for (int i = 0; i < this.numPlayers; i++) {
						localRootNode.attachChild(sphereGeomHumans.get(i));
					}
				}
			}
		}
		try {
			x = admin.retrivePositions();

			float[] concatenatedArray = new float[x.length + 1];
			System.arraycopy(x, 0, concatenatedArray, 0, x.length);
			concatenatedArray[x.length] = currentT-startT;
			pos.add(concatenatedArray);

			if ((currentT - startT) <= (this.time + 5) * 1000) {
				currentT = (int) System.currentTimeMillis();

				if (this.numVP != 0) {
					for (int i = 0; i < this.numPlayers; i++) {
						switch (this.type[i]) {
							case (1):

								break;
							case (2):
								int k = 0;
								float[] positions = new float[Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a()];
								for (int j = 0; j < this.numPlayers; j++) {
									if (this.type[j]==0) {
										positions[k] = x[j];
										k++;
									}
								}
								//System.out.println(positions.length);
								x_VP[i] = VPs_L3_a[i].PositionComputation(positions, parameter[i]);
								admin.setPositions(i+1, x_VP[i]);
								break;
							case (3):
								int f = 0;
								float[] positionsr = new float[Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_r()];
								for (int j = 0; j < this.numPlayers; j++) {
									if (this.type[j]==0) {
										positionsr[f] = x[j];
										f++;
									}
								}
								//System.out.println(Settings.getSettings().getNumPlayers()+" "+positionsr.length);
								x_VP[i] = VPs_L3_r[i].PositionComputation(positionsr, parameter[i]);
								admin.setPositions(i+1, x_VP[i]);
								break;
						}
					}
				}


				if (currentT - startT > 5 * 1000) {
					timeSamples.add(currentT - startT - 5000);
				}


				if (this.numPlayers != 0) {
					float[] concatenatedTheta = new float[Settings.getSettings().getMaxNum()];

					if (socialMemory.length != 1) {
						if ((indexSocialMemory % 2) != 0) {
							int n=0;
							int m=0;
							for (int i = 0; i < this.numPlayers; i++) {
								switch (this.type[i]) {
									case (0):

										sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i+1], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										n++;
										break;
									case (1):

										sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i+1 + Settings.getSettings().getMaxNum()], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(x_VP[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										n++;
										m++;
										break;
									case (2):
										if(Math.abs(this.x_VP[i]*10) <= 5) {
											sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(this.x_VP[i]*10, -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);

										} else {
											sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(Math.signum(this.x_VP[i]*10)*5f, -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										}
										m++;
										break;
									case (3):

										sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i + Settings.getSettings().getMaxNum()], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(x_VP[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
										n++;
										m++;
										break;
								}
							}
						}
					} else {
						int n=0;
						int m=0;
						for (int i = 0; i < this.numPlayers; i++) {
							switch (this.type[i]) {
								case (0):
									sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									n++;
									break;
								case (1):
									sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i + Settings.getSettings().getMaxNum()], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(x_VP[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									n++;
									m++;
									break;
								case (2):
									sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(x_VP[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									m++;
									break;
								case (3):

									sphereGeomHumans.get(this.randomPosition.get(n)).setLocalTranslation(x[i+ Settings.getSettings().getMaxNum()], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									sphereGeomVPs.get(this.randomPosition.get(m)).setLocalTranslation(x_VP[i], -2.75f + (5.5f / this.numPlayers) + i * (5.5f / this.numPlayers), 1);
									n++;
									m++;
									break;
							}
						}
					}
				}


				while (currentT - prevT < 25)
					currentT = (int) System.currentTimeMillis();

				prevT = currentT;

			} else {
				whiteNoise.stop();
				try {
					// send the positions of the HP to the server
					int trial = 0;
					PrintWriter saveSamples = null;
					PrintWriter saveMean = null;
					PrintWriter saveSamples1 = null;
					PrintWriter saveAnswer = null;
					String folderPath = ".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test();
					File folder = new File(folderPath);
					if (!folder.exists()) {
						if (folder.mkdirs()) {
							trial = 1;
						} else {
							System.err.println("Errore durante la creazione della cartella.");
						}
					} else {
						// Se la cartella esiste, conta il numero di cartelle contenute
						File[] subfolders = folder.listFiles(File::isDirectory);
						if (subfolders != null) {
							trial = subfolders.length + 1;
						} else {
							trial = 1;
						}
					}
					folderPath = ".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test() + "//Trial" + Integer.toString(trial);
					folder = new File(folderPath);
					if (!folder.exists()) {
						folder.mkdirs();
					}
					saveSamples = new PrintWriter(new BufferedWriter(new FileWriter(".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test() + "//Trial" + Integer.toString(trial) +"//Position_time" + ".txt")));
					saveSamples1 = new PrintWriter(new BufferedWriter(new FileWriter(".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test() + "//Trial" + Integer.toString(trial) +"//phase" + ".txt")));
					saveMean = new PrintWriter(new BufferedWriter(new FileWriter(".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test() + "//Mean_phase" + ".txt", true)));
					saveAnswer = new PrintWriter(new BufferedWriter(new FileWriter(".//Groups//" + Setting_general.getSettings().getGroup_name() + "//" + Setting_general.getSettings().getName_Test() + "//Trial" + Integer.toString(trial) +"//Answer" + ".txt")));
					int size = pos.size();
					int arraySize = pos.get(0).length;
					float[][] positions = new float[size][arraySize];
					for (int i = 0; i < size; i++) {
						positions[i] = pos.get(i);
					}
                    for (int m = 0; m < x.length; m++) {
						double[] signal = new double[positions.length];
						for (int i = 0; i < positions.length; i++) {
							signal[i] = positions[i][m];
						}
                        Hilbert h = new Hilbert(signal);
                        h.transform();
						double[] phase = h.getInstantaneousPhase();
						theta.add(phase);
					}
					float[] mean = new float[this.numPlayers];
					Arrays.fill(mean,0);
					int contm=0;
					for (int j = (int)(5/0.025); j < (theta.get(0).length-1); j++) {
						contm++;
						for (int m = 0; m < this.numPlayers ; m++) {
							saveSamples1.print(Double.toString(theta.get(m)[j])); // Scrivi il valore double come stringa
							saveSamples1.print(";");
							mean[m]+=(theta.get(m)[j+1]-theta.get(m)[j])/0.025;
						}
						saveSamples1.println("");
					}
					saveSamples1.close(); // Chiudi il writer
					for (int m = 0; m < this.numPlayers ; m++) {
						mean[m]=mean[m]/contm;
						saveMean.print(Float.toString(mean[m]) + ";");
					}
					saveMean.println("");
					saveMean.close(); // Chiudi il writer
					for (int j = 0; j < pos.size(); j++) {
						for (int m = 0; m < x.length + 1; m++) {
							saveSamples.print(Float.toString(pos.get(j)[m]) + ";");
						}
						saveSamples.println("");
					}
					saveSamples.close();
					boolean set=false;
					String[] Answer=new String[Settings.getSettings().getMaxNum()];
					if(Settings.getSettings().getAsk()){
						while(!set){
							Answer=admin.getAnswer();
							int cont=0;
							for(int f=0;f<Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a();f++){
								if(Answer[f]!=null){
									cont++;
									}
							}
							if(cont==Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a())
								set=true;
						}
						int contL1=0;
						for(int f=0;f<Settings.getSettings().getNumPlayers();f++){
							System.out.println(Answer[f]);
							int cont=0;
								boolean isL1=true;
								if(Settings.getSettings().getL2_algorithm(f+1)!=null){
									isL1=true;
								}
								if(Settings.getSettings().getL3_a_algorithm(f+1)!=null){
									isL1=false;
								}
								if(Settings.getSettings().getL3_s_algorithm(f+1)!=null){
									isL1=true;
								}
								if(isL1){
										if(Integer.parseInt(Answer[f])!=0 && Integer.parseInt(Answer[f])!=8) {
											for(int j = 0; j < Settings.getSettings().getNumPlayers(); j++) {
												if (Settings.getSettings().getNet()[Settings.getSettings().getNumPlayers() * f+j]==1){
													cont++;
												}
												if (cont == Integer.parseInt(Answer[f])) {
													saveAnswer.print("Player "+String.valueOf(f+1)+": "+String.valueOf(j + 1));
													saveAnswer.println(";");
													j=Settings.getSettings().getNumPlayers();
												}
											}
										}else if(Integer.parseInt(Answer[f])==0){
											saveAnswer.print("Player "+String.valueOf(f+1)+": NO");
											saveAnswer.println(";");
										} else {
											saveAnswer.print("Player "+String.valueOf(f+1)+": BOH");
											saveAnswer.println(";");
										}
								}

						}
						for (int i = 0; i < this.numPlayers; i++) {
							switch (this.type[i]) {
								case (1):
									saveAnswer.print("L2: "+String.valueOf(i+1));
									saveAnswer.println(";");
									break;
								case (2):
									saveAnswer.print("L3_a: "+String.valueOf(i+1));
									saveAnswer.println(";");
									break;
								case (3):
									saveAnswer.print("L3_s: "+String.valueOf(i+1));
									saveAnswer.println(";");
									break;
							}
						}
						saveAnswer.close();
					}
					Element image = this.screen.findElementByName("gameGo");
					image.hideWithoutEffect();
					clean();
				}catch (RemoteException e) {
					System.out.println("Error with the update of the screen");
					quit();
				} catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

		} catch (RemoteException e) {
			logger.warning("Server connection not established.");
		}
		if ((currentT - startT) >= (this.time - 1) * 1000 && (currentT - startT) <= (this.time) * 1000) {
			try {
				admin.setIsStart(false);
			} catch (RemoteException e) {
				System.out.println("Error with the update of the screen");
			}
		}
	}


		void clean () {
			whiteNoise.stop();
			cleanup();
			app.getStateManager().detach(this);
			SelectTestInterface startController = new SelectTestInterface(app, this.admin);
			nifty.fromXml("Interface/SetTestInterface2.xml", "setTest", startController);
			app.getStateManager().attach(startController);
			nifty.gotoScreen("setTest");
			//EndNetworkScreenPlayerAppState endController = new EndNetworkScreenPlayerAppState(this.app, this.indexPlayer);
			//nifty.fromXml("Interface/endScreenNetworkPlayer.xml", "endNetworkScreen", endController);
			//app.getStateManager().attach(endController);
		}
	public static double[] calculatePhase(Complex[] signal) {
		double[] phase = new double[signal.length];

		// Calcola la fase utilizzando atan2
		for (int i = 0; i < signal.length; i++) {
			phase[i] = Math.atan2(signal[i].getImaginary(), signal[i].getReal());
		}

		return phase;
	}


		@Override
		public void cleanup () {
			rootNode.detachChild(localRootNode);
			guiNode.detachChild(localGuiNode);
			super.cleanup();
		}


		@Override
		public void bind (Nifty nifty, Screen screen){
			this.nifty = nifty;
			this.screen = screen;
		}


		@Override
		public void onEndScreen () {
		}


		@Override
		public void onStartScreen () {
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

		public void quit () {
			cleanup();
			app.getStateManager().detach(this);

			StartScreenAppState startController = new StartScreenAppState(app);
			nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
			app.getStateManager().attach(startController);
			nifty.gotoScreen("startAdmin");
		}
	public double wrapped_difference(double angle1, double angle2) {
		double diff=angle1-angle2;
		if (diff > Math.PI) {
			diff -= 2 * Math.PI;
		}
		else if (diff < -Math.PI) {
			diff += 2 * Math.PI;
		}
		return diff;
	}


	}