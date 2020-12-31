package cz.vse.sudoku.logic;

public class SudokuCells {
    private Cell[][] arraySudoku;
    public static final int sizeSudoku = 9;

    public SudokuCells(int[][] generatedArrSudoku) {
        this.arraySudoku = new Cell[sizeSudoku][sizeSudoku];
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                int num = generatedArrSudoku[i][j];
                if (num == 0) {
                    this.arraySudoku[i][j] = new Cell(generatedArrSudoku[i][j]);
                } else {
                    this.arraySudoku[i][j] = new Cell(generatedArrSudoku[i][j], false);
                }
            }
        }
        printSudoku();
    }

    private boolean checkRows() {
        for (int row = 0; row < sizeSudoku; row++) {
            for (int col = 0; col < sizeSudoku - 1; col++) {
                for (int col2 = col + 1; col2 < sizeSudoku; col2++) {
                    int num1 = arraySudoku[row][col].getCellNum();
                    int num2 = arraySudoku[row][col2].getCellNum();
                    if (num1 == num2) {
                        System.out.println("řádek špatně");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkColumns() {
        for (int col = 0; col < sizeSudoku; col++) {
            for (int row = 0; row < sizeSudoku - 1; row++) {
                for (int row2 = row + 1; row2 < sizeSudoku; row2++) {
                    int num1 = arraySudoku[row][col].getCellNum();
                    int num2 = arraySudoku[row2][col].getCellNum();
                    if (num1 == num2) {
                        System.out.println("sloupec špatně");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkGrids() {
        for (int row = 0; row < sizeSudoku; row += 3) {
            for (int col = 0; col < sizeSudoku; col += 3) {
                for (int pos = 0; pos < sizeSudoku - 1; pos++) {
                    for (int pos2 = pos + 1; pos2 < sizeSudoku; pos2++) {
                        int num1 = arraySudoku[row + (pos % 3)][col + (pos / 3)].getCellNum();
                        int num2 = arraySudoku[row + (pos2 % 3)][col + (pos2 / 3)].getCellNum();
                        if (num1 == num2) {
                            System.out.println("grid špatně");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean areAllCellsFilled() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                if (arraySudoku[i][j].getCellNum() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSudokuValid() {
        return checkRows() && checkColumns() && checkGrids();
    }

    public void printSudoku() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                System.out.print(arraySudoku[i][j].getCellNum() + " ");
            }
            System.out.println();
        }
    }

    public Cell[][] getArraySudoku() {
        return arraySudoku;
    }

    public void changeElement(int i, int j, int newNum) {
        arraySudoku[i][j].setCell(newNum);
    }
}
