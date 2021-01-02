package cz.vse.sudoku.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testovací třída pro otestování třídy {@link NumberGenerator}.
 */
public class NumberGeneratorTest {

    private NumberGenerator numberGenerator;

    @Before
    public void setUp() {
        numberGenerator = new NumberGenerator();
    }

    @Test
    public void testRandomSudoku() {
        for (int i = 0; i <= 100; i++) {
            assertNotEquals(null, numberGenerator.getRandomSudoku());
        }
    }
}