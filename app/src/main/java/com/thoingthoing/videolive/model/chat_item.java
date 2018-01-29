package com.thoingthoing.videolive.model;


public class chat_item {

    public String chat_time;
    public String chat_nickname;
    public String chat_message;

    public chat_item(){}

    public chat_item(String chat_time, String chat_nickname, String chat_message){
        this.chat_message = chat_message;
        this.chat_nickname = chat_nickname;
        this.chat_time = chat_time;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public void setChat_nickname(String chat_nickname) {
        this.chat_nickname = chat_nickname;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public String getChat_nickname() {

        return chat_nickname;
    }

    public String getChat_message() {
        return chat_message;
    }
}
