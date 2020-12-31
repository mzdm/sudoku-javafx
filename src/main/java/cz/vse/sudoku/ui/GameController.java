package cz.vse.sudoku.ui;

import cz.vse.sudoku.logic.Cells;
import cz.vse.sudoku.logic.NumberGenerator;
import cz.vse.sudoku.main.Start;
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

import static cz.vse.sudoku.logic.Cells.sizeSudoku;

public class GameController {

    private MenuController menuController;

    public GridPane sudokuGrid;
    public Label timerTextLabel;

    private Cells cells;
    private Stage gameStage;

    private FirebaseService firebaseService;

    private Timer timer;
    private int timerSecs = 0;
    private boolean scoreAlreadySaved = false;

    public void init(MenuController menuController, Stage primaryStage) {
        this.menuController = menuController;
        this.gameStage = primaryStage;
        gameStage.close();

        firebaseService = FirebaseService.getInstance();

        NumberGenerator numberGenerator = new NumberGenerator();
        cells = new Cells(numberGenerator.getRandom());

        createGrid();
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    int currentTime = ++timerSecs;
                    timerTextLabel.setText("" + currentTime);
                });
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void printSudoku() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                this.cells.getArraySudoku()[i][j] = cells.getArraySudoku()[i][j];
                System.out.print(cells.getArraySudoku()[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void createGrid() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                int num = cells.getArraySudoku()[i][j];

                String color = "black";
                final TextField textFieldCell = new TextField("" + num);

                if (num == 0) {
                    textFieldCell.clear();
                } else {
                    textFieldCell.setMouseTransparent(true);
                    color = "red";
                }
                textFieldCell.setStyle(" -fx-text-fill: " + color + ";-fx-font-size: 20px;-fx-alignment: CENTER;");

                final int finalI = i;
                final int finalJ = j;
                textFieldCell.textProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        int maxLength = 1;
                        if (newValue.length() > maxLength) {
                            String cut = textFieldCell.getText().substring(0, maxLength);
                            textFieldCell.setText(cut);
                        } else if (newValue.length() == 0) {
                            System.out.println("smazano pole");

                            int[][] changed = cells.getArraySudoku();
                            changed[finalI][finalJ] = 0;
                            cells.setArraySudoku(changed);
                            printSudoku();
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

                                int[][] changed = cells.getArraySudoku();
                                changed[finalI][finalJ] = Integer.parseInt(newValue);
                                cells.setArraySudoku(changed);
                            }

                            printSudoku();
                            if (allFilled()) {
                                boolean isCorrect = cells.checkSudoku();
                                System.out.println("sudoku je vyplneno spravne: " + isCorrect);
                                if (isCorrect) {
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

    private void showWinDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game finished");
        alert.setHeaderText("You have successfully solved this Sudoku\nTIME: " + timerSecs + "s");
        alert.setContentText("Would you like to save your score to the leaderboard or Go back to the menu?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.getDialogPane().setPrefSize(480, 200);

        Button saveButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
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

    private boolean allFilled() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                if (cells.getArraySudoku()[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onRegenerate() {
        timerSecs = 0;
        stopTimer();

    }

    public void onSave() {

    }

    public void onHelp() {
        menuController.onHelp();
    }

    // TODO nezavira se
    public void onBack() throws IOException {
        timerSecs = 0;
        stopTimer();

        gameStage.close();
        Start.showMenu(gameStage);
    }
}
