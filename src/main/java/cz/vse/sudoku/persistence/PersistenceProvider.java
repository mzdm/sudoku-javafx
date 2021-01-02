package cz.vse.sudoku.persistence;

import cz.vse.sudoku.logic.Cell;

public interface PersistenceProvider {
    Cell[][] loadGame() throws PersistenceException;

    void saveGame(Cell[][] sudokuArray) throws PersistenceException;
}