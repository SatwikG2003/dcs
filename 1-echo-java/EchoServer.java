import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EchoServer {
    private static final int PORT = 5000;
    private static final int MAX_CLIENTS = 50;

    // To assign unique IDs to clients (Client 1, 2, 3...)
    private static AtomicInteger clientCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService clientPool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientCounter.incrementAndGet(); // Assign unique client number

                System.out.println("Client " + clientId + " connected: " +
                        clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                clientPool.execute(new ClientHandler(clientId, clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            clientPool.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final int clientId;
    private final Socket socket;

    public ClientHandler(int clientId, Socket socket) {
        this.clientId = clientId;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from Client " + clientId + ": \"" + message + "\"");
                out.println("Echo from server: " + message);
            }
        } catch (IOException e) {
            System.err.println("Client " + clientId + " error: " + e.getMessage());
        } finally {
            try {
                System.out.println("Client " + clientId + " disconnected (" + socket.getInetAddress() + ")");
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing Client " + clientId + " socket: " + e.getMessage());
            }
        }
    }
}
