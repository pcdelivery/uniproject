package com.example.universityproject

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.universityproject.data.JSONUnwrapper
import com.example.universityproject.data.Models.Place
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList


// @todo? : https://www.youtube.com/watch?v=VOl3gkHJf1Q&list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt&index=9
// Last video I've seen
// https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi
// and here's another playlist from the same dude
// https://www.youtube.com/results?search_query=android+studio+google+maps
// Here you can find other videos about Google Maps API
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSIONS_REQUEST_CODE = 9002
    private val QUIZ_REQUEST_CODE = 9003
    private val DEFAULT_ZOOM = 15F

    private val TAG = "MapActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var cursor : Cursor
    private var mLocationPermissionsGranted : Boolean = false
    private lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var placesInTown: ArrayList<Place>

    private lateinit var mGpsIcon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (!isServiceOk())
            finish()

        val json = this.intent.getStringExtra("places")

        if (json == null) {
            Log.d(TAG, "Places unable to unwrap: $json")
            // TODO offer Telegram

        } else {
            Log.d(TAG, "Places to unwrap: $json")
            try {
                placesInTown = JSONUnwrapper.getPlaces(JSONObject(json))
            } catch (x: JSONException) {
                Toast.makeText(this, "Connection error: " + x.toString(), Toast.LENGTH_LONG).show()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        getLocationPermission()
        layoutInterfaceInit()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        val db = LocationDatabase(this)
//        val COMMAND_SELECT = "SELECT * FROM places WHERE status='OK'";
//        cursor = db.readableDatabase.rawQuery(COMMAND_SELECT, null)
//        cursor.moveToFirst()
    }

    private fun layoutInterfaceInit() {
        mGpsIcon = findViewById(R.id.ic_gps)
        mGpsIcon.setOnClickListener {
            getCurrentDeviceLocation()
        }

        val mTelegramBot = findViewById<ImageView>(R.id.ic_telegram)
        mTelegramBot.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/local_places_bot"))
            startActivity(intent)
        }

        // Billing required
        /*
        if (!Places.isInitialized())
            Places.initialize(applicationContext, resources.getString(R.string.google_maps_key), Locale.getDefault())

        val autocompleteSupportFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))

        autocompleteSupportFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                Log.d(TAG, "onPlaceSelected: Places was selected: " + p0.address)
                val location = p0.latLng as LatLng
                moveCamera(location, DEFAULT_ZOOM)

                try {
                    moveCamera(location, DEFAULT_ZOOM)
                } catch (e: Exception) {
                    Log.d(TAG, "autocompleteSupportFragment: onPlaceSelected: Error: " + e.message)
                }
            }

            override fun onError(p0: Status) {
                Log.d(TAG, "autocompleteSupportFragment: onError: " + p0.statusMessage)
            }
        })
        */
    }

    private fun getCurrentDeviceLocation() {
        Log.d(TAG, "getCurrentDeviceLocation: Getting location")

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            val location = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "onComplete: Found location")
                    val currentLocation = task.result
                    moveCamera(LatLng(currentLocation!!.latitude, currentLocation!!.longitude), DEFAULT_ZOOM)
                }
                else {
                    Log.d(TAG, "onComplete: Couldn't find location")
                }
            })
        }
        catch (e: SecurityException) {
            Log.d(TAG, "getCurrentDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, pinning: Boolean = false, markerTitle : String = "Null") {
        Log.d(TAG, "moveCamera: Moving camera to ${latLng.latitude}:${latLng.longitude} (${zoom})")
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        if (pinning) {
            val options = MarkerOptions()
                .position(latLng)
                .title(markerTitle)

            mMap.addMarker(options)
        }
    }

    private fun isServiceOk(): Boolean {
        Log.d(TAG, "isServicesOk: checking google services version")
        val googleServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)


        if (googleServicesStatus == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOk: Google Services is working correctly")
            return true
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(googleServicesStatus)) {
            Log.d(TAG, "isServicesOk: An error occurred but we can fix it")
            GoogleApiAvailability.getInstance().getErrorDialog(this, googleServicesStatus, 9001).show()
        }
        else {
            Log.d(TAG, "isServicesOk: Google Services Error. There's some yours problems with Google")
        }

        return false
    }

    private fun hideKeyboard() {
        val imm = applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        var view = this.currentFocus

        if (view == null)
            view = View(this)

        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getLocationPermission(){
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)

        mLocationPermissionsGranted = true

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(applicationContext, permission)
            == PackageManager.PERMISSION_DENIED) {
                mLocationPermissionsGranted = false
                break
            }
        }

        if (!mLocationPermissionsGranted) {
            Log.d(TAG, "getLocationPermission: Location permissions required. Trying to get some")
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSIONS_REQUEST_CODE)
        }
        else
            Log.d(TAG, "getLocationPermission: Location permissions granted")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false
                        Log.d(TAG, "onRequestPermissionsResult: One or more required permissions wasn't granted")
                        finish()
                    }
                }

                mLocationPermissionsGranted = true
                Log.d(TAG, "onRequestPermissionsResult: Location permissions was granted")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            QUIZ_REQUEST_CODE -> when(resultCode) {
                Activity.RESULT_OK -> {
                    // Just pass data to main menu
                    this.setResult(Activity.RESULT_OK, data)
                    finish()
                }
            }
        }
    }

    private fun geoLocate() {
        val searchRequest : String = findViewById<EditText>(R.id.input_search).text.toString()
        val geocoder = Geocoder(this)
        var results : List<Address> = ArrayList()

        Log.d(TAG, "geoLocate: Searching string: " + "\"${searchRequest}\"")

        try {
            results = geocoder.getFromLocationName(searchRequest, 1)
        } catch (e: IOException) {
            Log.d(TAG, "geoLocate: IOException: " + e.message + " (${searchRequest})")
        }

        if (results.isNotEmpty()) {
            Log.d(TAG, "geoLocate: Resulting list is not empty: " + results.size.toString())
            val address = results[0]

            moveCamera(LatLng(address.latitude, address.longitude), DEFAULT_ZOOM, true, address.getAddressLine(0))
            hideKeyboard()
        }
        else {
            Log.d(TAG, "geoLocate: Resulting list is empty")
            AlertDialog.Builder(this).setTitle("Ooh").setMessage("Can't find this place. Try to enter it somehow else").show()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this, "BAM!", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        if (mLocationPermissionsGranted) {
            getCurrentDeviceLocation()
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }

        findViewById<EditText>(R.id.input_search).setOnEditorActionListener { v, actionId, event ->
            Log.d(TAG, "input_search: Edit Action")

            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "input_search: Required action was triggered")
                // @todo
                v.text = v.text.toString().replaceFirst("\n", "")
                geoLocate()
            }
            Log.d(TAG, "input_search: Required action wasn't triggered")
            false
        }

        for (p: Place in placesInTown) {
            mMap.addMarker(MarkerOptions().position(p.location).title(p.title).snippet(p.description))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p.location))
        }

        mMap.setOnInfoWindowClickListener(object: GoogleMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(p0: Marker?) {
                Log.d(TAG, "Marker info window clock: " + p0?.title)
                var placeID = 0

                for (p: Place in placesInTown) {
                    placeID = p.id
                    if (p.title.equals(p0?.title))
                        break
                }

                GetQuizFromServer(applicationContext, this@MapActivity, QUIZ_REQUEST_CODE).execute("quiz&placeid=" + placeID)
            }
        })
    }

    private class GetQuizFromServer(context: Context, activity: Activity, requestCode: Int): ReceiveDataFromMySQLTask() {
        private val mContext = context
        private val mActivity = activity
        private val mRequestCode = requestCode

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val intent = Intent(mContext, QuizActivity::class.java)
            intent.putExtra("quiz", result)
            mActivity.startActivityForResult(intent, mRequestCode)
        }
    }
}
