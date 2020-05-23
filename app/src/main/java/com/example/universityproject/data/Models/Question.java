package com.example.universityproject.data.Models;

/* question-object example
    {
        "question-type" : "text",
        "question-unique-image" : "Blah-blah-blah",
        "question" : "Blah-blah-blah",
        "answers" : ["blah-blah-blah1", "blah-blah-blah2", "blah-blah-blah3"],
        "correctAnswerIndex" : 3
    }
     */

import android.util.Log;

import com.example.universityproject.data.Models.Answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Question {
    private static final String TAG = "Question";
    //@todo: private QuestionTypes questionType;
    public int id;
    public String questionType;
    public String questionImageURI;
    public String question;
    public String questionThemes;
    public ArrayList<Answer> answers;
//    public int correctAnswerIndex;

    /**
     *
     * @param json: String representation like "{"question_type": "", "question": "" ...}"
     */
    public Question(JSONObject json) {
        answers = new ArrayList<Answer>();

        try {
            id = json.getInt("id");
            if (json.getString("question_type").equals("text"))
                questionType = "text";
            else {
                questionType = "image";
                questionImageURI = json.getString("question_unique_image");
            }

            question = json.getString("question");
            questionThemes = json.getString("question_themes");

            JSONArray arr = json.getJSONArray("answers");
            for (int i = 0; i < arr.length(); i++) {
                Log.d(TAG, "Adding answer string: " + arr.getString(i));
                arr.getJSONObject(i);
                answers.add(
                        new Answer(arr.getJSONObject(i))
                );
            }

//            correctAnswerIndex = json.getInt("correct_answer_index");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public int id;
//    public String questionType;
//    public String questionImageURI;
//    public String question;
//    public String questionThemes;
//    public ArrayList<Answer> answers;

    public String present() {
        StringBuilder resLog = new StringBuilder();

        resLog.append("\t[Question ID: " + id + "]").append("\n");
        resLog.append("\t[Question Type: " + questionType + "]").append("\n");
        resLog.append("\t[Question Image URI: " + questionImageURI + "]").append("\n");
        resLog.append("\t[Question Text: " + question + "]").append("\n");
        resLog.append("\t[Question Themes: " + questionThemes + "]").append("\n");

        for (Answer a : answers)
            resLog.append(a.present()).append("\n\n");

        return resLog.toString();
    }
}
