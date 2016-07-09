package com.wavemaker.tutorial.chat.server;


import com.wavemaker.tutorial.chat.server.event.EventManager;
import com.wavemaker.tutorial.chat.server.manager.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by srujant on 27/6/16.
 */
public class ClientReaderThread implements Runnable {

    private String currentUser;
    private ObjectInputStream objectInputStream;
    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);
    private EventManager eventManager = ObjectFactory.getInstance(EventManager.class);

    private static final Logger logger = LoggerFactory.getLogger(ClientReaderThread.class.getName());

    ClientReaderThread(String currentUser, ObjectInputStream objectInputStream) {
        this.currentUser = currentUser;
        this.objectInputStream = objectInputStream;
    }

    public void run() {
        logger.info("Looping for user data {}", currentUser);
        while (true) {
            try {
                Object object = objectInputStream.readObject();
                logger.info("Received object {} ", object);
                eventManager.publishEvent(object, currentUser);
                /*if (System.currentTimeMillis()%2 == 0) {
                }*/
            } catch (SocketTimeoutException e) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
