package cz.vse.sudoku.service;

public class User {
    private final String name;
    private final int scoreTime;

    public User(String name, int scoreTime) {
        this.name = name;
        this.scoreTime = scoreTime;
    }

    public String getName() {
        return name;
    }

    public int getScoreTime() {
        return scoreTime;
    }

    // TODO (minutes, seconds)
    public String getFormattedScoreTime() {
        return "";
    }
}
