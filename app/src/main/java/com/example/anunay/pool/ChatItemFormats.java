package com.example.anunay.pool;

import java.util.ArrayList;

/**
 * Created by f390 on 29/12/17.
 */

public class ChatItemFormats {

    private long timeDate;
    private Integer type;   //0 message 1 vote
    private String uid,message,name,photourl,question;
    private ArrayList<String> options = new ArrayList<>();

    public ChatItemFormats(long timeDate, Integer type, String uid, String message, String name, String photourl, String question, ArrayList<String> options) {
        this.timeDate = timeDate;
        this.type = type;
        this.uid = uid;
        this.message = message;
        this.name = name;
        this.photourl = photourl;
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public ChatItemFormats() {
    }

    public long getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(long timeDate) {
        this.timeDate = timeDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
