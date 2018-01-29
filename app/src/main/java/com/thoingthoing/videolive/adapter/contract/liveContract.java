package com.thoingthoing.videolive.adapter.contract;

import com.thoingthoing.videolive.model.stream_item;

public interface liveContract {

    interface View{
        void notifyChanged();
    }

    interface Model{
        void addItem(stream_item e);
        void removeItem(stream_item e);
    }
}
