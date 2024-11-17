class Sender {
    public void sendMessage(int senderId, int receiverId, String text, String timestamp, MessageManager manager) {
        manager.sendMessage(senderId, receiverId, text, timestamp);
    }
}