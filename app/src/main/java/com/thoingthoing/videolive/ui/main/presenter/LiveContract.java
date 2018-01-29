package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.videolive.adapter.contract.liveContract;
import com.thoingthoing.videolive.adapter.contract.replayContract;

public interface LiveContract {

    interface View{}

    interface Presenter{

        void getView(View v);

        void getContext(Context context);

        void getFirebase(FirebaseDatabase firebaseDatabase);

        void getLiveRecyclerView(RecyclerView recyclerView);

        void getReplayRecycleerView(RecyclerView recyclerView);

        void getLiveList();

        // 어뎁터 연결
        void setliveConstractView(liveContract.View liveView);
        void setliveConstractModel(liveContract.Model liveModel);

        void setreplayConstractView(replayContract.View adapterView);

        void setreplayConstractModel(replayContract.Model adapterModel);
    }
}
