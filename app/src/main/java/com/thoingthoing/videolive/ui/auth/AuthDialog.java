package com.thoingthoing.videolive.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.ui.auth.presenter.authContract;
import com.thoingthoing.videolive.ui.auth.presenter.authPresenter;
import com.thoingthoing.videolive.utils.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class AuthDialog extends AppCompatActivity implements authContract.View, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private authPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private CustomToast toast;

    @Nullable
    @BindView(R.id.login_signin)
    SignInButton signInButton;
    @BindView(R.id.auth_progress)
    ProgressBar progressBar;

    @Optional
    @OnClick({R.id.authdialog_register, R.id.login_signin, R.id.auth_back})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.auth_back:    // 뒤로가기
                finish();
                break;

            case R.id.authdialog_register:  // 회원가입
                Intent regist = new Intent(this, Register.class);
                startActivity(regist);
                break;

            case R.id.login_signin:
                progress(true);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authdialog);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        toast = new CustomToast(getApplicationContext());

        presenter = new authPresenter();

        presenter.setView(this);
        presenter.getContext(AuthDialog.this);
        presenter.init();
        presenter.setLoginBtn(signInButton);
        presenter.setGooglePlusButtonText(signInButton, getString(R.string.auth_tv1));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                presenter.firebaseAuthWithGoogle(account);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deathView();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String msg = "연결에 실패했습니다.";
        toastMessage(msg);
    }

    @Override
    public void appfinish() {
        this.finish();
    }

    @Override
    public void intentReg(final String key, final String tocken, final String email) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("key", key);
        intent.putExtra("tocken", tocken);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void toastMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.toast(message, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void progress(final boolean isRun) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRun) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
