package com.thoingthoing.videolive.model;

import java.util.HashMap;
import java.util.Map;

public class stream_item {

    public  String title, nickname, url, startTime, thumbnail, uid;
    public int onoff;
    public HashMap<String, Object> userlist;

    public stream_item(){}

    public stream_item(String title, String nickname, String url, String startTime, String thumbnail, String uid, int onoff) {
        this.title = title;
        this.nickname = nickname;
        this.url = url;
        this.startTime = startTime;
        this.thumbnail = thumbnail;
        this.uid = uid;
        this.onoff = onoff;
    }

    class userlist{
        private Map<String, Object> userlist() {
            HashMap<String, Object> list = new HashMap<>();

            return list;
        }

        public userlist(){}
    }

}
