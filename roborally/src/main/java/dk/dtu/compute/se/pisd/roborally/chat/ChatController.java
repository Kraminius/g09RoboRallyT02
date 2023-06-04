package dk.dtu.compute.se.pisd.roborally.chat;
import dk.dtu.compute.se.pisd.roborally.chat.ChatView.OnSendMessageListener;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatController implements OnSendMessageListener {
    private ChatServer chatServer;
    private List<ChatClient> chatClients;
    private List<ChatView> chatViews;

    public ChatController () {
        this.chatClients = new ArrayList<>();
        this.chatViews = new ArrayList<>();
    }

    public void startServer(int serverPort){
        try {
            this.chatServer = new ChatServer(serverPort);
            Thread serverThread = new Thread(() -> {
                chatServer.startServer();
            });
            serverThread.start();
        } catch (IOException e){
            //TODO something
        }
    }

    public void addClient(String serverIP, int serverPort, String userName){
        ChatClient chatClient = new ChatClient(serverIP, serverPort, userName);
        chatClients.add(chatClient);

        ChatView clientChatView = new ChatView(chatClient);
        chatViews.add(clientChatView);

        clientChatView.setOnSendMessageListener(this);

        System.out.println("Connected to server: " + chatServer.getServerIP() + ":" + chatServer.getServerPort());
        System.out.println("Local client address: " + chatClient.getClientIP() + ":" + chatClient.getClientPort() + "\nName: " + chatClient.getUserName());

        Thread receiveThread = new Thread(() -> {
            try {
                while(true){
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

    public String getServerIP(){
        return chatServer.getServerIP();
    }

    public int getServerPort(){
        return chatServer.getServerPort();
    }

    @Override
    public void onSendMessage(String message) {
        for(ChatClient chatClient : chatClients){
            chatClient.sendMessage(message);
        }
        for(ChatView clientChatView : chatViews) {
            clientChatView.displayMessage(message);
        }
    }
}
