package com.wavemaker.tutorial.chat.common; /**
 * Created by srujant on 29/6/16.
 */
import java.io.Serializable;

public class Action implements  Serializable{
    private String action;
    private String group;

    public Action(String action,String group){
        this.action=action;
        this.group=group;
    }


    public String getAction() {
        return action;
    }


    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Action{" +
                "action='" + action + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
