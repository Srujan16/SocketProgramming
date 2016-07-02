package com.wavemaker.tutorial.chat.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by srujant on 1/7/16.
 */
public class OneToMany implements Serializable{
    private String message;
    private List<String> receivers=new ArrayList();

    public OneToMany(String message,Object receiversObject){
        this.message=message;
        try {
        JSONArray jsonArray=new JSONArray(receiversObject.toString());

        for(int i=0;i<jsonArray.length();i++){
                receivers.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage() {
        return message;
    }

    public List<String> getReceivers() {
        return receivers;
    }
}