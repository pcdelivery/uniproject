package com.example.universityproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.TextView


const val TRUE_LOGIN = "Admin"
const val TRUE_PASSWORD = "qwerty123"
const val UNSUCCESS = "LoginOrPassUnmatch"

class LoginVerificationActivity : AppCompatActivity() {

    var login : String? = null
    var password : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_verification)

        login = intent.getStringExtra(ACCOUNT_LOGIN_NAME)
        password = intent.getStringExtra(ACCOUNT_PASSWORD)
//        login = "Admin"
//        password = "qwerty123"

        findViewById<TextView>(R.id.ver_login).text = login


    }

    override fun onStart() {
        super.onStart()

        val handler = Handler()
        handler.postDelayed(Runnable {
            if (password == "_google_pass" || login == TRUE_LOGIN && password == TRUE_PASSWORD)
                success()
            else unsuccess()
        }, 1000)
    }

    private fun success() {
        val loginIntent = Intent(this, MainPageActivity::class.java)

        // ONLY IF VIA GOOGLE!
        if (intent.getStringExtra(ACCOUNT_PASSWORD) == "_google_pass")
            login = intent.getStringExtra(ACCOUNT_LOGIN_NAME)

        loginIntent.putExtra(ACCOUNT_LOGIN_NAME, login.toString())
        loginIntent.putExtra(ACCOUNT_PASSWORD, password.toString())

        Toast.makeText(this, "[SUCCESS] " + login + " : " + password, Toast.LENGTH_SHORT).show()
        startActivity(loginIntent)
    }

    private fun unsuccess() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "[Аккаунта с такими данными не существует] " + login + " : " + password, Toast.LENGTH_SHORT).show()
        startActivity(loginIntent)
    }
}
