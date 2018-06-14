package com.wavemaker.tutorial.chat.server;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wavemaker.tutorial.chat.common.Action;
import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.common.OneToOne;
import com.wavemaker.tutorial.chat.server.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;

/**
 * Created by srujant on 27/6/16.
 */
public class ClientReaderThread implements Runnable {

    private String currentUser;
    private BufferedReader bufferedReader;

    private EventManager eventManager = ObjectFactory.getInstance(EventManager.class);

    private static final Logger logger = LoggerFactory.getLogger(ClientReaderThread.class.getName());

    ClientReaderThread(String currentUser, BufferedReader bufferedReader) {
        this.currentUser = currentUser;
        this.bufferedReader = bufferedReader;
    }


    public void run() {
        while (true) {
            try {
                String s = bufferedReader.readLine();
                logger.info("Looping for user data {}", currentUser);
                ObjectMapper objectMapper=new ObjectMapper();
                JsonNode jsonNode=objectMapper.readValue(s,JsonNode.class);
                Object object=null;
                if (jsonNode.get("type").toString().equals("\"Action\"")){
                    object=objectMapper.readValue(s,Action.class);
                }else if(jsonNode.get("type").toString().equals("\"BroadCast\"")){
                    object=objectMapper.readValue(s,BroadCast.class);
                }else {
                    object=objectMapper.readValue(s,OneToOne.class);
                }
                logger.info(currentUser+" "+"Received object {} ", object);
                eventManager.publishEvent(object, currentUser);
            } catch (SocketTimeoutException e) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error in reading object for the user" + currentUser, e);
            }
        }
    }
}
