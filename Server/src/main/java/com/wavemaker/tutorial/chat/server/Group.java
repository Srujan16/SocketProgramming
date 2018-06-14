package com.wavemaker.tutorial.chat.server;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by srujant on 29/6/16.
 */
public class Group {

    private CopyOnWriteArrayList<String> usersList = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<String> getUsersList() {
        return usersList;
    }
}
