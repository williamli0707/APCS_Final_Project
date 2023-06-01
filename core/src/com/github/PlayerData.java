package com.github;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores statistics of the player.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class PlayerData {
    /** username */
    public static String user = "";
    /** stats */
    public static int kills = 0, stars = 0, games = 0, wins = 0;

    /**
     * initializes the player data.
     * @param user the username
     * @param kills kills received from the server
     * @param stars stars received from the server
     * @param games games received from the server
     * @param wins wins received from the server
     */
    public static void init(String user, int kills, int stars, int games, int wins) {
        PlayerData.user = user;
        PlayerData.kills = kills;
        PlayerData.stars = stars;
        PlayerData.games = games;
        PlayerData.wins = wins;
    }

    /**
     * Adds a certain amount of stats to the total, and sends a server request to complete it on
     * the server side.
     * @param kills kills to add
     * @param stars stars to add
     * @param games games to add
     * @param wins wins to add
     */
    public static void add(int kills, int stars, int games, int wins) {
        //send req
        PlayerData.kills += kills;
        PlayerData.stars += stars;
        PlayerData.games += games;
        PlayerData.wins += wins;
        System.out.println("adding " + kills + " kills, " + stars + " stars, " + games + " games" + ", " + wins + " wins");
        try {
            req(kills, stars, games, wins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to update the player's stats.
     * @param kills kills to add
     * @param stars stars to add
     * @param games games to add
     * @param wins wins to add
     * @throws IOException in case unable to read server response
     */
    public static void req(int kills, int stars, int games, int wins) throws IOException {
        URL url = new URL("http://192.9.249.213:3000");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("stat", "2");
        parameters.put("user", user);
        parameters.put("kills", String.valueOf(kills));
        parameters.put("stars", String.valueOf(stars));
        parameters.put("games", String.valueOf(games));
        parameters.put("wins", String.valueOf(wins));

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        int status = con.getResponseCode();
        con.disconnect();

        if(status > 299) throw new ConnectException();
    }
}
