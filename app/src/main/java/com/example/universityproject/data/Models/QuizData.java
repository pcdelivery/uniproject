package com.example.universityproject.data.Models;

import android.util.Log;

import com.example.universityproject.data.Models.Place;
import com.example.universityproject.data.Models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class QuizData {
    private final static String TAG = "QuizData";

    private Place place;
    private ArrayList<Question> questions;

    public Place getPlace() {
        return place;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Get all questions about place
     * @param jsonObject: String like "{"place": {}, "questions": []}"
     */
    public QuizData(String jsonObject) {
        questions = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonObject);

            place = new Place(json.getJSONObject("place").toString());
            Log.d("QuizData", "Place created: " + place.present());

            JSONArray arr = json.getJSONArray("questions");
            for (int i = 0; i < arr.length(); i++)
                questions.add(new Question(
                        arr.getJSONObject(i)));
            Log.d(TAG, "Success!");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSONException: ERROR while creating JSON object");
        }
    }

    public String present() {
        StringBuilder resLog = new StringBuilder();

        resLog.append(place.present()).append("\n\n");

        for (Question q : questions)
            resLog.append(q.present()).append("\n");

        return resLog.toString();
    }
}
