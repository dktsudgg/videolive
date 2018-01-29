package com.thoingthoing.videolive.ui.stream;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.Chat_Adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.chat_item;
import com.thoingthoing.videolive.model.stream_config;
import com.thoingthoing.videolive.ui.stream.presenter.StreamContract;
import com.thoingthoing.videolive.ui.stream.presenter.StreamPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Stream extends AppCompatActivity implements StreamContract.View, StreamContract.streamCallback {

    private CameraPreview preview;
    private StreamPresenter presenter;
    private int RECORD = 0;
    private NotificationManager notificationManager;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView.LayoutManager layoutManager;
    private Chat_Adapter adapter;
    private List<chat_item> item;

    @Nullable
    @BindView(R.id.t_surfaceview)   // surfaceview
            FrameLayout cameralayout;
    @BindView(R.id.stream_progress) // progress bar
            ProgressBar progressBar;
    @BindView(R.id.stream_title)    // 방송 제목
            TextView title;
    @BindView(R.id.stream_recycler_view)    // 채팅창
            RecyclerView recyclerView;
    @BindView(R.id.flash)   // 플레시 버튼
            ImageButton flash_btn;
    @BindView(R.id.switch_camera)   // 카메라 전환 버튼
            ImageButton switch_camera_btn;
    @BindView(R.id.stream_start)    // 방송 시작 버튼
            ImageButton start_btn;
    @BindView(R.id.stream_quality)   // 방송 품질 버튼
            ImageButton quality_btn;

    // 비디오 퀄리티 레이아웃
    @BindView(R.id.quality_layout)
    FrameLayout quality_layout;
    @BindView(R.id.radio)
    RadioGroup radioGroup;
    Camera camera;

    @OnClick({R.id.stream_start, R.id.switch_camera, R.id.flash, R.id.stream_quality, R.id.title_edit})
    void click(View v) {
        switch (v.getId()) {
            case R.id.stream_start: // 방송 시작
                startOrstopStream();
                break;

            case R.id.switch_camera:    // 카메라 앞뒤 전환
                presenter.switchCamera();
                break;

            case R.id.flash:
                break;

            case R.id.stream_quality:

                break;

            case R.id.title_edit:
                setTitleDialog();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);           // 상태바 툴바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);    // 화면꺼짐 방지
        setContentView(R.layout.activity_stream);
        ButterKnife.bind(this);
        initView();
        camera = Camera.open();
    }


    // 기본 세팅
    private void initView() {
        // RecyclerView setting
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        item = new ArrayList<>();
        adapter = new Chat_Adapter(item);

        defaultdata();

        preview = new CameraPreview(this, presenter, this);
        cameralayout.addView(preview);


    }


    private void defaultdata() {

        stream_config.title = Data.nickname + R.string.stream_title;

        firebaseDatabase = FirebaseDatabase.getInstance();

        presenter = new StreamPresenter();
        presenter.setView(this);
        presenter.getContext(Stream.this);
        presenter.notificationManager(notificationManager);
        presenter.chatAdapter(adapter);
        presenter.initRecyclerView(recyclerView);
        presenter.firebaseDatabase(firebaseDatabase);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deathView();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  presenter.backPressedDialog();

    }

    // 방송 시작할 시 스트리밍 온 & 이미지 아이콘 업데이트
    private void startOrstopStream() {
        if (RECORD != 0) {
            presenter.stopStream(RECORD, Data.uid, stream_config.title);
            RECORD--;
        } else {
            presenter.connectRtsp(stream_config.url + Data.uid);
        }
    }

    // 서버에 연결됬을 시 콜백을 받아서 UI갱신 및 채팅서버 연결
    @Override
    public void successCallback() {
        presenter.startStream(RECORD, Data.uid, stream_config.title);
        RECORD++;
    }

    @Override
    public void failCallback() {
        String message = null;
        presenter.failMessage(message);
    }

    @Override
    public void updateImage(final int record) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (record != 0) {
                    start_btn.setBackgroundResource(R.drawable.record_off);

                } else {
                    start_btn.setBackgroundResource(R.drawable.record_on);

                }
            }
        });
    }

    @Override
    public void setTitleDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final EditText et = new EditText(Stream.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(Stream.this);
                builder.setTitle(R.string.stream_title_dialog)
                        .setView(et)
                        .setPositiveButton(R.string.stream_enable_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTextView(et.getText().toString());
                                stream_config.title = et.getText().toString();
                            }

                        })
                        .setNegativeButton(R.string.stream_disable_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

    }

    @Override
    public void setTextView(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setText(msg);
            }
        });
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
    public void setRecyclerView(final boolean isRecord) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRecord) {
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }


}
