package com.thoingthoing.videolive.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.ui.auth.presenter.regContract;
import com.thoingthoing.videolive.ui.auth.presenter.regPresenter;
import com.thoingthoing.videolive.utils.ClearEditText;
import com.thoingthoing.videolive.utils.CustomToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity implements regContract.View {

    private String tocken, key, email;
    private CustomToast toast;
    private FirebaseDatabase firebaseDatabase;

    private regPresenter presenter;

    @BindView(R.id.reg_email_t)
    TextView email_text;
    @BindView(R.id.reg_nickname_e)
    ClearEditText nickname_edit;
    @BindView(R.id.reg_progress)
    ProgressBar progressBar;

    @OnClick(R.id.reg_submit_btn)
    void click() {

        submit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        presenter = new regPresenter();

        presenter.setView(this);
        presenter.getContext(Register.this);
        presenter.init();

        toast = new CustomToast(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();

        tocken = intent.getStringExtra("tocken");
        key = intent.getStringExtra("key");
        email = intent.getStringExtra("email");
        email_text.setText(email);

    }

    // 회원가입 하기
    private void submit() {
        final String nickname = nickname_edit.getText().toString();

        if (nickname.length() > 0) {    // 닉네임 설정을 했을 경
            progressBar.setVisibility(View.VISIBLE);

            final String fb_key = firebaseDatabase.getReference("user").push().getKey();

            firebaseDatabase.getReference("user").child(fb_key).setValue(regdata(nickname)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    toast.toast("회원가입 성공", Toast.LENGTH_SHORT);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toast.toast("서버 연동에 실패했습니다.", Toast.LENGTH_SHORT);
                    progressBar.setVisibility(View.GONE);
                }
            });


        } else {
            toast.toast("닉네임을 입력해주세요.", Toast.LENGTH_SHORT);
        }
    }

    private Map<String, Object> regdata(String nickname) {
        HashMap<String, Object> reg = new HashMap<>();
        reg.put("email", email);
        reg.put("tocken", tocken);
        reg.put("key", key);
        reg.put("nickname", nickname);
        reg.put("streaming", 0);

        return reg;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deathView();
    }
}
