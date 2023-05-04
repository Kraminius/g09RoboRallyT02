package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class BoardLoadWindow {
    String board;
    Stage stage;

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Removes an extension from the end of the file name.
     * @param val the string to remove the extension of
     * @return the name of the file with no extension
     */
    private String removeExtension(String val){
        String[] split = val.split("\\.");
        String toReturn = "";
        for(int i = 0; i < split.length-1; i++){
            if(i > 0) toReturn += ".";
            toReturn += split[i];
        }
        return toReturn;
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Looks in the file folder to see all maps, it then adds it to the combobox, so we can choose between them.
     * @param box the combobox to add the files to
     */
    private void addFiles(ComboBox<String> box){
        File folder = new File("roborally/src/main/resources/boards");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                box.getItems().add(removeExtension(listOfFiles[i].getName()));
            }
        }
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * opens a window for the user to input which board they want to play on.
     * @return the name of the board the user chose.
     */
    public String getBoardInput(){
        VBox window = new VBox();
        window.setAlignment(Pos.TOP_CENTER);
        Label label = new Label("Choose Board");
        label.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
        ComboBox<String> choices = new ComboBox<>();
        addFiles(choices);
        window.getChildren().add(label);
        window.getChildren().add(choices);
        Scene scene = new Scene(window, 200, 100);
        stage = new Stage();
        stage.setTitle("");
        stage.setScene(scene);
        choices.setOnAction(e -> setString(choices.getValue()));
        stage.showAndWait();
        return board;
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Updates the chosen name with the combobox after an action. It also closes the window.
     * @param value the name of the board
     */
    private void setString(String value){
        board = value;
        stage.close();
    }
}
