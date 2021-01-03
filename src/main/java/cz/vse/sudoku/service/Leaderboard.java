package cz.vse.sudoku.service;

import java.util.*;

/**
 * Třída Leaderboard představuje tabulku s nejlepšími výsledky
 */
public class Leaderboard {
    private List<User> userList;

    /**
     * Konstruktor pro vytvoření seznamu s nejlepšími výsledky (časy)
     *
     * @param scoreMap seznam nejlepších výsledků
     */
    public Leaderboard(Map<String, Object> scoreMap) {
        this.userList = new ArrayList<>();
        addSortedScores(scoreMap);
    }

    /**
     * Metoda přidívající výsledky z Firebase do leaderboardu
     *
     * @param scores výsledky
     */
    private void addSortedScores(Map<String, Object> scores) {
        Map<String, Integer> castedScores = (Map) scores;
        Set<Map.Entry<String, Integer>> set = castedScores.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(set);
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry);

            User user = new User(entry.getKey(), entry.getValue());
            userList.add(user);
        }
    }

    /**
     * Getter vracející seznam hráčů
     *
     * @return seznam hráčů
     */
    public List<User> getUserList() {
        return userList;
    }
}
