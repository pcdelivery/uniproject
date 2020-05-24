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
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.add_personal_info.*
import org.json.JSONObject
import kotlin.math.log

private const val SPECIALITY_REQUEST = 2101
private const val REQUEST_RESULT_OK = 1020
private const val MAP_AND_QUIZ_REQUEST = 8000
private const val TAG = "MainPageActivity"

class MainPageActivity : AppCompatActivity() {
    private lateinit var loginTextView: TextView
    private lateinit var interestsHint: TextView
    private lateinit var textPoints: TextView
    private lateinit var interestsButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var townListAdapter: ArrayAdapter<String>
    private lateinit var townList: ArrayList<String>

    private var userPoints : Int = 0
    private var userID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
//        setSupportActionBar(toolbar)

        interestsButton = findViewById<Button>(R.id.add_your_data_button)
        interestsHint = findViewById<TextView>(R.id.add_your_data_hint)
        loginTextView = findViewById<TextView>(R.id.textViewLogin)
        textPoints = findViewById<TextView>(R.id.points)
        sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
        loginTextView.text = sharedPreferences.getString("login", "Пользователь")
        userPoints = sharedPreferences.getString("points", "0").toString().toInt()
        userPoints = sharedPreferences.getString("id", "0").toString().toInt()

        // Spinners
        // TODO: add country select on registration and setting change
        val town = sharedPreferences.getString("cur_town", "Moscow").toString()
        var country = sharedPreferences.getString("cur_country", "Russia").toString()

        // TODO delete later
        // TODO country = nonull in mysql
        if (country == "null")
            country = "Russia"

        townList = arrayListOf(town)

        val townSpinner = findViewById<Spinner>(R.id.town_select_spinner)
        townListAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, townList)
        townSpinner.adapter = townListAdapter
        townSpinner.setSelection(0)

        townSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferences.edit().putString("town", parent?.selectedItem.toString()).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        Log.d(TAG, "Country to towns get: " + country)
        GetTownsJSON(townSpinner, this).execute("towns&country=" + country)
        // /Spinners


        //        listElements = new ArrayAdapter<String>(this, R.layout._adapter_item_test, new String[] {"Loading..."});
//        listElements.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        mSpinner.setAdapter(listElements);
        //

        // check if user got bonus before, hide button and textview
        checkBonus()


        findViewById<Button>(R.id.add_your_data_button).setOnClickListener {
            val intent = Intent(this, AddPersonalInfoActivity::class.java)
            startActivityForResult(intent, SPECIALITY_REQUEST)
        }
        findViewById<Button>(R.id.go_test_button).setOnClickListener {
            // TODO
            val currentCountry = sharedPreferences.getString("country", "Russia")
            val currentTown = sharedPreferences.getString("town", "null")

            GetPlacesAndGoToMap(this, this, MAP_AND_QUIZ_REQUEST)
                .execute("places&country=$currentCountry&town=$currentTown")
        }
        findViewById<Button>(R.id.account_info_button).setOnClickListener {
            val intent = Intent(this, AccountInfoActivity::class.java)
            intent.putExtra(ACCOUNT_DETAILS_KEY, this.intent.getStringExtra(ACCOUNT_DETAILS_KEY))

            startActivity(intent)
        }
        findViewById<Button>(R.id.go_map_button).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }

    private fun checkBonus() {
        val userGotBonusBefore = sharedPreferences.getString("bonus", "false")

        if (userGotBonusBefore.equals("true")) {
            interestsButton.visibility = View.GONE
            interestsHint.visibility = View.GONE
        }
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
                val newUserPoints = userPoints + 3

                UpdatePoints(sharedPreferences, interestsButton, interestsHint, textPoints, newUserPoints)
                    .execute("change&id=" + userID + "&field=points" + "&value=" + newUserPoints)
            }
            MAP_AND_QUIZ_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    var newPoints = userPoints

                    if (data != null)
                        newPoints += data.getIntExtra("points", 0)

                    UpdatePoints(sharedPreferences, null, null, textPoints, newPoints)
                        .execute("change&id=" + userID + "&field=points" + "&value=" + newPoints)
                } else
                    Toast.makeText(this, "Error while passing test", Toast.LENGTH_LONG).show()
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

    private class UpdatePoints(shared: SharedPreferences, buttonToHide: Button?, textToHide: TextView?, textPoints: TextView, newPoints: Int)
        : ReceiveDataFromMySQLTask() {
        val mSharedPreferences = shared
        val mButton = buttonToHide
        val mText = textToHide
        val mPoints = textPoints
        val mNewPoints = newPoints

        override fun onPostExecute(result: String?) {

            if (result.equals("true")) {
                Log.d("UpdatePoints", "Points updated: $result")
                mSharedPreferences.edit().putString("bonus", "true").apply()
                mSharedPreferences.edit().putInt("points", mNewPoints).apply()

                if (mButton != null)
                    mButton.visibility = View.GONE
                if (mText != null)
                    mText.visibility = View.GONE

                mPoints.text = mNewPoints.toString()
            }
        }
    }

    private class GetTownsJSON(spinner: Spinner, context: Context) : ReceiveDataFromMySQLTask() {
        private val mSpinner = spinner
        private val mContext = context


        override fun onPostExecute(result: String?) {
            Log.d(TAG, "JustGetOneJSON: $result")

            if (result != null) {
                val towns = JSONUnwrapper.getTowns(JSONObject(result))
                val newAdapter = ArrayAdapter<String>(mContext, R.layout._adapter_item_test, towns)
                mSpinner.adapter = newAdapter
            }
        }
    }
}
