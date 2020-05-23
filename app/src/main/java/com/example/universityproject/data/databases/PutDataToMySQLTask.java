package com.example.universityproject.data.databases;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class PutDataToMySQLTask extends AsyncTask<String, String, Boolean> {
    private String SERVLET_URL = "http://192.168.1.33:8088/DJWA_war/t";
    private String CLASS_TAG = "PutDataToMySQLTask";
    private static int _num = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        ProgressDialog pd = new ProgressDialog();
//        pd = new ProgressDialog(TEST_ACTIVITY.this);
//        pd.setMessage("Please wait");
//        pd.setCancelable(false);
//        pd.show();
    }

    @Override
    protected Boolean doInBackground(String... jsonObject) {
        HttpURLConnection connection = null;
        BufferedWriter writer = null;


        try {
            URL link = new URL(SERVLET_URL + "?want=t&_num=" + _num++);
            connection = (HttpURLConnection) link.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(2);
            connection.setDoInput(true);
//            connection.setChunkedStreamingMode(0);
//            connection.setUseCaches(false);
//            connection.setDefaultUseCaches(false);

//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-type", "application/json");
//            connection.setRequestProperty("Accept", "application/json");
            connection.connect();


//            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            Log.d("doInBackground", "Success post");

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            _close_writer_and_disconnect(connection, writer);
        }

        return false;
    }


    private void _close_writer_and_disconnect(HttpURLConnection cn, BufferedWriter wr) {
        if (cn != null)
            cn.disconnect();

        if (wr != null) {
            try {
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
