package cz.vse.sudoku.ui;

import cz.vse.sudoku.logic.Cell;
import cz.vse.sudoku.logic.SudokuCells;
import cz.vse.sudoku.logic.NumberGenerator;
import cz.vse.sudoku.main.Start;
import cz.vse.sudoku.persistence.PersistenceException;
import cz.vse.sudoku.persistence.PersistenceProvider;
import cz.vse.sudoku.service.FirebaseService;
import cz.vse.sudoku.service.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static cz.vse.sudoku.logic.SudokuCells.sizeSudoku;

/**
 * Třída GameController reprezentuje jednotlivé metody, které se provedou, vyvolají v grafické verzi hry na obrazovce se samotnou hrou
 */
public class GameController {

    private MenuController menuController;

    public GridPane sudokuGrid;
    public Label timerNumberLabel;

    private SudokuCells cells;
    private Stage gameStage;

    private FirebaseService firebaseService;
    private PersistenceProvider persistenceProvider;

    private Timer timer;
    private int timerSecs = 0;
    private boolean scoreAlreadySaved = false;

    private boolean wasGameLoaded = false;

    /**
     * Spuštění nového okna se sudoku
     *
     * @param menuController okno s menu
     * @param primaryStage   primární okno
     */
    public void init(MenuController menuController, Stage primaryStage) {
        this.init(menuController, primaryStage, null);
    }

