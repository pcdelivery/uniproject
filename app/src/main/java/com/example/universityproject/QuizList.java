package com.example.universityproject;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;


// 242324
// [12341324, 23431242, 2141234, 213423]
class Question {
    private Integer _question;
    Integer _rightAnswer;
    private ArrayList<Integer> _answers;

    public Question(Integer question, Integer answer, Integer ... answers) {
        this._question = question;
        this._rightAnswer = answer;

        for(Integer i : answers)
            this._answers.add(i);
    }

    public ArrayList<Integer> getIndexes() {
        return _answers;
    }

    public Integer getQuestion() {
        return _question;
    }
}

public class QuizList {
    String quizPlase;
    ArrayList<Question> list;

    public QuizList(String place) {
        this.quizPlase = place;

        //
        _init();
    }

    public int add(Question question) {
        if (question == null)
            return 0;

        list.add(question);
        return 1;
    }

    public void _init() {
        this.add(new Question(R.string.init_question_1, R.string.init_answer_2, R.string.init_answer_1, R.string.init_answer_2, R.string.init_answer_3, R.string.init_answer_4));
        this.add(new Question(R.string.init_question_2, R.string.init_answer_8, R.string.init_answer_5, R.string.init_answer_6, R.string.init_answer_7, R.string.init_answer_8));
    }
 }
