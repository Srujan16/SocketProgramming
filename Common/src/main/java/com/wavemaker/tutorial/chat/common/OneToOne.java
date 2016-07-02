package com.wavemaker.tutorial.chat.common;

import java.io.Serializable;

/**
 * Created by srujant on 1/7/16.
 */
public class OneToOne implements Serializable {
    private String message;
    private String destination;

    public OneToOne(String message,String destination){
        this.message=message;
        this.destination=destination;
    }

    public String getMessage() {
        return message;
    }

    public String getDestination() {
        return destination;
    }
}
