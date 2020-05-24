package com.example.universityproject.data.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
    private int id;
    private int question_id;
    private String answer;
    private boolean isTrue;
    private String imageUri;

    public String getAnswer() {
        return answer;
    }

    public Answer(JSONObject json) throws JSONException {
        id = json.getInt("id");
        question_id = json.getInt("question_id");
        answer = json.getString("answer");
        isTrue = json.getInt("is_true") == 1;
        imageUri = json.getString("image_uri");
    }

    public boolean isTrue() {
        return isTrue;
    }

    public String present() {
        StringBuilder resLog = new StringBuilder();

        resLog.append("\t[Answer ID: " + id + "]").append("\n");
        resLog.append("\t[Answer Question ID: " + question_id + "]").append("\n");
        resLog.append("\t[Answer Text: " + answer + "]").append("\n");
        resLog.append("\t[Answer Is Correct: " + isTrue + "]").append("\n");
        resLog.append("\t[Answer Image URI: " + imageUri + "]").append("\n");

        return resLog.toString();
    }
}
