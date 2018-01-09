package com.wiatec.btv_launcher.rxevent;

/**
 * Created by patrick on 09/01/2018.
 * create time : 3:05 PM
 */

public enum EnumRxEvent {

    EVENT_LIMITED(0, "user limited");

    private int event;
    private String description;

    EnumRxEvent(int event, String description) {
        this.event = event;
        this.description = description;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EnumRxEvent{" +
                "event=" + event +
                ", description='" + description + '\'' +
                '}';
    }
}
