package cz.vse.sudoku.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testChangingCells() {
        Cell cellModifiable = new Cell(5);
        cellModifiable.setCell(1);
        assertTrue(cellModifiable.isModifiable());
        assertEquals(1, cellModifiable.getCellNum());

        cellModifiable.setCell(1);
        assertTrue(cellModifiable.isModifiable());
        assertEquals(1, cellModifiable.getCellNum());

        cellModifiable.setCell(6);
        assertTrue(cellModifiable.isModifiable());
        assertEquals(6, cellModifiable.getCellNum());

        Cell cellUnModifiable = new Cell(5, false);
        cellUnModifiable.setCell(1);
        assertFalse(cellUnModifiable.isModifiable());
        assertEquals(5, cellUnModifiable.getCellNum());
    }
}