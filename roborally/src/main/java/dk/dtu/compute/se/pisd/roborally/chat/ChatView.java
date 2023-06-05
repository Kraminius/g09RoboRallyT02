package dk.dtu.compute.se.pisd.roborally.chat;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
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
    //private HBox onlineClients;
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
       /* onlineClients = new HBox(10);
        onlineClients.setPadding(new Insets(10));*/
        VBox window = new VBox(10);
        window.getChildren().addAll(/*onlineClients*/ chatBox, messageField, sendButton);

        Scene scene = new Scene(window);
        this.stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("RoboRally Chat - Join");
    }

    /*private VBox createInitialWindow(){
        Button joinButton = new Button("Join chat");
        joinButton.setOnAction(actionEvent -> {
            String username = promptForUsername();
            client.setUserName(username);
            if(!username.isEmpty()){
                personalizeChatWindow(username);
                stage.setScene(createChatScene());
            } else {
                showErrorDialog("Invalid Username", "Please enter a valid username.");
            }
        });

        VBox initialWindow = new VBox(10);
        initialWindow.setPadding(new Insets(50));
        initialWindow.getChildren().add(joinButton);

        return initialWindow;
    }

    private Scene createChatScene() {
        VBox window = new VBox(10);
        window.getChildren().addAll(onlineClients, chatBox, messageField, sendButton);

        return new Scene(window, 10, 10);
    }

    public void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
*/
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
