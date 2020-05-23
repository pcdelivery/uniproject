package com.example.universityproject

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import com.google.firebase.auth.FirebaseAuth

class PreferenceChangeActivity : AppCompatActivity() {
    private val TAG = "PreferenceChange"
    private var clientID: String? = null
    private var message: String? = null
    private var fieldName: String? = null
    private lateinit var fieldContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_change)

        message = this.intent.getStringExtra("message")
        fieldName = this.intent.getStringExtra("field")
        fieldContent = findViewById(R.id.preferences_change_field)
        clientID = getSharedPreferences("account", Context.MODE_PRIVATE).getString("id", "-1")

        findViewById<Button>(R.id.preferences_change_button).setOnClickListener {
            Log.d(TAG, "Activity init change: [Message] " + message + " [Field name] " +
                    fieldName + " [New Value] " + fieldContent.text + " [ID] " + clientID)

            tryToChangeData(this, this, fieldContent).execute(
                "change&id=" + clientID + "&field=" + fieldName + "&value=" + fieldContent.text)
        }
    }

    class tryToChangeData(context: Context, activity: Activity, fieldToGet: EditText) : ReceiveDataFromMySQLTask() {
        private var mContext: Context = context
        private var mActivity: Activity = activity
        private var newValue: String = fieldToGet.text.toString()

        private lateinit var mProgressDialog: ProgressDialog
        public var isSuccess = false

        init {
            mProgressDialog = ProgressDialog(context)
        }

        override fun onPreExecute() {
            mProgressDialog.setMessage("Loading...")
            mProgressDialog.show()
        }

        override fun onPostExecute(result: String?) {
            isSuccess = result.equals("true")

            mProgressDialog.dismiss()

            if (isSuccess) {
                mActivity.setResult(Activity.RESULT_OK, Intent().putExtra("result", newValue))
                Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show()
            }
            else {
                mActivity.setResult(Activity.RESULT_CANCELED)
                Toast.makeText(mContext, "Error while", Toast.LENGTH_LONG).show()
            }

            mActivity.finish()
        }
    }



}
