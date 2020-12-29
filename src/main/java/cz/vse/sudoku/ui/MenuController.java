package cz.vse.sudoku.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    Stage menuStage;

    public void init(Stage primaryStage) {
        menuStage = primaryStage;
        menuStage.close();
    }

    public void onNewGame() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getClassLoader().getResourceAsStream("game.fxml"));
        Scene scene = new Scene(root);

        GameController gameController = loader.getController();
        gameController.init(menuStage);

        Stage gameStage = new Stage();
        gameStage.setScene(scene);
        gameStage.setTitle("Game");
        gameStage.show();
    }

    public void onLoad() {


    }

    public void onLeaderboard() {

    }

    public void onHelp() {

    }

    public void onExit() {

    }
}
