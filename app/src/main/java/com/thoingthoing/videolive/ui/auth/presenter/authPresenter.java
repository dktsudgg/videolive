package com.thoingthoing.videolive.ui.auth.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.UserData;

public class authPresenter extends FragmentActivity implements authContract.Presenter {

    private authContract.View v;
    private Context context;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;

    @Override
    public void setView(authContract.View v) {
        this.v = v;
    }

    @Override
    public void deathView() {
        v = null;
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void init() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void setLoginBtn(SignInButton sign) {
        this.signInButton = sign;
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {  // 정보 출력시
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String email = firebaseUser.getEmail();
                            final String key = firebaseUser.getUid();
                            final String tocken = FirebaseInstanceId.getInstance().getToken();

                            FirebaseDatabase.getInstance().getReference("user").orderByChild("key").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue() != null) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            if (snapshot.getValue(UserData.class).getKey().equals(key)) {

                                                v.progress(false);

                                                Data.uid = snapshot.getKey();
                                                Data.email = email;
                                                Data.nickname = snapshot.getValue(UserData.class).getNickname();
                                                Data.fb_key = key;
                                                Data.tocken = tocken;

                                                final String msg = Data.nickname + "님 환영합니다.";
                                                v.toastMessage(msg);
                                                v.appfinish();
                                                break;
                                            }
                                        }
                                    } else {
                                        reg_dialog(key, tocken, email);
                                    }
                                    v.progress(false);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    v.progress(false);
                                }
                            });
                        } else {
                            v.toastMessage("실패");
                            v.appfinish();
                        }
                    }
                });
    }


    @Override
    public void setGooglePlusButtonText(SignInButton signInButton, final String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            final View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView) v;
                        tv.setText(buttonText);
                    }
                }).run();

                return;
            }
        }
    }

    private void reg_dialog(final String key, final String tocken, final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.reg_title_text);
                builder.setMessage(R.string.dialog_message);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        v.intentReg(key, tocken, email);

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }).run();
    }

}
