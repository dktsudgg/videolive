package com.thoingthoing.videolive.ui.main.presenter;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class mainPresenter implements mainContract.Presenter {

    private mainContract.View v;
    private Context context;


    @Override
    public void setView(mainContract.View v) {
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
    public void getPermission() {
        PermissionListener camerapermission = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };

        new TedPermission(context)
                .setPermissionListener(camerapermission)
                .setDeniedMessage("권한 설정 동의를 안하신다면, 나중에 이곳에서 설정해 주세요. [설정] > [권한]")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }


}
