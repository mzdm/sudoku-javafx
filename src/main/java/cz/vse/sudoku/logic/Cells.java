package cz.vse.sudoku.logic;

public class Cells {
    private int[][] arraySudoku;
    public static final int sizeSudoku = 9;

    public Cells(int[][] arraySudoku) {
        this.arraySudoku = new int[sizeSudoku][sizeSudoku];
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                this.arraySudoku[i][j] = arraySudoku[i][j];
                System.out.print(arraySudoku[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean checkRow() {
        for (int row = 0; row < sizeSudoku; row++)
            for (int col = 0; col < sizeSudoku - 1; col++)
                for (int col2 = col + 1; col2 < sizeSudoku; col2++)
                    if (arraySudoku[row][col] == arraySudoku[row][col2]) {
                        System.out.println("řádek špatně");
                        return false;
                    }
        return true;
    }

    private boolean checkColumns() {
        for (int col = 0; col < sizeSudoku; col++)
            for (int row = 0; row < sizeSudoku - 1; row++)
                for (int row2 = row + 1; row2 < sizeSudoku; row2++)
                    if (arraySudoku[row][col] == arraySudoku[row2][col]) {
                        System.out.println("sloupec špatně");
                        return false;
                    }
        return true;
    }

    private boolean checkGrids() {
        for (int row = 0; row < sizeSudoku; row += 3)
            for (int col = 0; col < sizeSudoku; col += 3)
                for (int pos = 0; pos < sizeSudoku - 1; pos++)
                    for (int pos2 = pos + 1; pos2 < sizeSudoku; pos2++)
                        if (arraySudoku[row + pos % 3][col + pos / 3] == arraySudoku[row + pos2 % 3][col + pos2 / 3]) {
                            System.out.println("grid špatně");
                            return false;
                        }
        return true;
    }

    public boolean checkSudoku() {
        return checkColumns() && checkGrids() && checkRow();
    }

    public int[][] getArraySudoku() {
        return arraySudoku;
    }

    public void setArraySudoku(int[][] arraySudoku) {
        this.arraySudoku = arraySudoku;
    }

}
