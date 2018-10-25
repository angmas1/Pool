package com.example.anunay.pool;

import java.util.Timer;

/**
 * Created by Anunay on 23-03-2018.
 */

public class EventFormat {
    private String eventname,description,type,subtype,endsin;
    //int type; //0 for food, 1 for sports, 2 for games, 3 for others
    private Integer noofpeople;
    private String pushid;
    private Boolean isactive;

    public EventFormat()
    {

    }

    public EventFormat(String eventname, String description, String type, String subtype, Integer noofpeople, String endsin,String pushid,Boolean isactive) {
        this.eventname = eventname;
        this.description = description;
        this.type = type;
        this.subtype = subtype;
        this.noofpeople = noofpeople;
        this.endsin = endsin;
        this.pushid=pushid;
        this.isactive=isactive;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Integer getNoofpeople() {
        return noofpeople;
    }

    public void setNoofpeople(Integer noofpeople) {
        this.noofpeople = noofpeople;
    }

    public String getEndsin() {
        return endsin;
    }

    public void setEndsin(String endsin) {
        this.endsin = endsin;
    }
}