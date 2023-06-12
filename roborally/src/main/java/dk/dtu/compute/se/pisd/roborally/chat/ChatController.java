package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.chat.ChatClient;
import dk.dtu.compute.se.pisd.roborally.chat.ChatView;
import dk.dtu.compute.se.pisd.roborally.chat.ClientInfo;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing the ChatController.
 */
public class ChatController {
    private ChatClient client;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Constructs a new ChatController object.
     */
    public ChatController(){
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Adds a new chat client to the chat room.
     * Creates a new ChatView and ChatClient for the client with the given username.
     * @param username the username of the client to be added
     */
    public void addChatClient(String username){
        ChatView chatView = new ChatView(this);
        this.client = new ChatClient(username, chatView);
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Disconnects the currently connected client from the chat room.
     * Calls the disconnectClient() method of the ChatClient to close the client connection.
     */
    public void disconnectClient() {
        client.disconnectClient();
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * method invoked when a message is sent from the chat view.
     * Sends the message to the server by calling the sendMessage() method of the ChatClient.
     * @param message the message to be sent
     */
    public void onMessageSent(String message) {
        client.sendMessage(message);
    }
}
