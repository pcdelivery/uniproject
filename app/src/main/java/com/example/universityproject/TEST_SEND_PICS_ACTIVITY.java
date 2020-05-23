package com.example.universityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.universityproject.data.SendImageTask;
import com.example.universityproject.data.databases.PutDataToMySQLTask;
import com.example.universityproject.data.databases.ReceiveDataFromMySQLTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TEST_SEND_PICS_ACTIVITY extends AppCompatActivity {
    private String SERVLET_URL = "http://localhost:8088/DJWA_war/MyServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_test_send_pics);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading. Please wait");
        pd.show();

        Log.d("TEST", "1");

        //converting image to base64 string
//        Bitmap pictureToSend = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        pictureToSend.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
//        byte[] byteImage = byteStream.toByteArray();
//        final String stringImage = Base64.encodeToString(byteImage, Base64.DEFAULT);

        Log.d("TEST", "2");

        class r extends PutDataToMySQLTask {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_LONG).show();
            }
        }

        new r().execute("sdfasdfsdfasdfsdfasdfsdfasdfsdfasdfsdfasdfsdfasdfsdfasdfsdfasdfsdfasdf");


        //sending image to server
//        StringRequest request = new StringRequest(Request.Method.POST, SERVLET_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                pd.dismiss();
//
//                if (response.equals("true"))
//                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Some error occurred: " + error, Toast.LENGTH_LONG).show();
//            }
//        })
//
//        {
//            //adding parameters to send
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("image", stringImage);
//                return parameters;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
        }

    /*
    private class SendOneImage extends SendImageTask {
        @Override
        protected Boolean doInBackground(Bitmap... bitmaps) {
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVLET_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(TEST_SEND_PICS_ACTIVITY.this, response, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TEST_SEND_PICS_ACTIVITY.this, "Fail", Toast.LENGTH_LONG).show();
                }
            });

            rq.add(stringRequest);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean)
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Fail...", Toast.LENGTH_LONG).show();
        }
    }
    */
}
