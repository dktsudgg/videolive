package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;

public interface mainContract {

    interface View{

    }

    interface Presenter{

        void setView(View v);
        void deathView();
        void getContext(Context context);

        // 권한 설정
        void getPermission();
    }
}
