package com.wavemaker.tutorial.chat.common;

import java.io.Serializable;

/**
 * Created by srujant on 1/7/16.
 */
public class OneToOne extends  AbstractMessage implements Serializable {
    private String message;
    private String destination;

    public OneToOne(){}

    public OneToOne(String message,String destination){
        super("OneToOne");
        this.message=message;
        this.destination=destination;
    }

    public String getMessage() {
        return message;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "OneToOne{" +
                "message='" + message + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
