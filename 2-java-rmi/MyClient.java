
import java.rmi.*;
import java.util.Scanner;
import java.net.*;


public class MyClient {
    public static void main(String args[]) {
        try {
            Adder stub = (Adder) Naming.lookup("rmi://localhost:3000/Pooja");

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter first number: ");
            int a = sc.nextInt();

            System.out.print("Enter second number: ");
            int b = sc.nextInt();

            Socket tempSocket = new Socket("localhost", 3000);
            int clientPort = tempSocket.getLocalPort();
            tempSocket.close();

            int result = stub.add(a, b, clientPort);
            System.out.println("Sum from server: " + result);

            sc.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
