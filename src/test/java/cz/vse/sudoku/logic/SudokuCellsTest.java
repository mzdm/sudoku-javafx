package cz.vse.sudoku.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuCellsTest {
    private int[][] newGameSudokuBoard = {{0, 1, 3, 7, 4, 9, 6, 8, 2}, {4, 6, 2, 1, 5, 8, 7, 3, 9}, {9, 8, 7, 2, 3, 6, 1, 5, 4}, {1, 5, 6, 8, 9, 2, 4, 7, 3}, {8, 2, 4, 6, 7, 3, 5, 9, 1}, {7, 3, 9, 5, 1, 4, 2, 6, 8}, {0, 4, 8, 3, 2, 5, 9, 1, 7}, {3, 9, 1, 4, 6, 7, 8, 2, 5}, {2, 7, 5, 9, 8, 1, 3, 4, 6}};
    private int[][] sudokuBoardFilledCorrect = {{5, 1, 3, 7, 4, 9, 6, 8, 2}, {4, 6, 2, 1, 5, 8, 7, 3, 9}, {9, 8, 7, 2, 3, 6, 1, 5, 4}, {1, 5, 6, 8, 9, 2, 4, 7, 3}, {8, 2, 4, 6, 7, 3, 5, 9, 1}, {7, 3, 9, 5, 1, 4, 2, 6, 8}, {6, 4, 8, 3, 2, 5, 9, 1, 7}, {3, 9, 1, 4, 6, 7, 8, 2, 5}, {2, 7, 5, 9, 8, 1, 3, 4, 6}};
    private int[][] sudokuBoardFilledIncorrect = {{5, 1, 3, 7, 4, 9, 6, 8, 2}, {4, 6, 2, 1, 5, 8, 7, 3, 9}, {9, 8, 7, 2, 3, 6, 1, 5, 4}, {1, 5, 6, 8, 9, 2, 4, 7, 3}, {8, 2, 4, 6, 7, 3, 5, 9, 1}, {7, 3, 9, 5, 1, 4, 2, 6, 8}, {5, 4, 8, 3, 2, 5, 9, 1, 7}, {3, 9, 1, 4, 6, 7, 8, 2, 5}, {2, 7, 5, 9, 8, 1, 3, 4, 6}};

    private SudokuCells sudokuCellsNewGame;
    private SudokuCells sudokuCellsFilledCorrect;
    private SudokuCells sudokuCellsFilledIncorrect;

    @Before
    public void setUp() {
        sudokuCellsNewGame = new SudokuCells(newGameSudokuBoard);
        sudokuCellsFilledCorrect = new SudokuCells(sudokuBoardFilledCorrect);
        sudokuCellsFilledIncorrect = new SudokuCells(sudokuBoardFilledIncorrect);
    }

    @Test
    public void testInitializing() {
        Cell[][] cellsNewGame = new SudokuCells(sudokuCellsNewGame.getArraySudoku()).getArraySudoku();
        Cell[][] cellsFilledCorrect = new SudokuCells(sudokuCellsFilledCorrect.getArraySudoku()).getArraySudoku();
        Cell[][] cellsFilledIncorrect = new SudokuCells(sudokuCellsFilledIncorrect.getArraySudoku()).getArraySudoku();

        assertEquals(sudokuCellsNewGame.getArraySudoku(), cellsNewGame);
        assertEquals(sudokuCellsFilledCorrect.getArraySudoku(), cellsFilledCorrect);
        assertEquals(sudokuCellsFilledIncorrect.getArraySudoku(), cellsFilledIncorrect);
    }

    @Test
    public void testAreAllCellsFilled() {
        assertFalse(sudokuCellsNewGame.areAllCellsFilled());
        assertTrue(sudokuCellsFilledCorrect.areAllCellsFilled());
        assertTrue(sudokuCellsFilledIncorrect.areAllCellsFilled());
    }

    @Test
    public void testIsSudokuValid() {
        assertFalse(sudokuCellsNewGame.isSudokuValid());
        assertTrue(sudokuCellsFilledCorrect.isSudokuValid());
        assertFalse(sudokuCellsFilledIncorrect.isSudokuValid());
    }

    @Test
    public void testChangeElement() {
        SudokuCells copySudokuCellsNewGame = this.sudokuCellsNewGame;
        assertFalse(copySudokuCellsNewGame.areAllCellsFilled());
        assertFalse(copySudokuCellsNewGame.isSudokuValid());

        copySudokuCellsNewGame.changeElement(0, 0, 4);
        assertFalse(copySudokuCellsNewGame.areAllCellsFilled());
        assertFalse(copySudokuCellsNewGame.isSudokuValid());

        copySudokuCellsNewGame.changeElement(0, 0, 5);
        assertFalse(copySudokuCellsNewGame.areAllCellsFilled());
        assertTrue(copySudokuCellsNewGame.isSudokuValid());

        copySudokuCellsNewGame.changeElement(6, 0, 6);
        assertTrue(copySudokuCellsNewGame.areAllCellsFilled());
        assertTrue(copySudokuCellsNewGame.isSudokuValid());

        copySudokuCellsNewGame.changeElement(0, 0, 9);
        assertTrue(copySudokuCellsNewGame.areAllCellsFilled());
        assertFalse(copySudokuCellsNewGame.isSudokuValid());
    }
}