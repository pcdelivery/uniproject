package com.example.universityproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.universityproject.data.databases.DatabaseManager
import com.example.universityproject.data.databases.PutDataToMySQLTask
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"

    private lateinit var manager : DatabaseManager
    private var mAuth: FirebaseAuth? = null

    private lateinit var email: String
    private lateinit var login: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        manager = DatabaseManager()

        findViewById<Button>(R.id.registration_button).setOnClickListener {
            email = findViewById<EditText>(R.id.registration_email_field).text.toString()
            login = findViewById<EditText>(R.id.registration_login_field).text.toString()
            password = findViewById<EditText>(R.id.registration_password_field).text.toString()


            registerAndContinueIfSuccess()
        }
    }

    private fun registerAndContinueIfSuccess() {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            applicationContext,
                            "Registration failed",
                            Toast.LENGTH_LONG
                        ).show()

                        updateUI(null)
                    }
                }
            })
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            // Incorrect email or password input
            registration_login.setTextColor(resources.getColor(R.color.red_alert))
            registration_password.setTextColor(resources.getColor(R.color.red_alert))
        }
        else {
            // Successful registration
            val sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
            manager.handleNewlyCreatedAccount("def", email, login, sharedPreferences)

            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra(ACCOUNT_EMAIL, email)
            startActivity(intent)
        }
    }

    private class PutNewAccountToMySQL : PutDataToMySQLTask() {
        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            Log.e("PutNewAccountToMySQL", "Error: data wasn't updated")
        }
    }

//    private fun putIntoDatabase() {
////        manager.sendAccountToServer()
//        Log.d(TAG, "Created account sending...")
//    }
//
//    private fun initUserPreferences() {
//        val sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
//        manager.getAccount(email, sharedPreferences)
//        Log.d(TAG, "Shared preferences editing: " + sharedPreferences.getString("auth_type", " "))
//    }
}
