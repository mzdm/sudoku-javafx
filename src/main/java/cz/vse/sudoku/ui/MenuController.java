package cz.vse.sudoku.ui;

import cz.vse.sudoku.logic.Cell;
import cz.vse.sudoku.persistence.LocalStorage;
import cz.vse.sudoku.persistence.PersistenceException;
import cz.vse.sudoku.persistence.PersistenceProvider;
import cz.vse.sudoku.service.FirebaseService;
import cz.vse.sudoku.service.Leaderboard;
import cz.vse.sudoku.service.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Třída MenuController reprezentuje jednotlivé metody, které se provedou, vyvolají v grafické verzi hry na první obrazovce
 */
public class MenuController {

    private Stage menuStage;
    private FirebaseService firebaseService;
    private PersistenceProvider persistenceProvider;

    /**
     * Spuštění nového okna s hlavním menu
     *
     * @param primaryStage primární okno
     */
    public void init(Stage primaryStage) {
        this.menuStage = primaryStage;
        firebaseService = FirebaseService.getInstance();
    }

    /**
     * Nastavuje tuto třídu jako ukládání objektů do serializace
     *
     * @param persistenceProvider
     */
    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    /**
     * Metoda, která vytvoří novou hru
     *
     * @throws IOException
     */
    public void onNewGame() throws IOException {
        onNewGame(null);
    }

    /**
     * Metoda, která vytvoří a načte novou hru ze souboru
     *
     * @param loadedSudokuSaveFile uložená hra
     * @throws IOException
     */
    private void onNewGame(Cell[][] loadedSudokuSaveFile) throws IOException {
        menuStage.close();

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getClassLoader().getResourceAsStream("game.fxml"));
        Scene scene = new Scene(root);

        Stage gameStage = new Stage();
        GameController gameController = loader.getController();
        if (loadedSudokuSaveFile == null) {
            gameController.init(this, gameStage);
        } else {
            gameController.init(this, gameStage, loadedSudokuSaveFile);
        }
        gameController.setPersistenceProvider(new LocalStorage());

        gameStage.setScene(scene);
        gameStage.setTitle("Game");
        gameStage.show();
    }

    /**
     * Metoda sloužící k načtení uložené hry
     */
    public void onLoad() {
        try {
            Cell[][] loadedSudokuSaveFile = persistenceProvider.loadGame();
            if (loadedSudokuSaveFile != null) {
                onNewGame(loadedSudokuSaveFile);
            }
        } catch (PersistenceException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda, která zobrazí leaderboard, tabulku nejlepších výkonů v novém okně.
     */
    public void onLeaderboard() {
        StackPane layout = new StackPane();
        layout.setPrefSize(350, 500);
        Scene scene = new Scene(layout);

        Stage secondaryStage = new Stage();
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Leaderboard");
        secondaryStage.show();

        try {
            ListView<Label> listView = new ListView<Label>();
            Leaderboard leaderboard = firebaseService.getScores();
            if (leaderboard != null) {
                List<User> userList = leaderboard.getUserList();
                for (int i = 0; i < 10; i++) {
                    User user = null;
                    if (i < userList.size()) {
                        user = userList.get(i);
                    }
                    listView.getItems().add(addLeaderboardItem(i + 1, user));
                }
                layout.getChildren().add(listView);
            } else {
                Label errorLabel = new Label("Could not load the Leaderboard");
                layout.getChildren().add(errorLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Could not load the Leaderboard");
            layout.getChildren().add(errorLabel);
        }
    }

    /**
     * Metoda, která přidá do seznamu nejlepších výsledků nového hráče
     *
     * @param pos  pozice
     * @param user hráč
     * @return řetězec s umístěním, jménem hráče a časem
     */
    private Label addLeaderboardItem(int pos, User user) {
        String text = pos + "      ------------(-- m --s)";

        if (user != null) {
            text = pos + "      " + user.getName() + " (" + user.getFormattedScoreTime() + ")";
        }

        Label label = new Label();
        label.setText(text);
        return label;
    }

    /**
     * Metoda zobrazující nápovědu v novém okně
     */
    public void onHelp() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/help.html").toString());
        Scene scene = new Scene(webView, 840, 650);

        Stage secondaryStage = new Stage();
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Help");
        secondaryStage.initOwner(menuStage);
        secondaryStage.show();
    }

    /**
     * Metoda, která ukončí hru
     */
    public void onExit() {
        System.exit(0);
    }
}
