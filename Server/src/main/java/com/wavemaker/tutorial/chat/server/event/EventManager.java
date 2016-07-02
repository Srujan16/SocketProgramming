package com.wavemaker.tutorial.chat.server.event;

import java.util.*;

/**
 * Created by srujant on 1/7/16.
 */
public class EventManager {
    private List<EventListener> eventListeners = new ArrayList();

    public void publishEvent(Object messagePacket, String currentUser) {
        for (EventListener listener : eventListeners) {
            listener.onEvent(messagePacket, currentUser);
        }
    }

    public void registerEvent(EventListener listener) {
        eventListeners.add(listener);
    }

}
