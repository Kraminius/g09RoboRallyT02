package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.view.Option;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing the chatview
 * Implements the ViewListener interface to listen for message received events.
 */
public class ChatView implements ViewListener {
    private Stage stage;
    private TextArea chatBox;
    private TextField messageField;
    private Button sendButton;
    private Button exitButton;
    private ChatController chatController;
    private VBox chatRoomView;
    private String userName;
    private boolean currentChatActive = true;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Constructs a new ChatView object with the given ChatController.
     * Initializes the ChatView by creating the chat room stage.
     * @param chatController the ChatController object to handle chat interactions
     */
    public ChatView(ChatController chatController) {
        this.chatController = chatController;
        createChatRoomStage();
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Creates the chat room stage and initializes the UI elements.
     * Sets up event handlers for the send button and the window close request.
     */
    private void createChatRoomStage() {
        chatRoomView = new VBox(10);
        chatRoomView.setPadding(new Insets(10));

        chatBox = new TextArea();
        chatBox.setEditable(false);
        chatBox.setWrapText(true);

        messageField = new TextField();

        sendButton = new Button("Send");
        exitButton = new Button("Leave chat");
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(sendButton);
        borderPane.setRight(exitButton);
        chatRoomView.getChildren().addAll(chatBox, messageField, borderPane);


        /* It doesnt need to make it's own scene and stage, we are just using the view.
        Scene scene = new Scene(chatRoomView);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("RoboRally Chat");
        stage.setOnCloseRequest(e -> {
            Option option = new Option("Leave RoboRally chat");
            if(option.getYESNO("Would you like to leave the chat room?")){
                chatController.disconnectClient();
                stage.close();
            } else {
                e.consume();
            }
        });

        stage.show();

         */

        sendButton.setOnAction(actionEvent -> {
            String message = messageField.getText();
            chatController.onMessageSent(message);
            displayMessage(message);
            messageField.clear();
        });
        exitButton.setOnAction(e -> {
            closeChat(currentChatActive);
        });
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Displays a message in the chat box.
     * @param message
     */
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

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * method invoked when a new message is received from the server.
     * Displays the received message in the chat box.
     * @param message the message received from the server
     */
    @Override
    public void onMessageReceived(String message) {
        displayMessage(message);
    }

    public VBox getChatRoomView(){
        return chatRoomView;
    }
    private void closeChat(boolean hide){
        if(hide){
            Option option = new Option("Leave RoboRally chat");
            if(option.getYESNO("Would you like to leave the chat room?")){
                chatController.disconnectClient();
            } else {
                return;
            }
        }
        else{
            chatController.addChatClient(ClientInfo.getUsername());
        }
        if(hide){
            chatBox.setDisable(true);
            messageField.setDisable(true);
            sendButton.setDisable(true);
            chatBox.setOpacity(0);
            messageField.setOpacity(0);
            sendButton.setOpacity(0);
            exitButton.setText("Rejoin chat");
        }
        else{
            chatBox.setDisable(false);
            messageField.setDisable(false);
            sendButton.setDisable(false);
            chatBox.setOpacity(1);
            messageField.setOpacity(1);
            sendButton.setOpacity(1);
            exitButton.setText("Leave chat");
        }

        currentChatActive = !hide;
    }
}
