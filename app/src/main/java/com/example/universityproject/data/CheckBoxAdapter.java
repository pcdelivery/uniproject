package com.example.universityproject.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.universityproject.data.databases.DatabaseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class CheckBoxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> checkBoxesStringList;

    private int size;
    private CheckBox[] checkBoxesList;

    public CheckBoxAdapter(Context context, String databaseResponse) {
        this.context = context;

        try {
            checkBoxesStringList = JSONUnwrapper.getSpecs(new JSONObject(databaseResponse));
            size = checkBoxesStringList.size();

            checkBoxesList = new CheckBox[size];
            for (int i = 0; i < size; i++) {
                checkBoxesList[i] = new CheckBox(context);
                checkBoxesList[i].setText(checkBoxesStringList.get(i));
            }
        } catch (JSONException e) {
            Log.d("CheckBoxAdapter", "Error while JSON unwrapping: list is null");
            checkBoxesStringList = null;
            size = 0;
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return checkBoxesList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return checkBoxesList[position];
    }

    public ArrayList<String> getChecked() {
        ArrayList<String> result = new ArrayList<String>();

        for (CheckBox cb: checkBoxesList) {
            if (cb.isChecked())
                result.add(cb.getText().toString());
        }

        return result;
    }
}
