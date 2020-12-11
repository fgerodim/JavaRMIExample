import java.rmi.Remote;
import java.rmi.RemoteException;
//Interface for exporting user's call back method
public interface RMICallBack extends Remote{
   public void callBack(String msg) throws RemoteException;
}
