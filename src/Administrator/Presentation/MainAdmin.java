package Administrator.Presentation;

import Server.Control.ServerStartup;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
// Main class administrator
public class MainAdmin extends SimpleApplication{
    public static void main(String[] args) {
        MainAdmin app = new MainAdmin();
        
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Mirror Game");
        settings.setResolution(800, 600);
        settings.setVSync(true);
        settings.setFrameRate(100);
        settings.setSamples(4);          
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    // first screen to display at the beginning (startInterfaceAdmin)
    @Override
    public void simpleInitApp(){ 
        setDisplayFps(false);
        setDisplayStatView(false);
        inputManager.setCursorVisible(true);
        //flyCam.setDragToRotate(false);
        flyCam.setEnabled(false);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,inputManager,audioRenderer, guiViewPort);       
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        StartServerAppState startAdminController = new StartServerAppState(this);
        nifty.fromXml("Interface/StartServer.xml", "settServer", startAdminController);
        stateManager.attach(startAdminController);
         nifty.gotoScreen("setServer");
    }


}
