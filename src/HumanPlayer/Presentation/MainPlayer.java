package HumanPlayer.Presentation;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

public class MainPlayer extends SimpleApplication{


    public static void main(String[] args) {

        MainPlayer app = new MainPlayer();

        AppSettings settings = new AppSettings(true);

        settings.setTitle("Mirror Game");
        settings.setResolution(800, 600);
        //settings.setFullscreen(true);
        settings.setVSync(true);
        settings.setFrameRate(100);
        settings.setSamples(4);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);

        app.start();
    }


    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        inputManager.setCursorVisible(true);
        //flyCam.setDragToRotate(false);
        flyCam.setEnabled(false);

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,inputManager,audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();

        StartScreenNamePlayerAppState startPlayerController = new StartScreenNamePlayerAppState(this);
        nifty.fromXml("Interface/startInterfaceNamePlayer.xml", "startPlayer", startPlayerController);
        stateManager.attach(startPlayerController);

        guiViewPort.addProcessor(niftyDisplay);
    }
}
