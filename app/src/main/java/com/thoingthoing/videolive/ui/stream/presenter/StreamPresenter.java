package com.thoingthoing.videolive.ui.stream.presenter;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.rtsp.Protocol;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.Chat_Adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.chat_item;
import com.thoingthoing.videolive.model.stream_config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class StreamPresenter implements StreamContract.Presenter {

    private StreamContract.View view;
    private Context context;
    private NotificationManager notificationManager;
    private FirebaseDatabase firebaseDatabase;
    private Chat_Adapter adapter;
    private RecyclerView recy;
    private RtspCamera1 rtspCamera1;

    @Override
    public void setView(StreamContract.View view) {
        this.view = view;
    }


    @Override
    public void deathView() {
        view = null;
        stream_config.title = null;

    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    // get FirebaseData
    @Override
    public void firebaseDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }


    // 방송 시작 고정 알림
    @Override
    public void notificationManager(NotificationManager notificationManager) {
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        this.notificationManager = notificationManager;
    }

    // RecyclerView
    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        this.recy = recyclerView;
        recy.setAdapter(adapter);
    }

    // Chat Adapter
    @Override
    public void chatAdapter(Chat_Adapter adapter) {
        this.adapter = adapter;

    }


    // Rtsp 카메라
    @Override
    public void RtspCamera(RtspCamera1 rtspCamera1) {
        this.rtspCamera1 = rtspCamera1;
    }

    @Override
    public void rtspPreview(boolean isPreview) {
        if (isPreview) {
            rtspCamera1.startPreview();
        } else {
            rtspCamera1.stopPreview();
        }
    }

    // 카메라 앞뒤 반전
    @Override
    public void switchCamera() {
        try {
            rtspCamera1.switchCamera();
        } catch (CameraOpenException e) {

        }
    }

    // 스트리밍 서버에 연결
    @Override
    public void connectRtsp(String url) {
        view.setProgress(true);

        rtspCamera1.setAuthorization(stream_config.user, stream_config.password);
        rtspCamera1.setVideoBitrateOnFly(stream_config.bitrate);
        rtspCamera1.setProtocol(Protocol.UDP);
        rtspCamera1.prepareAudio(128 * 1024, stream_config.samplerate, true, false, false);
        rtspCamera1.prepareVideo(stream_config.width, stream_config.height, stream_config.fps, stream_config.bitrate, false, 0);
        rtspCamera1.getResolutionsBack().get(8);

        rtspCamera1.enableVideo();
        rtspCamera1.enableAudio();
        rtspCamera1.startStream(url);

    }
    // 연결에 성공했을경우 db업데이트 및 채팅서버 연결
    @Override
    public void startStream(int record, String uid, String title) {
        // record Btn update
        view.updateImage(record);

        streamChatting(record, title);
        holdNotification(record);
        view.setRecyclerView(true);
        getFirebaseChatting(uid);
    }

    // 스트리밍 종료
    @Override
    public void stopStream(int record, String uid, String title) {
        try {
            rtspCamera1.disableVideo();
            rtspCamera1.disableAudio();
            rtspCamera1.stopStream();

            view.updateImage(record);

            holdNotification(record);
            view.setRecyclerView(false);
          //  streamChatting(record, stream_config.title);
        } catch (Exception e) {

        }
    }

    // 서버 연결에 실패했을시 메시지 출력
    @Override
    public void failMessage(String msg) {
        view.setProgress(false);
    }

    @Override
    public void backPressedDialog() {

        if (rtspCamera1.isStreaming()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.stream_pressed_title_dialog)
                            .setPositiveButton(R.string.stream_enable_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
            }).run();
        }

    }


    // 방송 시작시 실시간 채팅방 생성
    @Override
    public void streamChatting(int record, String titleMessage) {

        if (record != 0) {

            // 방송이 끝날시 채팅정보 및 스트리밍 정보 삭제
            firebaseDatabase.getReference().child("stream").child(Data.uid).setValue(null);
            firebaseDatabase.getReference().child("chat").child(Data.uid).setValue(null);

            // 유저 정보에서 데이터 변경
            Map<String, Object> update = new HashMap<>();
            update.put("streaming", 0);
            firebaseDatabase.getReference("user").child(Data.uid).updateChildren(update);

            view.setProgress(false);

        } else {


            final chat_item chatModel = new chat_item();

            String url = context.getString(R.string.stream_url) + Data.uid + "/manifast.mpd";
            String thumbnail = context.getString(R.string.stream_thumbnail) + Data.uid + "&format=png&size=118x118";

            firebaseDatabase.getReference().child("stream").child(Data.uid).
                    setValue(map(titleMessage, Data.nickname, url, startStreamTime(), Data.uid, 1, thumbnail)).
                    addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {

                            // 유저정보에서 방송중인지 업데이트
                            Map<String, Object> update = new HashMap<>();
                            update.put("streaming", 1);
                            firebaseDatabase.getReference("user").child(Data.uid).updateChildren(update);

                            chatModel.chat_message = context.getString(R.string.system_message);
                            chatModel.chat_time = startStreamTime();
                            chatModel.chat_nickname = context.getString(R.string.sys);

                            // 방송 시작 메시지
                            firebaseDatabase.getReference("stream").child(Data.uid).child("userlist").push().setValue(userlist());
                            firebaseDatabase.getReference("chat").child(Data.uid).push().setValue(chatModel);
                            view.setProgress(false);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.setProgress(false);
                    e.printStackTrace();
                }
            });
        }

    }

    // Stream Info Data update
    private Map<String, Object> map(String title, String nickname, String url, String time, String uid, int onoff, String thumbnail) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("title", title);
        hashMap.put("nickname", nickname);
        hashMap.put("url", url);
        hashMap.put("startTime", time);
        hashMap.put("uid", uid);
        hashMap.put("onoff", onoff);
        hashMap.put("thumbnail", thumbnail);

        return hashMap;
    }

    // get Stream Start Time
    private String startStreamTime() {

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        return time;
    }

    private Map<String, Object> userlist() {
        HashMap<String, Object> list = new HashMap<>();

        list.put("uid", Data.uid);
        list.put("nickname", Data.nickname);
        list.put("level", 2);

        return list;
    }

    // 실시간 채팅 업데이트
    @Override
    public void getFirebaseChatting(String uid) {
        firebaseDatabase.getReference().child("chat").child(uid).addChildEventListener(new ChildEventListener() {

            // firebase에서 업로드되는 정보들을 받아와서 RecylerView 에 추가
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.addItem(dataSnapshot.getValue(chat_item.class));
                recy.scrollToPosition(adapter.getItemCount() - 1);
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

    // 상단바 노티피케이션 고정
    @Override
    public void holdNotification(int record) {
        if (record != 0) {
            notificationManager.cancelAll();

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("방송중")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher);

            notificationManager.notify(0, builder.build());
        }

    }


}
