package com.wavemaker.tutorial.chat.server.manager;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by srujant on 7/7/16.
 */

/*This class stores all Client-Threads created by server*/

public class ThreadManager {
    private CopyOnWriteArrayList<Thread> threads = new CopyOnWriteArrayList();

    public void add(Thread thread) {
        threads.add(thread);
    }

    public CopyOnWriteArrayList<Thread> getThreads() {
        return threads;
    }


    public boolean isEmpty() {
        return threads.size() == 0;
    }

}
