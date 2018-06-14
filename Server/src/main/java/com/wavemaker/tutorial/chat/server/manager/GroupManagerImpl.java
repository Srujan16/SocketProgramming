package com.wavemaker.tutorial.chat.server.manager;


import com.wavemaker.tutorial.chat.common.Action;
import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.server.event.EventListener;
import com.wavemaker.tutorial.chat.server.Group;
import com.wavemaker.tutorial.chat.server.ObjectFactory;
import com.wavemaker.tutorial.chat.server.event.EventManager;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by srujant on 1/7/16.
 */
public class GroupManagerImpl implements EventListener, GroupManager {

    private Map<String, Group> groupNameVsGroupObjectMap = new ConcurrentHashMap<>();


    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);

    public GroupManagerImpl(EventManager eventManagerBean){
        eventManagerBean.registerEvent(this);
    }


    public GroupManagerImpl() {
        ObjectFactory.getInstance(EventManager.class).registerEvent(this);
    }


    private void add(String currentUser, Group group) {
        String message;
        CopyOnWriteArrayList<String> usersList = group.getUsersList();
        if (usersList.contains(currentUser)) {
            clientManager.sendMessage("You already joined the group", currentUser);
        } else {
            usersList.add(currentUser);
            clientManager.sendMessage("You Joined the group", currentUser);
            for (String client : usersList) {
                if (!client.equals(currentUser)) {
                    message = currentUser + " Joined the group";
                    clientManager.sendMessage(message, client);
                }
            }
        }
    }

    private void remove(String currentUser, Group group) {
        CopyOnWriteArrayList<String> usersList = group.getUsersList();
        String broadcastMessage;
        if (usersList.contains(currentUser)) {
            usersList.remove(currentUser);
            clientManager.sendMessage("You left the group", currentUser);
            for (String client : usersList) {
                if (!client.equals(currentUser)) {
                    broadcastMessage = currentUser + " left the group";
                    clientManager.sendMessage(broadcastMessage, client);
                }
            }

        } else {
            clientManager.sendMessage("You are not a member in the group", currentUser);
        }
    }

    private void createGroup(String currentUser, Group group) {
        CopyOnWriteArrayList<String> usersList = group.getUsersList();
        usersList.add(currentUser);
        System.out.println(currentUser + " first " + group);
        clientManager.sendMessage("You Joined the group", currentUser);
    }


    private void send(Group group, BroadCast broadCast, String currentUser) {
        String message;
        CopyOnWriteArrayList<String> usersList = group.getUsersList();
        if (usersList.contains(currentUser)) {
            for (String member : usersList) {
                if (!member.equals(currentUser)) {
                    message = currentUser + " : " + broadCast.getMessage();
                    clientManager.sendMessage(message, member);
                }
            }
        } else {
            clientManager.sendMessage("You didn't join the group ", currentUser);
        }
    }

    private void action(Action action, String currentUser) {
        synchronized (groupNameVsGroupObjectMap) {
            if (action.getAction().equals("Join")) {
                Group group = groupNameVsGroupObjectMap.get(action.getGroup());
                if (group != null) {
                    add(currentUser, group);
                } else {
                    group = new Group();
                    createGroup(currentUser, group);
                    groupNameVsGroupObjectMap.put(action.getGroup(), group);
                }
            } else if (action.getAction().equals("Leave")) {
                if (!groupNameVsGroupObjectMap.containsKey(action.getGroup())) {
                    clientManager.sendMessage("Group doesn't exist", currentUser);
                } else {
                    Group group = groupNameVsGroupObjectMap.get(action.getGroup());
                    remove(currentUser, group);
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }


    private void broadCast(BroadCast broadcast, String currentUser) {
        String message;
        synchronized (groupNameVsGroupObjectMap) {
            if (groupNameVsGroupObjectMap.containsKey(broadcast.getGroup())) {
                String destination = broadcast.getGroup();
                Set<String> groups = groupNameVsGroupObjectMap.keySet();
                for (String groupName : groups)
                    if (destination.equals(groupName)) {
                        Group group = groupNameVsGroupObjectMap.get(groupName);
                        send(group, broadcast, currentUser);
                        break;
                    }
            } else {
                message = " Group doesn't exist";
                clientManager.sendMessage(message, currentUser);
            }
        }
    }


    @Override
    public void onEvent(Object object, String currentUser) {
        if (object instanceof BroadCast) {
            broadCast((BroadCast) object, currentUser);
        }
        if (object instanceof Action) {
            action((Action) object, currentUser);
        }
    }
}
