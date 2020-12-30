package cz.vse.sudoku.ui;

import cz.vse.sudoku.logic.Cells;
import cz.vse.sudoku.logic.NumberGenerator;
import cz.vse.sudoku.main.Start;
import cz.vse.sudoku.service.FirebaseService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static cz.vse.sudoku.logic.Cells.sizeSudoku;

public class GameController {

    public GridPane sudokuGrid;

    private Cells cells;
    private Stage gameStage;

    private FirebaseService firebaseService;

    private long startTime;
    private long endTime;

    public void init(Stage primaryStage) {
        this.gameStage = primaryStage;
        gameStage.close();

        NumberGenerator numberGenerator = new NumberGenerator();
        cells = new Cells(numberGenerator.getRandom());

        createGrid();

        startTime = System.nanoTime();
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
//                                System.out.println(cells.solve());
                                boolean isCorrect = cells.checkSudoku();
                                System.out.println("sudoku je vyplneno spravne: " + isCorrect);
                                if (isCorrect) {
                                    showWinDialog();
                                }
                            }
                            System.out.println();
//                            System.out.println(cells.solve(cells.getArraySudoku()));
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
        alert.setHeaderText("You have successfully solved this Sudoku\nTIME: XXmm:XXss");
        alert.setContentText("Would you like to save your score to the leaderboard or Go back to the menu?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.getDialogPane().setPrefSize(480, 200);

        Button saveButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        saveButton.setText("Save my score");

        Button showLeaderboardButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        showLeaderboardButton.setText("Show leaderboard");

        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Back to menu");


        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            // do nothing
        } else if (result.get() == ButtonType.YES) {
            // TODO save score
            saveScore();
        } else if (result.get() == ButtonType.NO) {
            // TODO show leaderboard
        } else if (result.get() == ButtonType.CANCEL) {
            try {
                onBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveScore() {

    }

    private void stopTimer() {
        //  long endTime = System.nanoTime();
        //  long timeElapsed = endTime - startTime;
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

    public void onCellFilled() {
//        cells.solve(cells.getArraySudoku());
    }

    public void onRegenerate() {

    }

    public void onSave() {

    }

    public void onHelp() {


    }

    // TODO nezavira se
    public void onBack() throws IOException {
        gameStage.close();
        Start.showMenu(gameStage);
    }
}
