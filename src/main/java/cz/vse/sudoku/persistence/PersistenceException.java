package cz.vse.sudoku.persistence;

/**
 * Instance třídy PersistenceException.
 * Spustí se v případě chyby při ukládání hry.
 *
 * @Matěj Žídek, Jan Kubata, Dominik Sluka, Jakub Frolík, Vít Kollarczyk
 */
public class PersistenceException extends Exception {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}