package cz.vse.sudoku.persistence;

import cz.vse.sudoku.logic.Cell;

/**
 * Instance třídy pro načtení. Při uložení hry, v případě
 * chyby vyhodí persistenceException.
 *
 * @author Matěj Žídek, Jakub Frolík, Vít Kollarczy, Jan Kubata, Dominik Sluka
 * @version 1.0
 */
public interface PersistenceProvider {
    Cell[][] loadGame() throws PersistenceException;

    void saveGame(Cell[][] sudokuArray) throws PersistenceException;
}