package Administrator.Presentation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;


import java.io.FileWriter;
import java.io.IOException;

import Administrator.Algorithm.TypeTest;
import com.opencsv.CSVWriter;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import Administrator.Algorithm.Settings;
import Server.Control.AdminCoordinator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Controller for the interface where the network is set
 * known topology or user-defined with adjacency matrix
 * duration trial and number of players are set as well
 */
@SuppressWarnings("unused")
public class WithVirtualAgentsAppState extends AbstractAppState implements ScreenController{

    private static final Logger logger = Logger.getLogger(WithVirtualAgentsAppState.class.getName());

    private SimpleApplication app;
    private Nifty nifty;
    private Screen screen;
    private ViewPort viewPort;
    private Camera camera;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");
    private InputManager inputManager;
    private int num;

    private DropDown<String> networkTopology;
    private DropDown<String> networkDirection;
    private DropDown<String> networkL2_replaced;
    private DropDown<String> networkL3_replaced;
    private DropDown<String> networkL3_added;
    private DropDown<String> networkL2_r_d;
    private DropDown<String> networkL3_r_d;
    private DropDown<String> networkL3_a_d;



    private int maxNumPlayers = 7;
    private int minNumPlayers = 1;

    private int num_L2 = 0;

    private int num_L3_r =0;

    private int num_L3_a=0;
    private Boolean ask=false;

    private int time = 30;
    private int previousNum, currentNum = 0;

    private int previousTrial, currentTrial = 0;
    private int previousCenterNode, currentCenterNode = 0;
    private String previousDir, currentDir = "";
    private String previousNet, currentNet = "";
    private int[] previousSequence, currentSequence; // max 7 players



    //costruttore
    public WithVirtualAgentsAppState(SimpleApplication app){
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();
    }


