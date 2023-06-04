package dk.dtu.compute.se.pisd.roborally.chat;

import javafx.event.Event;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ChatView{
    private Stage stage;
    private TextArea chatBox;
    private TextField messageField ;
    private Button sendButton;

    private ChatClient client;
    private OnSendMessageListener listener;

    public ChatView(ChatClient client){
        this.client = client;
        chatBox = new TextArea();

        chatBox.setEditable(false);
        chatBox.setWrapText(true);

        messageField = new TextField();

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            if(listener != null){
                String message = messageField.getText();
                listener.onSendMessage(message);
                messageField.clear();
            }
        });

        VBox window = new VBox(10);
        window.getChildren().addAll(chatBox, messageField, sendButton);

        Scene scene = new Scene(window);
        this.stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("RoboRally chat");
    }

    public void displayMessage(String message){
        chatBox.appendText(message + "\n");
    }

    public void show(){
        stage.show();
    }

    public void setOnSendMessageListener(OnSendMessageListener listener) {
        this.listener = listener;
    }

    public interface OnSendMessageListener {
        void onSendMessage(String message);
    }

    public ChatClient getChatClient(){
        return client;
    }
}
