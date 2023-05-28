package store;
                                                                             
/**
 * remote interface for Store services
 */
public interface Store extends java.rmi.Remote {
	
 
	float getPrice(String ingredient) throws java.rmi.RemoteException;       
	String getNameMag() throws java.rmi.RemoteException;       
}                                                                               
             


