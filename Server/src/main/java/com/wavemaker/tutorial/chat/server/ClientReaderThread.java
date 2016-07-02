package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.server.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by srujant on 27/6/16.
 */
public class ClientReaderThread implements Runnable {

    private  String currentUser;
    private ObjectInputStream objectInputStream;

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
                logger.info("Received object {}", object);
                eventManager.publishEvent(object,currentUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
               throw new RuntimeException(e);
            }
        }
    }
}
