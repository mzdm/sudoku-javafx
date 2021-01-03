package cz.vse.sudoku.service;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Testovací třída pro otestování třídy {@link Leaderboard}.
 */
public class LeaderboardTest {

    private final Map<String, Object> FAKE_RESPONSE_DATA = new HashMap<String, Object>() {{
        put("brad pitt", 1521);
        put("novák", 405);
        put("někdo123", 8420);
    }};

    @Test
    public void testLeaderboard() {
        Leaderboard leaderboard = new Leaderboard(FAKE_RESPONSE_DATA);
        List<User> userList = leaderboard.getUserList();

        assertEquals(3, userList.size());

        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);

        // top 1
        assertEquals("novák", user1.getName());

        // top 2
        assertEquals("brad pitt", user2.getName());

        // top 3
        assertEquals("někdo123", user3.getName());
    }
}