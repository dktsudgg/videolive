package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.videolive.adapter.contract.rankingContract;
import com.thoingthoing.videolive.adapter.ranking_adapter;
import com.thoingthoing.videolive.model.UserData;
import com.thoingthoing.videolive.model.ranking_item;

public class RankingPresenter implements RankingContract.Presenter {

    private rankingContract.View rcontract_view;
    private rankingContract.Model rcontract_model;

    private RankingContract.View v;
    private Context context;
    private FirebaseDatabase firebaseDatabase;

    private ranking_adapter adapter;

    @Override
    public void setrankingcontractview(rankingContract.View view) {
        this.rcontract_view = view;
    }

    @Override
    public void setrankingcontractmodel(rankingContract.Model model) {
        this.rcontract_model = model;
    }

    @Override
    public void setView(RankingContract.View v) {
        this.v = v;
    }

    @Override
    public void deathView() {
        v = null;
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void getRankingData(RecyclerView recyclerView) {
        firebaseDatabase = FirebaseDatabase.getInstance();

        adapter = new ranking_adapter(context);
        recyclerView.setAdapter(adapter);

        setrankingcontractmodel(adapter);
        setrankingcontractview(adapter);


        firebaseDatabase.getReference("user").addChildEventListener(new ChildEventListener() {
            // firebase에서 업로드되는 정보들을 받아와서 RecylerView 에 추가
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String email = dataSnapshot.getValue(UserData.class).getEmail();
                String nickname = dataSnapshot.getValue(UserData.class).getNickname();
                int onoff = dataSnapshot.getValue(UserData.class).getStreaming();
                String profileimg = dataSnapshot.getValue(UserData.class).getProfile_url();
                String key = dataSnapshot.getKey();

                rcontract_model.addItem(new ranking_item(nickname, profileimg, email, onoff, key));
                rcontract_view.notifyChanged();
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

}
