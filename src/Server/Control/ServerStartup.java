package Server.Control;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;

public class ServerStartup {

	private static int PORT = 1024;
	private static Registry registryAdmin;
	private static Registry[] registryPlayer;

	public ServerStartup() {

	}
	public void startServer(int player) {

		try {
			this.registryAdmin = LocateRegistry.createRegistry(PORT);
			AdminCoordinator adminCoordinator = new AdminCoordinator();
			this.registryAdmin.rebind("ServiceAdmin", adminCoordinator);
			this.registryPlayer = new Registry[player];
			for(int i = 1; i <= player; i++) {
				this.registryPlayer[i - 1] = LocateRegistry.createRegistry(PORT + i);
				PlayerCoordinator playerCoordinator = new PlayerCoordinator();
				this.registryPlayer[i-1].rebind("ServicePlayer", playerCoordinator);
				System.out.println ("Player " + i + " registered on port " + (PORT+i) );	
			}
		}catch(Exception e){
			System.err.println("The Server has found a problem with the opening "
					+ "of the connection" );
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Server ready");
	}
	public void turnOffServer() {
		try {
			// Unbind all registered objects
			this.registryAdmin.unbind("ServiceAdmin");
			UnicastRemoteObject.unexportObject(this.registryAdmin, true);
			for(int i = 1; i <= 7; i++) {
				if(this.registryPlayer[i-1].list()!=null){
					this.registryPlayer[i-1].unbind("ServicePlayer");
					UnicastRemoteObject.unexportObject(this.registryPlayer[i-1], true);
				}
			}
			System.out.println("Server turned off gracefully.");
		} catch (Exception e) {
			System.err.println("Error while turning off the server:");
			e.printStackTrace();
		}
	}
}
