package com.example.universityproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.*
import android.widget.Toast

const val OVERALL_SPECIALITY = "[SPECIALITY]"
const val SUB_SPECIALITY = "[SUB_SPECIALITY]"

class AddPersonalInfoActivity : AppCompatActivity() {
    private var isSecond : Boolean = false
    private var speciality : String? = "NONE"
    private var subSpeciality : String? = "NONE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_personal_info)


        findViewById<Button>(R.id.buttonNext).setOnClickListener {
            val group = findViewById<RadioGroup>(R.id.radioGroup)
            val checkedID = group.checkedRadioButtonId
            val checkedString = group.findViewById<RadioButton>(checkedID).text.toString()

//            Toast.makeText(this, "Check: " + checkedButton, Toast.LENGTH_SHORT).show()

            if (checkedID == -1)
                Toast.makeText(this, "Check something!", Toast.LENGTH_LONG).show()
            else {
                when (isSecond) {
                    false -> {speciality = checkedString; update(); }
                    true -> {subSpeciality = checkedString; endExecuting(); }
                }
            }
        }

        findViewById<TextView>(R.id.answerText).text = "Укажите вид своей профессиональной деятельности:"
        findViewById<RadioButton>(R.id.radioButton).text = "Гуманитарные науки"
        findViewById<RadioButton>(R.id.radioButton2).text = "Технические науки"
        findViewById<RadioButton>(R.id.radioButton3).text = "Экономика"
        findViewById<RadioButton>(R.id.radioButton4).text = "Лингвистика"
        findViewById<RadioButton>(R.id.radioButton5).text = "Другое"
    }

    private fun update() {
        isSecond = true

        findViewById<TextView>(R.id.answerText).text = "Техническая специальность:"
        findViewById<RadioButton>(R.id.radioButton).text = "Архитектура"
        findViewById<RadioButton>(R.id.radioButton2).text = "Электротехника"
        findViewById<RadioButton>(R.id.radioButton3).text = "Ядерная энергетика"
        findViewById<RadioButton>(R.id.radioButton4).text = "Машиностроение"
        findViewById<RadioButton>(R.id.radioButton5).text = "Информатика"
    }

    private fun endExecuting() {
        val intent = Intent()

        intent.putExtra(OVERALL_SPECIALITY, speciality)
        intent.putExtra(SUB_SPECIALITY, subSpeciality)
        setResult(REQUEST_RESULT_OK, intent)

        finish()
    }
}
