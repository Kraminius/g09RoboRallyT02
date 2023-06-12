package dk.dtu.compute.se.pisd.roborally.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing the chatserver
 */
public class ChatServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> activeClientHandlers;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * This is the constructer for the ChatServer, it takes a ServerSocket as argument
     * and constructs an arrayList to keep track of the active clients.
     * @param serverSocket the server socket which the server should listen on.
     */
    public ChatServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        this.activeClientHandlers = new ArrayList<>();
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * This is the main method run on the server. It listens for new clients and creates a ClientHandler for each connected client.
     * It then creates a thread to handle that communication for that client.
     * If any IOExceptions is thrown it closes the server.
     */
    public void startServer(){
        try {
            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostAddress() + " has connected");
                System.out.flush();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                activeClientHandlers.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e){
            closeServer();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * The method that closes the server, it closes the serverSocket, and it calls the closeEverythin method for each clientHandler in the activeHandlers array
     */
    public void closeServer(){
        try {
            if(serverSocket != null){
                serverSocket.close();
                for(ClientHandler clientHandler : activeClientHandlers){
                    clientHandler.closeEverything();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @author Freja Egelund Grønnemose
     * The main method for the ClientServer.
     * It creates a serverSocket which is passed in the creation of the ChatServer object.
     * @param args
     * @throws IOException
     */
    public static void main(String [] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(4999);
        System.out.println("Server online, listening on port " + 4999);
        ChatServer ChatServer = new ChatServer(serverSocket);
        ChatServer.startServer();
    }

    /**
     * @author Freja Egelund Grønnemose
     * A method to retreive the list of active client handlers.
     * @return the array list of active client handlers.
     */
    public List<ClientHandler> getActiveHandlers() {
        return activeClientHandlers;
    }
}
