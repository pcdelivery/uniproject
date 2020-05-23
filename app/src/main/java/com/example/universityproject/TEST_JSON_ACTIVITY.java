package com.example.universityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.universityproject.data.databases.PutDataToMySQLTask;
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask;

public class TEST_JSON_ACTIVITY extends AppCompatActivity {
    String SERVLET_URL = "http://192.168.1.33:8088/DJWA_war/MyServlet/t";
    private TextView tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_test_json);

        tw = findViewById(R.id.textView3);
//        new GetStringFromMySQLServer().execute("quiz&types=this*that*thisanother*&placeid=14");
//        new GetStringFromMySQLServer().execute("countries");
//        new GetStringFromMySQLServer().execute("towns&country=Russia");
//        new GetStringFromMySQLServer().execute("account&email=daniyar.burakaev@mail.ru");
//        new GetStringFromMySQLServer().execute("put_acc&auth_type=def&email=qw@mail.ru&login=qsLogin");

//        new GetStringFromMySQLServer().execute("update&arg=quiz&id=1&placeid=3&points=15");
//        new GetStringFromMySQLServer().execute("update&arg=country&id=1&country=China");
//        new GetStringFromMySQLServer().execute("update&arg=town&id=1&town=ChinaTown");

    }

    private class GetStringFromMySQLServer extends ReceiveDataFromMySQLTask {
        @Override
        protected void onPostExecute(String s) {
//            try {
//                tw.setText(JSONUnwrapper.getTowns(new JSONObject(s)).toString());
//
//                ArrayList<Place> places = JSONUnwrapper.getPlaces(new JSONObject(s));
//                StringBuilder sb = new StringBuilder();
//                for (Place p : places)
//                    sb.append(p.present());

//                JSONObject obj = new JSONObject(s);
//                ArcantownAccount acc = JSONUnwrapper.getAccount(obj);

//            QuizData d = new QuizData(s);
                tw.setText(s);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Log.d("Got string:", s);
        }
    }

    private class PutStringToMySQLServer extends PutDataToMySQLTask {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                tw.setText("Success!");
            else
                tw.setText("Fail...");


        }
    }
}
