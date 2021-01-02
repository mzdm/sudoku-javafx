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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static cz.vse.sudoku.logic.SudokuCells.sizeSudoku;

public class GameController {

    private MenuController menuController;

    public GridPane sudokuGrid;
    public Label timerTextLabel;

    private SudokuCells cells;
    private Stage gameStage;

    private FirebaseService firebaseService;
    private PersistenceProvider persistenceProvider;

    private Timer timer;
    private int timerSecs = 0;
    private boolean scoreAlreadySaved = false;

    private boolean wasGameLoaded = false;

    public void init(MenuController menuController, Stage primaryStage) {
        this.init(menuController, primaryStage, null);
    }

    public void init(MenuController menuController, Stage primaryStage, Cell[][] loadedSudokuSaveFile) {
        this.menuController = menuController;
        this.gameStage = primaryStage;

        firebaseService = FirebaseService.getInstance();

        if (loadedSudokuSaveFile == null) {
            NumberGenerator numberGenerator = new NumberGenerator();
            cells = new SudokuCells(numberGenerator.getRandomSudoku());
        } else {
            wasGameLoaded = true;
            cells = new SudokuCells(loadedSudokuSaveFile);
        }

        createGrid();
        startTimer();
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    private void startTimer() {
        if (!wasGameLoaded) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        int currentTime = ++timerSecs;
                        timerTextLabel.setText("" + currentTime);
                    });
                }
            }, 500, 1000);
        } else {
            timerTextLabel.setText("-");
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void createGrid() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                int num = cells.getArraySudoku()[i][j].getCellNum();
                boolean isCellModifiable = cells.getArraySudoku()[i][j].isModifiable();

                String color = "black";
                final TextField textFieldCell = new TextField("" + num);

                if (isCellModifiable) {
                    if (num == 0) {
                        textFieldCell.clear();
                    }
                } else {
                    textFieldCell.setEditable(false);
                    color = "red";
                }

                String gridStyle = "";
                if (shouldCellBlue(i, j)) {
                    gridStyle = "-fx-background-color: rgba(229, 238, 247, 0.85);";
                }
                textFieldCell.setStyle(" -fx-text-fill: " + color + ";-fx-font-size: 20px;-fx-alignment: CENTER; -fx-border-color: rgb(192,192,192);" + gridStyle);


                final int tempI = i;
                final int tempJ = j;
                textFieldCell.textProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
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
                    }
                });
                sudokuGrid.add(textFieldCell, j, i);
            }
        }

    }

    private boolean shouldCellBlue(int i, int j) {
        if (sizeSudoku == 9) {
            if ((i <= 2 || i >= 6)) {
                return j >= 3 && j <= 5;
            } else return !(j >= 3 && j <= 5);
        }
        return false;
    }

    private void showWinDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game finished");

        String timerText = "";
        if (!wasGameLoaded) {
            timerText = "\nTIME: " + timerSecs + "s";
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

    public void onRegenerate() {
        stopTimer();

        try {
            menuController.onNewGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSave() {
        try {
            persistenceProvider.saveGame(cells.getArraySudoku());
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    public void onHelp() {
        menuController.onHelp();
    }

    public void onBack() throws IOException {
        stopTimer();
        Start.showMenu(gameStage);
    }
}
