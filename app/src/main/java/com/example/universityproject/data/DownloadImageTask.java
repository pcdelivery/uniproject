package com.example.universityproject.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView imageView) {
        this.bmImage = imageView;
    }

    protected Bitmap doInBackground(String ... urls) {
        String urlToDisplay = urls[0];
        Bitmap image = null;

        try {
            InputStream in = new URL(urlToDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch(Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return image;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
