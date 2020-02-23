package com.example.universityproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class TryActivity : AppCompatActivity() {

    var currentQuestion : Int = 0
    var points : Int = 0
    var lastPressedAnswerID : Int? = null
    var isClicked : Boolean = false
    var ansID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        findViewById<TextView>(R.id.question_text).text = getString(R.string.init_place) + " ${currentQuestion + 1}"


        showQuestion(currentQuestion)
        setButton()
    }

    private fun showQuestion(index : Int) {
        isClicked = false

        if (index == 0) {
            findViewById<TextView>(R.id.question_place_text).text = "[" + getString(R.string.init_question_1) + "]"
            findViewById<Button>(R.id.answer_one).text = getString(R.string.init_answer_1)
            findViewById<Button>(R.id.answer_two).text = getString(R.string.init_answer_2)
            findViewById<Button>(R.id.answer_three).text = getString(R.string.init_answer_3)
            findViewById<Button>(R.id.answer_four).text = getString(R.string.init_answer_4)

            ansID = R.id.answer_two
        }
        else if (index == 1) {
            findViewById<TextView>(R.id.question_place_text).text = "[" + getString(R.string.init_question_2) + "]"
            findViewById<Button>(R.id.answer_one).text = getString(R.string.init_answer_5)
            findViewById<Button>(R.id.answer_two).text = getString(R.string.init_answer_6)
            findViewById<Button>(R.id.answer_three).text = getString(R.string.init_answer_7)
            findViewById<Button>(R.id.answer_four).text = getString(R.string.init_answer_8)

            ansID = R.id.answer_four
        }
        else
            testIsPassed()
    }

    private fun setButton() {

        findViewById<Button>(R.id.next_button).setOnClickListener {
            currentQuestion += 1

            if (lastPressedAnswerID == null)
                Toast.makeText(this, "Click on something!", Toast.LENGTH_SHORT).show()
            else if (lastPressedAnswerID == ansID)
                points += 1

            showQuestion(currentQuestion)
        }

        findViewById<Button>(R.id.answer_one).setOnClickListener {
            lastPressedAnswerID = R.id.answer_one
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.answer_two).setOnClickListener {
            lastPressedAnswerID = R.id.answer_two
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.answer_three).setOnClickListener {
            lastPressedAnswerID = R.id.answer_three
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.answer_four).setOnClickListener {
            lastPressedAnswerID = R.id.answer_four
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun testIsPassed() {
        val intent = Intent()
        intent.putExtra(POINTS_RECIEVED_KEY, points)
        setResult(REQUEST_RESULT_OK, intent)
        finish()
    }
}