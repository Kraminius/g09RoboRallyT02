package dk.dtu.compute.se.pisd.roborally.view;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Option {

    Stage stage;
    VBox window;
    String answer;

    public String getAnswer(String text, String[] options){
        stage = new Stage();
        window  = new VBox();
        window.setAlignment(Pos.CENTER);
        window.setPadding(new Insets(20, 20 ,20, 20));
        window.setSpacing(30);
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        HBox optionsPanel = new HBox();
        optionsPanel.setSpacing(5);
        for(int i = 0; i < options.length; i++){
            Button option = new Button(options[i]);
            final String optionAnswer = options[i];
            option.setOnAction(e -> {
                answer = optionAnswer;
                stage.close();
            });
            optionsPanel.getChildren().add(option);
        }
        window.getChildren().add(textLabel);
        window.getChildren().add(optionsPanel);
        Scene scene = new Scene(window);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
        stage.showAndWait();
        return answer;
    }
}
