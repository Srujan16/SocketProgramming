package com.wavemaker.tutorial.chat.server;

/**
 * Created by srujant on 5/7/16.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.tutorial.chat.common.Action;
import com.wavemaker.tutorial.chat.common.BroadCast;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


public class ServerTest {


    private ConcurrentHashMap<String, BufferedWriter> userVsUserObjectOutputStream = new ConcurrentHashMap();
    private List<String> expectedOutput = Collections.synchronizedList(new ArrayList());
    private List<String> receiver_Output = new ArrayList();
    private String[] groups = {"group1", "group2", "group3", "group4"};
    private String[] actions = {"Join", "Leave", "Broadcast"};
    private String[] users;
    short port = 5000;
    private final Server server = new Server((short) 5000);

    private static final String JOIN_ACTION = "Join";
    private static final String LEAVE_ACTION = "Leave";

    private static final String JOINED_GROUP_MESSAGE = " Joined the group";
    private static final String YOU_JOINED_GROUP_MESSAGE = "You" + JOINED_GROUP_MESSAGE;

    private static final String LEAVE_MESSAGE = " left the group";

    private String receiverId;
    private String receiverGroupId;



    @Test
    public void serverTest() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                server.startServer();
            }
        });
        t1.start();


        receiverId = "u1";
        receiverGroupId = "group1";

        Client client1 = new Client(port, "u1");
        Client client2 = new Client(port, "u2");
        Client client3 = new Client(port, "u3");
        Client client4 = new Client(port, "u4");

        client1.runClient();
        client2.runClient();
        client3.runClient();
        client4.runClient();


        userVsUserObjectOutputStream.put(client1.getUser(), client1.getBufferedWriter());
        userVsUserObjectOutputStream.put(client2.getUser(), client2.getBufferedWriter());
        userVsUserObjectOutputStream.put(client3.getUser(), client3.getBufferedWriter());
        userVsUserObjectOutputStream.put(client4.getUser(), client4.getBufferedWriter());

        users = userVsUserObjectOutputStream.keySet().toArray(new String[]{});

        BufferedWriter bufferedWriter =userVsUserObjectOutputStream.get(receiverId);
        try {
            sendObject(bufferedWriter,new Action("Join",receiverGroupId));
            expectedOutput.add(YOU_JOINED_GROUP_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        final Map<String, ReentrantLock> userVsLockMap = new ConcurrentHashMap<>();
        for (String user : users) {
            userVsLockMap.put(user, new ReentrantLock());
        }

        Runnable runnable = new Runnable() {

            private Random random = new Random();
            private int actionId;
            private int groupId;
            boolean[] receiver_group_members = new boolean[users.length];

            @Override
            public void run() {
                ReentrantLock reentrantLock = null;
                int count = 1000;
                while (count > 0) {
                    count--;
                    int userId = random.nextInt(users.length);
                    actionId = random.nextInt(actions.length);
                    groupId = random.nextInt(groups.length);
                    String user = users[userId];
                    String action = actions[actionId];
                    String group = groups[groupId];
                    if (user.equals(receiverId)) {
                        continue;
                    }
                    try {
                        String message = UUID.randomUUID().toString();
                        reentrantLock = userVsLockMap.get(user);
                        reentrantLock.lock();
                        BufferedWriter bufferedWriter = userVsUserObjectOutputStream.get(user);
                        if (JOIN_ACTION.equals(action)) {
                            sendObject(bufferedWriter, new Action(action, group));
                            if (receiverGroupId.equals(group)) {
                                synchronized (receiver_group_members) {
                                    if (!receiver_group_members[userId]) {
                                        expectedOutput.add(user + JOINED_GROUP_MESSAGE);
                                        receiver_group_members[userId] = true;
                                    }
                                }
                            }
                        } else if (LEAVE_ACTION.equals(action)) {
                            sendObject(bufferedWriter, new Action(action, group));
                            if (receiverGroupId.equals(group)) {
                                synchronized (receiver_group_members) {
                                    if (receiver_group_members[userId]) {
                                        expectedOutput.add(user + LEAVE_MESSAGE);
                                        receiver_group_members[userId] = false;
                                    }
                                }
                            }
                        } else {
                            sendObject(bufferedWriter, new BroadCast(group,message));
                            synchronized (receiver_group_members) {
                                if (receiverGroupId.equals(group) && receiver_group_members[userId]) {
                                    expectedOutput.add(user + " : " + message);
                                }
                            }
                        }
                        reentrantLock.unlock();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t2.start();
        t3.start();
        try {
            t2.join();
            t3.join();
            server.stopServer();
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        readReceiverFile(receiver_Output, new File(receiverId));
        Assert.assertTrue(compare(receiver_Output, expectedOutput));
    }

    private void readReceiverFile(List receiver_Output, File file) {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                receiver_Output.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean compare(List receiver_Output, List<String> expected_Output) {
        for (String line : expected_Output) {
            if (!receiver_Output.contains(line)) {
                return false;
            }
        }
        return true;
    }

    private void sendObject(BufferedWriter bufferedWriter, Object o) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        bufferedWriter.write(objectMapper.writeValueAsString(o) + "\n");
        bufferedWriter.flush();
    }
}
