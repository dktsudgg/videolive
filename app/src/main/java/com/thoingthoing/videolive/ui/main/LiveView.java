package com.thoingthoing.videolive.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.ui.main.presenter.LiveContract;
import com.thoingthoing.videolive.ui.main.presenter.LivePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveView extends Fragment implements LiveContract.View {

    private FirebaseDatabase firebaseDatabase;
    private LivePresenter presenter;

    @BindView(R.id.main_live_recyclerview)
    RecyclerView live_recyclerView;
    @BindView(R.id.main_replay_recyclerview)
    RecyclerView replay_recyclerView;

    public LiveView() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_liveview, container, false);
        ButterKnife.bind(this, v);

        init();

        return v;
    }

    private void init() {

        presenter = new LivePresenter();

        presenter.getView(this);
        presenter.getContext(getActivity());

        // RecyclerView setting
        presenter.getLiveRecyclerView(live_recyclerView);
        presenter.getReplayRecycleerView(replay_recyclerView);

        // FireBase
        firebaseDatabase = FirebaseDatabase.getInstance();
        presenter.getFirebase(firebaseDatabase);

        // 방송정보 리스트 추가
        presenter.getLiveList();


    }

}
