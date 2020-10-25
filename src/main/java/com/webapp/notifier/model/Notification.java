package com.webapp.notifier.model;

import javax.persistence.*;


@Entity
public class Notification {

    @Id
    private Long alertID;

    private Long watchID;

    @Lob
    private String value;

    private String Status;

    private Long time;

    public Notification(){

    }

    public Notification(long alertID, Long watchID){
        this.alertID = alertID;
        this.watchID = watchID;
    }

    public Long getAlertID() {
        return alertID;
    }

    public void setAlertID(Long alertID) {
        this.alertID = alertID;
    }

    public Long getWatchID() {
        return watchID;
    }

    public void setWatchID(Long watchID) {
        this.watchID = watchID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
