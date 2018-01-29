package com.thoingthoing.videolive.model;

public class vod_item {

    public  String title, nickname, thumbnail;

    public vod_item(String title, String nickname, String thumbnail) {
        this.title = title;
        this.nickname = nickname;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {

        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
