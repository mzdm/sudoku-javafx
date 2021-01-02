package cz.vse.sudoku.service;

import java.util.*;

public class Leaderboard {
    private List<User> userList;

    public Leaderboard(Map<String, Object> scoreMap) {
        this.userList = new ArrayList<>();
        addSortedScores(scoreMap);
    }

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

    public List<User> getUserList() {
        return userList;
    }
}
