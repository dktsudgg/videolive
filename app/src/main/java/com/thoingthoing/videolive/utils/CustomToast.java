package com.thoingthoing.videolive.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thoingthoing.video_live.R;


public class CustomToast extends Toast {

   private Context context;

    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    public void toast(String message, int duration) {
        LayoutInflater inflater;
        View v;

        if (false) {
            Activity act = (Activity) context;
            inflater = act.getLayoutInflater();
            v = inflater.inflate(R.layout.custom_toast, null);
        } else {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_toast, null);
        }
        TextView text = (TextView) v.findViewById(R.id.toast_message);

        text.setText(message);
        show(this, v, duration);
    }

    private void show(Toast toast, View v, int duration) {
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.BOTTOM,
                0, 0);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}

