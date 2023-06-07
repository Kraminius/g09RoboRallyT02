package dk.dtu.compute.se.pisd.roborally.chat;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements ChatListener{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private ViewListener viewListener;

    public ChatClient(String username) {
        try {
            this.socket = new Socket("localHost", 4999);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            sendUsername(username);
            System.out.println(username + "'s client created");
            receiveMessage();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public String getUsername(){
        return username;
    }

    public void sendUsername(String username){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message){
        System.out.println("Sending message: " + message);
        try {
            bufferedWriter.write(username + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessage() {
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;
                while (socket.isConnected()) {
                    try {
                        messageFromChat = bufferedReader.readLine();
                        System.out.println("Received message from chat: " + messageFromChat);
                        if(viewListener != null) {
                            System.out.println("Calling onMessageReceived");
                            viewListener.onMessageReceived(messageFromChat);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        });
        receiveThread.start();
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
        System.out.println("ViewListener Created");
    }

    @Override
    public void onMessageSent(String message) {
        System.out.println("Called");
        sendMessage(message);
    }

    @Override
    public void disconnectClient(){
        try {
            socket.close();
            System.out.println(username + "'s socket closed");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
