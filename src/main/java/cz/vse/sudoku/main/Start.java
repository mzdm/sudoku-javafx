package cz.vse.sudoku.main;

import cz.vse.sudoku.ui.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showMenu(primaryStage);
    }

    public static void showMenu(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(Start.class.getClassLoader().getResourceAsStream("menu.fxml"));

        MenuController menuController = loader.getController();
        menuController.init(primaryStage);

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}