    /**
     * Spuštění nového okna se sudokou z uložené hry v souboru
     *
     * @param menuController       okno s menu
     * @param primaryStage         primární okno
     * @param loadedSudokuSaveFile uložená hra
     */
    public void init(MenuController menuController, Stage primaryStage, Cell[][] loadedSudokuSaveFile) {
        this.menuController = menuController;
        this.gameStage = primaryStage;
        firebaseService = FirebaseService.getInstance();

        generateSudoku(loadedSudokuSaveFile)
        createGrid();
        startTimer();
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
     * Metoda, která generuje herní pole Sudoku
     *
     * @param loadedSudokuSaveFile pokud není null tak se načte uložené herní Sudoku pole
     */
    private void generateSudoku(Cell[][] loadedSudokuSaveFile) {
        if (loadedSudokuSaveFile == null) {
            NumberGenerator numberGenerator = new NumberGenerator();
            cells = new SudokuCells(numberGenerator.getRandomSudoku());
        } else {
            wasGameLoaded = true;
            cells = new SudokuCells(loadedSudokuSaveFile);
        }
    }

    /**
     * Metoda, která spustí timer
     */
    private void startTimer() {
        if (!wasGameLoaded) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        int currentTime = ++timerSecs;
                        timerNumberLabel.setText("" + currentTime);
                    });
                }
            }, 500, 1000);
        } else {
            timerNumberLabel.setText("-");
        }
    }

    /**
     * Metoda, která zastaví timer
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Metoda vytvářející a upravující v okně samotnou tabulku sudoku a udávající pravidla při vyplňování políček
     */
    private void createGrid() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                int num = cells.getArraySudoku()[i][j].getCellNum();
                final TextField textFieldCell = new TextField("" + num);

                boolean isCellModifiable = cells.getArraySudoku()[i][j].isModifiable();
                String color = "black";
                if (isCellModifiable) {
                    if (num == 0) {
                        textFieldCell.clear();
                    }
                } else {
                    textFieldCell.setEditable(false);
                    color = "grey";
                }

                String gridStyle = "";
                if (shouldCellBlue(i, j)) {
                    gridStyle = "-fx-background-color: rgb(150,210,241,0.6);";
                }
                textFieldCell.setStyle(" -fx-text-fill: " + color + ";-fx-font-size: 20px;-fx-alignment: CENTER; -fx-border-color: rgb(192,192,192);" + gridStyle);


                final int tempI = i;
                final int tempJ = j;
                textFieldCell.textProperty().addListener((observable, oldValue, newValue) -> {
                    int maxLength = 1;
                    if (newValue.length() > maxLength) {
                        String cut = textFieldCell.getText().substring(0, maxLength);
                        textFieldCell.setText(cut);
                    } else if (newValue.length() == 0) {
                        System.out.println("smazano pole");

                        cells.changeElement(tempI, tempJ, 0);
                        cells.printSudoku();
                        textFieldCell.setText("");
                    } else {
                        int typedNumber = 0;
                        try {
                            System.out.println(newValue);
                            typedNumber = Integer.parseInt(newValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (typedNumber <= 0 || typedNumber >= 10) {
                            System.out.println("špatný číslo (není 1-9)");
                            textFieldCell.clear();
                        } else {
                            System.out.println("dobrý číslo (1-9)");
                            cells.changeElement(tempI, tempJ, Integer.parseInt(newValue));
                        }

                        cells.printSudoku();

                        if (cells.areAllCellsFilled()) {
                            boolean isSudokuValid = cells.isSudokuValid();
                            System.out.println("sudoku je vyplneno spravne: " + isSudokuValid);
                            if (isSudokuValid) {
                                stopTimer();
                                showWinDialog();
                            }
                        }
                        System.out.println();
                    }
                });
                sudokuGrid.add(textFieldCell, j, i);
            }
        }

    }

    /**
     * Metoda zjišťující zda mají být políčka modrá a rozdělovat tabulku na jednotlivé bloky po devíti políčkách
     *
     * @param i
     * @param j
     * @return boolean hodnota
     */
    private boolean shouldCellBlue(int i, int j) {
        if (sizeSudoku == 9) {
            if ((i <= 2 || i >= 6)) {
                return j >= 3 && j <= 5;
            } else return !(j >= 3 && j <= 5);
        }
        return false;
    }

    /**
     * Metoda, která vracející vítězný dialog a možnost uložení a publikování svého výsledku do leaderboardu
     */
    private void showWinDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game finished");

        String timerText = "";
        if (!wasGameLoaded) {
            String formattedTime = new User("", timerSecs).getFormattedScoreTime();
            timerText = "\nTIME: " + formattedTime;
        }

        alert.setHeaderText("You have successfully solved this Sudoku." + timerText);
        alert.setContentText("Would you like to save your score to the leaderboard or Go back to the menu?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.getDialogPane().setPrefSize(480, 200);

        Button saveButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        if (wasGameLoaded) {
            saveButton.setDisable(true);
        }
        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (scoreAlreadySaved) {
                saveButton.setDisable(true);
            } else {
                saveScore();
            }
            event.consume();
        });
        saveButton.setText("Save my score");

        Button showLeaderboardButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        showLeaderboardButton.addEventFilter(ActionEvent.ACTION, event -> {
            menuController.onLeaderboard();
            event.consume();
        });
        showLeaderboardButton.setText("Show leaderboard");

        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Back to menu");

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            // do nothing
        } else if (result.get() == ButtonType.CANCEL) {
            // back to the menu
            try {
                onBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda, která slouží k uložení hry
     */
    private void saveScore() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Save score to leaderboard");
        dialog.setContentText("username:");

        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) return;

        if (!result.get().isEmpty()) {
            scoreAlreadySaved = true;

            String username = result.get();
            int scoreTime = timerSecs;

            User user = new User(username, scoreTime);
            firebaseService.saveScore(user);
        }
    }

    /**
     * Metoda, která slouží k obnovení sudoku
     */
    public void onRegenerate() {
        stopTimer();
        closeWindow();

        try {
            menuController.onNewGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda, která zavře aktualní okno
     */
    private void closeWindow() {
        gameStage.close();
    }

    /**
     * Metoda sloužící k uložení aktualní pozice ve hře
     */
    public void onSave() {
        try {
            persistenceProvider.saveGame(cells.getArraySudoku());
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda, která otevře okno s nápovědou
     */
    public void onHelp() {
        menuController.onHelp();
    }

    /**
     * Metoda složící k návratu na první obrazovku do hlavního menu
     *
     * @throws IOException
     */
    public void onBack() throws IOException {
        stopTimer();
        Start.showMenu(gameStage);
    }
}
