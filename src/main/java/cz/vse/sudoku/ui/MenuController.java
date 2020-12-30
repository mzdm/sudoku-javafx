package cz.vse.sudoku.ui;

import cz.vse.sudoku.service.FirebaseService;
import cz.vse.sudoku.service.Leaderboard;
import cz.vse.sudoku.service.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuController {

    private Stage menuStage;
    private FirebaseService firebaseService;

    public void init(Stage primaryStage) {
        this.menuStage = primaryStage;
        menuStage.close();

        firebaseService = FirebaseService.getInstance();
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
        Leaderboard leaderboard = firebaseService.getScores();
        ListView listView = new ListView();
        StackPane layout = new StackPane();

        if (leaderboard != null) {
            List<User> userList = leaderboard.getUserList();
            for (int i = 0; i < 10; i++) {
                User user = null;
                if (i < userList.size()) {
                    user = userList.get(i);
                }
                listView.getItems().add(addLeaderboardItem(i + 1, user));
            }
        }

        layout.getChildren().add(listView);
        Scene scene = new Scene(layout);

        Stage secondaryStage = new Stage();
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Leaderboard");
//        secondaryStage.initOwner(menuStage);
        secondaryStage.show();
    }

    private Label addLeaderboardItem(int pos, User user) {
        String text = pos + "   -----: --s";

        if (user != null) {
            text = pos + "   User: " + user.getName() + " : " + user.getScoreTime() + "s";
        }

        Label label = new Label();
        label.setText(text);
        return label;
    }

    public void onHelp() {

    }

    public void onExit() {

    }
}
