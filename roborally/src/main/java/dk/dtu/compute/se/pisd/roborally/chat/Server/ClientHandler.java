package dk.dtu.compute.se.pisd.roborally.chat.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private static List<ClientHandler> activeClientHandlers = new ArrayList<>();
    private Socket clientSocket;
    private String username;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket clientSocket){
        try {
            this.clientSocket = clientSocket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.username = bufferedReader.readLine();
            activeClientHandlers.add(this);
            broadcastMessage("SERVER: " + username + " has joined the chat!");
        } catch (IOException e){
            closeEverything(clientSocket, bufferedReader, bufferedWriter);
        }
    }

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
                        closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    }
            } catch (IOException e){
                closeEverything(clientSocket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String message){
        System.out.println("Broadcasting message: " + message);
        for (ClientHandler client : activeClientHandlers) {
            try {
                if(!client.username.equals(this.username)) {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(clientSocket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        activeClientHandlers.remove(this);
        broadcastMessage("SERVER: " + username + " has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
        removeClientHandler();
        try {
            if(reader != null){
                reader.close();
            }
            if(writer != null){
                writer.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

