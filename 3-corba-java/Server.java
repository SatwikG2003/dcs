import HelloApp.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class Server {
    public static void main(String args[]) {
        try {
            // Create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // Get reference to rootpoa & activate the POA manager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create servant and register it with the ORB
            HelloImpl helloServant = new HelloImpl();
            helloServant.setORB(orb);

            // Get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloServant);
            Hello href = HelloHelper.narrow(ref);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the object reference in Naming
            String name = "HelloService";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("Server ready and waiting ...");
            
            // Wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        System.out.println("Server Exiting ...");
    }
}
