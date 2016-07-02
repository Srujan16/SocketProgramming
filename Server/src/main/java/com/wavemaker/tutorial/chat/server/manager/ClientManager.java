package com.wavemaker.tutorial.chat.server.manager;

import java.io.OutputStream;

/**
 * Created by srujant on 1/7/16.
 */
public interface ClientManager {

    void registerClient(String user, OutputStream outputStream);

    void deRegister(String user, OutputStream outputStream);

    boolean isRegistered(String user);

    void sendMessage(String message, String userName);

    void broadCast(String broadCastMesage, String user, String userMessage);

}
