package com.example.watingshuttleformanager.mode;

public class WaitingProduct {
    private int id;
    private int owner_id;
    private int user_id;
    private String user_userID;
    private String phone;

    private String person_number;

    public WaitingProduct(int id, int user_id, String user_userID, String phone, String person_number) {
        this.id = id;
        //this.owner_id = owner_id;
        this.user_id = user_id;
        this.user_userID = user_userID;
        this.phone = phone;
        this.person_number = person_number;
    }
    public WaitingProduct(String user_userID,String phone, String person_number) {
        this.id = id;
        //this.owner_id = owner_id;
        this.user_id = user_id;
        this.user_userID = user_userID;
        this.phone = phone;
        this.person_number = person_number;
    }
    public WaitingProduct(String phone, String person_number) {
        this.id = id;
        //this.owner_id = owner_id;
        this.user_id = user_id;
        this.user_userID = user_userID;
        this.phone = phone;
        this.person_number = person_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_userID() {
        return user_userID;
    }

    public void setUser_userID(String user_userID) {
        this.user_userID = user_userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPerson_number() {
        return person_number;
    }

    public void setPerson_number(String person_number) {
        this.person_number = person_number;
    }
}