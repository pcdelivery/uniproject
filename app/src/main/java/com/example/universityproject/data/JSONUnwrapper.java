package com.example.universityproject.data;

import android.util.Log;

import com.example.universityproject.data.Models.ArcantownAccount;
import com.example.universityproject.data.Models.Place;
import com.example.universityproject.data.Models.QuizData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Using to show debug info to check if given string creates correct QuizData object
 */
public class JSONUnwrapper {
    /**
     * Using to show debug info to check if given string creates correct QuizData object
     * @param json: String in JSON notation (QuizData object)
     * @return String describing QuizData contents
     */
    public static String showJSON (@NotNull String json) {
        QuizData quiz = new QuizData(json);

        StringBuilder resLog = new StringBuilder();
        resLog.append("[Place:] ").append(quiz.getPlace().present()).append("\n");
        resLog.append("[Questions:] ").append("\n");

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            resLog.append("\t[Question " + i + ":] ").append("\n");
            resLog.append("\t\t[Question Image URI:] ").append(quiz.getQuestions().get(i).questionImageURI).append("\n");
            resLog.append("\t\t[Question:] ").append(quiz.getQuestions().get(i).question).append("\n");
//            resLog.append("\t\t[Correct ans index:] ").append(quiz.getQuestions().get(i).correctAnswerIndex).append("\n");
            resLog.append("\t\t[Question type:] ").append(quiz.getQuestions().get(i).questionType).append("\n");
            resLog.append("\t\t[Answers (ArrayList):] ").append(quiz.getQuestions().get(i).answers.toString()).append("\n");
            resLog.append("\t\t[First answer (example):] ").append(quiz.getQuestions().get(i).answers.get(0)).append("\n");
            resLog.append("\t\t[Last answer (example):] ").append(quiz.getQuestions().get(i).answers.get(quiz.getQuestions().get(i).answers.size()-1)).append("\n");
        }

        return resLog.toString();
    }

    /**
     *
     * @param jsonContent: {"specs": [...]}
     * @return
     * @throws JSONException
     */
    public static ArrayList<String> getSpecs(JSONObject jsonContent) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        JSONArray arr = jsonContent.getJSONArray("specs");

        for (int i = 0; i < arr.length(); i++)
            result.add(arr.getString(i));

        return result;
    }

    /**
     *
     * @param jsonContent: {"countries": [...]}
     * @return
     * @throws JSONException
     */
    public static ArrayList<String> getCountries(JSONObject jsonContent) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        JSONArray arr = jsonContent.getJSONArray("countries");

        for (int i = 0; i < arr.length(); i++)
            result.add(arr.getString(i));

        return result;
    }

    /**
     *
     * @param jsonContent: {"towns": [...]}
     * @return
     * @throws JSONException
     */
    public static ArrayList<String> getTowns(JSONObject jsonContent) throws JSONException {
        Log.d("GetTowns", jsonContent.toString());
        ArrayList<String> result = new ArrayList<>();
        JSONArray arr = jsonContent.getJSONArray("towns");

        for (int i = 0; i < arr.length(); i++)
            result.add(arr.getString(i));

        return result;
    }

    /**
     *
     * @param jsonContent: {"places": [{...}, {...}, ...]}
     * @return
     * @throws JSONException
     */
    public static ArrayList<Place> getPlaces(JSONObject jsonContent) throws JSONException {
        Log.d("getPlaces", "Json on enter: " + jsonContent.toString());
        ArrayList<Place> result = new ArrayList<>();
        JSONArray arr = jsonContent.getJSONArray("places");

        for (int i = 0; i < arr.length(); i++)
            result.add(new Place(arr.getJSONObject(i).toString()));

        return result;
    }

    /**
     *
     * @param jsonContent: {"account": {...}}
     * @return
     * @throws JSONException
     */
    public static ArcantownAccount getAccount(JSONObject jsonContent) throws JSONException {
        return new ArcantownAccount(jsonContent.getJSONObject("account").toString());
    }
}
