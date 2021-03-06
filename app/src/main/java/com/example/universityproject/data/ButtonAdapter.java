package com.example.universityproject.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.universityproject.R;

import java.util.List;
import java.util.jar.Attributes;

public class ButtonAdapter extends ArrayAdapter<String> {
    private int layout;
    private int lastClickedPosition;
    private Context mContext;
    private Button lastClickedView;

    public ButtonAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        lastClickedView = null;
        layout = resource;
        mContext = context;
    }

    private static class ViewHolder {
        Button button;
    }

    public int popLastClickedPosition() {
        int res = lastClickedPosition;
        lastClickedPosition = -1;

        return res;
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
            viewHolder.button.setText(getItem(position));
            viewHolder.button.setTextColor(mContext.getResources().getColor(R.color.grey_neutral));
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastClickedPosition = position;

                    if (lastClickedView != null)
                        lastClickedView.setTextColor(mContext.getResources().getColor(R.color.grey_neutral));

                    lastClickedView = (Button) v;
                    lastClickedView.setTextColor(mContext.getResources().getColor(R.color.green_not_alert));

                    // todo animation
//                    Toast.makeText(getContext(), "Click position: " + position, Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(viewHolder);
        }
        else {
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.button.setText(getItem(position));
        }

        return convertView;
    }
}
