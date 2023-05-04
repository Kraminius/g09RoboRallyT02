package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WinnerDisplay {
        VBox window;

    ImageLoader imageLoader = ImageLoader.get();

    ImageView dance = new ImageView();
    StackPane holder;

        Label label;
        Stage stage;


        public void createWindow(Player winner){

            dance.setImage(imageLoader.winner);

            window = new VBox();
            window.setAlignment(Pos.TOP_CENTER);
            window.setSpacing(10);
            window.setPadding(new Insets(30, 50, 30, 50));

            label = new Label("We have a WINNER!\n" + "Congratulations to "+ winner.getName());
            label.setAlignment(Pos.TOP_CENTER);
            label.setMinHeight(30);
            label.setWrapText(true);
            label.setStyle("-fx-font-size: 13; -fx-font-weight: bold");

            dance.setImage(imageLoader.winner);
            dance.setFitHeight(165);
            dance.setFitWidth(220);

            holder = new StackPane();
            holder.getChildren().add(dance);
            holder.getChildren().add(label);
            holder.setAlignment(Pos. CENTER);

            window.getChildren().add(label);
            window.getChildren().add(holder);
            Scene scene = new Scene(window, 300, 300);
            stage = new Stage();
            stage.setTitle("Chiken-Dinner");
            stage.setScene(scene);
            stage.setX(900);
            stage.setY(300);
            stage.setOnCloseRequest(Event::consume);
            stage.showAndWait();
        }
    }



