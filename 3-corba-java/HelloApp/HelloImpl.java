package HelloApp;

import org.omg.CORBA.*;

public class HelloImpl extends HelloPOA {
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public String sayHello(String name) {
        System.out.println("Request from client: " + name);
        return "Hello, " + name + " (from CORBA Server)";
    }
}
