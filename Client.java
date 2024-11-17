import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;
    private static final MessageManager messageManager = new MessageManager();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(in.readLine()); // Welcome message from server

            // Start a thread to listen for incoming messages from the server
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\n[SERVER]: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Connection to server lost.");
                }
            }).start();

            //  menu interface
            while (true) {
                displayMenu();
                int choice = getValidatedChoice(scanner);

                switch (choice) {
                    case 1 -> sendMessage(scanner, out);
                    case 2 -> messageManager.displayAllMessages();
                    case 3 -> markMessagesAsRead(scanner);
                    case 4 -> searchMessages(scanner);
                    case 5 -> deleteMessages(scanner);
                    case 6 -> {
                        System.out.println("Exiting...");
                        out.println("Goodbye");
                        break;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                if (choice == 6) break;
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to connect to the server. Please try again later.");
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Send Message");
        System.out.println("2. Display All Messages");
        System.out.println("3. Mark Messages as Read");
        System.out.println("4. Search Messages");
        System.out.println("5. Delete Messages for Contact");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getValidatedChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid option: ");
            scanner.next(); // Discard invalid input
        }
        return scanner.nextInt();
    }

    private static void sendMessage(Scanner scanner, PrintWriter out) {
        try {
            System.out.print("Enter your ID: ");
            int senderId = getValidatedInput(scanner);

            System.out.print("Enter receiver ID: ");
            int receiverId = getValidatedInput(scanner);

            scanner.nextLine(); // Consume newline
            System.out.print("Enter your message: ");
            String message = scanner.nextLine();
            String timestamp = new java.util.Date().toString();

            // Send message to the server
            out.println(senderId + "|" + receiverId + "|" + message + "|" + timestamp);

            // Store message locally
            messageManager.sendMessage(senderId, receiverId, message, timestamp);
            System.out.println("Message sent!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int getValidatedInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            scanner.next(); // Discard invalid input
        }
        return scanner.nextInt();
    }

    private static void markMessagesAsRead(Scanner scanner) {
        System.out.print("Enter contact ID to mark messages as read: ");
        int contactId = getValidatedInput(scanner);
        messageManager.markMessageStatus(contactId, true);
        System.out.println("Messages marked as read.");
    }

    private static void searchMessages(Scanner scanner) {
        System.out.print("Enter sender ID to search messages (or -1 for all): ");
        int senderId = getValidatedInput(scanner);

        scanner.nextLine(); // Consume newline
        System.out.print("Enter search query (leave blank for all messages): ");
        String query = scanner.nextLine();

        messageManager.searchMessages(query, senderId);
    }

    private static void deleteMessages(Scanner scanner) {
        System.out.print("Enter contact ID to delete messages for: ");
        int contactId = getValidatedInput(scanner);
        messageManager.deleteMessagesForContact(contactId);
        System.out.println("Messages for contact " + contactId + " have been deleted.");
    }
}

