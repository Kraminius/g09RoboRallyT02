package dk.dtu.compute.se.pisd.roborally.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket serverSocket;


    public ChatServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostAddress() + " has connected");
                System.out.flush();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e){
            closeServer();
        }
    }

    public void closeServer(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(4999);
        System.out.println("Server online, listening on port " + 4999);
        ChatServer ChatServer = new ChatServer(serverSocket);
        ChatServer.startServer();
    }
}
