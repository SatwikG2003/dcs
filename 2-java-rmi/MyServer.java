import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

public class MyServer {
    public static void main(String args[]) {
        try {
            Adder stub = new AdderRemote();
            LocateRegistry.createRegistry(3000);
            Naming.rebind("rmi://localhost:3000/Pooja", stub);
            System.out.println("Server ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
