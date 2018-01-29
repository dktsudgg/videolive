package com.thoingthoing.videolive.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.ui.main.presenter.RankingContract;
import com.thoingthoing.videolive.ui.main.presenter.RankingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Ranking extends Fragment implements RankingContract.View {

    private RankingPresenter presenter;

    public Ranking() {
    }

    @BindView(R.id.ranking_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this, v);
        initView();
        init();
        return v;
    }

    private void initView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void init(){
        presenter = new RankingPresenter();

        presenter.setView(this);
        presenter.getContext(getActivity());
        presenter.getRankingData(recyclerView);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.deathView();
    }
}
