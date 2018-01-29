package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.videolive.adapter.contract.liveContract;
import com.thoingthoing.videolive.adapter.contract.replayContract;
import com.thoingthoing.videolive.adapter.live_adapter;
import com.thoingthoing.videolive.adapter.replay_adapter;
import com.thoingthoing.videolive.model.stream_item;

import java.util.ArrayList;
import java.util.List;

public class LivePresenter implements LiveContract.Presenter {

    private LiveContract.View v;
    private Context context;
    private RecyclerView live;
    private RecyclerView replay;
    private FirebaseDatabase firebaseDatabase;
    private replayContract.View replayadapterView;
    private replayContract.Model replayadapterModel;
    private liveContract.View liveadapterView;
    private liveContract.Model liveadapterModel;
    private int incomming = 0;

    @Override
    public void getView(LiveContract.View v) {
        this.v = v;
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    // 디비 설정
    @Override
    public void getFirebase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    // 생방송 리스트 설정
    @Override
    public void getLiveRecyclerView(RecyclerView recyclerView) {
        this.live = recyclerView;

        live.setHasFixedSize(true);
        live.setItemAnimator(new DefaultItemAnimator());
        live.scrollToPosition(0);
        live.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        live.setLayoutManager(layoutManager);

        live_adapter live_adapter = new live_adapter(context);
        live.setAdapter(live_adapter);

        setliveConstractModel(live_adapter);
        setliveConstractView(live_adapter);


    }

    // 다시보기 리스트 설정
    @Override
    public void getReplayRecycleerView(RecyclerView recyclerView) {
        this.replay = recyclerView;

        replay.setHasFixedSize(true);
        replay.setItemAnimator(new DefaultItemAnimator());
        replay.scrollToPosition(0);
        replay.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        replay.setLayoutManager(layoutManager);

        List<stream_item> item = new ArrayList<>();

        replay_adapter adapter = new replay_adapter(context);
        replay.setAdapter(adapter);

        setreplayConstractModel(adapter);
        setreplayConstractView(adapter);

        replayadapterModel.addItem(item);
        replayadapterView.notifyAdapter();
    }

    // 실시간 방송정보 추가
    @Override
    public void getLiveList() {
        firebaseDatabase.getReference("stream").addChildEventListener(new ChildEventListener() {

            // firebase에서 업로드되는 정보들을 받아와서 RecylerView 에 추가
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                // 방송중일 경우 RecyclerView에 추가
                liveadapterModel.addItem(dataSnapshot.getValue(stream_item.class));
                liveadapterView.notifyChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                liveadapterModel.removeItem(dataSnapshot.getValue(stream_item.class));
                liveadapterView.notifyChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void setliveConstractView(liveContract.View liveView) {
        this.liveadapterView = liveView;
    }

    @Override
    public void setliveConstractModel(liveContract.Model liveModel) {
        this.liveadapterModel = liveModel;
    }

    @Override
    public void setreplayConstractView(replayContract.View adapterView) {
        this.replayadapterView = adapterView;
    }

    @Override
    public void setreplayConstractModel(replayContract.Model adapterModel) {
        this.replayadapterModel = adapterModel;

    }

}
