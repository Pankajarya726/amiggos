package com.tekzee.amiggos.base.model;

public class Sender {
    public NotificationData data;
    public String to;

    public Sender(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
