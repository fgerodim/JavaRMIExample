import static java.lang.Thread.sleep;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
 
public class RMIServer extends UnicastRemoteObject implements RMI{
    private ArrayList clientList;
    public RMIServer() throws RemoteException{
        super();
        //keep a client lists to knows the reference of the callBack() method's object
        clientList = new ArrayList(); 
    }
    //interface's methods implementetion. The methods that offers server for remote invocation 
    @Override
    public String getData(String text) throws RemoteException {
        text=text+this.createEvents();
        return text;
    }
    //these methods are for registering and deregistering a client from the client list
    @Override
    public void register(RMICallBack obj)throws RemoteException{
       clientList.add(obj);
    }
    @Override
    public void deregister(RMICallBack obj) throws RemoteException{
        clientList.remove(obj);
    }
    //method that creates movement event depending on the Date Class
    public String createEvents(){
            Date date = new Date();
    //Randomise the wait time between 2 and 5 seconds
            Random rn = new Random();
            int delay = 2+rn.nextInt(4);
            try {
                sleep(delay*1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                System.out.println("Unable to sleep");
            }
            String data=date.toString();
            //Verificate the active clients 
            System.out.println("Active clients:"+clientList.size());
            //Return the event created 
            return data;
        
            
    }
    //method that simulates the servers function
    public void simulation()throws RemoteException{
        //server never exit. This happens only manually
        //for this reason i use a while true loop
        while(true){         
                String msg=createEvents();
                //java iterator that helps to call back all active clients
                Iterator<RMICallBack> iterator = clientList.iterator();
		while (iterator.hasNext()) {
			iterator.next().callBack(msg);
		}
            }
    }
    //method that bind server object for remote call invocation in the default port 
    public void rebindServer(){
    try{
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Server", this);
            System.out.println("Server started");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void main(String args[])throws RemoteException{
        RMIServer server=new RMIServer();
        server.rebindServer();
        server.simulation();
    }
}
