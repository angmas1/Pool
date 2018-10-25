package com.example.anunay.pool;

public class ParticipantsFormat {
    String uid,state;

    public ParticipantsFormat(){}

    public ParticipantsFormat(String uid, String state) {
        this.uid = uid;

        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
