package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.server.event.EventManager;
import com.wavemaker.tutorial.chat.server.manager.ClientManager;
import com.wavemaker.tutorial.chat.server.manager.ClientManagerImpl;
import com.wavemaker.tutorial.chat.server.manager.GroupManager;
import com.wavemaker.tutorial.chat.server.manager.GroupManagerImpl;
import com.wavemaker.tutorial.chat.server.manager.MessageManager;
import com.wavemaker.tutorial.chat.server.manager.MessageManagerImpl;
import com.wavemaker.tutorial.chat.server.manager.ThreadManager;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by srujant on 1/7/16.
 */

/*This factory class provides singleton object for the given classes*/
public class ObjectFactory {

    private static Map<Class, Object> objectsMap = new ConcurrentHashMap();

    static {
        objectsMap.put(EventManager.class,  new EventManager());
        objectsMap.put(ClientManager.class, new ClientManagerImpl());
        objectsMap.put(GroupManager.class,  new GroupManagerImpl());
        objectsMap.put(MessageManager.class,new MessageManagerImpl());
        objectsMap.put(ThreadManager.class, new ThreadManager());
    }

    public static <T> T getInstance(Class<T> c) {
        Object o = objectsMap.get(c);
        if (o != null) {
            return (T) o;
        }
        throw new IllegalArgumentException("No Instance found for class " + c);
    }
}

