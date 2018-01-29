package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thoingthoing.videolive.adapter.contract.rankingContract;
import com.thoingthoing.videolive.adapter.ranking_adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.UserData;
import com.thoingthoing.videolive.model.ranking_item;

import java.util.ArrayList;
import java.util.List;


public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View v;
    private Context context;
    private rankingContract.View rcontract_view;
    private rankingContract.Model rcontract_model;
    private List<String> item;
    private FirebaseDatabase firebaseDatabase;

    @Override
    public void setView(FavoriteContract.View v) {
        this.v = v;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    // 내 북마크 리스트 검색
    @Override
    public void getFavoriteList(RecyclerView recyclerView) {
       firebaseDatabase = FirebaseDatabase.getInstance();

        ranking_adapter adapter = new ranking_adapter(context);
        recyclerView.setAdapter(adapter);

        setrankingcontractmodel(adapter);
        setrankingcontractview(adapter);

        item = new ArrayList<>();

        firebaseDatabase.getReference("user").child(Data.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot node : dataSnapshot.child("bookmark").getChildren()){

                    // 북마크 목록을 리스트에 추가
                    item.add(String.valueOf(node.child("bookmark").getValue()));

                }
                // 해당 북마크 스트리머 검색
                getUserList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setrankingcontractview(rankingContract.View view) {
        this.rcontract_view = view;
    }

    @Override
    public void setrankingcontractmodel(rankingContract.Model model) {
        this.rcontract_model = model;
    }

    // 해당 북마크 스트리머 정보 검색
    private void getUserList(){

        firebaseDatabase.getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;

                for(DataSnapshot node : dataSnapshot.getChildren()){

                    // 아이템 검색이 끝낫을 경우 종료
                    if(item.size() == i){
                        break;
                    }

                    String childkey = node.getKey();

                    // 리스트 아이템 검색하여 있을경우 i ++
                    if(item.get(i).equals(childkey)){

                        String email = node.getValue(UserData.class).getEmail();
                        String nickname = node.getValue(UserData.class).getNickname();
                        int onoff = node.getValue(UserData.class).getStreaming();
                        String profileimg = node.getValue(UserData.class).getProfile_url();
                        String key = node.getKey();

                        rcontract_model.addItem(new ranking_item(nickname,profileimg, email, onoff, key));
                        rcontract_view.notifyChanged();

                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
