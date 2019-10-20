package com.futureapp.studyground.fcm;

public class Sender {
    String to;
    Notification notification;
    Data data;

    public Sender(String to, Notification notification, Data data) {
        this.to = to;
        this.notification = notification;
        this.data=data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

}
