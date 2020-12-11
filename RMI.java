import java.rmi.Remote;
import java.rmi.RemoteException;
//Interface that declare the server's remote methods 
public interface RMI extends Remote{
   public String getData(String text) throws RemoteException;
   public void register(RMICallBack obj) throws RemoteException;
   public void deregister(RMICallBack obj) throws RemoteException;
}
