package com.example.universityproject.data;

import android.content.ContentValues;

public class Place {
    private String title;
    private double locLat;
    private double locLon;
    private double locNE;
    private double locSW;
    private int openTime;
    private int closeTime;
    private String photos;
    private String types;
    private String status;
    private double rating;

    // Alt + Insert
    public Place(String title, double locLat, double locLon, double locNE, double locSW, int openTime, int closeTime, String photos, String types, String status, double rating) {
        this.title = title;
        this.locLat = locLat;
        this.locLon = locLon;
        this.locNE = locNE;
        this.locSW = locSW;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.photos = photos;
        this.types = types;
        this.status = status;
        this.rating = rating;
    }

    public ContentValues generateDatabaseRow() {
        ContentValues cv = new ContentValues();

        cv.clear();

        cv.put("name", this.title);
        cv.put("location_lat", this.locLat);
        cv.put("location_long", this.locLon);
        cv.put("location_ne", this.locNE);
        cv.put("location_sw", this.locSW);
        cv.put("location_open_time", this.openTime);
        cv.put("location_close_time", this.closeTime);
        cv.put("photos", this.photos);
        cv.put("types", this.types);
        cv.put("status", this.status);
        cv.put("rating", this.rating);

        return cv;
    }
}
