package com.example.universityproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent

const val POINTS_RECIEVED_KEY = "[POINTS-RECIEVED]"

class QuizActivity : AppCompatActivity() {

    var currentQuestion : Int = 0
    var points : Int = 0
    var isClicked : Boolean = false
    var ql : QuizList = QuizList(getString(R.string.init_place))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        findViewById<TextView>(R.id.question_text).text = "${currentQuestion + 1}" + ". " + "Answer about"
        findViewById<TextView>(R.id.question_place_text).text = getString(R.string.init_place)

        showQuestion(currentQuestion)
        setButton()
    }

    private fun showQuestion(index : Int) {
        isClicked = false

        findViewById<Button>(R.id.answer_one).text = getString(ql.list[index].indexes[0])
        findViewById<Button>(R.id.answer_two).text = getString(ql.list[index].indexes[1])
        findViewById<Button>(R.id.answer_three).text = getString(ql.list[index].indexes[2])
        findViewById<Button>(R.id.answer_four).text = getString(ql.list[index].indexes[3])
    }

//    private fun updateQuestion() {
//        showQuestion()
//        setQuestionNumber()
//        setAnswerButtonText(1)
//    }

    private fun setButton() {
//        findViewById<Button>(R.id.prev_button).setOnClickListener {
//            mCurrentIndex = mCurrentIndex - 1
//            if (mCurrentIndex < 0) {
//                mCurrentIndex = mCountryList.mQuestions.size - 1
//            }
//            updateQuestion()
//        }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            currentQuestion += 1

            if (currentQuestion == ql.list.size)
                testIsPassed()

            if (!isClicked)
                Toast.makeText(applicationContext, "Click on something!", Toast.LENGTH_SHORT).show()
            else {
                showQuestion(currentQuestion)
            }
        }

        findViewById<Button>(R.id.answer_one).setOnClickListener {
            findViewById<Button>(R.id.answer_one).setTextColor(4)

            if (ql.list.get(currentQuestion)._rightAnswer == R.id.answer_one) {
                points += 1
                isClicked = true
            }

//            if (resources.getString(mCountryList.mQuestions[mCurrentIndex].capital) == resources.getString(mCountryList.mQuestions[mAnswer.get(0)].capital)) {
//                Toast.makeText(applicationContext, R.string.answer_true, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(applicationContext, R.string.answer_false, Toast.LENGTH_SHORT).show()
//            }
        }

        findViewById<Button>(R.id.answer_two).setOnClickListener {
            findViewById<Button>(R.id.answer_two).setTextColor(4)

            if (ql.list.get(currentQuestion)._rightAnswer == R.id.answer_two) {
                points += 1
                isClicked = true
            }
        }

        findViewById<Button>(R.id.answer_three).setOnClickListener {
            findViewById<Button>(R.id.answer_three).setTextColor(4)

            if (ql.list.get(currentQuestion)._rightAnswer == R.id.answer_three) {
                points += 1
                isClicked = true
            }
        }

        findViewById<Button>(R.id.answer_four).setOnClickListener {
            findViewById<Button>(R.id.answer_four).setTextColor(4)

            if (ql.list.get(currentQuestion)._rightAnswer == R.id.answer_four) {
                points += 1
                isClicked = true
            }
        }
    }

    private fun testIsPassed() {
        val intent = Intent()
        intent.putExtra(POINTS_RECIEVED_KEY, points)
        setResult(REQUEST_RESULT_OK, intent)
        finish()
    }

//    private fun setQuestionNumber() {
//        var isDuplicated = false
//        mAnswer.clear()
//        mAnswer.add(0, -1)
//        mAnswer.add(1, -1)
//        mAnswer.add(2, -1)
//        mAnswer.add(3, -1)
//
//        var count = 0
//        val random = Random()
//
//        var temp: Int
//        while (true) {
//            temp = random.nextInt(mCountryList.mQuestions.size - 1)
//            if (temp == mCurrentIndex) {
//                continue
//            }
//
//            for (i in 0..3) {
//                if (temp == mAnswer.get(i)) {
//                    isDuplicated = true
//                }
//            }
//            if (isDuplicated) {
//                isDuplicated = false
//                continue
//            } else {
//                mAnswer.set(count, temp)
//                count++
//            }
//            if (count > 3) {
//                break
//            }
//        }
//        mAnswer.set(0, mCurrentIndex)
//        Collections.shuffle(mAnswer)
//    }

//    private fun setAnswerButtonText() {
//        answer_one.setText(resources.getString(mCountryList.mQuestions[mAnswer.get(0)].capital))
//        answer_two.setText(resources.getString(mCountryList.mQuestions[mAnswer.get(1)].capital))
//        answer_three.setText(resources.getString(mCountryList.mQuestions[mAnswer.get(2)].capital))
//        answer_four.setText(resources.getString(mCountryList.mQuestions[mAnswer.get(3)].capital))
//    }

//    private fun setAnswerButtonText(i: Int) {
//        if (i == 0) {
//            answer_one.setText(resources.getString(mCountryList.mQuestions[0].capital))
//            answer_two.setText(resources.getString(mCountryList.mQuestions[2].capital))
//            answer_three.setText(resources.getString(mCountryList.mQuestions[3].capital))
//            answer_four.setText(resources.getString(mCountryList.mQuestions[4].capital))
//        }
//        else {
//            answer_one.setText(resources.getString(mCountryList.mQuestions[1].capital))
//            answer_two.setText(resources.getString(mCountryList.mQuestions[5].capital))
//            answer_three.setText(resources.getString(mCountryList.mQuestions[6].capital))
//            answer_four.setText(resources.getString(mCountryList.mQuestions[7].capital))
//        }
//    }
}
