package com.example.universityproject

//import com.firebase.ui.auth.AuthUI

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_page.*


const val GOOGLE_SIGN_IN = 2020
// @todo: delete
const val ACCOUNT_LOGIN_NAME = "[USER_LOGIN]"
const val ACCOUNT_EMAIL = "[USER_EMAIL]"
const val ACCOUNT_PASSWORD = "[USER_PASSWORD]"

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private final val TAG = "LoginActivity"
    lateinit var callbackManager : CallbackManager
    lateinit var email : String
    lateinit var password : String

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        mAuth = FirebaseAuth.getInstance();


        findViewById<Button>(R.id.default_sign_in_button).setOnClickListener {
            email = loginEditText.text.toString()
            password = passwordEditText.text.toString()
            login()
        }
        findViewById<SignInButton>(R.id.google_sign_in_button).setOnClickListener { enterGoogle() }
        findViewById<Button>(R.id.reg_button).setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            // @todo: activity for result
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
//        findViewById<Button>(R.id.button2).setOnClickListener {
//            startActivity(Intent(this, TEST_DIALOG_ACTIVITY::class.java))
//        }
        // /DialogBox

        // JSON
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, TEST_JSON_ACTIVITY::class.java))
        }
        // /JSON

        // Send pic to server
//        findViewById<Button>(R.id.button2).setOnClickListener {
//            startActivity(Intent(this, TEST_SEND_PICS_ACTIVITY::class.java))
//        }
        // /Send pic to server

        // facebook n' sheet
        val loginButton = findViewById<LoginButton>(R.id.facebook_login_button)
        loginButton.setPermissions("email")

        val callbackManager = CallbackManager.Factory.create();

        class FacebookAuth<T> : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                email = result!!.accessToken.userId.toString()

                findViewById<EditText>(R.id.loginEditText).text = Editable.Factory().newEditable(email)
                findViewById<EditText>(R.id.loginEditText).text = Editable.Factory().newEditable("_facebook_pass")

                continueWithoutAuth()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, "Error: Connection Failed", Toast.LENGTH_LONG).show()
            }

        }

        loginButton.registerCallback(callbackManager, FacebookAuth<LoginResult>())
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser


        if (currentUser != null) {
            Toast.makeText(applicationContext, "Welcome again!", Toast.LENGTH_SHORT).show()
            email = currentUser.email.toString()
            // @todo
            continueWithoutAuth()
        }
    }

    private fun login() {
        email = loginEditText.text.toString()
        password = passwordEditText.text.toString()

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth!!.currentUser
                        continueWithoutAuth()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG,"signInWithEmail:failure", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
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

    private fun continueWithoutAuth () {
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra(ACCOUNT_EMAIL, email)
        startActivity(intent)
    }
}

