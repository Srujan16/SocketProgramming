package com.wavemaker.tutorial.chat.server;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
    private File file;
    private PrintWriter printWriter;

    ClientReader(Socket socket, File file) {
        this.socket = socket;
        this.file = file;
    }

    public void run(){
        String message = null;

        try {
            printWriter=new PrintWriter(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((message = bufferedReader.readLine()) != "Quit" && socket.isConnected()) {
                    printWriter.println(message);
                    printWriter.flush();
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
