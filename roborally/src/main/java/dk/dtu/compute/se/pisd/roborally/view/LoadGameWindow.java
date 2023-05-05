package dk.dtu.compute.se.pisd.roborally.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class LoadGameWindow {
    String saveName;
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
     * Looks in the file folder to see all games, it then adds it to the combobox, so we can choose between them.
     * @param box the combobox to add the files to
     */
    private void addFiles(ComboBox<String> box){
        File folder = new File("roborally/src/main/resources/games");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                box.getItems().add(removeExtension(listOfFiles[i].getName()));
            }
        }
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * opens a window for the user to input which game they want to load
     * @return the name of the save the user chose.
     */
    public String getLoadInput(){
        VBox window = new VBox();
        window.setAlignment(Pos.TOP_CENTER);
        Label label = new Label("Choose Save");
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
        return saveName;
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Updates the chosen name with the combobox after an action. It also closes the window.
     * @param value the name of the board
     */
    private void setString(String value){
        saveName = value;
        stage.close();
    }
}
