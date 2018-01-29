package com.thoingthoing.videolive.ui.auth.presenter;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;

public interface authContract {

    interface View{

        // 액티비티 종료 종료
        void appfinish();

        // 회원가입 액티비티 실행
        void intentReg(final String key, final String tocken, final String email);

        void toastMessage(String message);
        void progress(boolean isRun);
    }

    interface Presenter{
        // 기본 설정
        void setView(View v);
        void deathView();
        void getContext(Context context);
        void init();

        // 구글 로그인
        void setLoginBtn(SignInButton sign);
        void firebaseAuthWithGoogle(GoogleSignInAccount acct);
        void setGooglePlusButtonText(SignInButton signInButton, final String buttonText);

    }
}
