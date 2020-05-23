package com.example.universityproject.data.databases;

import android.os.AsyncTask;
import android.util.Log;

import com.example.universityproject.data.SendImageTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Just doInBackground executed
 */
public abstract class ReceiveDataFromMySQLTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... servletCommands) {
        String SERVLET_URL = "http://192.168.1.33:8088/DJWA_war/t";
        String CLASS_TAG = "ReceiveDataFromMySQL";

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder url = new StringBuilder(SERVLET_URL + "?want=" + servletCommands[0]);

        try {
            for (int i = 1; i < servletCommands.length; i++)
                url.append("&").append(servletCommands[i]);

            URL link = new URL(url.toString());
            connection = (HttpURLConnection) link.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder result = new StringBuilder();
            String readerLine;

            while((readerLine = reader.readLine()) != null) {
                result.append(readerLine);
                Log.d(CLASS_TAG, "Line read from buffer: " + readerLine);
            }

            return result.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            _close_reader_and_disconnect(connection, reader);
        }

        return null;
    }

    private void _close_reader_and_disconnect(HttpURLConnection cn, BufferedReader rd) {
        if (cn != null)
            cn.disconnect();

        if (rd != null) {
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
