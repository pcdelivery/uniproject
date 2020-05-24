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

    private lateinit var buttonList: ArrayList<Button>
    private lateinit var listAdapter: ArrayAdapter<Button>
    private lateinit var listAdapter2: ButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionImage = findViewById(R.id.quiz_image)
        questionText = findViewById(R.id.quiz_question)
        questionButtonsList = findViewById(R.id.quiz_button_list)
        buttonList = ArrayList<Button>()
        listAdapter = ArrayAdapter<Button>(this, R.layout.quiz_answer_button, buttonList)
        questionButtonsList.adapter = listAdapter

        DownloadImageTask(questionImage).execute("https://imgur.com/8ntwC3q.png")

        val json = this.intent.getStringExtra("quiz")

        if (json == null)
            // TODO
            Log.d(TAG, "Error: JSON is null")
        else {
            quiz = QuizData(json)
            Log.d(TAG, "QuizData success: " + quiz.present())
        }

        questionButtonsList.onItemClickListener = object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (correctAnswer.toLong() == id)
                    points += POINTS_FOR_ANSWER

                Toast.makeText(applicationContext, "click: " + id.toInt(), Toast.LENGTH_SHORT).show()
                updateQuestion(quiz.next)
            }

        }

        // todo set place image and name in the tab
        Log.d(TAG, "HERE: 1")

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
                val b = Button(this)
                b.text = answer.answer
                buttonList.add(b)
            }

            Log.d(TAG, "Button list to adapter: ${buttonList.get(0).text.toString()}")
            Log.d(TAG, "NOB: ${buttonList.size}")

            listAdapter.notifyDataSetChanged()
//            listAdapter = ArrayAdapter<Button>(this, R.layout.quiz_answer_button, buttonList)
//            questionButtonsList.adapter = listAdapter
        }
    }

    private fun quizIsDone() {
        intent = Intent()
            .putExtra("points", points)

        Toast.makeText(this, "Done! [$points]", Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
