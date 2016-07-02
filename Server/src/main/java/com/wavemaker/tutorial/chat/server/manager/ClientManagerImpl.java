package com.wavemaker.tutorial.chat.server.manager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by srujant on 1/7/16.
 */


public class ClientManagerImpl implements ClientManager {


    private Map<String, OutputStream> clients = new HashMap<String, OutputStream>();

    public ClientManagerImpl() {
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
    public void deRegister(String user, OutputStream outputStream) {
        clients.remove(user);
    }


    public Map<String, OutputStream> getClients() {
        return clients;
    }

    @Override
    public synchronized void sendMessage(String message, String user) {
        try {
            OutputStream outputStream = getClients().get(user);
            outputStream.write(message.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void broadCast(String broadcastMessage, String user, String userMessage) {
        Set keySet = clients.keySet();
        OutputStream outputStream;
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
