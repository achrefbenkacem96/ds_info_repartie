package client;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
 

import store.Store;

 
public class StoreClient {

    // the result message to be displayed to the user
    static String message = "";

    // displays the result of the search for the minimum ingredient price
    public void displayResult() {
        System.out.println(message);
    }

    public static void main(String args[]) {

        // check input arguments
        if (args.length < 2) {
            System.out.println("At least three parameters: " +
                    " 0) host-name; {host-name}...   1) product;");
            System.exit(1);
        }

        float price = 0; // stores the price inquiry result from each store
        float leastExpensive = Float.MAX_VALUE; // stores the lowest price (found thus far)
        String leastExpensiveSite = "no site"; // the name of the least expensive store (found thus far)

        // create a client instance
        StoreClient client = new StoreClient();

        // the name of the remote host on which runs the Store server to be inquired about its prices
        String hostName;

        // the rmiregistry port

        // the name of the remote service
        String name = "StoreService";

        hostName = "DESKTOP-JRMJUM1";
        // the product to look for
        String mag = "";

        String product = args[0];
        int i = 1;
        int rmiPort = Integer.parseInt(args[1]);
        while(true) {
            rmiPort += 1;

             // get the reference to the remote Store service to inquire
            // then inquire the service about the price of the product
            try {

                // the name of the remote host on which run the rmiregistry and store service to inquire


                Registry registry = LocateRegistry.getRegistry(hostName, rmiPort);

                // lookup the store service
                Store store = (Store) registry.lookup(name);
 
                price = store.getPrice(product);
                if (leastExpensive > price) {
                	leastExpensive = price;
                	mag = store.getNameMag();
                }
            } catch (Exception e) {
              break;
            }
 
            // determine the minimum price considering the new price from the last inquiry
        }

        // display the message announcing the store with the cheapest product
        message = " the minimum price of " + args[0] + " is " + leastExpensive +" in : " +mag;
        client.displayResult();
    }
}