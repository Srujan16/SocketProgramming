package com.wavemaker.tutorial.chat.common; /**
 * Created by srujant on 29/6/16.
 */

import java.io.Serializable;

public class BroadCast implements Serializable {


    private String group;
    private String message;


    public BroadCast(String group, String message) {
        this.group = group;
        this.message = message;
    }

    public String getGroup() {
        return group;
    }

    public String getMessage() {
        return message;
    }
}

