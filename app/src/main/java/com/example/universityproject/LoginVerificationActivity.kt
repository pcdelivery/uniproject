package com.example.universityproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.TextView
import android.util.Log
import com.example.universityproject.data.Models.ArcantownAccount
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask

const val ACCOUNT_DETAILS_KEY = "[ACCOUNT_DETAILS_KEY]"

class LoginVerificationActivity : AppCompatActivity() {
    val TAG = "LoginVerificationActivity"

    lateinit var login : String
    lateinit var password : String
//    var database : AccountDatabase? = null
    var verifiedAccount : ArcantownAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_verification)

        // @todo to loginActivity
//        database = AccountDatabase(this, 1)

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
//            if (password == "_google_pass")
//                registerIfNotRegistered(login, password)

//            if (isRegistered(login, password)) {
//                success()
//            }

//                val account : ArcantownAccount = ArcantownAccount(cursor.getString(1), cursor.getString(2))
//                    .Build(cursor.getString(3), cursor.getString(4))
//                account.id = cursor.getInt(0)
//                verifiedAccount = account

//            else
//                unsuccess()
        }, 1000)
    }

    private fun isRegistered (loginToVerify : String, passwordToVerify : String = "_google_pass"): Boolean {
//        val db = database!!.readableDatabase
//        val COMMAND_SELECT = "SELECT * FROM accounts WHERE" +
//                " login='${loginToVerify}' AND" +
//                " password='${passwordToVerify}'"

        val task = VerifyTask()
        val res = task.execute("auth&login=" + loginToVerify + "&email=" + loginToVerify + "@mail.ru" + "&password=" + passwordToVerify).get()

        if (res.equals("false")) {
            Toast.makeText(this, "FALSE", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (res.equals("true")) {
            Toast.makeText(this, "TRUE", Toast.LENGTH_SHORT).show()
            return true
    }
        else
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()

//        val cursor = db.rawQuery(COMMAND_SELECT, null)
//        cursor.close()

//        return cursor.moveToFirst()
        return false
    }

    /*
    private fun registerIfNotRegistered (loginToVerify : String, passwordToVerify : String = "_google_pass") {
        if (isRegistered(loginToVerify, passwordToVerify))
            return

        if (passwordToVerify == "_google_pass") {
            val db = database!!.writableDatabase
            val ga = GoogleSignIn.getLastSignedInAccount(this)

            AccountDatabase.putNewRow(db,
                ArcantownAccount(
                    loginToVerify,
                    passwordToVerify,
                    ga!!.displayName,
                    ga!!.familyName
                )
            )
        }
    }

    fun success() {
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

        database?.close()

        Toast.makeText(this, "[SUCCESS] $login : $password ($temp)", Toast.LENGTH_SHORT).show()
        startActivity(loginIntent)
    }
    */

    private fun unsuccess() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "[Аккаунта с такими данными не существует] " + login + " : " + password, Toast.LENGTH_SHORT).show()
//        database?.close()
        startActivity(loginIntent)
    }


    private class VerifyTask : ReceiveDataFromMySQLTask() {
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result.equals("false"))
                Log.d("LoginVerification", "FALSE")
            else if (result.equals("true")) {
                Log.d("LoginVerification", "TRUE")
                //
            }
            else
                Log.d("LoginVerification", "ERROR")
        }
    }
}
