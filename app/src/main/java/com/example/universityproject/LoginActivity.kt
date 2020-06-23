package com.example.universityproject

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.universityproject.data.Models.ArcantownAccount
import com.example.universityproject.data.databases.DatabaseManager
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
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
import org.json.JSONObject
import kotlin.collections.ArrayList


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private val TAG = "LoginActivity"
    private val GOOGLE_SIGN_IN = 2020

    private lateinit var manager: DatabaseManager
    private var mAuth: FirebaseAuth? = null

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var googleLoginButton: SignInButton
    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText

    private lateinit var forbiddenPasswords: ArrayList<String>
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        forbiddenPasswords = ArrayList()
        forbiddenPasswords.add("google_pass")

        manager = DatabaseManager(applicationContext)
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.loginEditText)
        passwordTextView = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.default_sign_in_button)
        registerButton = findViewById(R.id.reg_button)
        googleLoginButton = findViewById(R.id.google_sign_in_button)

        loginButton.setOnClickListener {
            password = passwordTextView.text.toString()
            email = emailTextView.text.toString()

            if (forbiddenPasswords.contains(password))
                Toast.makeText(this, "Inappropriate password: " + password, Toast.LENGTH_LONG).show()
            else
                defaultLogin(false, email, password)
        }
        googleLoginButton.setOnClickListener {
            val ga = GoogleSignIn.getLastSignedInAccount(this)

            if (ga == null || ga.email != manager.emailFromShared) {
                // Last signed account wasnt detected
                // login or register
                Log.d(TAG, "Last Signed account is null")
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                        .requestProfile().build()

                val gc = GoogleApiClient.Builder(this).enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

                intent = Auth.GoogleSignInApi.getSignInIntent(gc)
                startActivityForResult(intent, GOOGLE_SIGN_IN)
            }
            else {
                email = ga.email.toString()
                Log.d(TAG, "Last Signed account was saved: $email")

                // enter
                defaultLogin(false, email, "google_pass")
            }
        }
        registerButton.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("auth_type", "def")
            startActivity(intent)
        }

        // Google previous sign in check
        if (mAuth != null && mAuth!!.currentUser != null)
            GetAccountInfoAndPassToMain(this, false)
                .execute("account&email=" + mAuth!!.currentUser?.email)
    }

    /**
     * Starts MainActivity if account <emailTextView, passwordTextView> exists in Firebase and MuSQL database
     */
    private fun defaultLogin(registerIfNotRegistered: Boolean, email: String, password: String) {
        Log.d(TAG, "Default login: " + email + " : " + password)

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "Login complete! ($email, $password)")
                        Toast.makeText(this@LoginActivity, "Login complete!", Toast.LENGTH_SHORT).show()
                        GetAccountInfoAndPassToMain(this@LoginActivity, registerIfNotRegistered, password, email)
                            .execute("account&email=" + email)
                    } else {
                        if (registerIfNotRegistered) {
                            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                                .putExtra("auth_type", password)
                                .putExtra("email", email)

                            startActivity(intent)
                        }

                        Log.w(TAG,"signInWithEmail: Failure ($email, $password)", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed. Email-password pair isn't right", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GOOGLE_SIGN_IN -> {
                val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                if (signInResult!!.isSuccess) {
                    Toast.makeText(this, "Google Sign-In was successful", Toast.LENGTH_LONG).show()
                    val account = signInResult.signInAccount

                    Log.d(TAG, "After Google sign-in: " + account?.email + " : " + "google_pass")
                    // Google Account is correct but we're not sure if it's registered in MySQL database
                    // Register if not registered anyway
                    defaultLogin(true, account?.email.toString(), "google_pass")
                } else {
                    Log.d(TAG, "After Google sign-in: ERROR WHILE SIGN-IN")
                    Toast.makeText(this, "Google Sign-In wasn't successful", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Error: Connection Failed", Toast.LENGTH_LONG).show()
    }

    // Getting account info or "false"
    // url-tail example: "account&email=email@email.email"
    private class GetAccountInfoAndPassToMain(context: Context, registerIfNotRegistered: Boolean = false, auth_type: String? = "def", email: String? = "null") : ReceiveDataFromMySQLTask() {
        private val pd = ProgressDialog(context)
        private val mContext = context

        private val mRegisterIfNotRegistered = registerIfNotRegistered
        private val mAuthType = auth_type
        private val mEmail = email

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
            else if (result.equals("false")) {
                // No such account in MySQL
                if (mRegisterIfNotRegistered) {
                    Toast.makeText(mContext, "Account wasn't found. Register init", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterIfNotRegistered", "auth_type=" + mAuthType + " email=" + mEmail)

                    val intent = Intent(mContext, RegisterActivity::class.java)
                        .putExtra("auth_type", mAuthType)
                        .putExtra("email", mEmail)

                    mContext.startActivity(intent)
                } else
                    Toast.makeText(
                        mContext,
                        "Error while fetching data. Is your account still exist?",
                        Toast.LENGTH_LONG
                    ).show()
            }
            else {
                Log.d("LoginActivityAsync", "Result includes account: " + result)
                val accountBald = JSONObject(result).getJSONObject("account")
                DatabaseManager(mContext).setShared(ArcantownAccount(accountBald.toString()))

                Toast.makeText(mContext, "Welcome back again!", Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, MainPageActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

}

