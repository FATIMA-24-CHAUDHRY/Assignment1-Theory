class Reciever {
    public void receiveMessage(int senderId, int receiverId, String text, String timestamp, MessageManager manager) {
        manager.receiveMessage(senderId, receiverId, text, timestamp);
    }
}