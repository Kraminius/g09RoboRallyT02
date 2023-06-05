package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String userName;

    public ChatClient(){
    }

    public void connectToServer(String serverIP, int serverPort) throws IOException {
        this.socket = new Socket(serverIP, serverPort);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendUserName(String username) throws IOException {
        writer.write(username);
        writer.newLine();
        writer.flush();
    }

    public void sendMessage(String message){
        System.out.println("Send message from client: " + userName);
        try {
            writer.write(this.userName + ": " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
            closeEverything(socket, reader, writer);
        }
    }

    public String receiveMessage() throws IOException {
        try {
            String messageFromChat = reader.readLine();
            System.out.println("Client that receives message: " + userName);
            return messageFromChat;
        } catch (IOException e){
            e.printStackTrace();
            closeEverything(socket, reader, writer);
            throw e;
        }
    }

    public String getClientIP(){
        return socket.getInetAddress().getHostAddress();
    }

    public int getClientPort(){
        return socket.getLocalPort();
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
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
