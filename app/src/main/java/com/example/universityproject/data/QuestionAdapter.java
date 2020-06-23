package com.example.universityproject.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.universityproject.R;
import com.example.universityproject.data.Models.Question;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {
    private int layout;

    public QuestionAdapter(@NonNull Context context, int resource, @NonNull List<Question> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    private class ViewHolder {
        Button button;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mainViewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.button = (Button) convertView.findViewById(R.id._quiz_button);
//            viewHolder.button.setText(getItem(position));
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Click position: " + position, Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(viewHolder);
        }
        else {
            mainViewHolder = (ViewHolder) convertView.getTag();
//            mainViewHolder.button.setText(getItem(position));
        }

        return convertView;
    }
}
