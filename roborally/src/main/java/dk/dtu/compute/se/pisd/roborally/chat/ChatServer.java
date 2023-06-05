package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer(int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
        System.out.println("Chat server started. Listening on: " + getServerIP() + ":" + getServerPort());
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String userName = getClientUsername(bufferedReader);
                System.out.println("username passed to client: " + userName);
                ClientHandler clientHandler = new ClientHandler(clientSocket, bufferedReader, userName);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e){
            //TODO
        }
    }

    private String getClientUsername(BufferedReader bufferedReader) throws IOException {
        String username = bufferedReader.readLine();
        return username;
    }

    public String getServerIP(){
        return serverSocket.getInetAddress().getHostAddress();
    }

    public int getServerPort(){
        return serverSocket.getLocalPort();
    }

    public void closeServer(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}