package com.wavemaker.tutorial.chat.common; /**
 * Created by srujant on 29/6/16.
 */
import java.io.Serializable;

public class User implements Serializable{
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
