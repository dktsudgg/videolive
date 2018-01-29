package com.thoingthoing.videolive.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.ui.auth.AuthDialog;
import com.thoingthoing.videolive.ui.main.presenter.mainContract;
import com.thoingthoing.videolive.ui.main.presenter.mainPresenter;
import com.thoingthoing.videolive.ui.stream.Stream;
import com.thoingthoing.videolive.ui.streamerinfo.StreamerInfo;
import com.thoingthoing.videolive.utils.DisableViewPager;
import com.thoingthoing.videolive.utils.Tablayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements mainContract.View {

    private Intent intent;
    private Tablayout adapter;
    private mainPresenter presenter;

    @BindView(R.id.main_tablayout)
    TabLayout tab;
    @BindView(R.id.main_viewpager)
    ViewPager viewpager;

    @OnClick({R.id.livestream, R.id.main_mypage})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.livestream:   // 방송하기

                if (Data.uid != null) {
                    intent = new Intent(getApplicationContext(), Stream.class);
                    startActivity(intent);
                } else {
                    checkUser();
                }
                break;

            case R.id.main_mypage:
                if (Data.uid != null) {
                    intent = new Intent(this, StreamerInfo.class);
                    intent.putExtra("key", Data.uid);
                    startActivity(intent);

                } else {
                    checkUser();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        presenter = new mainPresenter();
        presenter.setView(this);
        presenter.getContext(MainActivity.this);

        // 권한 체크
        presenter.getPermission();

        DisableViewPager dvp = (DisableViewPager) viewpager;     // ViewPager Disable Swipe
        dvp.setPagingEnabled(false);

        layout();
    }

    private void layout() {    // 바텀 레이아웃
        tab.addTab(tab.newTab().setText("방송보기"));
        tab.addTab(tab.newTab().setText("랭킹"));
        tab.addTab(tab.newTab().setText("즐겨찾기"));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new Tablayout(getSupportFragmentManager(), tab.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3); // 페이지 수  ( 추가안하면 스크롤할때 끈김)

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 2) {

                    if (Data.uid == null) {
                        checkUser();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void checkUser() {
        Intent intent = new Intent(getApplicationContext(), AuthDialog.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
