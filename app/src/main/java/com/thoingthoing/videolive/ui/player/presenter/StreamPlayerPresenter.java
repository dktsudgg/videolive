package com.thoingthoing.videolive.ui.player.presenter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.videolive.adapter.Chat_Adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.chat_item;
import com.thoingthoing.videolive.ui.player.StreamPlayerListener;
import com.thoingthoing.videolive.utils.KeyboardManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamPlayerPresenter implements StreamPlayerContract.Presenter, StreamPlayerContract.playCallback {

    private StreamPlayerContract.View view;
    private Context context;
    private String url, uid;
    private FirebaseDatabase firebaseDatabase;
    private Chat_Adapter adapter;
    private List<chat_item> item;
    private RecyclerView recyclerView;
    private chat_item chatmodel;
    private EditText message;
    private KeyboardManager manager;

    private long connectTime;

    private static final DefaultBandwidthMeter def = new DefaultBandwidthMeter();
    private SimpleExoPlayer player;
    private StreamPlayerListener componentListener;
    private long playback;
    private int currentwindow;
    private boolean ready = true;
    private SimpleExoPlayerView playerView;

    @Override
    public void view(StreamPlayerContract.View v) {
        this.view = v;
    }

    @Override
    public void deathView() {
        view = null;
        disconnectStream();
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void setExoplayer(SimpleExoPlayerView playerView) {
        this.playerView = playerView;
    }


    @Override
    public void setRecyclerView(RecyclerView recyclerView1) {
        this.recyclerView = recyclerView1;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getFirebaseData(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void keyboardHide(KeyboardManager keyboardManager) {
        this.manager = keyboardManager;
        manager = new KeyboardManager(context);
    }

    @Override
    public void setChatAdapter(Chat_Adapter adapter1, List<chat_item> item1) {
        this.adapter = adapter1;
        this.item = item1;

    }

    @Override
    public void setTitle(final TextView tv, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tv.setText(title);
            }
        }).run();
    }

    @Override
    public void chatMessage(EditText et) {
        this.message = et;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public void setUri(String uri) {
        this.url = uri;
    }

    // 스트리밍 서버 연결
    @Override
    public void connectStream(String url) {

        componentListener = new StreamPlayerListener(this, this);

        if (player == null) {
            TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(def);

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(factory), new DefaultLoadControl());
            player.addListener(componentListener);
            player.setVideoDebugListener(componentListener);
            player.setAudioDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(ready);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse("StreamingServerUrl"));

        player.prepare(mediaSource, true, false);

    }

    @Override
    public void connectData() {

        // 영상이 시작될 경우
        chatmodel = new chat_item();

        // 채팅서버 연결
        nowTime();
        getFirebaseRealTimeMessage(uid);

        // 유저 리스트 추가
        if (Data.uid != null) {
            firebaseDatabase.getReference("stream").child(uid).child("userlist").push().setValue(userlist());
        }
    }

    // 영상 연결 해제
    private void disconnectStream() {
        if (player != null) {
            playback = player.getCurrentPosition();
            currentwindow = player.getCurrentWindowIndex();
            ready = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.setVideoListener(null);
            player.setVideoListener(null);
            player.setVideoDebugListener(null);
            player.setAudioDebugListener(null);
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory factory = new DefaultHttpDataSourceFactory("ua", def);
        DashChunkSource.Factory dfactory = new DefaultDashChunkSource.Factory(factory);
        return new DashMediaSource(uri, factory, dfactory, null, null);
    }


    @Override
    public void visibleChatList(boolean isHide) {
        if (isHide) {
            view.chatListbtn(true);
        } else {
            view.chatListbtn(false);
        }
    }

    // 채팅 전송
    @Override
    public void sendChatMessage(String msg) {
        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = sdf.format(date);

        firebaseDatabase.getReference("chat").child(uid).push().setValue(chatMessage(msg, time, Data.nickname)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearChatMessage();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);   // 채팅 마지막 포지션으로 이동

            }
        });

    }

    // 채팅 정보 불러오기
    private void getFirebaseRealTimeMessage(String uid) {
        firebaseDatabase.getReference("chat").child(uid).addChildEventListener(new ChildEventListener() {
            // firebase에서 업로드되는 정보들을 받아와서 RecylerView 에 추가
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // 방에 중간에 접속 했을 경우 이전 채팅내용을 리스트에 추가하지 않기 위해
                // 현재 시간과 이전시간을 비교하여 리스트에 추가
                String time = dataSnapshot.getValue(chat_item.class).getChat_time();

                try {
                    if (transDate(time)) {
                        adapter.addItem(dataSnapshot.getValue(chat_item.class));
                    }
                } catch (ParseException e) {

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // 시간 비교
    private boolean transDate(String time) throws ParseException {
        // 메시지 시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date reqdate = sdf.parse(time);
        long reqTime = reqdate.getTime();

        if (reqTime < connectTime) {
            return false;
        } else {
            return true;
        }
    }

    // 현재시간 구하기
    private long nowTime() {
        long now = System.currentTimeMillis();

        Date nowdate = new Date(now);
        connectTime = nowdate.getTime();

        return connectTime;
    }

    // 채팅 전송할시 에디트 텍스트초기화
    private void clearChatMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                message.setText("");
                manager.hide(message); // 키보드 없애기
            }
        }).run();
    }

    private Map<String, Object> userlist() {
        HashMap<String, Object> list = new HashMap<>();
        list.put("uid", Data.uid);
        list.put("nickname", Data.nickname);
        list.put("level", 0);

        return list;
    }

    // 채팅 메시지
    private Map<String, String> chatMessage(String msg, String time, String nick) {
        HashMap<String, String> hash = new HashMap<>();

        hash.put("chat_time", time);
        hash.put("chat_nickname", nick);
        hash.put("chat_message", msg);

        return hash;
    }


    @Override
    public void successCallback(boolean isRun) {
        if (isRun) {
            view.setProgress(true);
        } else {
            view.setProgress(false);
        }
    }
}
