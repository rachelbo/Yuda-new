package com.example.myapplication.app;


public class ChatModel {

    private String message;
    private Boolean isMe;
    private String time;

    public ChatModel() {
    }

    public ChatModel(String message, Boolean isMe, String time) {
        this.message = message;
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
}