    //init the screen
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication)app;
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        //camera = app.getCamera();
        inputManager = app.getInputManager();
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);

        this.previousSequence = new int[this.maxNumPlayers];
        this.currentSequence = new int[this.maxNumPlayers];

        for(int i = 0; i < this.maxNumPlayers; i++){
            this.previousSequence[i] = 0;
            this.currentSequence[i] = 0;
        }

        this.networkTopology = this.screen.findNiftyControl("input_network", DropDown.class);
        this.networkTopology.addItem("Complete graph");
        this.networkTopology.addItem("Star graph");
        this.networkTopology.addItem("Path graph");
        this.networkTopology.addItem("Ring graph");
        this.networkTopology.addItem("Solo graph");
        this.networkTopology.addItem("User defined graph");

        this.networkDirection = this.screen.findNiftyControl("input_direction", DropDown.class);
        this.networkDirection.addItem("Directed");
        this.networkDirection.addItem("Undirected");

        this.networkL2_replaced = this.screen.findNiftyControl("L2_replaced", DropDown.class);
        this.networkL2_replaced.addItem("DQN");
        this.networkL2_replaced.addItem("PD");

        this.networkL2_r_d = this.screen.findNiftyControl("L2_r_d", DropDown.class);
        this.networkL2_r_d.addItem("kuramoto");
        this.networkL2_r_d.addItem("HKB");

        this.networkL3_replaced = this.screen.findNiftyControl("L3_replaced", DropDown.class);
        this.networkL3_replaced.addItem("DQN");
        this.networkL3_replaced.addItem("PD");

        this.networkL3_r_d = this.screen.findNiftyControl("L3_r_d", DropDown.class);
        this.networkL3_r_d.addItem("Kuramoto");
        this.networkL3_r_d.addItem("HKB");

        this.networkL3_a_d = this.screen.findNiftyControl("L3_a_d", DropDown.class);
        this.networkL3_a_d.addItem("Kuramoto");
        this.networkL3_a_d.addItem("HKB");

        this.networkL3_added = this.screen.findNiftyControl("L3_added", DropDown.class);
        this.networkL3_added.addItem("DQN");
        this.networkL3_added.addItem("PD");


    }


    // if quit, return to the start screen
    public void quit(){

        cleanup();
        app.getStateManager().detach(this);
        StartScreenAppState startController = new StartScreenAppState(app);
        nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
        app.getStateManager().attach(startController);
        nifty.gotoScreen("startAdmin");
    }


    // get the user inputs
    public void setNetwork(){
        TextField textFieldTime = this.screen.findNiftyControl("input_time", TextField.class);
        TextField textFieldTitle = this.screen.findNiftyControl("testNameField", TextField.class);
        TextField textFieldNumPlayers = this.screen.findNiftyControl("input_numPlayer", TextField.class);
        TextField textFieldL2 = this.screen.findNiftyControl("input_numL2", TextField.class);
        TextField textFieldL3_a = this.screen.findNiftyControl("input_numL3_A", TextField.class);
        TextField textFieldL3_r = this.screen.findNiftyControl("input_numL3_S", TextField.class);
        TextField textAsk = this.screen.findNiftyControl("input_ask", TextField.class);
        TextField textFieldValue;

        this.num = 0;
        this.time=30;
        this.num_L2=0;
        this.num_L3_a=0;
        this.num_L3_r=0;

        if(textFieldTime.getRealText().length() != 0){
            this.time=Integer.parseInt(textFieldTime.getRealText());
            Settings.getSettings().setTime(Integer.parseInt(textFieldTime.getRealText()));
        }else{
            this.time = 30;
            Settings.getSettings().setTime(30);
        }


        if(textFieldL3_a.getRealText().length() == 0){
            this.num_L3_a=0;
        }else if(Integer.parseInt(textFieldL3_a.getRealText()) < 0){
            this.num_L3_a=0;
        }else {
            this.num_L3_a = Integer.parseInt(textFieldL3_a.getRealText());
        }
        Settings.getSettings().setNumL3_a(this.num_L3_a);
        Settings.getSettings().setNumVP(this.num_L3_a);
        if(textFieldL2.getRealText().length() == 0){
            this.num_L2=0;
        }else if(Integer.parseInt(textFieldL2.getRealText()) < 0){
            this.num_L2=0;
        }else {
            this.num_L2 = Integer.parseInt(textFieldL2.getRealText());
        }
        Settings.getSettings().setNumL2(this.num_L2);
        if(textFieldL3_r.getRealText().length() == 0){
            this.num_L3_r=0;
        }else if(Integer.parseInt(textFieldL3_r.getRealText()) < 0){
            this.num_L3_r=0;
        }else {
            this.num_L3_r = Integer.parseInt(textFieldL3_r.getRealText());
        }
        Settings.getSettings().setNumL3_r(this.num_L3_r);

        if(textAsk.getRealText().length() != 0){
            String temp =textAsk.getRealText();
            if (Objects.equals(temp, "Y")){
                this.ask=true;
            }else{
                this.ask=false;
            }
        }else{
            this.ask = false;
        }
        Settings.getSettings().setAsk(this.ask);

        TypeTest test=TypeTest.GROUP_WITH_VA;
        Settings.getSettings().setTest(test);
        if(textFieldNumPlayers.getRealText().length() == 0){
            textFieldNumPlayers.setText("mandatory");
        }else if(Integer.parseInt(textFieldNumPlayers.getRealText()) > 7 ||
                Integer.parseInt(textFieldNumPlayers.getRealText()) < 1){
            textFieldNumPlayers.setText("out of range");
        }else{
            this.num = Integer.parseInt(textFieldNumPlayers.getRealText());
            Settings.getSettings().setNumPlayers(this.num);

            int[] net = new int[this.num*this.num];
            boolean entered = false;

            for(int i = 0; i < this.num; i++){
                for(int y = 0; y < this.num; y++){
                    textFieldValue = this.screen.findNiftyControl(
                            Integer.toString(i+1).concat(Integer.toString(y+1)), TextField.class);

                    if(textFieldValue.getRealText().length() != 0 &&
                            Integer.parseInt(textFieldValue.getRealText()) != 0 &&
                            Integer.parseInt(textFieldValue.getRealText()) != 1){
                        entered = true;
                        textFieldValue.setText("XXX");
                    }else{
                        if(textFieldValue.getRealText().length() != 0 &&
                                i == y && Integer.parseInt(textFieldValue.getRealText()) == 1){
                            entered = true;
                            textFieldValue.setText("XXX");
                        }else{

                            if(textFieldValue.getRealText().length() == 0)
                                textFieldValue.setText("0");

                            //logger.info("cell [" + i + " " + y + "]: " + Integer.parseInt(textFieldValue.getRealText()));

                            net[y + this.num*i] = Integer.parseInt(textFieldValue.getRealText());
                            //System.out.println("index vector " + (y + this.num*i) + ": " + Integer.parseInt(textFieldValue.getRealText()));

                            if(y == this.num-1 && i == this.num-1 && entered == false){
                                Settings.getSettings().setNet(net);
                                //Path outputPath = Paths.get("test");
                                if (Settings.getSettings().getNumL2()>0) {
                                    this.cleanup();
                                    this.app.getStateManager().detach(this);
                                    ChooseL2ScreenAppState vpController = new ChooseL2ScreenAppState(this.app, textFieldTitle.getRealText());
                                    this.nifty.fromXml("Interface/chooseL2Admin.xml", "chooseL2Admin", new ScreenController[]{vpController});
                                    this.app.getStateManager().attach(vpController);
                                    this.nifty.gotoScreen("chooseL2Admin");
                                } else if (Settings.getSettings().getNumL3_a()>0) {
                                    this.cleanup();
                                    this.app.getStateManager().detach(this);
                                    ChooseL3_aScreenAppState vpController = new ChooseL3_aScreenAppState(this.app, textFieldTitle.getRealText());
                                    this.nifty.fromXml("Interface/chooseVPAdmin.xml", "chooseVPAdmin", new ScreenController[]{vpController});
                                    this.app.getStateManager().attach(vpController);
                                    this.nifty.gotoScreen("chooseVPAdmin");
                                } else if (Settings.getSettings().getNumL3_r()>0) {
                                    this.cleanup();
                                    this.app.getStateManager().detach(this);
                                    ChooseL3_rScreenAppState vpController = new ChooseL3_rScreenAppState(this.app, textFieldTitle.getRealText());
                                    this.nifty.fromXml("Interface/chooseL3_rAdmin.xml", "chooseL3_rAdmin", new ScreenController[]{vpController});
                                    this.app.getStateManager().attach(vpController);
                                    this.nifty.gotoScreen("chooseL3_rAdmin");
                                } else {
                                    quit();
                                }

                                //Path currentDirectory = Paths.get(System.getProperty("user.dir")); // Ottieni la cartella corrente
                                //Path outputPath = currentDirectory.resolve("Test_settings").toAbsolutePath(); // Combina con il nome della directory
                                //Settings.getSettings().save(outputPath.toString(),textFieldTitle.getRealText());
                                //cleanup();
                                //app.getStateManager().detach(this);
                                //StartScreenAppState startController = new StartScreenAppState(app);
                                //nifty.fromXml("Interface/startInterfaceAdmin.xml", "startAdmin", startController);
                                //app.getStateManager().attach(startController);
                                //nifty.gotoScreen("startAdmin");


                                /*
                                try {
                                    AdminRMI admin = new AdminRMI();

                                    if(admin.settingNetwork(Settings.getSettings().getTime(), this.num, Settings.getSettings().getNumVP(), net)){
                                        logger.info("Network was set.");
                                        Settings.getSettings().setNumPlayers(this.num);

                                        if(Settings.getSettings().getNumVP() == 0){
                                            cleanup();
                                            app.getStateManager().detach(this);

                                            SocialMemoryAppState setSocialMemory = new SocialMemoryAppState(app);
                                            nifty.fromXml("Interface/setSocialMemoryAdmin.xml", "socialMemory", setSocialMemory);
                                            app.getStateManager().attach(setSocialMemory);
                                            nifty.gotoScreen("socialMemory");

                                        }else{
                                            cleanup();
                                            app.getStateManager().detach(this);

                                            ChooseVPScreenAppState vpController = new ChooseVPScreenAppState(app);
                                            nifty.fromXml("Interface/chooseVPAdmin.xml", "chooseVPAdmin", vpController);
                                            app.getStateManager().attach(vpController);
                                            nifty.gotoScreen("chooseVPAdmin");
                                        }
                                    }else{
                                        logger.warning("Error with settings.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warning("Connection with Server not established.");
                                    quit();
                                }*/
                            }
                        }
                    }
                }
            }
        }
    }


    // update in real time the screen according to the selection of the known network in the dropdown
    @Override
    public void update(float tpf) {

        String network = networkTopology.getSelection();
        String direction = networkDirection.getSelection();
        int numPlayers = 0;
        int centerNode = 0;
        int numTrial= 0 ;
        int numL3_a=0;
        int numL3_r=0;
        int numL2=0;
        Label title = this.screen.findNiftyControl("single_node", Label.class); //cambia sequenza: con nodo centrale:

        TextField textFieldNumPlayers = this.screen.findNiftyControl("input_numPlayer", TextField.class);
        if(textFieldNumPlayers.getRealText().length() == 0)
            numPlayers = 0;
        else{
            if(Integer.parseInt(textFieldNumPlayers.getRealText()) > this.maxNumPlayers ||
                    Integer.parseInt(textFieldNumPlayers.getRealText()) < this.minNumPlayers)
                numPlayers = 0;
            else
                numPlayers = Integer.parseInt(textFieldNumPlayers.getRealText());
        }



        TextField textFieldL2 = this.screen.findNiftyControl("input_numL2", TextField.class);
        TextField textFieldL3_a = this.screen.findNiftyControl("input_numL3_A", TextField.class);
        TextField textFieldL3_r = this.screen.findNiftyControl("input_numL3_S", TextField.class);
        if(textFieldL3_a.getRealText().length() == 0){
            numL3_a=0;
        }else if(Integer.parseInt(textFieldL3_a.getRealText()) < 0){
            numL3_a=0;
        }else {
            numL3_a = Integer.parseInt(textFieldL3_a.getRealText());
        }

        if(textFieldL2.getRealText().length() == 0){
            numL2=0;
        }else if(Integer.parseInt(textFieldL2.getRealText()) < 0){
            numL2=0;
        }else {
            numL2 = Integer.parseInt(textFieldL2.getRealText());
        }

        if(textFieldL3_r.getRealText().length() == 0){
            numL3_r=0;
        }else if(Integer.parseInt(textFieldL3_r.getRealText()) < 0){
            numL3_r=0;
        }else {
            numL3_r = Integer.parseInt(textFieldL3_r.getRealText());
        }

        if(this.screen.findNiftyControl("input_node1", TextField.class).getRealText().length() == 0){
            centerNode = 0;
        }else{
            if(Integer.parseInt(this.screen.findNiftyControl("input_node1", TextField.class).getRealText()) > numPlayers ||
                    Integer.parseInt(this.screen.findNiftyControl("input_node1", TextField.class).getRealText()) <= 0)
                centerNode = 0;
            else
                centerNode = Integer.parseInt(this.screen.findNiftyControl("input_node1", TextField.class).getRealText());
        }

        boolean sequenceIsSet = false;
        int z = 0;
        do{
            String node = this.screen.findNiftyControl("input_node" + (z+1), TextField.class).getRealText();
            if(!node.isEmpty()){
                sequenceIsSet = true;
            }else
                sequenceIsSet = false;
            z++;
        }while(z < numPlayers && sequenceIsSet);

        de.lessvoid.nifty.elements.Element panel = this.screen.findElementByName("panel_nodes");

        de.lessvoid.nifty.elements.Element[] nodes = new de.lessvoid.nifty.elements.Element[this.maxNumPlayers];

        for(int i = 0; i < this.maxNumPlayers; i++){
            nodes[i] = this.screen.findElementByName("input_node" + (i+1));
        }

        de.lessvoid.nifty.elements.Element[][] nodesMat = new de.lessvoid.nifty.elements.Element[this.maxNumPlayers][this.maxNumPlayers];
        for(int i = 0; i < this.maxNumPlayers; i++){
            for(int j = 0; j < this.maxNumPlayers; j++){
                nodesMat[i][j] = this.screen.findElementByName(Integer.toString(i+1)+Integer.toString(j+1));
            }
        }

        de.lessvoid.nifty.elements.Element[] rows = new de.lessvoid.nifty.elements.Element[this.maxNumPlayers];
        for(int i = 0; i < this.maxNumPlayers; i++){
            rows[i] = this.screen.findElementByName("panel_rigo" + (i+1));
        }

        for(int i = 0; i < this.maxNumPlayers; i++){
            if ((i+1) > numPlayers){
                rows[i].hide();
            }else{
                rows[i].show();
            }
            for(int j = 0; j < this.maxNumPlayers; j++){
                if(j+1 > numPlayers){
                    nodesMat[i][j].hide();
                }else{
                    nodesMat[i][j].show();
                }
            }
        }
        String text="            p1";
        if (numPlayers!=0){
            for(int i=1;i<numPlayers;i++){
               text+="       p"+(i+1);
            }
            for(int i=numPlayers;i<7;i++){
                text+="           ";
            }
        }else{
            text="";
        }
        this.screen.findNiftyControl("initial_row", Label.class).setText(text);

        /*switch(numPlayers){
            case 1:
                this.screen.findNiftyControl("initial_row", Label.class).setText("             p1                                                             ");
                break;
            case 2:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2                                                       ");
                break;
            case 3:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2       p3                                            ");
                break;
            case 4:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2       p3       p4                                 ");
                break;
            case 5:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2       p3       p4       p5                    ");
                break;
            case 6:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2       p3       p4       p5       p6          ");
                break;
            case 7:
                this.screen.findNiftyControl("initial_row", Label.class).setText("               p1       p2       p3       p4       p5       p6       p7");
                break;
            default:
                this.screen.findNiftyControl("initial_row", Label.class).setText("");
                break;
        }*/

        //check if the option are changed
        this.currentDir = direction;
        this.currentNet = network;
        this.currentNum = numPlayers;
        this.currentTrial = numTrial;
        this.currentCenterNode = centerNode;

        int[] sequenceNode = new int[numPlayers];
        if(sequenceIsSet){
            for(int i = 0; i < numPlayers; i++){
                sequenceNode[i] = Integer.parseInt(this.screen.findNiftyControl("input_node" + (i+1), TextField.class).getRealText());
                this.currentSequence[i] = sequenceNode[i];
            }
        }else{
            for(int i = 0; i < numPlayers; i++){
                sequenceNode[i] = 0;
                this.currentSequence[i] = sequenceNode[i];
            }
        }

        if(this.currentNum != this.previousNum || this.currentTrial != this.previousTrial || !this.currentNet.equals(this.previousNet) || !this.currentDir.equals(this.previousDir)
                || this.currentCenterNode != this.previousCenterNode || isSequenceDifferent(this.previousSequence, this.currentSequence)){

            clearNetwork(numPlayers);

            switch(network){
                case "Complete graph":
                    //clearNetwork(numPlayers);
                    panel.hide();

                    if(direction.equals("Directed")){
                        for(int i = 0; i < numPlayers; i++){
                            for(int j = 0; j < numPlayers; j++){
                                if(i < j){
                                    this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("1");
                                }else{
                                    this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                }
                            }
                        }
                    }else if(direction.equals("Undirected")){
                        for(int i = 0; i < numPlayers; i++){
                            for(int j = 0; j < numPlayers; j++){
                                if(i == j){
                                    this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                }else{
                                    this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("1");
                                }
                            }
                        }
                    }
                    break;

                case "Star graph":
                    //clearNetwork(numPlayers);
                    title.setText("Center node: ");
                    panel.show();
                    nodes[0].show();


                    for(int i = 1; i < this.maxNumPlayers; i++){
                        nodes[i].hide();
                        this.screen.findNiftyControl("input_node" + (i+1), TextField.class).setText("");
                    }

                    if(centerNode != 0){

                        if(direction.equals("Directed")){
                            logger.info("center node: " + centerNode);
                            for(int i = 0; i < numPlayers; i++){
                                for(int j = 0; j < numPlayers; j++){
                                    if((j+1) == centerNode && (i+1) != centerNode){
                                        this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("1");
                                    }else{
                                        this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                    }
                                }
                            }
                        }else if(direction.equals("Undirected")){
                            for(int i = 0; i < numPlayers; i++){
                                for(int j = 0; j < numPlayers; j++){
                                    if((i+1) == centerNode){
                                        if((j+1) != centerNode){
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("1");
                                        }else{
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                        }
                                    }else{
                                        if((j+1) == centerNode){
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("1");
                                        }else{
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                        }
                                    }

                                }
                            }
                        }
                    }

                    break;

                case "Path graph":
                case "Ring graph":
                    //clearNetwork(numPlayers);
                    title.setText("Sequence: ");
                    panel.show();
                    for(int i = 0; i < nodes.length; i++){
                        if(i < numPlayers)
                            nodes[i].show();
                        else
                            nodes[i].hide();
                    }

                    // check that the sequence is set
                    // check if the sequence contains different value

                    if(sequenceIsSet){

                        if(!sequenceIsCorrect(sequenceNode)){

                            for(int i = 0; i < sequenceNode.length; i++){
                                this.screen.findNiftyControl("input_node" + (i+1), TextField.class).setText("");
                            }

                        }else{

                            if(direction.equals("Directed")){

                                for(int n = 0; n < numPlayers-1; n++){
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[n])+Integer.toString(sequenceNode[n+1]), TextField.class).setText("1");
                                }
                                if(network.equals("Ring graph")){
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[numPlayers-1])+Integer.toString(sequenceNode[0]), TextField.class).setText("1");
                                }
                                for(int i = 0; i < numPlayers; i++){
                                    for(int j = 0; j < numPlayers; j++){
                                        if(this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).getRealText().isEmpty()){
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                        }
                                    }
                                }

                            }else if(direction.equals("Undirected")){
                                for(int n = 0; n < numPlayers-1; n++){
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[n])+Integer.toString(sequenceNode[n+1]), TextField.class).setText("1");
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[n+1])+Integer.toString(sequenceNode[n]), TextField.class).setText("1");
                                }
                                if(network.equals("Ring graph")){
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[numPlayers-1])+Integer.toString(sequenceNode[0]), TextField.class).setText("1");
                                    this.screen.findNiftyControl(Integer.toString(sequenceNode[0])+Integer.toString(sequenceNode[numPlayers-1]), TextField.class).setText("1");
                                }
                                for(int i = 0; i < numPlayers; i++){
                                    for(int j = 0; j < numPlayers; j++){
                                        if(this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).getRealText().isEmpty()){
                                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    break;
                case "Solo graph":
                    panel.hide();
                    for(int i = 0; i < numPlayers; i++){
                        for(int j = 0; j < numPlayers; j++){
                            this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("0");
                        }
                    }
                    break;
                case "User defined graph":
                    panel.hide();
                    break;
                default:
                    break;
            }
        }

        this.previousDir = this.currentDir;
        this.previousNet = this.currentNet;
        this.previousNum = this.currentNum;
        this.previousTrial = this.currentTrial;
        this.previousCenterNode = this.currentCenterNode;

        for(int i = 0; i < numPlayers; i++){
            this.previousSequence[i] = this.currentSequence[i];
        }
    }

    private boolean isSequenceDifferent(int[] previous, int[] current){
        boolean isDifferent = false;
        int i = 0;
        do{
            if(previous[i] != current[i])
                isDifferent = true;
            i++;
        }while(i < previous.length && isDifferent == false);

        if(isDifferent)
            clearNetwork(previous.length);

        return isDifferent;
    }

    private void clearNetwork(int numPlayers){
        for(int i = 0; i < numPlayers; i++){
            //this.screen.findNiftyControl("input_node" + (i+1), TextField.class).setText("");

            for(int j = 0; j < numPlayers; j++){
                this.screen.findNiftyControl(Integer.toString(i+1)+Integer.toString(j+1), TextField.class).setText("");
            }
        }
    }

    private boolean sequenceIsCorrect(int[] array){
        Set<Integer> set = new HashSet<Integer>();
        int min = array[0];
        int max = array[0];
        boolean find = false;

        for(int i = 0; i < array.length; i++){
            if(set.add(array[i]) == false)
                find = true;
        }

        for(int i = 1; i < array.length; i++){
            if(array[i] > max)
                max = array[i];
            if(array[i] < min)
                min = array[i];
        }

        return (find == false && min==1 && max==array.length);
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
}