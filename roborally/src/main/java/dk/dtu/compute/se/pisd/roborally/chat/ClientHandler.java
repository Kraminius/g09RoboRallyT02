package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    public static List<ClientHandler> clients = new ArrayList<>();
    private Socket clientSocket;
    private String userName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket clientSocket, BufferedReader bufferedReader, String username){
        try {
            this.clientSocket = clientSocket;
            this.bufferedReader = bufferedReader;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.userName = username;
            clients.add(this);
            broadcastMessage("SERVER: " + username + " has joined the chat!");
        } catch (IOException e){
            closeEverything(clientSocket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        try{
            String messageFromClient;
            while((messageFromClient = bufferedReader.readLine()) != null) {;
                System.out.println("Message passes to broadcast: " + messageFromClient);
                broadcastMessage(messageFromClient);
            }
        } catch (IOException e) {
            closeEverything(clientSocket, bufferedReader, bufferedWriter);
        }
    }

    private void broadcastMessage(String message){
        System.out.println("Broadcasting message = " + message);
        System.out.println("From user = " + userName);
        for (ClientHandler client : clients) {
            try {
                if(!client.userName.equals(userName)){
                    System.out.println("Sending message: " + message + " to client: " + client.userName);
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
        clients.remove(this);
        broadcastMessage("SERVER: " + this.userName + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
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
