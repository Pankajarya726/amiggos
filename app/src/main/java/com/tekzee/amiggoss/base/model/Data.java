package com.tekzee.amiggoss.base.model;

public class Data {
    private String user;
    private int icon;
    private String type;
    private String body;
    private String title;
    private String sented;
    private String notification_key;
    private String friendFirebaseid;

    public Data(String user, int icon, String type, String body, String title, String sented, String notification_key, String friendFirebaseid) {
        this.user = user;
        this.icon = icon;
        this.type = type;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.notification_key = notification_key;
        this.friendFirebaseid = friendFirebaseid;
    }

    public String getFriendFirebaseid() {
        return friendFirebaseid;
    }

    public void setFriendFirebaseid(String friendFirebaseid) {
        this.friendFirebaseid = friendFirebaseid;
    }

    public Data() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getNotification_key() {
        return notification_key;
    }

    public void setNotification_key(String notification_key) {
        this.notification_key = notification_key;
    }
}
