package cz.vse.sudoku.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testovací třída pro otestování třídy {@link User}.
 */
public class UserTest {
    @Test
    public void testGetFormattedScoreTime() {
        User user1 = new User("test1", 55);
        assertEquals("test1", user1.getName());
        assertEquals(55, user1.getScoreTime());
        assertEquals("00m 55s", user1.getFormattedScoreTime());

        User user2 = new User("test2", 221);
        assertEquals("03m 41s", user2.getFormattedScoreTime());

        User user3 = new User("test3", 485);
        assertEquals("08m 05s", user3.getFormattedScoreTime());

        User user4 = new User("test4", 1234);
        assertEquals("20m 34s", user4.getFormattedScoreTime());

        User user5 = new User("test5", 4350);
        assertEquals("72m 30s", user5.getFormattedScoreTime());

        User user6 = new User("test6", 9861);
        assertEquals("164m 21s", user6.getFormattedScoreTime());
    }
}