package com.example.universityproject.data.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class QuizData {
    private final static String TAG = "QuizData";

    private int _id;

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
        _id = -1;
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

    /**
     *
     * @return: next Question or null
     */
    public Question getNext() {
        if (questions.size() <= 0)
            return null;
        else if (_id + 1 >= questions.size())
            return null;
        else if (_id == -1)
            _id = 0;
        else
            _id++;

        return this.questions.get(_id);
    }

    public Question getPrevious() {
        if (questions.size() <= 0)
            return null;
        else if (_id == -1)
            _id = 0;
        else if (_id - 1 < 0)
            return null;
        else
            _id--;

        return this.questions.get(_id);
    }

    public int getCurrent() {
        return _id;
    }
}
