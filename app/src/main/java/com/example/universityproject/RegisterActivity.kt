package com.example.universityproject

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.universityproject.data.Models.ArcantownAccount
import com.example.universityproject.data.databases.DatabaseManager
import com.example.universityproject.data.databases.PutDataToMySQLTask
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import kotlin.math.log


class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"

    private lateinit var manager : DatabaseManager
    private var mAuth: FirebaseAuth? = null

    private lateinit var email: String
    private lateinit var login: String
    private lateinit var password: String

    private var authType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authType = intent.getStringExtra("auth_type")

        mAuth = FirebaseAuth.getInstance()
        manager = DatabaseManager(applicationContext)

        findViewById<Button>(R.id.registration_button).setOnClickListener {
            email = findViewById<EditText>(R.id.registration_email_field).text.toString()
            login = findViewById<EditText>(R.id.registration_login_field).text.toString()
            password = findViewById<EditText>(R.id.registration_password_field).text.toString()

            registerAndContinueIfSuccess()
        }

        if (authType == "google_pass") {
            email = intent.getStringExtra("email")
            login = email
            password = "google_pass"

            registerAndContinueIfSuccess()
        }
    }

    private fun registerAndContinueIfSuccess() {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "Firebase registration($email, $password): Success")
                        manager.handleNewlyCreatedAccount(authType, email, login)

                        val intent = Intent(this@RegisterActivity, MainPageActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d(TAG, "Firebase registration($email, $password): Failure")
                        registration_login.setTextColor(resources.getColor(R.color.red_alert))
                        registration_password.setTextColor(resources.getColor(R.color.red_alert))
                        Toast.makeText(applicationContext, "Registration failed. Mail or password incorrect", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    // Getting new account info or "false"
    // url-tail example: "put_acc&auth_type=[auth_type]&email=[email]&login=[login]"
    private class UpdateAccountAndPassToMain(context: Context) : ReceiveDataFromMySQLTask() {
        val pd = ProgressDialog(context)
        val mContext = context

        override fun onPreExecute() {
            super.onPreExecute()

            pd.setMessage("Fetching account info...")
            pd.setCancelable(false);
            pd.show()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            pd.dismiss()

            if (result == null)
                Toast.makeText(mContext, "Error while connecting to server", Toast.LENGTH_LONG).show()
            else if (result.equals("false"))
                Toast.makeText(mContext, "Error while fetching data. Is your account still exist?", Toast.LENGTH_LONG).show()
            else {
                val accountBald = JSONObject(result).getJSONObject("account")
                DatabaseManager(mContext).setShared(ArcantownAccount(accountBald.toString()))

                Toast.makeText(mContext, "Welcome back again!", Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, MainPageActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }
}
