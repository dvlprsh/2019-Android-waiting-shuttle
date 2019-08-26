package com.example.watingshuttleformanager.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatRoom implements Parcelable{
    int room_number;
    int owner_id;
    int user_id;
    String restaurant_name;
    //String last_message;
    String userID;

    public ChatRoom(int room_number, int owner_id, int user_id, String restaurant_name, String userID) {
        this.room_number = room_number;
        this.owner_id = owner_id;
        this.user_id = user_id;
        this.restaurant_name = restaurant_name;
        this.userID = userID;
    }

    protected ChatRoom(Parcel in) {
        room_number = in.readInt();
        owner_id = in.readInt();
        user_id = in.readInt();
        restaurant_name = in.readString();
        userID = in.readString();
    }

    public static final Creator<ChatRoom> CREATOR = new Creator<ChatRoom>() {
        @Override
        public ChatRoom createFromParcel(Parcel in) {
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
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

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(room_number);
        dest.writeInt(owner_id);
        dest.writeInt(user_id);
        dest.writeString(restaurant_name);
        dest.writeString(userID);
    }
}
