package com.example.democomm;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtility {

    private String text;
    private String sender;

    public String getText() {
        return text;
    }

    public  String getSender(){
        return sender;
    }

    public JSONUtility(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public JSONUtility(String string) {

        try {

            JSONObject json = new JSONObject(string);


        } catch (JSONException e) {

        }

    }

    public String getJSONstring(String sender) {
        String s;
        try {
            JSONObject jsonObject = new JSONObject().put("text",text).put("sender",sender);
            s= jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            s="";
        }
        return  s;
    }
}
