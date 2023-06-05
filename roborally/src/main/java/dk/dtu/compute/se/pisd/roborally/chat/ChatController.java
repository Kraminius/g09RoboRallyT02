package dk.dtu.compute.se.pisd.roborally.chat;
import dk.dtu.compute.se.pisd.roborally.chat.ChatView.OnSendMessageListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatController implements OnSendMessageListener {
    private ChatServer chatServer;
    private List<ChatClient> chatClients;
    private List<ChatView> chatViews;

    public ChatController() {
        this.chatClients = new ArrayList<>();
        this.chatViews = new ArrayList<>();
    }

    public void startServer(int serverPort) {
        try {
            this.chatServer = new ChatServer(serverPort);
            Thread serverThread = new Thread(() -> {
                chatServer.startServer();
            });
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String serverIP, int serverPort) {
        ChatClient chatClient = new ChatClient();
        ChatView clientChatView = new ChatView(chatClient);

        String username = clientChatView.promptForUsername();
        System.out.println("Username gotten from prompt = " + username);

        try {
            chatClient.connectToServer(serverIP, serverPort);
            chatClient.sendUserName(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatClient.setUserName(username);
        clientChatView.personalizeChatWindow(username);

        chatClients.add(chatClient);
        chatViews.add(clientChatView);

        clientChatView.setOnSendMessageListener(this);

        System.out.println("Connected to server: " + chatServer.getServerIP() + ":" + chatServer.getServerPort());
        System.out.println("Local client address: " + chatClient.getClientIP() + ":" + chatClient.getClientPort());

        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    String receivedMessage = chatClient.receiveMessage();
                    clientChatView.displayMessage(receivedMessage);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveThread.start();
        clientChatView.show();
    }

    public String getServerIP() {
        return chatServer.getServerIP();
    }

    public int getServerPort() {
        return chatServer.getServerPort();
    }

    @Override
    public void onSendMessage(ChatClient senderClient, String message) {
        System.out.println("called onSendMessage()");
        String sender = senderClient.getUserName();
        System.out.println(sender);
        for (ChatClient chatClient : chatClients) {
            if (chatClient.getUserName().equals(sender)) {
                chatClient.sendMessage(message);
            }
        }
        for (ChatView clientChatView : chatViews) {
            if(clientChatView.getClient().equals(senderClient))
            clientChatView.displayMessage(message);
        }
    }
}