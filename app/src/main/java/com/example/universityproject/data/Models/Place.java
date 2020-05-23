package com.example.universityproject.data.Models;

import android.content.ContentValues;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Place {
    private int id;

    public int getId() {
        return id;
    }

    private String title;
    private String pictureURI;
    private LatLng location;

    public String getTitle() {
        return title;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getDescription() {
        return "Position: " + country + ", " + town +
                "\nOpens: " + openTime + "\nCloses: " + closeTime +
                "\n\nRating: " + rating;
    }

    private int openTime;
    private int closeTime;
    private double rating;
    private String country;
    private String town;
//    private String photos;
//    private String types;
//    private String status;

    /**
     *
     * @param jsonContent: String like = "{"id": 0, "title": "" ...}"
     */
    public Place(String jsonContent) {
        try {
            JSONObject obj = new JSONObject(jsonContent);

            this.id = obj.getInt("id");
            this.title = obj.getString("title");
            this.pictureURI = obj.getString("picture_uri");
            this.openTime = obj.getInt("open_time");
            this.closeTime = obj.getInt("close_time");
            this.rating = obj.getDouble("rating");
            this.country = obj.getString("country");
            this.town = obj.getString("town");

            double lat = obj.getDouble("loc_lat");
            double lng = obj.getDouble("loc_long");
            location = new LatLng(lat, lng);

            Log.d("Place", "Completely formed from JSON");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Place", "Error while forming from JSON");
        }
    }

    public String present () {
        StringBuilder resLog = new StringBuilder();

        resLog.append("\t[Place ID: " + id + "]").append("\n");
        resLog.append("\t[Place title: " + title + "]").append("\n");
        resLog.append("\t[Place pictureURI: " + pictureURI + "]").append("\n");
        resLog.append("\t[Place location: " + location.latitude + ":" + location.longitude + "]").append("\n");
        resLog.append("\t[Place open time: " + openTime + "]").append("\n");
        resLog.append("\t[Place close time: " + closeTime + "]").append("\n");
        resLog.append("\t[Place rating: " + rating + "]").append("\n");
        resLog.append("\t[Place country: " + country + "]").append("\n");
        resLog.append("\t[Place town: " + town + "]").append("\n");

        return resLog.toString();
    }
//    public ContentValues generateDatabaseRow() {
//        ContentValues cv = new ContentValues();
//
//        cv.clear();
//
//        cv.put("name", this.title);
//        cv.put("location_lat", this.locLat);
//        cv.put("location_long", this.locLon);
//        cv.put("location_ne", this.locNE);
//        cv.put("location_sw", this.locSW);
//        cv.put("location_open_time", this.openTime);
//        cv.put("location_close_time", this.closeTime);
//        cv.put("photos", this.photos);
//        cv.put("types", this.types);
//        cv.put("status", this.status);
//        cv.put("rating", this.rating);
//
//        return cv;
//    }
}
