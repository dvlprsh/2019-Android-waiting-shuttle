package com.example.watingshuttleformanager.list_test;

public class Product {
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private double price;
    private String image;

    public Product(int id, String title, String shortdesc, double rating, double price, String image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }


    public Product(int id, String image, String owner_userID) {
        this.id = id;
        this.title = title;
        this.shortdesc = owner_userID;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return "http://13.125.159.24/owners_waiting_shuttle/MyApi/uploads/"+image;
    }
}