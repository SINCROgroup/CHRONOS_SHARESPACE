package Administrator.Presentation;

import Server.RMIInterface.ServiceAdmin;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1024);
            ServiceAdmin adder = (ServiceAdmin)registry.lookup("ServiceAdmin");
            // Ottieni l'oggetto RMI utilizzando il nome registrato
            //Adder adder = (Adder) Naming.lookup("rmi://localhost:5000/sonoo");

            // Ora puoi chiamare i metodi dell'oggetto remoto
            int result = adder.add(3, 4);
            System.out.println("Risultato: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

