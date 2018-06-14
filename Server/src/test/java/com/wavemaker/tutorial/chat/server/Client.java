package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.common.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by srujant on 5/7/16.
 */
public class Client {
    private short port;
    private String userName;
    private ObjectOutputStream objectOutputStream;

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
                objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(user);
                Thread readerThread = new Thread( new ClientReader(socket, new File(user.getUserName())));
                readerThread.start();
            } else {
                throw new ConnectException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Connection Refused");
        }
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }
}
