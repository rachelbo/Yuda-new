package com.example.myapplication.app;


public class ChatGroupModel {

    private String message,username;
    private Boolean isMe;
    private String time;

    public ChatGroupModel() {
    }

    public ChatGroupModel(String message, String username, Boolean isMe, String time) {
        this.message = message;
        this.username = username;
        this.isMe = isMe;
        this.time = time;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsMe() {
        return isMe;
    }

    public void setIsMe(Boolean isMe) {
        isMe = isMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}