package com.thoingthoing.videolive.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardManager {

    private InputMethodManager inputMethodManager;
    private Context context;
    private View v;

    public KeyboardManager(Context context) {
        this.context = context;

    }

    public void hide(View v) {

        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
