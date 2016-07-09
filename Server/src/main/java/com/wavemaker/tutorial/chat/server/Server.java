package com.wavemaker.tutorial.chat.server;

import com.wavemaker.tutorial.chat.common.User;

import com.wavemaker.tutorial.chat.server.manager.ClientManager;

import com.wavemaker.tutorial.chat.server.manager.ThreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.SyncFailedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by srujant on 26/6/16.
 */
public class Server {

    private short port;
    private ServerSocket serverSocket;

    private volatile boolean started = false;

    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);
    private final ThreadManager threadManager=ObjectFactory.getInstance(ThreadManager.class);

    private static final int GRACEFUL_SHUTDOWN_TIMEOUT= 10000;
    private static Logger logger = LoggerFactory.getLogger(Server.class.getName());

    public Server(short port) {
        this.port = port;
    }

    public static void main(String arg[]) {
        Server server = new Server(Short.parseShort(arg[0]));
        server.startServer();
    }

    public void stopServer(){
        if (!started) {
            return;
        }
        final Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!threadManager.isEmpty()){
                    CopyOnWriteArrayList<Thread> threads = threadManager.getThreads();
                    for(Thread thread:threads) {
                        thread.interrupt();
                    }
                    for(Thread thread:threads) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            }
        });
        t.start();
        try {
            t.join(GRACEFUL_SHUTDOWN_TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            serverSocket.close();
            started = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void startServer() {
        if (started) {
            return;
        }
        started = true;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(5000);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
        while (!serverSocket.isClosed()) {
            try {
                addNewClient();
            } catch (SocketTimeoutException e) {
                logger.debug("Socket Timed out", e);
            } catch (Exception e) {
                if (started) {
                    logger.warn("Failed to add new client", e);
                }
            } finally {
                if (started == false) {
                    return;
                }
            }
        }
    }

    private void addNewClient() throws IOException, ClassNotFoundException {
        Socket client = serverSocket.accept();
        client.setSoTimeout(5000);
        ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
        User user = (User) objectInputStream.readObject();
        logger.info("New User : {} Joined the network", user.getUserName());
        clientManager.registerClient(user.getUserName(), client.getOutputStream());
        Thread readerThread = new Thread(new ClientReaderThread(user.getUserName(), objectInputStream));
        readerThread.start();
        threadManager.add(readerThread);
    }
}






