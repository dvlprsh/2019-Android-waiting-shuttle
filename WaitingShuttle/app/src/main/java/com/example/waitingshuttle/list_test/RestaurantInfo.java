package com.example.waitingshuttle.list_test;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantInfo implements Parcelable{
    //recyclerView item 클릭 시 나오는 상세 페이지
    int owner_id;
    int image_id;
    int addr_id;
    String image;
    String name;
    String kind;
    String address;
    //위도, 경도 for 지도 마커
    String longitude;
    String latitude;
    //위도, 경도 for 지도 마커
    String tel;
    String time;
    String breaktime;
    String day_off;
    String price;
    int waiting;
    boolean able_to_waiting; //현재 웨이팅 받고 있는지
    public RestaurantInfo(Restaurant restaurant, int image_id, int addr_id, String longitude, String latitude, String tel,
                          String time, String breaktime, String day_off, String price, int waiting) {
        this.owner_id = restaurant.getOwner_id();
        this.image_id = image_id;
        this.addr_id = addr_id;
        this.image = restaurant.getImage();
        this.name = restaurant.getName();
        this.kind = restaurant.getKind();
        this.address = restaurant.getAddress();
        this.longitude = longitude;
        this.latitude = latitude;
        this.tel = tel;
        this.time = time;
        this.breaktime = breaktime;
        this.day_off = day_off;
        this.price = price;
        this.waiting=waiting;
        this.able_to_waiting = true;
    }
    public RestaurantInfo(int owner_id, int image_id, int addr_id, String image, String name,
                          String kind, String address, String longitude, String latitude, String tel,
                          String time, String breaktime, String day_off, String price) {
        this.owner_id = owner_id;
        this.image_id = image_id;
        this.addr_id = addr_id;
        this.image = image;
        this.name = name;
        this.kind = kind;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tel = tel;
        this.time = time;
        this.breaktime = breaktime;
        this.day_off = day_off;
        this.price = price;
        this.able_to_waiting = true;
    }

    protected RestaurantInfo(Parcel in) {
        owner_id = in.readInt();
        image_id = in.readInt();
        addr_id = in.readInt();
        image = in.readString();
        name = in.readString();
        kind = in.readString();
        address = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        tel = in.readString();
        time = in.readString();
        breaktime = in.readString();
        day_off = in.readString();
        price = in.readString();
        waiting = in.readInt();
        able_to_waiting = in.readByte() != 0;
    }

    public static final Creator<RestaurantInfo> CREATOR = new Creator<RestaurantInfo>() {
        @Override
        public RestaurantInfo createFromParcel(Parcel in) {
            return new RestaurantInfo(in);
        }

        @Override
        public RestaurantInfo[] newArray(int size) {
            return new RestaurantInfo[size];
        }
    };

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(int addr_id) {
        this.addr_id = addr_id;
    }

    public String getImage() {
        return image;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getBreaktime() {
        return breaktime;
    }

    public void setBreaktime(String breaktime) {
        this.breaktime = breaktime;
    }

    public String getDay_off() {
        return day_off;
    }

    public void setDay_off(String day_off) {
        this.day_off = day_off;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
        dest.writeInt(image_id);
        dest.writeInt(addr_id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(kind);
        dest.writeString(address);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(tel);
        dest.writeString(time);
        dest.writeString(breaktime);
        dest.writeString(day_off);
        dest.writeString(price);
        dest.writeInt(waiting);
        dest.writeByte((byte) (able_to_waiting ? 1 : 0));
    }
}
