package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.server.manager.ClientManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srujant on 29/6/16.
 */
public class Group {

    private List<String> usersList = new ArrayList<>();

    public List<String> getUsersList() {
        return usersList;
    }


}
