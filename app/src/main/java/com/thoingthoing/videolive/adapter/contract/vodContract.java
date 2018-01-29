package com.thoingthoing.videolive.adapter.contract;

import com.thoingthoing.videolive.model.vod_item;

import java.util.List;

public interface vodContract {

    interface View{
        void notifyChanged();
    }

    interface Model{
        void addItem(List<vod_item> e);

    }
}
