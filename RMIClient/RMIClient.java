import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.ImageIcon;
/* @author fanis gerodimos*/

public class RMIClient extends UnicastRemoteObject implements RMICallBack{
    //Interface Layout impement a simple Jframe
    //here call its constructor for initialise object components
    public Layout myLayout=new Layout();
    //boolean that helps to change the png image file in JLabel component
    private boolean flag=true;
    public RMIClient() throws RemoteException{
        super();
    }
    //Callback method that server calls when an event is created
    @Override
    public void callBack(String msg) throws RemoteException{
        System.out.println(msg);
        myLayout.appendText("Movement event caught on:"+msg);
        movementSimulation();
    }
    //Method that simulate the image streaming when an even is ocurred
    private void movementSimulation(){
        ImageIcon icon;
        flag=!(flag);
        if(flag){
             icon=new javax.swing.ImageIcon(getClass().getResource("/movement1.png"));
        }else{
             icon=new javax.swing.ImageIcon(getClass().getResource("/movement2.png"));
        }
        myLayout.setImage(icon);
    }
    public static void main(String args[]) throws RemoteException, NotBoundException{
        String retText;
        //Whenever a client is called, an object client is initialised 
        RMIClient client=new RMIClient();
        //get registry and lookup for the Server remote object
        Registry reg = LocateRegistry.getRegistry("127.0.0.1",1099);
        RMI rmi = (RMI)reg.lookup("Server");
        System.out.println("Connected to Server:");
        try{
            //call remote register method that register a client reference into a listArray that keeps server
            //for being able to call back when new events are ocurred.
            rmi.register(client);
        }catch (RemoteException e){
            System.out.println(e);
        }
        //Initilise GUI for each client
        client.initLayout(rmi);
        //addShutdownHook() will deregister the client on a Program's termination.
        //remote call deregister remove client from listArray clientList of server.  
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try{
                    rmi.deregister(client); 
                }catch (RemoteException e){
                    System.out.println(e);
                }
            }
        }));
    }
    public void initLayout(RMI rmi) throws RemoteException{
        myLayout.setVisible(true);
    }
}
