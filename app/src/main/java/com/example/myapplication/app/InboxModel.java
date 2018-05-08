package com.example.myapplication.app;


public class InboxModel {
    private String contact_id, image, username,time,message,unread,contact_image,contact_active;
    private int contact_online;

    public InboxModel() {
    }

    public InboxModel(String contact_id, String image, String username, String time, String message, String unread, String contact_active, String contact_image, int contact_online) {
        this.contact_id = contact_id;
        this.image = image;
        this.username = username;
        this.time = time;
        this.message = message;
        this.unread = unread;
        this.contact_active=contact_active;
        this.contact_image=contact_image;
        this.contact_online=contact_online;
    }

    public String getContactId() {
        return contact_id;
    }

    public void setContactId(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    public String getContact_active() {
        return contact_active;
    }

    public void setContact_active(String contact_active) {
        this.contact_active = contact_active;
    }

    public int getContact_online() {
        return contact_online;
    }

    public void setContact_online(int contact_online) {
        this.contact_online = contact_online;
    }
}