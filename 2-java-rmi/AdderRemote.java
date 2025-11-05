import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class AdderRemote extends UnicastRemoteObject implements Adder {

    AdderRemote() throws RemoteException {
        super();
    }
    public int add(int x, int y, int clientPort) {
        int sum = x + y;
        try {
            String clientHost = RemoteServer.getClientHost();
            System.out.println("Client [" + clientHost + ":" + clientPort + "] requested: " + x + " + " + y);
            System.out.println("Sum is: " + sum);
        } catch (Exception e) {
            System.out.println("Could not fetch client host. Request: " + x + " + " + y + " = " + sum);
        }
        return sum;
    }
}
