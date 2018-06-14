package com.wavemaker.tutorial.chat.server.manager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by srujant on 1/7/16.
 */


public class ClientManagerImpl implements ClientManager {


    private Map<String, OutputStream> clients = new ConcurrentHashMap<>();

    public ClientManagerImpl() {
    }

    @Override
    public boolean isEmpty() {
        return clients.size() == 0;
    }


    @Override
    public boolean isRegistered(String user) {
        if (clients.containsKey(user)) {
            return true;
        }
        return false;
    }

    @Override
    public void registerClient(String user, OutputStream outputStream) {
        clients.put(user, outputStream);
    }

    @Override
    public  void deRegister(String user) {
       }

    public Map<String, OutputStream> getClients() {
        return clients;
    }

    @Override
    public synchronized void sendMessage(String message, String user) {
        if (!message.endsWith("\n")) {
            message = message + '\n';
        }
        try {
            OutputStream outputStream = getClients().get(user);
            if (outputStream != null) {
                outputStream.write(message.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void broadCast(String broadcastMessage, String user, String userMessage) {
        Set keySet = clients.keySet();
        OutputStream outputStream;
        if (!(userMessage.endsWith("\n"))) {

            userMessage += "\n";
        }
        if (!(broadcastMessage.endsWith("\n"))) {

            broadcastMessage += "\n";
        }


        for (Object client : keySet) {
            outputStream = clients.get(client);
            try {
                if (client.equals(user)) {
                    outputStream.write(userMessage.getBytes());
                } else {
                    outputStream.write((user + " : " + broadcastMessage).getBytes());
                }
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

