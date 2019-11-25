package com.tekzee.amiggos.base.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

@SerializedName("title")
@Expose
private String title;
@SerializedName("body")
@Expose
private String body;
@SerializedName("subtitle")
@Expose
private String subtitle;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

public String getSubtitle() {
return subtitle;
}

public void setSubtitle(String subtitle) {
this.subtitle = subtitle;
}

    public Notification(String title, String body, String subtitle) {
        this.title = title;
        this.body = body;
        this.subtitle = subtitle;
    }
}