package com.thoingthoing.videolive.model;

import java.util.HashMap;

public class UserData {

    private String email, nickname, key, tocken, notice_message, back_thumbnail, profile_url;

    private int streaming, bookmarkcount;
    private HashMap<String, Object> bookmark;

    public String getBack_thumbnail() {
        return back_thumbnail;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getNotice_message() {
        return notice_message;
    }

    public int getBookmarkcount() {
        return bookmarkcount;
    }

    public HashMap<String, Object> getBookmark() {
        return bookmark;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTocken(String tocken) {
        this.tocken = tocken;
    }

    public void setStreaming(int streaming) {
        this.streaming = streaming;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getKey() {
        return key;
    }

    public String getTocken() {
        return tocken;
    }

    public int getStreaming() {
        return streaming;
    }


}
