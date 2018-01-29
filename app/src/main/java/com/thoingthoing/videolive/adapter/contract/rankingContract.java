package com.thoingthoing.videolive.adapter.contract;

import com.thoingthoing.videolive.model.ranking_item;

public interface rankingContract {

    interface View{
        void notifyChanged();
    }

    interface Model{
        void addItem(ranking_item item);
    }
}
