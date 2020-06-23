package com.example.universityproject

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.get
import com.example.universityproject.data.JSONUnwrapper
import com.example.universityproject.data.databases.DatabaseManager
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.add_personal_info.*
import org.json.JSONObject
import kotlin.math.log

class MainPageActivity : AppCompatActivity() {
    private val SPECIALITY_REQUEST = 2101
    private val MAP_AND_QUIZ_REQUEST = 8000
    private val TAG = "MainPageActivity"

    private lateinit var loginTextView: TextView
    private lateinit var interestsHint: TextView
    private lateinit var textPoints: TextView
    private lateinit var interestsButton: Button

    private lateinit var townListAdapter: ArrayAdapter<String>
    private lateinit var townList: ArrayList<String>

    private lateinit var manager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
//        setSupportActionBar(toolbar)

        manager = DatabaseManager(this)

        interestsButton = findViewById(R.id.add_your_data_button)
        interestsHint = findViewById(R.id.add_your_data_hint)
        loginTextView = findViewById(R.id.textViewLogin)
        textPoints = findViewById(R.id.points)

        loginTextView.text = manager.loginFromShared
        textPoints.text = manager.pointsFromShared

        // Spinner handle
        var town = manager.townFromShared
        val country = manager.countryFromShared

        if (town == null)
            town = "Moscow"

        townList = arrayListOf(town)

        val townSpinner = findViewById<Spinner>(R.id.town_select_spinner)
        townListAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, townList)
        townSpinner.adapter = townListAdapter
        townSpinner.setSelection(0)

        townSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                manager.updateAccount("cur_town", parent?.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        Log.d(TAG, "Country to towns get: " + country)
        GetTownsJSON(townSpinner, this).execute("towns&country=" + country)
        // /Spinner

        // check if user got bonus before, hide button and textview
        checkBonus()


        findViewById<Button>(R.id.add_your_data_button).setOnClickListener {
            val intent = Intent(this, AddPersonalInfoActivity::class.java)
            startActivityForResult(intent, SPECIALITY_REQUEST)
        }
        findViewById<Button>(R.id.go_test_button).setOnClickListener {
            val currentCountry = manager.countryFromShared
            val currentTown = manager.townFromShared

            GetPlacesAndGoToMap(this, this, MAP_AND_QUIZ_REQUEST)
                .execute("places&country=$currentCountry&town=$currentTown")
        }

        findViewById<Button>(R.id.user_album_button).setOnClickListener {
            val intent = Intent(this, UserAlbumActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkBonus() {
        val userGotBonusBefore = manager.bonusFromShared

        if (userGotBonusBefore == "1")
            hideInterest()
    }

    private fun hideInterest() {
        interestsButton.visibility = View.GONE
        interestsHint.visibility = View.GONE
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

        when (requestCode) {
            SPECIALITY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val newUserPoints = manager.pointsFromShared.toInt() + 3

                    manager.updateAccount("points", newUserPoints.toString());
                    manager.updateAccount("bonus", "1");

                    textPoints.text = newUserPoints.toString()
                    hideInterest()
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d(TAG, "SPECIALITY_REQUEST: Fail")
                    Toast.makeText(this, "Well...", Toast.LENGTH_LONG).show()
                }
            }
            MAP_AND_QUIZ_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val addPoints = data.getIntExtra("points", 0)
                        val placeID = data.getIntExtra("placeid", 0)

                        textPoints.text = (manager.pointsFromShared.toInt() + addPoints).toString()

                        // update only after setting because of Async
                        manager.updateQuizInAccount(placeID.toString(), addPoints.toString());
                    }
                } else
                    Toast.makeText(this, "Error while passing and synchronising test", Toast.LENGTH_LONG).show()
            }
        }
    }


    private class GetPlacesAndGoToMap(context: Context, activity: Activity, requestCode: Int) : ReceiveDataFromMySQLTask() {
        private val mContext = context
        private val mActivity = activity
        private val mRequestCode = requestCode

        override fun onPostExecute(result: String?) {
            Log.d("GetPlacesAndGoToMap", "Places received: $result")
            val intent = Intent(mContext, MapActivity::class.java)
            intent.putExtra("places", result)
            mActivity.startActivityForResult(intent, mRequestCode)
        }
    }


    private class GetTownsJSON(spinner: Spinner, context: Context) : ReceiveDataFromMySQLTask() {
        private val mSpinner = spinner
        private val mContext = context


        override fun onPostExecute(result: String?) {
            Log.d("GetTownsJSON", "JustGetOneJSON: $result")

            if (result != null) {
                val towns = JSONUnwrapper.getTowns(JSONObject(result))
                val newAdapter = ArrayAdapter<String>(mContext, R.layout._adapter_item_test, towns)
                mSpinner.adapter = newAdapter
            }
        }
    }
}
