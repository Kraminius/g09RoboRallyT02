package dk.dtu.compute.se.pisd.roborally.chat.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing the clienthandler
 */
public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private String username;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ChatServer server;

    /**
     * @author Freja Egelund Grønnemose, s224286
     * Constructs a new ClientHandler object with the given clientSocket and server.
     * @param clientSocket the Socket representing the client connection
     * @param server the ChatServer object managing the chat session
     */
    public ClientHandler(Socket clientSocket, ChatServer server){
        try {
            this.clientSocket = clientSocket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.username = bufferedReader.readLine();
            this.server = server;
            broadcastMessage("SERVER: " + username + " has joined the chat!");
            welcomeMessage();
        } catch (IOException e){
            closeEverything();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Sends a welcome message to the client upon joining the chat.
     * The message includes information about the current online players.
     */
    public void welcomeMessage(){
        List<ClientHandler> activeClientHandlers = server.getActiveHandlers();
        try{
            bufferedWriter.write("SERVER: Welcome to the RoboRally Chat!\nOnline players: ");
            bufferedWriter.newLine();
            int x = 0;
            for(ClientHandler clientHandler : activeClientHandlers){
                if(!clientHandler.username.equals(this.username)) {
                    x++;
                    bufferedWriter.write(clientHandler.username);
                    bufferedWriter.newLine();
                }
            }
            if(x == 0){
                bufferedWriter.write("None.");
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e){
            closeEverything();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Overrides the run() method of the Thread class.
     * Listens for messages from the client and broadcasts them to other clients in the chat.
     * Closes the connection if an error occurs or the client socket is closed.
     */
    @Override
    public void run() {
        String messageFromClient;
        while(!clientSocket.isClosed()){
            try {
                    messageFromClient = bufferedReader.readLine();
                    if(messageFromClient != null) {
                        System.out.println("Message from client to be broadcasted: " + messageFromClient);
                        broadcastMessage(messageFromClient);
                    } else {
                        closeEverything();
                    }
            } catch (IOException e){
                closeEverything();
                break;
            }
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Broadcasts a message to all active clients except the current client.
     * @param message the message to be broadcasted
     */
    private void broadcastMessage(String message){
        List<ClientHandler> activeClientHandlers = server.getActiveHandlers();
        System.out.println("Broadcasting message: " + message);
        for (ClientHandler client : activeClientHandlers) {
            try {
                if(!client.username.equals(this.username)) {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything();
            }
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Removes the current ClientHandler from the list of active client handlers in the server.
     * Broadcasts a message to inform other clients about the departure of the current client.
     */
    public void removeClientHandler(){
        List<ClientHandler> activeClientHandlers = server.getActiveHandlers();
        activeClientHandlers.remove(this);
        broadcastMessage("SERVER: " + username + " has left the chat");
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Closes all resources associated with the client connection and removes the client handler from the server.
     * This method is called when an error occurs or when the client socket is closed.
     */
    public void closeEverything(){
        removeClientHandler();
        try {
            if(this.bufferedWriter != null){
                bufferedWriter.close();
            }
            if(this.bufferedReader != null){
                bufferedReader.close();
            }
            if(this.clientSocket != null){
                clientSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

