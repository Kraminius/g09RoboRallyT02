package dk.dtu.compute.se.pisd.roborally.chat;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing the ChatClient.
 */
public class ChatClient{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private ViewListener viewListener;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Constructs a new ChatClient object with the given username and ViewListener.
     * Initializes the client socket and sets up the input and output streams.
     * Sends the username to the server and starts receiving messages.
     * @param username the username of the client
     * @param viewListener the ViewListener object to handle incoming messages
     */
    public ChatClient(String username, ViewListener viewListener) {
        try {
            this.socket = new Socket("localHost", 4999);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.viewListener = viewListener;
            this.username = username;
            sendUsername(username);
            System.out.println(username + "'s client created");
            receiveMessage();
        } catch (IOException e) {
            closeEverything();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Sends the username to the server.
     * @param username the username to be sent
     */
    public void sendUsername(String username){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
            closeEverything();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Sends a message to the server.
     * @param message the message to be sent
     */
    public void sendMessage(String message){
        System.out.println("Sending message: " + message);
        try {
            bufferedWriter.write(username + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Starts a thread to receive messages from the server.
     * When a message is received, it is passed to the ViewListener (if available) for further processing.
     */
    public void receiveMessage() {
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;
                while (socket.isConnected()) {
                    try {
                        messageFromChat = bufferedReader.readLine();
                        if(messageFromChat == null){
                            throw new IOException("Lost connection to server");
                        } else {
                        System.out.println("Received message from chat: " + messageFromChat);
                            if(viewListener != null) {
                            System.out.println("Calling onMessageReceived");
                            viewListener.onMessageReceived(messageFromChat);
                            }
                        }
                    } catch (IOException e) {
                        closeEverything();
                        break;
                    }
                }
            }
        });
        receiveThread.start();
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Closes all resources associated with the client connection.
     * This method is called when an error occurs or when the client needs to be closed.
     */
    public void closeEverything() {
        try {
            if (this.bufferedReader != null) {
                bufferedReader.close();
            }
            if (this.bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (this.socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Disconnects the client by closing the client socket.
     * This method is called when the client needs to be disconnected from the server.
     */
    public void disconnectClient(){
        try {
            socket.close();
            System.out.println(username + "'s socket closed");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
