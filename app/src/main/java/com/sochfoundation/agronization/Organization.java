package com.sochfoundation.agronization;

import android.os.Parcel;
import android.os.Parcelable;

public class Organization implements Parcelable {
    String name;
    String mainWork;
    String type;
    String phone;
    String email;
    String website;
    String img;
    String id;
    String location;
    String lat, lon;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Organization(String name, String mainWork, String type, String phone, String email, String website, String img) {
        this.name = name;
        this.mainWork = mainWork;
        this.type = type;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.img = img;
    }

    public Organization(String name, String type, String phone, String email, String website) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.email = email;
        this.website = website;
    }
    public Organization(String name, String type, String phone, String email, String website, String mainWork) {
        this.name = name;
        this.type = type;
        this.mainWork = mainWork;
        this.phone = phone;
        this.email = email;
        this.website = website;
    }
    public Organization() {
    }

    public Organization(String name, String type) {
        this.name = name;
        this.type = type;
    }

    protected Organization(Parcel in) {
        name = in.readString();
        mainWork = in.readString();
        type = in.readString();
        phone = in.readString();
        email = in.readString();
        website = in.readString();
        img = in.readString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainWork() {
        return mainWork;
    }

    public void setMainWork(String mainWork) {
        this.mainWork = mainWork;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static final Creator<Organization> CREATOR = new Creator<Organization>() {
        @Override
        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        @Override
        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mainWork);
        dest.writeString(type);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(website);
        dest.writeString(img);
    }
}
