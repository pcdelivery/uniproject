package com.example.universityproject.data.Models;

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

//    public ArcantownAccount(int id, String authType, String email, int points) {
//        this.id = id;
//        this.authType = authType;
//        this.email = email;
//        this.points = points;
//    }

    public ArcantownAccount(String jsonContent) throws JSONException {
        JSONObject obj = new JSONObject(jsonContent);


        this.id = obj.getInt("id");
        Log.d("Account", "Progress_1");
        this.authType = obj.getString("auth_type");
        Log.d("Account", "Progress_2");
        this.email = obj.getString("email");
        Log.d("Account", "Progress_3");
        this.login = obj.getString("login");
        Log.d("Account", "Progress_4");
        this.personalName = obj.getString("name");
        Log.d("Account", "Progress_5");
        this.points = obj.getInt("points");
        Log.d("Account", "Progress_6");
        this.currentCountry = obj.getString("country");
        Log.d("Account", "Progress_7");
        this.currentTown = obj.getString("town");
        Log.d("Account", "Progress_8");
        this.completedPlaces = obj.getString("completed");
        Log.d("Account", "Progress_9");

//            completedPlaces = new ArrayList<Integer>();
//            JSONArray arr = obj.getJSONArray("completed");
//            for (int i = 0; i < arr.length(); i++)
//                completedPlaces.add(arr.getInt(i));

        Log.d("Account", "Completely formed from JSON");
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
                                "\"completed\": %s" +
                        "}",
                id, authType, email, login, personalName, points, currentCountry, currentTown, completedPlaces
        );
    }
}
