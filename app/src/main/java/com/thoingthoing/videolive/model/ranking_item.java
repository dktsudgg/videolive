package com.thoingthoing.videolive.model;


public class ranking_item {

    private String nickname, profileimg, email, userkey;
    private int onoff;

    public ranking_item(String nickname, String profileimg, String email, int onoff, String userkey) {
        this.nickname = nickname;
        this.profileimg = profileimg;
        this.email = email;
        this.onoff = onoff;
        this.userkey = userkey;
    }

    public String getUserkey() {
        return userkey;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public String getEmail() {
        return email;
    }

    public int getOnoff() {
        return onoff;
    }
}
