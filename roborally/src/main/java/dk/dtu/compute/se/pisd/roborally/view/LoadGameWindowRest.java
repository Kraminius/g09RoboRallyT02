package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.MyClient;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoadGameWindowRest {
    String saveName;
    Stage stage;


    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Looks in the file folder to see all games, it then adds it to the combobox, so we can choose between them.
     * @param box the combobox to add the files to
     */
    public void addFiles(ComboBox<String> box){
        String[] listOfNames = MyClient.getSaveNames();
        for (int i = 0; i < listOfNames.length; i++) {
            box.getItems().add(listOfNames[i]);
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

    public String[] playerNames(String saveName){

        Load serverLoad = MyClient.getSave(saveName);

        String[] names = serverLoad.getPlayerNames();


        return names;

    }

    public String mapName(String saveName){
        Load serverLoad = MyClient.getSave(saveName);

        String mapName = serverLoad.getBoard();

        return mapName;
    }


}
