import java.io.*;
import java.net.*;

public class DynamicNameServer {
    public static void main(String[] args) {
        final int PORT = 5000;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Name Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String domain = in.readLine();

                if (domain == null || domain.trim().isEmpty()) {
                    out.println("Invalid domain name.");
                    continue;
                }

                try {
                    InetAddress inetAddress = InetAddress.getByName(domain);
                    String resolvedIP = inetAddress.getHostAddress();
                    System.out.println("Resolved " + domain + " -> " + resolvedIP);
                    out.println("Resolved IP Address: " + resolvedIP);
                } catch (UnknownHostException e) {
                    System.out.println("Could not resolve domain: " + domain);
                    out.println("Error: Could not resolve domain " + domain);
                }

                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

