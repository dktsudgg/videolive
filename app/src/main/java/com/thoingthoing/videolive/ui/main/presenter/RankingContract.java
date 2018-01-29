package com.thoingthoing.videolive.ui.main.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.thoingthoing.videolive.adapter.contract.rankingContract;

public interface RankingContract {

    interface View{

    }

    interface Presenter{

        void setrankingcontractview(rankingContract.View view);
        void setrankingcontractmodel(rankingContract.Model model);

        void setView(View v);
        void deathView();
        void getContext(Context context);

        // 기본 세팅
        void getRankingData(RecyclerView recyclerView);


    }
}
