package com.example.universityproject.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Uri images[];

    public ImageAdapter(Context context, int size) {
        this.context = context;
        images = new Uri[size];

        String dir = "/data/data/com.example.universityproject/files/";

        for (int i = 0; ; i++) {
            File fileToConnect = new File(dir, String.format("pic%d.png", i+1));

            if(!fileToConnect.canRead())
                break;

            images[i] = Uri.fromFile(fileToConnect);
        }
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageURI(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));

        return imageView;
    }
}
