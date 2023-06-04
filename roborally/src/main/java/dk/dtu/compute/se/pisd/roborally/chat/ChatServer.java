package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private static List<ClientHandler> clients = new ArrayList<>();

    public ChatServer(int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
        System.out.println("Chat server started. Listening on: " + getServerIP() + ":" + getServerPort());
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e){
            //TODO
        }
    }

    private class ClientHandler implements Runnable{
        private Socket clientSocket;
        private String userName;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;

        public ClientHandler(Socket clientSocket){
            try {
                this.clientSocket = clientSocket;
                this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clients.add(this);
                broadcastMessage("!!");
            } catch (IOException e){
                closeEverything(clientSocket, bufferedReader, bufferedWriter);
            }
        }

        @Override
        public void run() {
            String messageFromClient;
            while(clientSocket.isConnected()) {
                try {
                    messageFromClient = bufferedReader.readLine();
                    broadcastMessage(messageFromClient);
                } catch (IOException e) {
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }

        private void broadcastMessage(String message){
            for (ClientHandler client : clients) {
                try {
                    if(!client.userName.equals(userName)){
                        client.bufferedWriter.write(message);
                        client.bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }catch (IOException e){
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                }
            }
        }

        public void removeClientHandler(){
            clients.remove(this);
            //BroadCast
        }
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