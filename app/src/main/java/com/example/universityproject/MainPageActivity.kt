package com.example.universityproject

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.view.get
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.add_personal_info.*
import kotlin.math.log

const val SPECIALITY_REQUEST = 2101
const val TEST_REQUEST = 1002
const val REQUEST_RESULT_OK = 1020

class MainPageActivity : AppCompatActivity() {

    private var userPoints : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
//        setSupportActionBar(toolbar)

        val login = intent.getStringExtra(ACCOUNT_EMAIL)

        if (login == null)
            findViewById<TextView>(R.id.textViewLogin).text = login
        else
            findViewById<TextView>(R.id.textViewLogin).text = ""

        findViewById<Button>(R.id.addDataAboutUrself).setOnClickListener {
            val intent = Intent(this, AddPersonalInfoActivity::class.java)
            startActivityForResult(intent, SPECIALITY_REQUEST)
        }
//        findViewById<Button>(R.id.doTest).setOnClickListener {
//            val intent = Intent(this, TryActivity::class.java)
//            startActivityForResult(intent, TEST_REQUEST)
//        }
        findViewById<Button>(R.id.doTest).setOnClickListener {
            // TODO
            GetPlacesAndGoToMap(this, this).execute("places&town=Moscow&country=Russia")
            // TODO return if null
        }
        findViewById<Button>(R.id.accountInfoButton).setOnClickListener {
            val intent = Intent(this, AccountInfoActivity::class.java)
            intent.putExtra(ACCOUNT_DETAILS_KEY, this.intent.getStringExtra(ACCOUNT_DETAILS_KEY))

            startActivity(intent)
        }
        findViewById<Button>(R.id.doMap).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }


        // initial set
        updatePoints()
    }

    override fun onStart() {
        super.onStart()

        // load and set
//        findViewById<TextView>(R.id.points).text = getString(R.string.you_have_x_points, userPoints)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, PreferencesActivity::class.java))
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null)
            return

        when (requestCode) {
            SPECIALITY_REQUEST -> {
                updatePoints(5)
                findViewById<Button>(R.id.addDataAboutUrself).visibility = View.GONE
                findViewById<TextView>(R.id.addDataAboutUrself_hint).visibility = View.GONE
            }
            TEST_REQUEST -> {
                updatePoints(data.getIntExtra(POINTS_RECIEVED_KEY, 0))
            }
        }
    }

    private fun updatePoints(increment: Int = 0) {
        userPoints += increment
        findViewById<TextView>(R.id.points).text = getString(R.string.you_have_x_points, userPoints)
    }

    private class GetPlacesAndGoToMap(context: Context, activity: Activity) : ReceiveDataFromMySQLTask(){
        private val mContext = context
        private val mActivity = activity

        override fun onPostExecute(result: String?) {
            Log.d("GetPlacesAndGoToMap", "Places received: $result")
            val intent = Intent(mContext, MapActivity::class.java)
            intent.putExtra("places", result)
            mActivity.startActivity(intent)
        }
    }
}
