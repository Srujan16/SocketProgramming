package com.wavemaker.tutorial.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 * Created by srujant on 27/6/16.
 */
public class ClientReader implements Runnable {

    Socket socket;

    ClientReader(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        String message = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println(message);
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
