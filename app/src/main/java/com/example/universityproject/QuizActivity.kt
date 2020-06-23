package com.example.universityproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.View
import android.widget.*
import com.example.universityproject.data.ButtonAdapter
import com.example.universityproject.data.DownloadImageTask
import com.example.universityproject.data.JSONUnwrapper
import com.example.universityproject.data.Models.Answer
import com.example.universityproject.data.Models.Place
import com.example.universityproject.data.Models.Question
import com.example.universityproject.data.Models.QuizData
import org.xmlpull.v1.XmlPullParser


class QuizActivity : AppCompatActivity() {
    private final val TAG = "QuizActivity"
    private final val POINTS_FOR_ANSWER = 2

    lateinit var questionImage: ImageView
    lateinit var questionText: TextView
    lateinit var questionButtonsList: ListView

    var points : Int = 0
    var correctAnswer = 0
    private lateinit var quiz: QuizData
    private lateinit var results: IntArray

    private lateinit var buttonList: ArrayList<String>
    private lateinit var listAdapter: ButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionImage = findViewById(R.id.quiz_image)
        questionText = findViewById(R.id.quiz_question)
        questionButtonsList = findViewById(R.id.quiz_button_list)
        buttonList = ArrayList<String>()
        listAdapter = ButtonAdapter(this, R.layout.quiz_answer_button, buttonList)
        questionButtonsList.adapter = listAdapter

        val json = this.intent.getStringExtra("quiz")

        if (json == null)
            // TODO
            Log.d(TAG, "Error: JSON is null")
        else {
            quiz = QuizData(json)
            Log.d(TAG, "QuizData success: " + quiz.present())

            DownloadImageTask(questionImage).execute(quiz.place.image)
            Log.d(TAG, "Image to download: " + quiz.place.image)
        }

        findViewById<Button>(R.id.prev_button).setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "Clicked: current=" + quiz.current + "correct="+correctAnswer)

                if (listAdapter.popLastClickedPosition() == correctAnswer)
                    results[quiz.current] = POINTS_FOR_ANSWER
                else
                    results[quiz.current] = 0

                updateQuestion(quiz.previous)
                Toast.makeText(applicationContext, "Prev", Toast.LENGTH_SHORT).show()
            }

        })

        findViewById<Button>(R.id.next_button).setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "Clicked: current=" + quiz.current + "correct="+correctAnswer)

                if (listAdapter.popLastClickedPosition() == correctAnswer)
                    results[quiz.current] = POINTS_FOR_ANSWER
                else
                    results[quiz.current] = 0

                updateQuestion(quiz.next)
            }

        })

        // todo set place image and name in the tab
        Log.d(TAG, "HERE: 1")

        results = IntArray(quiz.questions.size) {0}
        updateQuestion(quiz.next)
    }

    private fun updateQuestion(question: Question?) {

        if (question == null)
            quizIsDone()
        else {
            questionText.text = question.question
            correctAnswer = question.correctAnswerIndex

            buttonList.clear()

            for (answer: Answer in question.answers) {
                buttonList.add(answer.answer)
            }

//            Log.d(TAG, "Button list to adapter: ${buttonList.get(0).text.toString()}")
            Log.d(TAG, "NOB: ${buttonList.size}")

            listAdapter.notifyDataSetChanged()
        }
    }

    private fun quizIsDone() {
        for (i: Int in results)
            points += i

        intent = Intent()
            .putExtra("points", points)
            .putExtra("placeid", quiz.place.id)

        Log.d(TAG, "Quiz is done: $points : " + results.toString())
        Toast.makeText(this, "Done! [$points]", Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
