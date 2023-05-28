package manager;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import store.Store;

/**
 * Class representing the remote Store objects/services.
 * Instances of this class can be registered with the RMI registry and called remotely.
 */
public class StoreManager extends UnicastRemoteObject implements Store {

    private static final long serialVersionUID = 1L;
    private static final String SERVICE_NAME = "StoreService";
    private String fileName ; 
    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// Arrays to store ingredient names and prices
    private String[] ingredientNames;
    private float[] ingredientPrices;


    public StoreManager(String fileName) throws RemoteException, IOException {
        super();
        ingredientNames = new String[100];
        ingredientPrices = new float[100];
        this.fileName = fileName;

        try (BufferedReader ingredients = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            int lineCounter = 0;

            while ((line = ingredients.readLine()) != null) {
                System.out.println("Ingredient number " + i + ": " + line);

                if ((lineCounter % 2) == 0) {
                    ingredientNames[i] = line;
                } else {
                    ingredientPrices[i] = Float.parseFloat(line);
                    i++;
                }

                lineCounter++;
            }
        }
    }

    
    @Override
    public float getPrice(String ingredient) throws RemoteException {
        System.out.println("The StoreManager is looking for the price of: " + ingredient);

        for (int i = 0; i < ingredientNames.length; i++) {
            if (ingredientNames[i] != null && ingredientNames[i].equals(ingredient)) {
                return ingredientPrices[i];
            }
        }

        return -1;
    }

    public static void main(String args[]) {
        // Check input parameters
        if (args.length != 2) {
            System.out.println("Two parameters required: 1) rmi-registry port; 2) file name");
            System.exit(1);
        }

        try {
            // Create a StoreManager object
            // This store will manage the products in the MagX file given as a parameter (args[1])
            StoreManager storeServer = new StoreManager(args[1]);
             // The rmi-registry port
            int rmiPort = Integer.parseInt(args[0]);

            // Create the RMI registry on the specified port (default port 1099)
            Registry registry = LocateRegistry.createRegistry(rmiPort);

            // Register the Store instance with the RMI registry
            registry.rebind(SERVICE_NAME, storeServer);

            System.out.println("StoreManager server bound: " + SERVICE_NAME);
            InetAddress localhost = InetAddress.getLocalHost();
            String hostname = localhost.getHostName();
         } catch (RemoteException e) {
            System.out.println("Store Manager error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

	@Override
	public String getNameMag() throws RemoteException {
		// TODO Auto-generated method stub
		return this.fileName;
	}
}