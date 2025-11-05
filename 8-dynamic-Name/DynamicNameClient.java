import java.io.*;
import java.net.*;

public class DynamicNameClient {
    public static void main(String[] args) {
        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 5000;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.print("Enter domain name (e.g., www.google.com): ");
            String domain = userInput.readLine();

            out.println(domain);        // Send to server
            String response = in.readLine(); // Receive IP

            System.out.println("Server Response: " + response);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
