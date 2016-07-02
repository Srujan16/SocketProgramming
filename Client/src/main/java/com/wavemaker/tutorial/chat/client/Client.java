package com.wavemaker.tutorial.chat.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by srujant on 26/6/16.
 */
public class Client {

    private short port;

    private Socket socket;

    Client(short port) {
        this.port = port;
    }

    public static void main(String arg[]) {
        Client client = new Client(Short.parseShort(arg[0]));
        client.runClient();
    }

    public void runClient() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), port);
            if (socket.isConnected()) {
                Thread inputThread = new Thread((Runnable) new ClientInputThread(socket));
                Thread readerThread = new Thread((Runnable) new ClientReader(socket));
                inputThread.start();
                readerThread.start();
            } else {
                throw new ConnectException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Connection Refused");
        }
    }
}
    