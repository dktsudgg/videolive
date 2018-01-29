package com.thoingthoing.videolive.ui.player.presenter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.videolive.adapter.Chat_Adapter;
import com.thoingthoing.videolive.model.chat_item;
import com.thoingthoing.videolive.utils.KeyboardManager;

import java.util.List;

public interface StreamPlayerContract {

    interface View {
        // 프로그레스바
        void setProgress(boolean isRun);
        void chatListbtn(boolean isHide);
    }

    interface Presenter {

        void view(View v);
        void deathView();
        void getContext(Context context);

        // 프리뷰 세팅
        void setExoplayer(SimpleExoPlayerView playerView);

        // 채팅 설정
        void setRecyclerView(RecyclerView recyclerView);
        void getFirebaseData(FirebaseDatabase firebaseDatabase);
        void keyboardHide(KeyboardManager keyboardManager);
        void setChatAdapter(Chat_Adapter adapter, List<chat_item> item);
        void setTitle(TextView tv, String title);
        void chatMessage(EditText et);

        void setUid(String uid);

        // 비디오 연결

        void setUri(String uri);
        void connectStream(String url);
        void connectData();
        void visibleChatList(boolean isHide);

        void sendChatMessage(String msg);

    }

    interface playCallback{

        void successCallback(boolean isRun);
    }
}
