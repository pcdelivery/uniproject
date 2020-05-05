package com.example.universityproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.universityproject.data.databases.AccountDatabase

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.registration_button).setOnClickListener {
            val db = AccountDatabase(this, 0).writableDatabase

            AccountDatabase.putNewRow(db, ArcantownAccount(
                findViewById<EditText>(R.id.registration_loginEditText).text.toString(),
                findViewById<EditText>(R.id.registration_passwordEditText).text.toString(),
                findViewById<EditText>(R.id.registration_nameEditText).text.toString(),
                findViewById<EditText>(R.id.registration_familyNameEditText).text.toString()
                )
            )
        }
    }
}
