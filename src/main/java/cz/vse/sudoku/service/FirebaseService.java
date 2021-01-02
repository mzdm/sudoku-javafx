package cz.vse.sudoku.service;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.io.UnsupportedEncodingException;
import java.util.*;

// https://firebase.google.com/docs/database/rest/start
// https://github.com/bane73/firebase4j

/**
 * Instance třídy FirebaseService. Služba umožňuje ukládání herního skóre.
 * Tyto se zobrazují v sekci Leaderboard spustitelného z menu.
 *
 * @author Matěj Žídek, Jakub Frolík, Vít Kollarczyk, Dominik Sluka, Jan Kubata
 * @version 1.0
 */
public class FirebaseService {
    private final String BASE_URL = "https://sudoku-javafx.firebaseio.com/scores";

    private Firebase firebase;
    private static FirebaseService instance;

    private FirebaseService() {
    }

    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }

    /**
     * Metoda ukládá hodnotu časového skóre, kterého hráč dosáhl.
     * @param user
     */
    public void saveScore(User user) {
        try {
            firebase = new Firebase(BASE_URL);
        } catch (FirebaseException e) {
            firebase = null;
            e.printStackTrace();
        }

        if (firebase != null) {
            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
            data.put(user.getName(), user.getScoreTime());

            try {
                firebase.patch(data);
            } catch (JacksonUtilityException | UnsupportedEncodingException | FirebaseException e) {
                e.printStackTrace();
            }
        }
    }

    public Leaderboard getScores() {
        try {
            firebase = new Firebase(BASE_URL);
        } catch (FirebaseException e) {
            e.printStackTrace();
        }

        if (firebase != null) {
            firebase.addQuery("orderBy", "\"$value\"");
            firebase.addQuery("limitToFirst", "10");

            FirebaseResponse response = null;
            try {
                response = firebase.get();
            } catch (FirebaseException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (response != null && response.getCode() == 200) {
                Map<String, Object> scoreMap = response.getBody();
                Leaderboard leaderboard = new Leaderboard(scoreMap);
                return leaderboard;
            }
        }

        return null;
    }
}
