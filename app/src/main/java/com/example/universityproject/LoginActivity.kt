package com.example.universityproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login_page.*


const val GOOGLE_SIGN_IN_REQUEST = 2020
const val ACCOUNT_LOGIN_NAME = "[USER_LOGIN]"
const val ACCOUNT_PASSWORD = "[USER_PASSWORD]"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)


        findViewById<Button>(R.id.default_sign_in_button).setOnClickListener { login() }
        findViewById<SignInButton>(R.id.google_sign_in_button).setOnClickListener { enterGoogle() }
    }

    private fun login() {
        val intent = Intent(this, LoginVerificationActivity::class.java)

        intent.putExtra(ACCOUNT_LOGIN_NAME, loginEditText.text.toString())
        intent.putExtra(ACCOUNT_PASSWORD, passwordEditText.text.toString())
        startActivity(intent)
    }

    private fun enterGoogle() {
        var account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            // Request more
            // https://developers.google.com/identity/sign-in/android/additional-scopes
            Toast.makeText(this, "La1", Toast.LENGTH_SHORT).show()
            val gsOptions : GoogleSignInOptions = GoogleSignInOptions
                .Builder()
                .requestIdToken("365090157083-ooslts2c10sn60rpkr587u0s12h2bjtg.apps.googleusercontent.com")
                .requestScopes(Scope(Scopes.PROFILE))
                .requestScopes(Scope(Scopes.EMAIL))
                .requestProfile()
                .requestEmail().build()
            val gsClient = GoogleSignIn.getClient(this, gsOptions)
            startActivityForResult(gsClient.signInIntent, GOOGLE_SIGN_IN_REQUEST)
        }
        else
            updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "qonActivityResult", Toast.LENGTH_SHORT).show()

//        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {
//        val name = GoogleSignIn.getSignedInAccountFromIntent(data).result?.displayName
        val name = GoogleSignIn.getSignedInAccountFromIntent(data).exception.toString()
        findViewById<TextView>(R.id.textView).text = name
//        val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//        if (task.isSuccessful) updateUI()
    }

    // @todo login + updateUI
    private fun updateUI(account : GoogleSignInAccount?) {
        val intent = Intent(this, LoginVerificationActivity::class.java)

        intent.putExtra(ACCOUNT_LOGIN_NAME, account?.displayName)
        intent.putExtra(ACCOUNT_PASSWORD, "_google_pass")
        startActivity(intent)

//        intent.putExtra("email", account?.email)
//        intent.putExtra("name", account?.displayName)
//        intent.putExtra("avi", account?.photoUrl)
    }
}

