import java.io.*;
import java.net.*;

public class EchoClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("Connected to Echo Server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            System.out.println("Type message (type 'exit' to quit):");

            String userInput;
            while (true) {
                System.out.print("You: ");
                userInput = console.readLine();

                if (userInput == null || "exit".equalsIgnoreCase(userInput.trim())) {
                    System.out.println("Disconnecting from server...");
                    break;
                }

                out.println(userInput); // send to server
                String response = in.readLine(); // read server echo

                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }
                System.out.println("Server: " + response);
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown server: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}

