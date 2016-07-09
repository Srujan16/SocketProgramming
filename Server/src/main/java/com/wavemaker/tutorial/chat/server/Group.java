package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.server.manager.ClientManager;

import java.util.ArrayList;
import java.util.List;
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
