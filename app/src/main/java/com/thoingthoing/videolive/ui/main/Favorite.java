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
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.ui.main.presenter.FavoriteContract;
import com.thoingthoing.videolive.ui.main.presenter.FavoritePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Favorite extends Fragment implements FavoriteContract.View {

    private FavoritePresenter presenter;

    @BindView(R.id.favorite_recyclerview)
    RecyclerView recyclerView;

    public Favorite(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
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

        presenter = new FavoritePresenter();

        presenter.setView(this);
        presenter.setContext(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        favoriteList();
    }

    private void favoriteList(){
        if(Data.uid != null){
            presenter.getFavoriteList(recyclerView);
        }
    }
}
