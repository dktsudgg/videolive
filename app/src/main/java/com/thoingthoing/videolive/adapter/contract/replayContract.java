package com.thoingthoing.videolive.adapter.contract;

import com.thoingthoing.videolive.model.stream_item;

import java.util.List;

public interface replayContract {

    interface View {

        void notifyAdapter();

    }

    interface Model {

        void addItem(List<stream_item> e);


    }
}
