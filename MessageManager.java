import java.util.ArrayList;

class MessageManager {
    private ArrayList<Message> messages = new ArrayList<>();

    // Method to send a message
    public void sendMessage(int senderId, int receiverId, String text, String timestamp) {
        Message message = new Message(senderId, receiverId, text, timestamp);
        messages.add(message);
        System.out.println("Message sent successfully!");
    }

    // Method to receive a message
    public void receiveMessage(int senderId, int receiverId, String text, String timestamp) {
        Message message = new Message(senderId, receiverId, text, timestamp);
        messages.add(message);
        System.out.println("Message received and stored!");
    }

    // Mark messages as read or unread
    public void markMessageStatus(int contactId, boolean isRead) {
        boolean updated = false;
        for (Message message : messages) {
            if (message.getSenderId() == contactId || message.getReceiverId() == contactId) {
                message.setRead(isRead);
                updated = true;
            }
        }
        if (updated) {
            System.out.println("Messages for contact " + contactId + " marked as " + (isRead ? "read" : "unread") + ".");
        } else {
            System.out.println("No messages found for contact " + contactId + ".");
        }
    }

    // Search messages by a query and optional sender ID
    public void searchMessages(String query, int senderId) {
        boolean found = false;
        for (Message message : messages) {
            if ((senderId == -1 || message.getSenderId() == senderId) &&
                    (query.isEmpty() || message.getText().toLowerCase().contains(query.toLowerCase()))) {
                message.displayMessage();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages.");
        }
    }

    // Delete messages for a contact
    public void deleteMessagesForContact(int contactId) {
        boolean removed = messages.removeIf(message -> message.getSenderId() == contactId || message.getReceiverId() == contactId);
        if (removed) {
            System.out.println("All messages for contact " + contactId + " have been deleted.");
        } else {
            System.out.println("No messages found for contact " + contactId + " to delete.");
        }
    }

    // Delete a specific message by its unique ID
    public void deleteMessageById(int messageId) {
        boolean removed = messages.removeIf(message -> message.getId() == messageId);
        if (removed) {
            System.out.println("Message with ID " + messageId + " has been deleted.");
        } else {
            System.out.println("Message with ID " + messageId + " not found.");
        }
    }

    // Display all messages for a contact
    public void displayMessagesForContact(int contactId) {
        boolean found = false;
        for (Message message : messages) {
            if (message.getSenderId() == contactId || message.getReceiverId() == contactId) {
                message.displayMessage();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for contact " + contactId + ".");
        }
    }

    // Display all messages
    public void displayAllMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages to display.");
        } else {
            for (Message message : messages) {
                message.displayMessage();
            }
        }
    }
}


