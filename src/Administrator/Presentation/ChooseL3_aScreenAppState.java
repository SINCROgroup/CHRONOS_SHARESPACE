//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Administrator.Presentation;

import Administrator.Algorithm.Settings;

import Administrator.Algorithm.TypeVP;
import Administrator.Algorithm.TypeDynamic;
import Administrator.Algorithm.TypeControl;
import Administrator.Algorithm.L3_a.*;
import Server.RMIInterface.Player;
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
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ChooseL3_aScreenAppState extends AbstractAppState implements ScreenController {
    private static final Logger logger = Logger.getLogger(ChooseL3_aScreenAppState.class.getName());
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
    private DropDown<String> dynamic;
    private DropDown<String> controller;
    private DropDown<String> signature;
    private DropDown<String> role;
    private ArrayList<Player> listPlayer = new ArrayList();
    private String name;
    private String Prevc="";
    private  Class<?> clazz;
    private int numparameter=0;


    public ChooseL3_aScreenAppState(SimpleApplication app,String name) {
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.name=name;
    }

    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication)app;
        super.initialize(stateManager, app);
        this.assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        this.camera = app.getCamera();
        this.inputManager = app.getInputManager();
        this.rootNode.attachChild(this.localRootNode);
        this.guiNode.attachChild(this.localGuiNode);
        this.setUpView();
    }

    private void setUpView() {
        Label l = (Label)this.screen.findNiftyControl("title", Label.class);
        if (Settings.getSettings().getCount() == 1) {
            l.setText("Set the features of the 2nd L3 added");
        } else if (Settings.getSettings().getCount() == 2) {
            l.setText("Set the features of the 3rd L3 added");
        } else if (Settings.getSettings().getCount() >= 3) {
            int var10001 = Settings.getSettings().getCount();
            l.setText("Set the features of the " + (var10001 + 1) + "th L3 added");
        }

        this.dynamic = (DropDown)this.screen.findNiftyControl("input_dynamic", DropDown.class);
        this.controller = (DropDown)this.screen.findNiftyControl("input_controller", DropDown.class);
        this.signature = (DropDown)this.screen.findNiftyControl("input_signature", DropDown.class);
        this.role = (DropDown)this.screen.findNiftyControl("input_role", DropDown.class);
        SetList(this.controller,(Paths.get("src\\Administrator\\Algorithm\\L3_a")).toString());
        //this.controller.addItem("deep Q network");
        System.out.println((Paths.get("src\\Administrator\\Algorithm\\L3_a")).toAbsolutePath().toString());
    }

    public void quit() {
        Settings.getSettings().setCount(0);
        StartScreenAppState startController = new StartScreenAppState(this.app);
        this.nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", new ScreenController[]{startController});
        this.app.getStateManager().attach(startController);
        this.app.getStateManager().detach(this);
        this.nifty.gotoScreen("startAdmin");
    }

    public void setVP() {
        String d = (String)this.dynamic.getSelection();
        String c = (String)this.controller.getSelection();
        String r = (String)this.role.getSelection();
        TextField textFieldNumPlayer = (TextField)this.screen.findNiftyControl("input_number", TextField.class);
        TextField[] textField=new TextField[8];
        textField[1]= (TextField)this.screen.findNiftyControl("input_gamma", TextField.class);
        textField[2]= (TextField)this.screen.findNiftyControl("input_omega", TextField.class);
        textField[3]= (TextField)this.screen.findNiftyControl("input_alpha", TextField.class);
        textField[4]= (TextField)this.screen.findNiftyControl("input_beta", TextField.class);
        textField[5]= (TextField)this.screen.findNiftyControl("input_k", TextField.class);
        textField[6]= (TextField)this.screen.findNiftyControl("input_c", TextField.class);
        textField[7]= (TextField)this.screen.findNiftyControl("input_delta", TextField.class);
        Settings.getSettings().setL3_a_inner_d(d,Integer.parseInt(textFieldNumPlayer.getRealText()));
        Settings.getSettings().setL3_a_algorithm(c,Integer.parseInt(textFieldNumPlayer.getRealText()));
        Settings.getSettings().setL3_a_role(r,Integer.parseInt(textFieldNumPlayer.getRealText()));
        for(int i=0;i<this.numparameter;i++){
            Settings.getSettings().setParameter(Integer.parseInt(textFieldNumPlayer.getRealText()),i,Float.parseFloat(textField[i+1].getRealText()));
        }

        Settings.getSettings().setCount(Settings.getSettings().getCount() + 1);
        if (Settings.getSettings().getNumL3_a() == Settings.getSettings().getCount()) {
            Settings.getSettings().setCount(0);
            logger.info("Settings of L3_as is completed.");
            if (Settings.getSettings().getNumL3_r()>0){
                this.cleanup();
                this.app.getStateManager().detach(this);
                ChooseL3_rScreenAppState vpController1 = new ChooseL3_rScreenAppState(this.app, this.name);
                this.nifty.fromXml("Interface/chooseL3_rAdmin.xml", "chooseL3_rAdmin", new ScreenController[]{vpController1});
                this.app.getStateManager().attach(vpController1);
                this.nifty.gotoScreen("chooseL3_rAdmin");
            } else {
                int[] valuesFrames = new int[]{0};
                Settings.getSettings().setSocialMemory(valuesFrames);
                Path outputPath = Paths.get("Test_settings").toAbsolutePath();
                Path currentDirectory = Paths.get(System.getProperty("user.dir")); // Ottieni la cartella corrente
                outputPath = currentDirectory.resolve("Test_settings").toAbsolutePath(); // Combina con il nome della directory
                String text;
                int numpeople=Settings.getSettings().getNumPlayers()-Settings.getSettings().getNumL3_a();
                text=name+"_L0"+numpeople+"_L3a"+Settings.getSettings().getNumL3_a()+"_L3r"+Settings.getSettings().getNumL3_r()+"_L2"+Settings.getSettings().getNumL2();
                Settings.getSettings().save(outputPath.toString(),text);
                cleanup();
                app.getStateManager().detach(this);
                StartScreenAppState startController = new StartScreenAppState(app);
                nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
                app.getStateManager().attach(startController);
                nifty.gotoScreen("startAdmin");
            }
        } else {
            ChooseL3_aScreenAppState vpController = new ChooseL3_aScreenAppState(this.app,this.name);
            this.nifty.fromXml("Interface/chooseVPAdmin.xml", "chooseVPAdmin", new ScreenController[]{vpController});
            this.app.getStateManager().attach(vpController);
            this.app.getStateManager().detach(this);
            this.nifty.gotoScreen("chooseVPAdmin");
        }

    }

    public void update(float tpf) {
        Element[] e =new Element[8];
        Element[] b =new Element[8];
        Label[] l=new Label[8];
        e[1] = this.screen.findElementByName("gamma");
        b[1] = this.screen.findElementByName("input_gamma");
        e[2] = this.screen.findElementByName("omega");
        b[2] = this.screen.findElementByName("input_omega");
        e[3] = this.screen.findElementByName("alpha");
        b[3] = this.screen.findElementByName("input_alpha");
        e[4] = this.screen.findElementByName("beta");
        b[4] = this.screen.findElementByName("input_beta");
        e[5] = this.screen.findElementByName("k");
        b[5] = this.screen.findElementByName("input_k");
        e[6] = this.screen.findElementByName("c");
        b[6] = this.screen.findElementByName("input_c");
        e[7] = this.screen.findElementByName("delta");
        b[7] = this.screen.findElementByName("input_delta");
        l[1] = (Label)this.screen.findNiftyControl("gamma", Label.class);
        l[2] = (Label)this.screen.findNiftyControl("omega", Label.class);
        l[3] = (Label)this.screen.findNiftyControl("alpha", Label.class);
        l[4] = (Label)this.screen.findNiftyControl("beta", Label.class);
        l[5] = (Label)this.screen.findNiftyControl("k", Label.class);
        l[6] = (Label)this.screen.findNiftyControl("c", Label.class);
        l[7] = (Label)this.screen.findNiftyControl("delta", Label.class);
        String c = (String)this.controller.getSelection();
        if(!Objects.equals(c, Prevc)){
            this.role.clear();
            this.dynamic.clear();
            try{
                //clazz = Class.forName((Paths.get("src\\Administrator\\Algorithm\\L3_a")).toAbsolutePath().toString()+"."+c+".java");
                clazz = Class.forName("Administrator.Algorithm.L3_a."+c);
                Object[] enumConstants;
                Class<?>[] innerClasses = clazz.getDeclaredClasses();
                ArrayList<String> list= new ArrayList<>();
                for (Class<?> innerClass : innerClasses) {
                    if (innerClass.isEnum()) {
                        int lastIndexOfDot = innerClass.getName().lastIndexOf("$");
                        //System.out.println(innerClass.getName().substring(lastIndexOfDot+1,innerClass.getName().length()));
                        //System.out.println(lastIndexOfDot);
                        switch (innerClass.getName().substring(lastIndexOfDot+1,innerClass.getName().length())){

                            case "TypeVP":
                                enumConstants = innerClass.getEnumConstants();
                                for (Object nomeFile : enumConstants) {
                                    this.role.addItem(nomeFile.toString());
                                }
                                break;
                            case "TypeInner":
                                enumConstants = innerClass.getEnumConstants();
                                for (Object nomeFile : enumConstants) {
                                    this.dynamic.addItem(nomeFile.toString());
                                }
                                break;
                            case "parameter":
                                enumConstants = innerClass.getEnumConstants();
                                for (Object nomeFile : enumConstants) {
                                    list.add(nomeFile.toString());
                                }
                                this.numparameter=list.size();
                                for(int i=1;i<7+1;i++){
                                    e[i].hide();
                                    b[i].hide();
                                }
                                for(int i=1;i<list.size()+1;i++){
                                    e[i].show();
                                    b[i].show();
                                    l[i].setText(list.get(i-1));
                                }
                                break;

                        }
                    }
                }
            }catch (ClassNotFoundException r) {
                System.err.println("Classe non trovata: " + c);
            }
            Prevc=c;
        }

    }

    public void cleanup() {
        this.rootNode.detachChild(this.localRootNode);
        this.guiNode.detachChild(this.localGuiNode);
        super.cleanup();
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onEndScreen() {
    }

    public void onStartScreen() {
    }
    private void SetList(DropDown ListName,String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            List<String> nomiFile = new ArrayList<>();

            for (File file : files) {
                if (file.isFile()) {
                    int lastIndexOfDot = file.getName().lastIndexOf(".");
                    nomiFile.add(file.getName().substring(0,lastIndexOfDot));
                }
            }

            // Aggiungi i nomi dei file a this.input_test
            for (String nomeFile : nomiFile) {
                ListName.addItem(nomeFile);
            }
        }
    }
}




