package dk.dtu.compute.se.pisd.roborally.chat;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;

public class ChatView{
    private Stage stage;
    private TextArea chatBox;
    private TextField messageField ;
    private Button sendButton;

    private ChatClient client;
    private OnSendMessageListener messageListener;

    public ChatView(ChatClient client){
        this.client = client;
        chatBox = new TextArea();

        chatBox.setEditable(false);
        chatBox.setWrapText(true);

        messageField = new TextField();

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            if(messageListener != null){
                String message = messageField.getText();
                System.out.println(this.client.getUserName());
                messageListener.onSendMessage(this.client, message);
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

    public String promptForUsername() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username");
        dialog.setHeaderText("Enter your username:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            // Handle case when the dialog is closed without entering a username
            return "";
        }
    }

    public void show(){
        stage.show();
    }

    public ChatClient getClient(){
        return this.client;
    }

    public void personalizeChatWindow(String name){
        stage.setTitle(name);
    }
    public void setOnSendMessageListener(OnSendMessageListener listener) {
        this.messageListener = listener;
    }

    public interface OnSendMessageListener {
        void onSendMessage(ChatClient senderClient, String message);
    }
}
