package com.px.common;

/**
 * Created by patrick on 15/09/2017.
 * create time : 3:36 PM
 */

public class TestEvent {
    private String content;

    public TestEvent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
