package cz.vse.sudoku.logic;

public class Cell {
    private int cellNum;
    private boolean isModifiable;

    public Cell(int cellNum) {
        this(cellNum, true);
    }

    public Cell(int cellNum, boolean isModifiable) {
        this.cellNum = cellNum;
        this.isModifiable = isModifiable;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCell(int cellNum) {
        if (isModifiable) {
            this.cellNum = cellNum;
        }
    }

    public boolean isModifiable() {
        return isModifiable;
    }
}

