package com.thoingthoing.videolive.ui.auth.presenter;

import android.content.Context;

import com.thoingthoing.videolive.utils.CustomToast;

public class regPresenter implements regContract.Presenter {

    private regContract.View v;
    private Context context;
    private CustomToast toast;

    @Override
    public void setView(regContract.View v) {
        this.v = v;
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void deathView() {
        v = null;
    }

    @Override
    public void init() {
        toast = new CustomToast(context);

    }
}
