package com.thoingthoing.videolive.ui.main.presenter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.thoingthoing.videolive.adapter.contract.rankingContract;

public interface FavoriteContract {

    interface View{

    }

    interface Presenter{

        void setView(View v);
        void setContext(Context context);
        void getFavoriteList(RecyclerView recyclerView);

        void setrankingcontractview(rankingContract.View view);
        void setrankingcontractmodel(rankingContract.Model model);
    }
}
