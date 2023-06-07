package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.view.Option;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class ChatView implements ViewListener {
    private Stage stage;
    private TextArea chatBox;
    private TextField messageField;
    private Button sendButton;
    private ChatListener chatListener;

    public ChatView(ChatListener chatListener) {
        this.chatListener = chatListener;
        System.out.println("ChatListener created");
        createChatRoomStage();
    }

    private void createChatRoomStage() {
        VBox chatRoomView = new VBox(10);
        chatRoomView.setPadding(new Insets(10));

        chatBox = new TextArea();
        chatBox.setEditable(false);
        chatBox.setWrapText(true);

        messageField = new TextField();

        sendButton = new Button("Send");

        chatRoomView.getChildren().addAll(chatBox, messageField, sendButton);

        Scene scene = new Scene(chatRoomView);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("RoboRally Chat");
        stage.setOnCloseRequest(e -> {
            Option option = new Option("Leave RoboRally chat");
            if(option.getYESNO("Would you like to leave the chat room?")){
                try {
                    chatListener.disconnectClient();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                stage.close();
            } else {
                e.consume();
            }
        });
        sendButton.setOnAction(actionEvent -> {
            String message = messageField.getText();
            if (chatListener != null) {
                chatListener.onMessageSent(message);
                displayMessage(message);
            }
            messageField.clear();
        });
        stage.show();
    }

    private void displayMessage(String message) {
        chatBox.appendText(message + "\n");
        if(message.contains("barrel roll") || message.contains("BARREL ROLL") || message.contains("Barrel roll")){
            performBarrelRollAnimation(stage);
        }
    }

    private void performBarrelRollAnimation(Stage stage) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), stage.getScene().getRoot());
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
    }

    @Override
    public void onMessageReceived(String message) {
        displayMessage(message);
    }
}
