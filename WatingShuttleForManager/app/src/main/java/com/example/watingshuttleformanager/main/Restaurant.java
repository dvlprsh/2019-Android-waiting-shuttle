package com.example.watingshuttleformanager.main;

public class Restaurant {
    String image;
    String name;
    String kind;
    String address;
    String tel;
    String time;
    String day_off;
    String breaktime;
    String price;

    public Restaurant(String image, String name, String kind, String address, String tel, String time, String day_off, String breaktime, String price) {
        this.image = image;
        this.name = name;
        this.kind = kind;
        this.address = address;
        this.tel = tel;
        this.time = time;
        this.day_off = day_off;
        this.breaktime = breaktime;
        this.price = price;
    }

    public String getImage() {
        return "http://13.125.147.57/owners_waiting_shuttle/MyApi/uploads/"+image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay_off() {
        return day_off;
    }

    public void setDay_off(String day_off) {
        this.day_off = day_off;
    }

    public String getBreaktime() {
        return breaktime;
    }

    public void setBreaktime(String breaktime) {
        this.breaktime = breaktime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
