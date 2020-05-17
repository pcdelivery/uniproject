package com.example.universityproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
//import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login_page.*


const val GOOGLE_SIGN_IN = 2020
const val ACCOUNT_LOGIN_NAME = "[USER_LOGIN]"
const val ACCOUNT_PASSWORD = "[USER_PASSWORD]"

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var callbackManager : CallbackManager
    // FIREBASE
//    lateinit var providers : List<AuthUI.IdpConfig>
    // /FIREBASE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        // FIREBASE
//        providers = listOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
//        )
        //


        findViewById<Button>(R.id.default_sign_in_button).setOnClickListener { login() }
        findViewById<SignInButton>(R.id.google_sign_in_button).setOnClickListener { enterGoogle() }
        findViewById<Button>(R.id.reg_button).setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // try Google Maps
//        findViewById<Button>(R.id.button2).setOnClickListener {
//            startActivity(Intent(this, MapActivity::class.java))
//        }
        // /try

        // try Server Connect
//        findViewById<Button>(R.id.button2).setOnClickListener {
//            startActivity(Intent(this, TEST_ACTIVITY::class.java))
//        }
        // /try

        // DialogBox
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, TEST_DIALOG_ACTIVITY::class.java))
        }
        // /DialogBox

        // facebook n' sheet
        val loginButton = findViewById<LoginButton>(R.id.facebook_login_button)
        loginButton.setPermissions("email")

        val callbackManager = CallbackManager.Factory.create();

        class sa<T> : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                findViewById<EditText>(R.id.loginEditText).text = Editable.Factory().newEditable(result!!.accessToken.userId.toString())
                findViewById<EditText>(R.id.passwordEditText).text = Editable.Factory().newEditable(result!!.accessToken.userId.toString())
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "[F]Warning: Connection Canceled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, "[F]Error: Connection Failed", Toast.LENGTH_LONG).show()
            }

        }
        loginButton.registerCallback(callbackManager, sa<LoginResult>())
    }

    private fun login() {
        verification(loginEditText.text.toString(), passwordEditText.text.toString())
    }

    private fun enterGoogle() {
        val ga = GoogleSignIn.getLastSignedInAccount(this)

//        if (ga == null) {
        if (true) {
//            Toast.makeText(this, "New access", Toast.LENGTH_LONG).show()
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                    .requestProfile().build()
            val gc = GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

            intent = Auth.GoogleSignInApi.getSignInIntent(gc)
            startActivityForResult(intent, GOOGLE_SIGN_IN)
//            finish()
        }
        else
            verification(ga!!.email.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)


            if (signInResult!!.isSuccess) {
                Toast.makeText(this, "Google Sign-In was successful", Toast.LENGTH_LONG).show()
                val account = signInResult.signInAccount


                /*
                // try
                val message = StringBuffer(account?.givenName.toString())
                message.append(account?.displayName)
                message.append(account?.familyName)
                message.append(account?.email)
                Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
                // /try
                */

                verification(account!!.email.toString())
            }
            else
                Toast.makeText(this, "Google Sign-In isn't successful", Toast.LENGTH_LONG).show()
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun verification (login : String, pass : String = "_google_pass") {
        val intent = Intent(this, LoginVerificationActivity::class.java)

        intent.putExtra(ACCOUNT_LOGIN_NAME, login)
        intent.putExtra(ACCOUNT_PASSWORD, pass)
        startActivity(intent)

//        intent.putExtra("email", account?.email)
//        intent.putExtra("name", account?.displayName)
//        intent.putExtra("avi", account?.photoUrl)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Error: Connection Failed", Toast.LENGTH_LONG).show()
    }
}

