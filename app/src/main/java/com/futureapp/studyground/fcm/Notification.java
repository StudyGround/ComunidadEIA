package com.futureapp.studyground.fcm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification {
    String title;
    String body;
    String click_action="OPEN_ACTIVITY_1";

    public Notification(String title, String body, String click_action) {
        this.title = title;
        this.body = body;
        this.click_action=click_action;

    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
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
