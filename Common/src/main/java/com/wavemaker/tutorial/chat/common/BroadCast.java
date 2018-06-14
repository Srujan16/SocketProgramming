package com.wavemaker.tutorial.chat.common; /**
 * Created by srujant on 29/6/16.
 */

import java.io.Serializable;

public class BroadCast extends AbstractMessage implements Serializable {


    private static final long serialVersionUID = -761847321411625438L;

    private String group;
    private String message;


    public  BroadCast(){}

    public BroadCast(String group, String message) {
        super("BroadCast");
        this.group = group;
        this.message = message;
    }

    public String getGroup() {
        return group;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "BroadCast{" +
                "group='" + group + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

