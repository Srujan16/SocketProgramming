package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.common.User;

import com.wavemaker.tutorial.chat.server.manager.ClientManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by srujant on 26/6/16.
 */
public class Server {

    private short port;

    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);

    private static Logger logger = LoggerFactory.getLogger(Server.class.getName());

    Server(short port) {
        this.port = port;
    }

    public static void main(String arg[]) {
        Server server = new Server(Short.parseShort(arg[0]));
        server.startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
                User user = (User) objectInputStream.readObject();
                logger.info("New User : {} Joined the network", user.getUserName());
                clientManager.registerClient(user.getUserName(), client.getOutputStream());
                Thread readerThread = new Thread(new ClientReaderThread(user.getUserName(), objectInputStream));
                readerThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}






