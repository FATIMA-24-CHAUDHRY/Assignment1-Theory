import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1234;
    private static final ConcurrentHashMap<Integer, Socket> clientSockets = new ConcurrentHashMap<>();
    private static final MessageManager messageManager = new MessageManager();

    public static void main(String[] args) {
        System.out.println("Server started. Waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientSockets.size() + 1; // Assign a new client ID
                clientSockets.put(clientId, clientSocket);

                System.out.println("Client " + clientId + " connected!");

                // Start a new thread to handle the client
                new Thread(new ClientHandler(clientId, clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {
        private final int clientId;
        private final Socket socket;

        public ClientHandler(int clientId, Socket socket) {
            this.clientId = clientId;
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("Welcome Client " + clientId + " to the Messaging Server!");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Client " + clientId + " says: " + message);

                    // Parse the message
                    String[] parts = message.split("\\|");
                    if (parts.length == 4) {
                        try {
                            int senderId = Integer.parseInt(parts[0]);
                            int receiverId = Integer.parseInt(parts[1]);
                            String text = parts[2];
                            String timestamp = parts[3];

                            // Store the message using MessageManager
                            messageManager.sendMessage(senderId, receiverId, text, timestamp);

                            // Forward the message to the receiver if connected
                            Socket receiverSocket = clientSockets.get(receiverId);
                            if (receiverSocket != null) {
                                PrintWriter receiverOut = new PrintWriter(receiverSocket.getOutputStream(), true);
                                receiverOut.println("From Client " + senderId + ": " + text);
                                out.println("Message delivered to Client " + receiverId);
                            } else {
                                out.println("Client " + receiverId + " is not connected. Message stored.");
                            }
                        } catch (NumberFormatException e) {
                            out.println("Error: Invalid message format. Please use: senderId|receiverId|text|timestamp");
                        }
                    } else {
                        out.println("Error: Invalid message format. Please use: senderId|receiverId|text|timestamp");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error with Client " + clientId + ": " + e.getMessage());
            } finally {
                disconnectClient();
            }
        }

        private void disconnectClient() {
            try {
                clientSockets.remove(clientId);
                socket.close();
                System.out.println("Client " + clientId + " disconnected.");
            } catch (IOException e) {
                System.err.println("Error while disconnecting Client " + clientId + ": " + e.getMessage());
            }
        }
    }
}

