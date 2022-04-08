package com.example.shibushi.Models;

public class cClothing {
    String ID, type, user;

    public cClothing(){}

    public cClothing(String ID, String type, String user) {
        this.ID = ID;
        this.type = type;
        this.user = user;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
