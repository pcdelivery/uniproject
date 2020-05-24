package com.example.universityproject.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.jar.Attributes;

public class ButtonAdapter extends ArrayAdapter<Button> {
    private Button buttons[];
    private int size;

    public ButtonAdapter(@NonNull Context context, int resource, int size) {
        super(context, resource);
        this.size += size;
        buttons = new Button[size];

        for (int i = 0; i < size; i++) {
            buttons[i] = new Button(context);
        }
    }

}
