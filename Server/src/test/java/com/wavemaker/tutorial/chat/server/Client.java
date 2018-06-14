package com.wavemaker.tutorial.chat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.tutorial.chat.common.User;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by srujant on 5/7/16.
 */
public class Client {
    private short port;
    private String userName;
    private BufferedWriter bufferedWriter;

    public String getUser() {
        return userName;
    }

    private Socket socket;

    Client(short port,String user) {
        this.port = port;
        this.userName=user;
    }


    public void runClient() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), port);
            if (socket.isConnected()) {
                User user = new User();
                user.setUserName(userName);
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] bytes = objectMapper.writeValueAsBytes(user);
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write(objectMapper.writeValueAsString(user) + "\n");
                bufferedWriter.flush();
                Thread readerThread = new Thread( new ClientReader(socket, new File(user.getUserName())));
                readerThread.start();
            } else {
                throw new ConnectException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Connection Refused");
        }
    }


    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

}
