package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String userName;

    public ChatClient(String serverIP, int serverPort, String userName){
        try {
            this.userName = userName;
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(serverIP, serverPort));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            closeEverything(socket, reader, writer);
        }

    }

    public void sendMessage(String message){
        try {
            writer.write(userName);
            writer.newLine();
            writer.flush();

            while (socket.isConnected()){
                writer.write(userName + message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e){
            closeEverything(socket, reader, writer);
        }
    }

    public String receiveMessage() throws IOException {
            String messageFromChat = reader.readLine();
            return messageFromChat;
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
