package com.wavemaker.tutorial.chat.common;

/**
 * Created by srujant on 11/7/16.
 */
public class AbstractMessage {

    private String type;

    public AbstractMessage(){}

    public AbstractMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
