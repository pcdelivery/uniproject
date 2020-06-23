package com.example.universityproject.data.Models;

import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class ArcantownAccount {
    public int getId() {
        return id;
    }

    public String getAuthType() {
        return authType;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPersonalName() {
        return personalName;
    }

    public int getPoints() {
        return points;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public String getCurrentTown() {
        return currentTown;
    }

    public String getCompletedPlaces() {
        return completedPlaces;
    }

    public int getBonus() {
        return bonus;
    }

    private int id;
    private String authType;
    private String email;
    private String login;
    private String personalName;

    private int points;
    private String currentCountry;
    private String currentTown;
//    private ArrayList<Integer> completedPlaces;
//    private ArrayList<Place> completedPlaces;
    private String completedPlaces;
    private int bonus;

//    public ArcantownAccount(int id, String authType, String email, int points) {
//        this.id = id;
//        this.authType = authType;
//        this.email = email;
//        this.points = points;
//    }

    public ArcantownAccount(String jsonContent) throws JSONException {
        JSONObject obj = new JSONObject(jsonContent);


        this.id = obj.getInt("id");
        this.authType = obj.getString("auth_type");
        this.email = obj.getString("email");
        this.login = obj.getString("login");
        this.personalName = obj.getString("name");
        this.points = obj.getInt("points");
        this.currentCountry = obj.getString("country");
        this.currentTown = obj.getString("town");
        this.completedPlaces = obj.getString("completed");
        this.bonus = obj.getInt("bonus");

        Log.d("Account", "Completely formed from JSON: " + this.generateJSONObject());
    }


//    <string name="bonus">0</string>
//    <string name="completed">0</string>


    public ArcantownAccount(SharedPreferences sp) throws JSONException {

        // todo delete -1
        this.id = Integer.decode(sp.getString("id", "-1"));
        this.authType = sp.getString("auth_type", "-1");
        this.email = sp.getString("email", "-1");
        this.login = sp.getString("login", "-1");
        this.personalName = sp.getString("name", "-1");
        this.points = Integer.decode(sp.getString("points", "-1"));
        this.currentCountry = sp.getString("cur_country", "-1");
        this.currentTown = sp.getString("cur_town", "-1");
        this.completedPlaces = sp.getString("completed", "-1");
        this.bonus = Integer.decode(sp.getString("bonus", "-1"));

        Log.d("Account", "Completely formed from JSON: " + this.generateJSONObject());
    }

    public String generateJSONObject() {
//        StringBuilder places = new StringBuilder();
//        for (int i = 0; i < completedPlaces.size(); i++) {
//            if (i == 0)
//                places.append(completedPlaces.get(i));
//            else
//                places.append(",").append(completedPlaces.get(i));
//        }

        return String.format(
                Locale.getDefault(),
                        "{" +
                                "\"id\": %d," +
                                "\"auth_type\": \"%s\"," +
                                "\"email\": \"%s\"," +
                                "\"login\": \"%s\"," +
                                "\"name\": \"%s\"," +
                                "\"points\": %d," +
                                "\"country\": \"%s\"," +
                                "\"town\": \"%s\"," +
                                "\"completed\": \"%s\"," +
                                "\"bonus\": %d" +
                        "}",
                id, authType, email, login, personalName, points, currentCountry, currentTown, completedPlaces, bonus
        );
    }
}
