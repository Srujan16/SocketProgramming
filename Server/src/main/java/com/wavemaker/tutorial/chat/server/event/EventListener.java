package com.wavemaker.tutorial.chat.server.event;

/**
 * Created by srujant on 1/7/16.
 */
public interface EventListener {
    void onEvent(Object object, String user);
}
