package com.example.watingshuttleformanager.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private  int room_number;
    private int user_id_number; //회원 번호
    private String userID; //아이디
    private String image_url=null; //프로필 이미지url
    public User(int room_number, int user_id_number, String userID) {
        this.room_number = room_number;
        this.user_id_number = user_id_number;
        this.userID = userID;

    }
    public User(int user_id_number, String userID, String image_url) {
        this.user_id_number = user_id_number;
        this.userID = userID;
        this.image_url = image_url;
    }

    protected User(Parcel in) {
        room_number = in.readInt();
        user_id_number = in.readInt();
        userID = in.readString();
        image_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public int getUser_id_number() {
        return user_id_number;
    }

    public void setUser_id_number(int user_id_number) {
        this.user_id_number = user_id_number;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(room_number);
        dest.writeInt(user_id_number);
        dest.writeString(userID);
        dest.writeString(image_url);
    }
}
