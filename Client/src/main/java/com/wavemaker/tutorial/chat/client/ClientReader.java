package com.wavemaker.tutorial.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


/**
 * Created by srujant on 27/6/16.
 */
public class ClientReader implements Runnable {

    private Socket socket;
    private PrintStream printStream;
    private OutputStream outputStream;

    ClientReader(Socket socket, OutputStream outputStream) {
        this.socket = socket;
        this.outputStream = outputStream;
    }

    public void run() {
        String message = null;
        printStream=new PrintStream(outputStream);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((message = bufferedReader.readLine()) != null) {
                    printStream.println(message);
                    printStream.flush();
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
