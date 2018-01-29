package com.thoingthoing.videolive.ui.player;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.Chat_Adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.chat_item;
import com.thoingthoing.videolive.ui.auth.AuthDialog;
import com.thoingthoing.videolive.ui.player.presenter.StreamPlayerContract;
import com.thoingthoing.videolive.ui.player.presenter.StreamPlayerPresenter;
import com.thoingthoing.videolive.utils.KeyboardManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamPlayer extends AppCompatActivity implements StreamPlayerContract.View {

    private FirebaseDatabase firebaseDatabase;

    private List<chat_item> item;
    private Chat_Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private KeyboardManager keyboardManager;
    private boolean isHide = false, fullScreen = false;

    private StreamPlayerPresenter presenter;

    @BindView(R.id.live_player_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.live_player_editText)
    EditText chat_message;
    @BindView(R.id.live_player_chatlist)
    LinearLayout chatlist;
    @BindView(R.id.live_player_title)
    TextView title;
    @BindView(R.id.live_player_hide_btn)
    Button hide_btn;
    @BindView(R.id.live_player_progress)
    ProgressBar progressBar;
    @BindView(R.id.live_player_exoplayer)
    SimpleExoPlayerView playerView;
    @BindView(R.id.live_player_frame)
    FrameLayout frame;


    @OnClick(R.id.live_player_submit_chatting)
    void click() {
        // 로그인된 사용자만 채팅을 칠 수 있도록 체크
        if (Data.uid != null) {
            String message = chat_message.getText().toString();

            if (message.length() > 0) {
                presenter.sendChatMessage(message);
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), AuthDialog.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.live_player_hide_btn)
    void hidebtn() {
        if (isHide) {
            presenter.visibleChatList(isHide);
            isHide = false;
        } else {
            presenter.visibleChatList(isHide);
            isHide = true;
        }
    }

    // 회전시
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                break;
            case Configuration.ORIENTATION_LANDSCAPE:

                break;
        }
    }

    // 전체보기 버튼
    @OnClick(R.id.live_player_fullscreen)
    void fullscreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    fullScreen = false;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    fullScreen = true;
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        init();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);           // 상태바 툴바 제거
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);    // 화면꺼짐 방지
        setContentView(R.layout.activity_streamplayer);
        ButterKnife.bind(this);

        //recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void init() {
        Intent intent = getIntent();

        presenter = new StreamPlayerPresenter();

        String uid = intent.getStringExtra("uid");
        presenter.setUid(uid);

        // Presenter
        presenter.view(this);
        presenter.getContext(StreamPlayer.this);

        String url = intent.getStringExtra("url");
        presenter.setUri(url);

        String title_s = intent.getStringExtra("title");
        presenter.setTitle(title, title_s);

        presenter.chatMessage(chat_message);
        presenter.keyboardHide(keyboardManager);

        // 채팅서버
        firebaseDatabase = FirebaseDatabase.getInstance();
        presenter.getFirebaseData(firebaseDatabase);

        item = new ArrayList<>();
        adapter = new Chat_Adapter(item);

        presenter.setChatAdapter(adapter, item);
        presenter.setRecyclerView(recyclerView);

        presenter.setExoplayer(playerView);
        presenter.connectStream(url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.deathView();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deathView();
    }

    @Override
    public void setProgress(final boolean isRun) {
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

    @Override
    public void chatListbtn(final boolean isHide) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isHide) {

                    chatlist.setVisibility(View.GONE);
                } else {

                    chatlist.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
