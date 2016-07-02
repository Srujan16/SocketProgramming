package com.wavemaker.tutorial.chat.server.manager;

import com.wavemaker.tutorial.chat.common.Action;
import com.wavemaker.tutorial.chat.common.BroadCast;
import com.wavemaker.tutorial.chat.server.event.EventListener;
import com.wavemaker.tutorial.chat.server.Group;
import com.wavemaker.tutorial.chat.server.ObjectFactory;
import com.wavemaker.tutorial.chat.server.event.EventManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by srujant on 1/7/16.
 */
public class GroupManagerImpl implements EventListener,GroupManager {

    private Map<String, Group> groupNameVsGroupObjectMap = new HashMap<String, Group>();

    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);

    public GroupManagerImpl() {
        ObjectFactory.getInstance(EventManager.class).registerEvent(this);
    }
    

    private void add(String currentUser,Group group) {
        String message;
        List<String> usersList=group.getUsersList();
        if (usersList.contains(currentUser)) {
            clientManager.sendMessage("You already joined the usersList\n",currentUser);
        } else {
            usersList.add(currentUser);
            clientManager.sendMessage("You Joined the usersList\n",currentUser);
            for(String client: usersList){
                if(!client.equals(currentUser)){
                    message=currentUser+" Joined the usersList\n";
                    clientManager.sendMessage(message,client);
                }
            }
        }
    }

    private void remove(String currentUser,Group group) {
        List<String> usersList=group.getUsersList();
        String broadcastMessage;

        if (usersList.contains(currentUser)) {
            usersList.remove(currentUser);
            clientManager.sendMessage("You left the usersList\n",currentUser);
            for(String client: usersList){
                if(!client.equals(currentUser)){
                    broadcastMessage = currentUser+" left the usersList\n";
                    clientManager.sendMessage(broadcastMessage,client);
                }
            }

        } else {
            clientManager.sendMessage("You are not a member in the usersList\n",currentUser);
        }
    }

    private void createGroup(String currentUser, Group group) {
        List<String> usersList=group.getUsersList();
        usersList.add(currentUser);
        clientManager.sendMessage("You joined the usersList\n", currentUser);

    }


    private void send(Group group, BroadCast broadCast, String currentUser) {
        String message ;
        List<String> usersList=group.getUsersList();
        if (usersList.contains(currentUser)) {
            for (String member : usersList) {
                if (!member.equals(currentUser)) {
                    message = currentUser +" : "+ broadCast.getMessage() + "\n";
                    clientManager.sendMessage(message, member);
                }
            }
        } else {
            clientManager.sendMessage("You didn't join the usersList \n", currentUser);
        }
    }
    private void action(Action action, String currentUser) {
        if (action.getAction().equals("Join")) {
            if (groupNameVsGroupObjectMap.containsKey(action.getGroup())) {
                Group group = groupNameVsGroupObjectMap.get(action.getGroup());
                add(currentUser, group);
            } else {
                Group group = new Group();
                createGroup(currentUser, group);
                groupNameVsGroupObjectMap.put(action.getGroup(), group);
            }
        } else {
            if (!groupNameVsGroupObjectMap.containsKey(action.getGroup())) {
                clientManager.sendMessage( "com.wavemaker.tutorial.chat.server.Group doesn't exist\n",currentUser);
            } else {
                Group group = groupNameVsGroupObjectMap.get(action.getGroup());
               remove(currentUser, group);
            }
        }
    }


    private void broadCast(BroadCast broadcast, String currentUser) {
        String message;
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
            message = " com.wavemaker.tutorial.chat.server.Group doesn't exist";
            clientManager.sendMessage(message, currentUser);
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
