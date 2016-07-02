package com.wavemaker.tutorial.chat.client;

import com.wavemaker.tutorial.chat.common.Action;
import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.common.OneToMany;
import com.wavemaker.tutorial.chat.common.OneToOne;
import com.wavemaker.tutorial.chat.common.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by srujant on 28/6/16.
 */

public class ClientInputThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ClientInputThread.class.getName());
    Socket socket;

    ClientInputThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        String userName = null;
        String message;
        boolean flag = true;
        ObjectOutputStream objectOutputStream;
        System.out.println("UserName : ");
        BufferedReader keyBoard = new BufferedReader(new InputStreamReader(System.in));
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            userName = keyBoard.readLine();
            User user = new User();
            user.setUserName(userName);
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            while ((message = keyBoard.readLine()) != null && flag) {
                JSONParser jsonParser = new JSONParser();
                JSONObject json = (JSONObject) jsonParser.parse(message);
                String type = (String) json.get("type");
                if (type.equals("Broadcast")) {
                    objectOutputStream.writeObject(new BroadCast((String) json.get("group"), (String) json.get("message")));
                } else if (type.equals("Action")) {
                    objectOutputStream.writeObject(new Action((String) json.get("name"), (String) json.get("group")));
                } else if (type.equals("OneToOne")) {
                    objectOutputStream.writeObject(new OneToOne((String) json.get("message"), (String) json.get("to")));
                } else if (type.equals("OneToMany")) {
                    objectOutputStream.writeObject(new OneToMany((String) json.get("message"), json.get("to")));
                } else {
                    flag = false;
                }
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
