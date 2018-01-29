package com.thoingthoing.videolive.ui.streamerinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.ui.auth.AuthDialog;
import com.thoingthoing.videolive.ui.streamerinfo.presenter.InfoContract;
import com.thoingthoing.videolive.ui.streamerinfo.presenter.InfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class StreamerInfo extends AppCompatActivity implements InfoContract.View {

    private InfoPresenter presenter;

    @BindView(R.id.info_bookmark_btn)
    ImageButton bookmark_btn;
    @BindView(R.id.info_title)
    TextView title;
    @BindView(R.id.info_profile_title)
    CircleImageView profile_title;
    @BindView(R.id.info_nickname)
    TextView nickname;
    @BindView(R.id.info_boomark_count)
    TextView bookmark_count;
    @BindView(R.id.info_ranking)
    TextView ranking;
    @BindView(R.id.info_notice)
    TextView notice;
    @BindView(R.id.info_edit_notice_btn)
    ImageButton edit_notice_btn;
    @BindView(R.id.info_progress)
    ProgressBar progressBar;
    @BindView(R.id.info_vod_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.info_back_thumbnail)
    ImageView back_thumbnail;
    @BindView(R.id.info_profile_setting)
    ImageButton profile_setting;
    @BindView(R.id.info_back_thumbnail_setting)
    ImageButton back_thumbnail_setting;

    @OnClick(R.id.info_edit_notice_btn)
    void notice() {
        presenter.changeNotice();
    }

    @OnClick(R.id.info_bookmark_btn)
    void click() {
        if (checkauth()) {
            presenter.bookmark();
        } else {
            Intent intent = new Intent(this, AuthDialog.class);
            startActivity(intent);
        }

    }

    @OnClick({R.id.info_profile_setting, R.id.info_back_thumbnail_setting})
    void setting_btn(View v) {
        switch (v.getId()) {
            case R.id.info_profile_setting:

                presenter.changeImage(0, getSupportFragmentManager());
                break;
            case R.id.info_back_thumbnail_setting:
                presenter.changeImage(1, getSupportFragmentManager());
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamer_info);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        int rank = intent.getIntExtra("ranking", 0);
        setRanking("랭킹 " + String.valueOf(rank) + "등");
        presenter = new InfoPresenter();
        presenter.setView(this);
        presenter.getContext(StreamerInfo.this);

        presenter.firebase(key);
        presenter.setVodRecyclerview(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deathView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // 로그인 체크
    private boolean checkauth() {
        if (Data.uid != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTitle(final String title_msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setText(title_msg);
            }
        });
    }

    @Override
    public void setBookmarkCount(final String count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bookmark_count.setText(count);
            }
        });
    }

    @Override
    public void setNickname(final String nick) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nickname.setText(nick);
            }
        });
    }

    @Override
    public void setRanking(final String rank) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ranking.setText(rank);
            }
        });
    }

    @Override
    public void setNotice(final String notice_msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notice.setText(notice_msg);
            }
        });
    }

    @Override
    public void setbookMarkbtnChange(final boolean isChange) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isChange) {
                    bookmark_btn.setBackgroundResource(R.drawable.bookmark_on);
                } else {
                    bookmark_btn.setBackgroundResource(R.drawable.bookmark_off);
                }
            }
        });

    }

    @Override
    public void visibleNoticeBtn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edit_notice_btn.setVisibility(View.VISIBLE);
                profile_setting.setVisibility(View.VISIBLE);
                back_thumbnail_setting.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void noticeDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final EditText et = new EditText(StreamerInfo.this);

                et.setText(message);
                AlertDialog.Builder builder = new AlertDialog.Builder(StreamerInfo.this);
                builder.setTitle(R.string.info_notice_title)
                        .setView(et)
                        .setPositiveButton(R.string.stream_enable_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                presenter.sendNotice(et.getText().toString());
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
    public void setback_thumbnail(final String image_url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                        .load(image_url)
                        .thumbnail(0.3f)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(back_thumbnail);
            }
        });
    }

    @Override
    public void back_thumbnail(final Uri uri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(back_thumbnail);
            }
        });
    }


    @Override
    public void profile_image(final Uri uri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                .load(uri)
                .into(profile_title);
            }
        });
    }

    @Override
    public void setprofiel_image(final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                        .load(url)
                        .into(profile_title);
            }
        });
    }
}
