package cz.vse.sudoku.ui;

import cz.vse.sudoku.logic.Cells;
import cz.vse.sudoku.logic.NumberGenerator;
import cz.vse.sudoku.main.Start;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

import static cz.vse.sudoku.logic.Cells.sizeSudoku;

public class GameController {

    public GridPane sudokuGrid;
    Stage gameStage;
    Cells cells;

    public void init(Stage primaryStage) {
        gameStage = primaryStage;
        gameStage.close();

        NumberGenerator numberGenerator = new NumberGenerator();
        cells = new Cells(numberGenerator.getRandom());

        createGrid();
    }

    void printSudoku() {
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

    public void onBack() throws IOException {
        gameStage.close();
        Start.showMenu(gameStage);
    }
}
