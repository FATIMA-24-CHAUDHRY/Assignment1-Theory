class Message {
    private static int idCounter = 0;
    private int id;
    private int senderId;
    private int receiverId;
    private String text;
    private String timestamp;
    private boolean isRead;

    public Message(int senderId, int receiverId, String text, String timestamp) {
        this.id = ++idCounter;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getText() {
        return text;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void displayMessage() {
        System.out.println("[ID: " + id + "] From: " + senderId + " To: " + receiverId + " | " + timestamp + " | Read: " + isRead);
        System.out.println("Message: " + text);
    }
}
