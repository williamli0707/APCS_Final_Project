package com.github;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    public static String user = "";
    public static int kills = 0, stars = 0, games = 0, wins = 0;
    public static void init(String user, int kills, int stars, int games, int wins) {
        PlayerData.user = user;
        PlayerData.kills = kills;
        PlayerData.stars = stars;
        PlayerData.games = games;
        PlayerData.wins = wins;
    }

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
