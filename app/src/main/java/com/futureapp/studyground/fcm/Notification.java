package com.futureapp.studyground.fcm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification {
    String title;
    String body;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;

    }




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
}
