package com.thoingthoing.videolive.ui.stream.presenter;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.thoingthoing.videolive.adapter.Chat_Adapter;

public interface StreamContract {

    interface View {

        // 녹화 버튼 UI변경
        void updateImage(int record);

        // 제목 변경
        void setTitleDialog();
        void setTextView(String title);

        // 프로그레스 바
        void setProgress(boolean isRun);

        // 리사이클러뷰
        void setRecyclerView(boolean isRecord);
    }

    interface Presenter {
        void setView(View view);
        void getContext(Context context);
        void deathView();

        // 상단 고정 알림
        void notificationManager(NotificationManager notificationManager);
        void holdNotification(int record);

        // 디비
        void firebaseDatabase(FirebaseDatabase firebaseDatabase);
        void getFirebaseChatting(String uid);

        // 리사이클러뷰
        void initRecyclerView(RecyclerView recyclerView);


        //채팅방
        void chatAdapter(Chat_Adapter adapter);
        void streamChatting(int record, String title);

        // 스트리밍 시작
        void connectRtsp(String url);

        void startStream(int record, String uid, String title);
        void stopStream(int record, String uid, String title);
        void failMessage(String msg);

        // 뒤로가기 다이얼로그
        void backPressedDialog();

        /////////////////// Preview ////////////////

        // 카메라
        void RtspCamera(RtspCamera1 rtspCamera1);
        void rtspPreview(boolean isPreview);
        void switchCamera();

    }

    interface streamCallback{

        void successCallback();
        void failCallback();
    }
}
