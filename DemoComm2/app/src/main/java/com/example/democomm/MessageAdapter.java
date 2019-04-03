package com.example.democomm;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<JSONUtility> {


    private List<JSONUtility> messages;
    private Activity act;

    public MessageAdapter (Activity context, int res, List<JSONUtility> messageList) {

        super(context,res,messageList);
        act=context;
        messages=messageList;
    }



}
