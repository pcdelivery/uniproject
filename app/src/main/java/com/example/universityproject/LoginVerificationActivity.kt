package com.example.universityproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.TextView
import android.database.sqlite.*
import java.sql.*

const val ACCOUNT_DETAILS_KEY = "[ACCOUNT_DETAILS_KEY]"

class LoginVerificationActivity : AppCompatActivity() {

    var login : String? = null
    var password : String? = null
    var database = SQLiteDatabaseHelper(this, "MYDATA.dll", null, 1)
    var verifiedAccount : ArcantownAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_verification)

        // @todo to loginActivity
        database.Build();

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
            if (password == "_google_pass")
                success()

            val db = database.readableDatabase
            val COMMAND_SELECT = "SELECT * FROM accounts WHERE" +
                    " login='${login}' AND" +
                    " password='${password}'"

            val cursor = db.rawQuery(COMMAND_SELECT, null)

            if (cursor.moveToFirst()) {
                val account : ArcantownAccount = ArcantownAccount(cursor.getString(1), cursor.getString(2))
                    .Build(cursor.getString(3), cursor.getString(4))
                account.id = cursor.getInt(0)
                verifiedAccount = account

                cursor.close()
                success()
            }
            else
                unsuccess()
        }, 1000)
    }

    private fun success() {
        val loginIntent = Intent(this, MainPageActivity::class.java)

        // ONLY IF VIA GOOGLE!
        if (intent.getStringExtra(ACCOUNT_PASSWORD) == "_google_pass")
            login = intent.getStringExtra(ACCOUNT_LOGIN_NAME)

//        loginIntent.putExtra(ACCOUNT_LOGIN_NAME, login.toString())
//        loginIntent.putExtra(ACCOUNT_PASSWORD, password.toString())

        // $todo use Parcelable
        // https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
        loginIntent.putExtra(ACCOUNT_DETAILS_KEY, "verifiedAccount?._getFullAccountInfo()")
        val temp = verifiedAccount?.accountInfo


        Toast.makeText(this, "[SUCCESS] $login : $password ($temp)", Toast.LENGTH_SHORT).show()
        startActivity(loginIntent)
    }

    private fun unsuccess() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "[Аккаунта с такими данными не существует] " + login + " : " + password, Toast.LENGTH_SHORT).show()
        startActivity(loginIntent)
    }
}
