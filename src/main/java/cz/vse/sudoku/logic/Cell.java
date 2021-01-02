package cz.vse.sudoku.logic;

import java.io.Serializable;

/**
 * Instance třídy Cell. Nastavení parametrů a vlastností.
 * Každá buňka v sudoku je daná hodnotou typu Integer a liší
 * se pouze ve vlastnosti, zda-li je možné buňku přepisovat či nikoli.
 *
 * @author Matěj Žídek, Vít Kollarczyk, Dominik Sluka, Jan Kubata, Jakub Frolík
 * @version 1.0
 */
public class Cell implements Serializable {
    private int cellNum;
    private boolean isModifiable;

    /**
     * konstruktor pro vytvoření buňky v sudoku daná číslem.
     * Buňka je určená k vyplnění, tedy hodnota zda-li je
     * modifikovatelná je nastavena na true.
     *
     * @param cellNum typu Integer
     */
    public Cell(int cellNum) {
        this(cellNum, true);
    }

    /**
     * konstruktor pro vytvoření buňky, u které lze nastavit hodnotu a možnost,
     * zda - li lze přepisovat.
     *
     * @param cellNum
     * @param isModifiable
     */
    public Cell(int cellNum, boolean isModifiable) {
        this.cellNum = cellNum;
        this.isModifiable = isModifiable;
    }

    /**
     * Getter pro získání číselné hodnoty nacházející se v buňce.
     *
     * @return cellNum
     */
    public int getCellNum() {
        return cellNum;
    }

    /**
     * Setter pro nastavení číselné hodnoty v dané buňce.
     * Platí u modifikovatelných buněk.
     *
     * @param cellNum
     */
    public void setCell(int cellNum) {
        if (isModifiable) {
            this.cellNum = cellNum;
        }
    }

    /**
     * Metoda vrací hodnotu, zda-li je buňka modifikovatelná.
     *
     * @return isModifiable
     */
    public boolean isModifiable() {
        return isModifiable;
    }
}

