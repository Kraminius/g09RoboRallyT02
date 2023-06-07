package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.chat.ChatClient;
import dk.dtu.compute.se.pisd.roborally.chat.ChatView;
import dk.dtu.compute.se.pisd.roborally.chat.ClientInfo;

public class ChatController {
    private ChatClient client;
    private ClientInfo clientInfo;

    public ChatController(){
        this.clientInfo = new ClientInfo();
    }

    public void addChatClient(String username){
        this.client = new ChatClient(username);

        ChatView chatView = new ChatView(client);

        client.setViewListener(chatView);
    }
}
