package com.wavemaker.tutorial.chat.server.manager;

import com.wavemaker.tutorial.chat.common.OneToMany;
import com.wavemaker.tutorial.chat.common.OneToOne;
import com.wavemaker.tutorial.chat.server.ObjectFactory;
import com.wavemaker.tutorial.chat.server.event.EventListener;
import com.wavemaker.tutorial.chat.server.event.EventManager;

import java.util.List;

/**
 * Created by srujant on 1/7/16.
 */
public class MessageManagerImpl implements EventListener, MessageManager {

    private ClientManager clientManager = ObjectFactory.getInstance(ClientManager.class);

    public MessageManagerImpl() {
        ObjectFactory.getInstance(EventManager.class).registerEvent(this);
    }

    public MessageManagerImpl(EventManager eventManagerBean){
        eventManagerBean.registerEvent(this);
    }


    private synchronized void oneToOne(OneToOne oneToOne, String currentUser) {
        if (clientManager.isRegistered(oneToOne.getDestination())) {
            clientManager.sendMessage(currentUser + " : " + oneToOne.getMessage() + "\n", oneToOne.getDestination());
            clientManager.sendMessage("message sent successfully\n", currentUser);
        } else {
            clientManager.sendMessage(oneToOne.getDestination() + " didn't join the network\n", currentUser);
        }

    }

    private synchronized void oneToMany(OneToMany oneToMany, String currentUser) {
        List<String> receivers = oneToMany.getReceivers();
        for (String client : receivers) {
            if (clientManager.isRegistered(client)) {
                clientManager.sendMessage("message sent successfully to " + client + "\n", currentUser);
                clientManager.sendMessage(currentUser + " : " + oneToMany.getMessage() + "\n", client);
            } else {
                clientManager.sendMessage(client + " didn't join the network\n", currentUser);
            }
        }
    }


    @Override
    public void onEvent(Object object, String currentUser) {
        if (object instanceof OneToOne) {
            oneToOne((OneToOne) object, currentUser);
        }
        if (object instanceof OneToMany) {
            oneToMany((OneToMany) object, currentUser);
        }
    }

}   
