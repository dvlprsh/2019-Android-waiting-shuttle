package com.example.waitingshuttle.list_test;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{
    private int id;
    private String title;
    private String shortdesc;
    private String rating;
    private String price;
    private String image;

    public Product(int id, String title, String shortdesc, String rating, String price, String image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }
    public Product(int id,String title, String image, String owner_userID) {
        this.id = id;
        this.title = title;
        this.shortdesc = owner_userID;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }
    public Product(String title, String image, String owner_userID) {
        this.id = id;
        this.title = title;
        this.shortdesc = owner_userID;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }
    public Product(String title, String image, String owner_userID, int id) {
        this.id = id; //owner_id
        this.title = title;
        this.shortdesc = owner_userID;
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

    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        shortdesc = in.readString();
        rating = in.readString();
        price = in.readString();
        image = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return "http://13.125.147.57/owners_waiting_shuttle/MyApi/uploads/"+image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(shortdesc);
        dest.writeString(rating);
        dest.writeString(price);
        dest.writeString(image);

    }
}