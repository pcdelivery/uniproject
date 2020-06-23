package com.example.universityproject

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.database.DataSetObserver
import android.util.Log
import android.widget.*
import android.widget.Toast
import com.example.universityproject.data.CheckBoxAdapter
import com.example.universityproject.data.JSONUnwrapper
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask
import org.json.JSONObject

class AddPersonalInfoActivity : AppCompatActivity() {
    private val TAG = "AddPersonalInfoActivity"
    private lateinit var checkBoxesList : ListView
    private var checkBoxesListAdapter : CheckBoxAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_personal_info)

        checkBoxesList = findViewById(R.id.checkboxes_list)

        findViewById<Button>(R.id.buttonNext).setOnClickListener {
            val checkedBoxesAdapter = checkBoxesList.adapter as CheckBoxAdapter
            val checked = checkedBoxesAdapter.checked

            Log.d(TAG, "Finish button clicked: Specs: $checked")

            val intent = Intent()
            intent.putExtra("specs", checked)

            if (checked == null || checked.size == 0)
                this.setResult(Activity.RESULT_CANCELED)
            else
                this.setResult(Activity.RESULT_OK)

            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        GetSpecsJSON(checkBoxesList, checkBoxesListAdapter, this).execute("specs")
    }


    private class GetSpecsJSON(checkBoxes: ListView, adapter: CheckBoxAdapter?, context: Context) : ReceiveDataFromMySQLTask() {
        private val mCheckBoxes = checkBoxes
        private var mAdapter = adapter
        private val mContext = context
        private var pd = ProgressDialog(context)

        override fun onPreExecute() {
            super.onPreExecute()

            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun onPostExecute(result: String?) {
            Log.d("GetSpecsJSON", "Result: $result")

            pd.dismiss()

            if (result == null || result == "false") {
                Toast.makeText(mContext, "Unable to fetch speciality list", Toast.LENGTH_LONG).show()
                Log.d("GetSpecsJSON", "Unable to fetch speciality list")

                Activity().setResult(Activity.RESULT_CANCELED)
                Activity().finish()
            }
            else {
                Log.d("GetSpecsJSON", "Successful fetching speciality list")
                mAdapter = CheckBoxAdapter(mContext, result)
                mCheckBoxes.adapter = mAdapter
            }
        }
    }
}
