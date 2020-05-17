package com.example.universityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TEST_DIALOG_ACTIVITY extends AppCompatActivity {

        private Spinner mSpinner;
        private ArrayAdapter<String> listElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_test_dialog);

        String[] res = {"element_1", "element_2", "element_3", "element_4"};

        mSpinner = findViewById(R.id.TEST_SPINNER);
        listElements = new ArrayAdapter<String>(this, R.layout._adapter_item_test, res);
        listElements.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mSpinner.setAdapter(listElements);

//        listElements.add("element_1");
//        listElements.add("element_2");
//        listElements.add("element_3");
//        listElements.add("element_4");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
