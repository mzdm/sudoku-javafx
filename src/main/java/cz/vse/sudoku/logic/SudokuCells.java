package cz.vse.sudoku.logic;

/**
 * Instance třídy SudokuCells.
 * Vytváří sudoku ve formě pole s pevně danou velikostí (9 řádků, 9 sloupců)
 * V případě, že daná buňka v zadání je vyplněná (není 0), tak se daná buňka
 * nastaví na možnost, aby nebylo možné ji modifikovat.
 *
 * @author Jan Kubata, Dominik Sluka, Matěj Žídek, Vít Kollarczyk, Jakub Frolík
 * @version 1.0
 */
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

    /**
     * Načítá konkrétní sudoku, které již bylo uložené.
     * @param loadedSudokuSaveFile
     */
    public SudokuCells(Cell[][] loadedSudokuSaveFile) {
        this.arraySudoku = loadedSudokuSaveFile;
        System.out.println("loaded");
        printSudoku();
    }

    /**
     * Metoda kontroluje, zda-li se v daném řádku již nenachází stejné číslo.
     * V případě že ano, je to proti pravidlům hry. Metoda kontroluje
     * všechny řádky v Sudoku.
     *
     * @return false v případě, že se v daném řádku již dané číslo nachází, jinak true.
     */
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

    /**
     * Metoda kontroluje všechny sloupce v sudoku a v případě, že se
     * v jednom sloupci nachází konkrétní číslo vícekrát, je to opět proti pravidlům hry.
     * @return false, v případě, že se číslo již v daném sloupci nachází. V případě, že ne, vrací hodnotu true.
     */
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

    /**
     * Metoda kontroluje menší pole o velikosti 3x3. Dle pravidel se v tomto menším poli
     * rovněž nesmí nacházet stejné číslo.
     * @return true, v případě, že se dané číslo v poli již nenachází, jinak false.
     */
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

    /**
     * Metoda ověřuje, jsou-li vyplněny všechny buňky v celém poli sudoku.
     * @return false, v případě, že nějaká buňka není vyplněna, jinak true.
     */
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

    /**
     * Metoda kombinuje výše zmíněné metody, které se zaměřují na pravidla hry sudoku.
     * Tj. ve stejném řádku, sloupci a menším poli se nesmí nacházet stejné číslo vícekrát.
     * @return kombinace ověřujících metod checkRows, checkColumns, checkGrids
     */
    public boolean isSudokuValid() {
        return checkRows() && checkColumns() && checkGrids();
    }

    /**
     * Metoda tiskne sudoku.
     */
    public void printSudoku() {
        for (int i = 0; i < sizeSudoku; i++) {
            for (int j = 0; j < sizeSudoku; j++) {
                System.out.print(arraySudoku[i][j].getCellNum() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Getter pro navrácení celého pole sudoku.
     * @return pole Sudoku
     */
    public Cell[][] getArraySudoku() {
        return arraySudoku;
    }

    /**
     * Nastavuje danou buňku konkrétním číslem, je-li tato přepsána.
     * @param i Integer
     * @param j Integer
     * @param newNum Integer
     */
    public void changeElement(int i, int j, int newNum) {
        arraySudoku[i][j].setCell(newNum);
    }
}
