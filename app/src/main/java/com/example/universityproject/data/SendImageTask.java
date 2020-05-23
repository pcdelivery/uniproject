package com.example.universityproject.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendImageTask extends AsyncTask<Bitmap, Void, Boolean> {
    private String SERVLET_URL = "http://192.168.1.33:8090/DJWA_war/MyServlet";
    private String CLASS_TAG = "SendImageTask";

    @Override
    protected Boolean doInBackground(Bitmap... bitmaps) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(SERVLET_URL + "?want=sendpics");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.connect();

            OutputStream stream = new BufferedOutputStream(connection.getOutputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            baos.flush();
            stream.write(byteArray);
            stream.flush();
            baos.close();
            stream.close();

//            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);



//            connection.setRequestProperty("pic", encodedImage);
//            stream.write(byteArray);

//            OutputStream stream = connection.getOutputStream();
//            writer = new BufferedWriter(new OutputStreamWriter(stream));

//            DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
//            stream.writeUTF("HERE WE ARE");
//            stream.flush();
//            stream.close();

//            Log.d(CLASS_TAG, "Encoded image was send successfully: " + encodedImage);

            return true;
        } catch(IOException e) {
            Log.e(CLASS_TAG, "Error while sending image: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
