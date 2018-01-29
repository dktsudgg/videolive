package com.thoingthoing.videolive.ui.auth.presenter;

import android.content.Context;

public interface regContract {

    interface View{}

    interface Presenter{

        void setView(View v);
        void getContext(Context context);
        void deathView();
        void init();

    }
}
