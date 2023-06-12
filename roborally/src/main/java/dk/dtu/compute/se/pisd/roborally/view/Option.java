package dk.dtu.compute.se.pisd.roborally.view;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Option {

Stage stage;
VBox window;
String answer;
String flavorText;

String[] options;
boolean yesNo;

public Option(String flavorText){
    stage = new Stage();
    window  = new VBox();
    window.setAlignment(Pos.CENTER);
    VBox top = new VBox();
    top.setAlignment(Pos.CENTER);
    top.setPadding(new Insets(10, 10 ,10, 10));
    top.setStyle("-fx-background-color: #491886");
    Label textLabel = new Label(flavorText);
    textLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #eeeeee");
    top.getChildren().add(textLabel);
    window.getChildren().add(top);
}
public Option(String name, String flavorText, double width, double height){
    stage = new Stage();
    window  = new VBox();
    window.setMaxSize(width, height);
    window.setMinSize(width, height);
    window.setAlignment(Pos.TOP_CENTER);

    VBox top = new VBox();
    top.setAlignment(Pos.CENTER);
    top.setPadding(new Insets(5, 5 ,10, 10));
    top.setStyle("-fx-background-color: #491886");
    Label nameLabel = new Label(name);
    Label textLabel = new Label(flavorText);
    textLabel.setWrapText(true);
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #eeeeee");
    textLabel.setStyle("-fx-font-size: 13;");
    top.getChildren().add(nameLabel);
    window.getChildren().add(top);
    window.getChildren().add(textLabel);
}
private void show(){
    Scene scene = new Scene(window);
    stage.setScene(scene);
    stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
    stage.setOnCloseRequest(Event::consume);
    stage.showAndWait();
}
public void getOKPressed(){
    VBox panel = new VBox();
    panel.setPadding(new Insets(20, 20 ,20, 20));
    Button okButton = new Button("OK");
    okButton.setOnAction(e -> close());
    show();
}
public boolean getYESNO(){
    HBox panel = new HBox();
    panel.setPadding(new Insets(20, 20 ,20, 20));
    Button yesButton = new Button("Yes");
    Button noButton = new Button("No");
    panel.setSpacing(10);
    yesButton.setOnAction(e -> {
        yesNo = true;
        close();
    });
    noButton.setOnAction(e -> {
        yesNo = false;
        close();
    });
    show();
    return yesNo;
}

public String getChoice(String[] options){
    HBox optionsPanel = new HBox();
    optionsPanel.setPadding(new Insets(20, 20 ,20, 20));
    optionsPanel.setSpacing(5);
    for(int i = 0; i < options.length; i++){
        Button option = new Button(options[i]);
        final String optionAnswer = options[i];
        option.setOnAction(e -> {
            answer = optionAnswer;
            close();
        });
        optionsPanel.getChildren().add(option);
    }
    window.getChildren().add(optionsPanel);
    show();
    return answer;
}
public String getPromptedAnswer(String prompText){
    VBox panel = new VBox();
    panel.setPadding(new Insets(20, 20 ,20, 20));
    TextField textField = new TextField();
    textField.setPromptText(prompText);
    panel.getChildren().add(textField);
    Button okButton = new Button("OK");
    okButton.setOnAction(e -> {
        answer = textField.getText();
        close();
    });
    panel.getChildren().add(okButton);
    window.getChildren().add(panel);
    show();
    return answer;
}
private void close(){
    stage.close();
}
}

