package com.futureapp.studyground.fcm;

public class Sender {
    String to;
    Notification notification;
    Data data;
    Boolean content_available= true;

    public Sender(String to, Notification notification, Data data, Boolean content_available) {
        this.to = to;
        this.notification = notification;
        this.data=data;
        this.content_available=content_available;
    }

    public Boolean getContent_available() {
        return content_available;
    }

    public void setContent_available(Boolean content_available) {
        this.content_available = content_available;
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
