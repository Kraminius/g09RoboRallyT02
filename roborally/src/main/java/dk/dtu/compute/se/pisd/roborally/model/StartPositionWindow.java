package dk.dtu.compute.se.pisd.roborally.model;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Integer.parseInt;

public class StartPositionWindow {
    Board board;
    VBox window;
    Label label;
    TextField textField;
    Button button;
    ChoiceBox<String> choices;
    Stage stage;
    Player nextPlayer;


    public void getStartSpaces(Board board){
        this.board = board;
        for(int i = 0; i < board.getPlayersNumber(); i++){
            nextPlayer = board.getPlayer(i);
            createWindow();
        }
    }
    private ArrayList<Space> sortSpacesWithIDs(ArrayList<Space> arr){
        arr.sort(Comparator.comparingInt(g -> g.getElement().getStartField().getId()));
        return arr;
    }
    private void getChoices(){

        ArrayList<Space> spaces = board.getStartFieldSpaces();
        spaces = sortSpacesWithIDs(spaces);
        for(int i = 0; i < spaces.size(); i++){
            if(spaces.get(i).getPlayer() == null) choices.getItems().add(spaces.get(i).getElement().getStartField().getId() + "");
        }
    }


    public void createWindow(){
        window = new VBox();
        window.setAlignment(Pos.TOP_CENTER);
        window.setSpacing(10);
        window.setPadding(new Insets(30, 50, 30, 50));
        label = new Label("Choose Name & Starting Position");
        label.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        textField = new TextField();
        button = new Button("OK");
        button.setOnAction(e -> addPos());
        button.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        choices = new ChoiceBox<>();
        getChoices();
        choices.setPrefWidth(200);
        window.getChildren().add(label);
        window.getChildren().add(textField);
        window.getChildren().add(choices);
        window.getChildren().add(button);
        Scene scene = new Scene(window, 300, 200);
        stage = new Stage();
        stage.setTitle("");
        stage.setScene(scene);
        stage.setX(900);
        stage.setY(300);
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
        stage.showAndWait();
    }

    private void addPos(){

        if(textField.getText() == null || textField.getText().equals("") ){
            label.setText("Please Input a name");
            return;
        }
        int number = parseInt(choices.getValue());
        ArrayList<Space> startFields = board.getStartFieldSpaces();
        for(int i = 0; i < startFields.size(); i++){
            if(startFields.get(i).getElement().getStartField().getId() == number){
                startFields.get(i).setPlayer(nextPlayer);
                nextPlayer.setName(textField.getText());
                nextPlayer.getPlayerView().setText(nextPlayer.getName());
                stage.close();
                return;
            }
        }

    }
}
