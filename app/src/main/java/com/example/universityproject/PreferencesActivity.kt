package com.example.universityproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class PreferencesActivity : AppCompatActivity() {
    private final val REQUEST_CHANGE = 1100
    private final val TAG = "Preferences"

    private lateinit var changeMessage: String
    private lateinit var fieldToChange: String
    private lateinit var viewToHighlight: TextView
    private lateinit var editViewToHighlight: TextView

    private lateinit var emailButton: Button
    private lateinit var loginButton: Button
    private lateinit var nameButton: Button
    private lateinit var countryButton: Button
    private lateinit var passwordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)


        emailButton = findViewById<Button>(R.id.preferences_change_email_button)
        loginButton = findViewById<Button>(R.id.preferences_change_login_button)
        nameButton = findViewById<Button>(R.id.preferences_change_name_button)
        countryButton = findViewById<Button>(R.id.preferences_change_country_button)
        passwordButton = findViewById<Button>(R.id.preferences_change_password_button)

        val accountInfo = getSharedPreferences("account", Context.MODE_PRIVATE)

        when (accountInfo.getString("auth_type", "def")) {
            "def" -> findViewById<ImageView>(R.id.preferences_image).setImageResource(R.drawable.ic_account_logo)
            "google" -> findViewById<ImageView>(R.id.preferences_image).setImageResource(R.drawable.ic_google_logo)
            "facebook" -> findViewById<ImageView>(R.id.preferences_image).setImageResource(R.drawable.ic_facebook_logo)
        }

        findViewById<TextView>(R.id.preferences_email).text =
            accountInfo.getString("email", "")
        findViewById<TextView>(R.id.preferences_login).text =
            accountInfo.getString("login", "")
        findViewById<TextView>(R.id.preferences_name).text =
            accountInfo.getString("name", "")
        findViewById<TextView>(R.id.preferences_country).text =
            accountInfo.getString("cur_country", "")

        emailButton.setOnClickListener {
            changeMessage = "Введите новый email:"
            fieldToChange = "email"
            viewToHighlight = findViewById(R.id.preferences_email_string)
            editViewToHighlight = findViewById(R.id.preferences_email)
            changeClicked()
        }
        loginButton.setOnClickListener {
            changeMessage = "Введите новый логин:"
            fieldToChange = "login"
            viewToHighlight = findViewById(R.id.preferences_login_string)
            editViewToHighlight = findViewById(R.id.preferences_login)
            changeClicked()
        }
        nameButton.setOnClickListener {
            changeMessage = "Введите свое имя:"
            fieldToChange = "name"
            viewToHighlight = findViewById(R.id.preferences_name_string)
            editViewToHighlight = findViewById(R.id.preferences_name)
            changeClicked()
        }
        countryButton.setOnClickListener {
            changeMessage = "Введите текущую страну пребывания:"
            fieldToChange = "cur_country"
            viewToHighlight = findViewById(R.id.preferences_country_string)
            editViewToHighlight = findViewById(R.id.preferences_country)
            changeClicked()
        }
        passwordButton.setOnClickListener {
            changeMessage = "Введите новый пароль:"
            fieldToChange = "password"
            changeClicked()
        }
    }

    private fun changeClicked() {
        val changeIntent = Intent(this, PreferenceChangeActivity::class.java)
            .putExtra("message", changeMessage)
            .putExtra("field", fieldToChange)

        startActivityForResult(changeIntent, REQUEST_CHANGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val animationShake = AnimationUtils.loadAnimation(this, R.anim.shake)

        // @todo: requestCode
        when(resultCode) {
            Activity.RESULT_OK -> {
                val newValue = data?.getStringExtra("result").toString()
                val sp = getSharedPreferences("account", Context.MODE_PRIVATE)

                Log.d(TAG, "New Value: $newValue")

                if (fieldToChange != "password")
                    sp.edit().putString(fieldToChange, newValue).apply()

                viewToHighlight.setTextColor(resources.getColor(R.color.green_not_alert))
                editViewToHighlight.setTextColor(resources.getColor(R.color.green_not_alert))
                editViewToHighlight.setText(newValue)
                viewToHighlight.startAnimation(animationShake)

                if (fieldToChange == "email")
                    FirebaseAuth.getInstance().currentUser?.updateEmail(newValue)
                if (fieldToChange == "password")
                    FirebaseAuth.getInstance().currentUser?.updatePassword(newValue)
            }
            Activity.RESULT_CANCELED -> {
                viewToHighlight.setTextColor(resources.getColor(R.color.red_alert))
                viewToHighlight.startAnimation(animationShake)
            }
        }
    }
}
