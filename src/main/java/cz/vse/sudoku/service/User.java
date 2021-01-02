package cz.vse.sudoku.service;

/**
 * Třída User představuje hráče, kteří se budou snažit vyřešit sudoku v určitém čase.
 */
public class User {
    private String name;
    private int scoreTime;

    /**
     * Konstruktor pro vytvoření hráče se jménem a časem hry
     *
     * @param name      = jméno postavy
     * @param scoreTime = čas hry
     */
    public User(String name, int scoreTime) {
        this.name = name;
        this.scoreTime = scoreTime;
    }

    /**
     * Getter, který vrací jméno hráče
     *
     * @return jméno hráče
     */
    public String getName() {
        return name;
    }

    /**
     * Getter vracející čas hry
     *
     * @return čas hry
     */
    public int getScoreTime() {
        return scoreTime;
    }

    /**
     * Metoda, která vrací formatovaný String ze zadaného parametru v sekundách.
     * Formát je ve tvaru 'minuty:sekundy'.
     *
     * @return formatovaný String
     */
    public String getFormattedScoreTime() {
        int secondsTotal = scoreTime;
        int m = secondsTotal / 60;
        int s = secondsTotal % 60;

        return String.format("%02dm %02ds", m, s);
    }
}
