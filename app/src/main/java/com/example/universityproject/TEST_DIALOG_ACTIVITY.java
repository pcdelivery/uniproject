package com.example.universityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask;
import com.google.android.gms.common.internal.GetServiceRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TEST_DIALOG_ACTIVITY extends AppCompatActivity {

    private Spinner mSpinner;
    private ArrayAdapter<String> listElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_test_dialog);

        String[] res = {"element_1"};

        mSpinner = findViewById(R.id.TEST_SPINNER);
//        listElements = new ArrayAdapter<String>(this, R.layout._adapter_item_test, new String[] {"Loading..."});
//        listElements.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        mSpinner.setAdapter(listElements);

        new GetTownList().execute("towns&country=Russia");
    }

    @SuppressLint("StaticFieldLeak")
    private class GetTownList extends ReceiveDataFromMySQLTask {
        @Override
        protected void onPostExecute(String pageReceived) {
            super.onPostExecute(pageReceived);

            String[] resourcesUpdate = pageReceived.split("([*])");

            for (String s : resourcesUpdate)
                Log.d("CURRENT_TAG!!!", s + " end");

            listElements = new ArrayAdapter<String>(getApplicationContext(), R.layout._adapter_item_test, resourcesUpdate);
            listElements.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinner.setAdapter(listElements);
        }
    }
}
