package com.example.waitingshuttle.list_test;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable{
    //식당 리스트에 나오는 간략한 식당 정보
    int owner_id;

    String image;
    String name; //식당 이름
    String kind; //식당 종류
    String address; //식당 주소
    int waiting; //대기자 수
    boolean able_to_waiting; //현재 웨이팅 받고 있는지

    public Restaurant(int owner_id, String image, String name, String kind, String address,int waiting) {
        this.owner_id = owner_id;
        this.image = image;
        this.name = name;
        this.kind = kind;
        this.address = address;
        this.waiting = waiting;

        //temp
        this.able_to_waiting=true; //일단 모두 웨이팅 가능으로 표시
    }

    protected Restaurant(Parcel in) {
        owner_id = in.readInt();
        image = in.readString();
        name = in.readString();
        kind = in.readString();
        address = in.readString();
        waiting = in.readInt();
        able_to_waiting = in.readByte() != 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
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

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public boolean isAble_to_waiting() {
        return able_to_waiting;
    }

    public void setAble_to_waiting(boolean able_to_waiting) {
        this.able_to_waiting = able_to_waiting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(owner_id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(kind);
        dest.writeString(address);
        dest.writeInt(waiting);
        dest.writeByte((byte) (able_to_waiting ? 1 : 0));
    }
}
